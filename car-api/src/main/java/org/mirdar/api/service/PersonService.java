package org.mirdar.api.service;

import com.mwga.storage.orm.jpa.GenericFilterableService;
import com.mwga.storage.orm.model.SearchCondition;
import com.mwga.storage.orm.model.SearchFilter;
import lombok.RequiredArgsConstructor;
import org.mirdar.api.exception.CustomException;
import org.mirdar.api.model.dto.PersonFilter;
import org.mirdar.api.model.dto.in.CarDtoIn;
import org.mirdar.api.model.dto.in.PersonDtoIn;
import org.mirdar.api.model.dto.in.PersonNewDtoIn;
import org.mirdar.api.model.dto.in.PersonUpdateIn;
import org.mirdar.api.model.dto.out.PersonDtoOut;
import org.mirdar.api.model.entity.CarEntity;
import org.mirdar.api.model.entity.PersonEntity;
import org.mirdar.api.repository.PersonRepository;
import org.mirdar.api.util.SortUtil;
import org.mirdar.api.validation.CarLicenseValidation;
import org.mirdar.api.validation.NationalCodeValidation;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService extends GenericFilterableService<PersonEntity , PersonFilter> {
    private final PersonRepository personRepository;
    private final CarService carService;

    public List<PersonDtoOut> getAllPersons(PersonFilter filter) {
        return  getAllEntities(filter , null)
                .stream()
                .map(PersonDtoOut::new)
                .collect(Collectors.toList());
    }

    public PersonDtoOut getPersonById(Long id) {
        PersonEntity personEntity = getEntityById(id , null);;
        return new PersonDtoOut(personEntity);
    }

    public void save(PersonDtoIn personDtoIn) {
        PersonEntity personEntity = personDtoIn.mapToEntity();
        validateNationalCode(personDtoIn.getNationalCode());
        checkDuplicateNationalCode(personDtoIn.getNationalCode());
        //personRepository.save(personEntity);
        createEntity(personEntity);
    }

    @Transactional
    public void save(PersonNewDtoIn personNewDtoIn){
        PersonEntity personEntity = personNewDtoIn.mapToEntity();
        String licensePlate = personNewDtoIn.getLicensePlate();
        if (!CarLicenseValidation.isValidationLicensePlate(licensePlate)) {
            throw new CustomException("Invalid licensePlate : " + licensePlate, HttpStatus.BAD_REQUEST, "50273");
        }
        if (carService.existsByLicensePlate(licensePlate)) {
            throw new CustomException("Duplicate licensePlate : " + licensePlate, HttpStatus.BAD_REQUEST, "50274");
        }
        validateNationalCode(personNewDtoIn.getNationalCode());
        checkDuplicateNationalCode(personNewDtoIn.getNationalCode());
        //personRepository.save(personEntity);
        createEntity(personEntity);
        CarDtoIn carDtoIn = new CarDtoIn();
        carDtoIn.mapToEntity();
        carService.save(carDtoIn);
    }

    public void update(Long id, PersonUpdateIn personUpdateIn) {
        PersonEntity personEntity =  getEntityById(id , null);
        personEntity.setFirstName(Optional.ofNullable(personUpdateIn.getFirstName())
                .orElse(personEntity.getFirstName()));
        personEntity.setLastName(Optional.ofNullable(personUpdateIn.getLastName())
                .orElse(personEntity.getLastName()));
        //personRepository.save(personEntity);
        createEntity(personEntity);
    }

    public void delete(Long id) {
        getEntityById(id , null);
        List<CarEntity> carEntities = carService.findByPersonId(id);
        if (!carEntities.isEmpty()) {
            throw new CustomException("Cannot delete person.The person is the owner of one or more cars.",
                    HttpStatus.BAD_REQUEST, "50275");
        }
        //personRepository.deleteById(id);
        deleteEntityById(id);
    }

    public List<PersonDtoOut> fetchPersonDataWithFilteringAndSorting(String firstNameFilter,
                                                                     String lastNameFilter,
                                                                     List<String> sortList,
                                                                     String sortOrder) {
        Sort sort = Sort.by(SortUtil.createSortOrder(sortList, sortOrder));
        List<PersonEntity> personEntities = personRepository
                .findByFirstNameLikeAndLastNameLike(firstNameFilter, lastNameFilter, sort);
        return personEntities.stream()
                .map(PersonDtoOut::new)
                .collect(Collectors.toList());
    }

    public void validateNationalCode(String code){
        if (!NationalCodeValidation.isValidationNationalCode(code)) {
            throw new CustomException("Invalid nationalCode : " + code, HttpStatus.BAD_REQUEST, "50271");
        }
    }

    public void checkDuplicateNationalCode(String code){
        if (personRepository.existsByNationalCode(code)) {
            throw new CustomException("Duplicate nationalCode : " + code, HttpStatus.BAD_REQUEST, "50272");
        }
    }

    @Override
    public SearchFilter filterChain(PersonFilter filter) {
        SearchFilter searchFilter = new SearchFilter();
        setPagination(filter, searchFilter);
        searchFilter.setSortCriteria(filter.getSortList());
        searchFilter.setDistinct(filter.isDistinct());
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.addEqualCondition("id" , filter.getId());
        searchCondition.addLikeCondition("firstName" , filter.getFirstName());
        searchCondition.addLikeCondition("lastName" , filter.getLastName());
        searchCondition.addEqualCondition("nationalCode" , filter.getNationalCode());
        searchFilter.setSearchCondition(searchCondition);
        return searchFilter;
    }
}