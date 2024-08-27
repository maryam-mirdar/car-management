package org.mirdar.api.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mirdar.api.exception.DuplicateNationalCodeException;
import org.mirdar.api.exception.NoSuchEntityExistsException;
import org.mirdar.api.exception.PersonHasCarException;
import org.mirdar.api.exception.ValidationNationalCodeException;
import org.mirdar.api.model.dto.in.PersonDtoIn;
import org.mirdar.api.model.dto.out.PersonDtoOut;
import org.mirdar.api.model.entity.CarEntity;
import org.mirdar.api.model.entity.PersonEntity;
import org.mirdar.api.repository.CarRepository;
import org.mirdar.api.repository.PersonRepository;
import org.mirdar.api.util.SortUtil;
import org.mirdar.api.validation.NationalCodeValidation;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final CarService carService;

    public List<PersonDtoOut> getAllPersons() {
        List<PersonEntity> personEntities = personRepository.findAll();
        return personEntities.stream()
                .map(PersonDtoOut::new)
                .collect(Collectors.toList());
    }

    public PersonDtoOut getPersonById(long id) {
        PersonEntity personEntity = personRepository.findById(id).orElseThrow(() -> new NoSuchEntityExistsException("Person", id));
        return new PersonDtoOut(personEntity);
    }

    public void save(PersonDtoIn personDtoIn) {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setFirstName(personDtoIn.getFirstName());
        personEntity.setLastName(personDtoIn.getLastName());
        String code = personDtoIn.getNationalCode();
        if (!NationalCodeValidation.isValidationNationalCode(code)) {
            throw new ValidationNationalCodeException(code);
        }
        if (personRepository.existsByNationalCode(code)) {
            throw new DuplicateNationalCodeException(code);
        }
        personEntity.setNationalCode(code);
        personRepository.save(personEntity);
    }

    public void update(long id, PersonDtoIn personDtoIn) {
        PersonEntity personEntity = personRepository.findById(id).orElseThrow(() -> new NoSuchEntityExistsException("Person", id));
        personEntity.setFirstName(Optional.ofNullable(personDtoIn.getFirstName()).orElse(personEntity.getFirstName()));
        personEntity.setLastName(Optional.ofNullable(personDtoIn.getLastName()).orElse(personEntity.getLastName()));
        personRepository.save(personEntity);
    }

    public void delete(long id) {
        PersonEntity personEntity = personRepository.findById(id).orElseThrow(() -> new NoSuchEntityExistsException("Person", id));
        List<CarEntity> carEntities = carService.findByPerson(personEntity);
        if (!carEntities.isEmpty()) {
            throw new PersonHasCarException();
        }
        personRepository.deleteById(id);
    }

    public List<PersonDtoOut> fetchPersonDataWithFilteringAndSorting(String firstNameFilter, String lastNameFilter, List<String> sortList, String sortOrder) {
        Sort sort = Sort.by(SortUtil.createSortOrder(sortList, sortOrder));
        List<PersonEntity> personEntities = personRepository.findByFirstNameLikeAndLastNameLike(firstNameFilter, lastNameFilter, sort);
        return personEntities.stream()
                .map(PersonDtoOut::new)
                .collect(Collectors.toList());
    }

    public PersonEntity getPersonEntityById(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new NoSuchEntityExistsException("Person", id));
    }
}