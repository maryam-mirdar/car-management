package org.mirdar.api.service.impl;

import lombok.AllArgsConstructor;
import org.mirdar.api.exception.DuplicateNationalCodeException;
import org.mirdar.api.exception.NoSuchEntityExistsException;
import org.mirdar.api.exception.PersonHasCarException;
import org.mirdar.api.exception.ValidationNationalCodeException;
import org.mirdar.api.model.dto.PersonDTO;
import org.mirdar.api.model.entity.Car;
import org.mirdar.api.model.entity.Person;
import org.mirdar.api.repository.CarRepository;
import org.mirdar.api.repository.PersonRepository;
import org.mirdar.api.service.PersonService;
import org.mirdar.api.util.SortUtil;
import org.mirdar.api.validation.NationalCodeValidation;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final CarRepository carRepository;

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public Person getPersonById(long id) {
        return personRepository.findById(id).orElseThrow(() -> new NoSuchEntityExistsException("Person", id));
    }

    @Override
    public Person save(PersonDTO personDTO) {
        Person person = new Person();
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        String code = personDTO.getNationalCode();
        if (!NationalCodeValidation.isValidationNationalCode(code)) {
            throw new ValidationNationalCodeException(code);
        }
        if (personRepository.existsByNationalCode(code)) {
            throw new DuplicateNationalCodeException(code);
        }
        person.setNationalCode(code);
        return personRepository.save(person);
    }

    @Override
    public Person update(long id, PersonDTO updatedPersonDTO) {
        Person person = personRepository.findById(id).orElseThrow(() -> new NoSuchEntityExistsException("Person", id));
        person.setFirstName(Optional.ofNullable(updatedPersonDTO.getFirstName()).orElse(person.getFirstName()));
        person.setLastName(Optional.ofNullable(updatedPersonDTO.getLastName()).orElse(person.getLastName()));
        personRepository.save(person);
        return person;
    }

    @Override
    public void delete(long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new NoSuchEntityExistsException("Person", id));
        List<Car> cars = carRepository.findByPerson(person);
        if (!cars.isEmpty()) {
            throw new PersonHasCarException();
        }
        personRepository.deleteById(id);
    }

    @Override
    public List<Person> fetchPersonDataWithFilteringAndSorting(String firstNameFilter, String lastNameFilter, List<String> sortList, String sortOrder) {
        Sort sort = Sort.by(SortUtil.createSortOrder(sortList, sortOrder));
        return personRepository.findByFirstNameLikeAndLastNameLike(firstNameFilter, lastNameFilter, sort);
    }
}