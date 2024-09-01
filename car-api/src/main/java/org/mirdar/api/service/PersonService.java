package org.mirdar.api.service;

import com.mwga.common.shared.util.PaginatedOut;
import com.mwga.storage.orm.jpa.GenericFilterableService;
import com.mwga.storage.orm.model.SearchCondition;
import com.mwga.storage.orm.model.SearchFilter;
import lombok.RequiredArgsConstructor;
import org.mirdar.api.exception.CustomException;
import org.mirdar.api.model.dto.filter.PersonFilter;
import org.mirdar.api.model.dto.filter.PersonPageableFilter;
import org.mirdar.api.model.dto.in.PersonDtoIn;
import org.mirdar.api.model.dto.in.PersonNewDtoIn;
import org.mirdar.api.model.dto.in.PersonUpdateIn;
import org.mirdar.api.model.dto.out.PersonDtoOut;
import org.mirdar.api.model.entity.PersonEntity;
import org.mirdar.api.repository.PersonRepository;
import org.mirdar.api.util.SortUtil;
import org.mirdar.api.validation.CarLicenseValidation;
import org.mirdar.api.validation.NationalCodeValidation;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService extends GenericFilterableService<PersonEntity, PersonFilter> {
    private final PersonRepository personRepository;
    private final CarService carService;

    public PaginatedOut<PersonDtoOut> getAll(PersonFilter filter) {
        return new PaginatedOut<>(getAllEntities(filter, null)
                .stream()
                .map(PersonDtoOut::new)
                .collect(Collectors.toList()), countEntities(filter));
    }

    public PersonDtoOut getById(String id) {
        PersonEntity personEntity = getEntityById(id, null);
        return new PersonDtoOut(personEntity);
    }

    public void save(PersonDtoIn personDtoIn) {
        PersonEntity entity = personDtoIn.mapToEntity();
        validateNationalCode(personDtoIn.getNationalCode());
        checkDuplicatedNationalCode(personDtoIn.getNationalCode());
        createEntity(entity);
    }


    public void save(PersonNewDtoIn personNewDtoIn) {
        PersonEntity entity = personNewDtoIn.mapToEntity();
        String licensePlate = personNewDtoIn.getLicensePlate();
        if (!CarLicenseValidation.isValidationLicensePlate(licensePlate)) {
            throw new CustomException("Invalid licensePlate : " + licensePlate, HttpStatus.BAD_REQUEST, "50273");
        }
        if (carService.existsByLicensePlate(licensePlate)) {
            throw new CustomException("Duplicate licensePlate : " + licensePlate, HttpStatus.BAD_REQUEST, "50274");
        }
        validateNationalCode(personNewDtoIn.getNationalCode());
        checkDuplicatedNationalCode(personNewDtoIn.getNationalCode());
        createEntity(entity);
    }

    public void update(String id, PersonUpdateIn personUpdateIn) {
        PersonEntity entity = getEntityById(id, null);
        entity.setFirstName(Optional.ofNullable(personUpdateIn.getFirstName())
                .orElse(entity.getFirstName()));
        entity.setLastName(Optional.ofNullable(personUpdateIn.getLastName())
                .orElse(entity.getLastName()));
        updateEntity(entity);
    }

    public void delete(String id) {
        try {
            deleteEntity(getEntityById(id, null));
        } catch (DataIntegrityViolationException e) {
            throw new CustomException("Cannot delete person.The person is the owner of one or more cars.",
                    HttpStatus.BAD_REQUEST, "50275");
        }
    }

    public List<PersonDtoOut> fetchDataWithFilteringAndSorting(String firstNameFilter,
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

    public PaginatedOut<PersonDtoOut> fetchByFilterAndSortingAndPagination(Integer size,
                                                                           Integer page,
                                                                           String firstName,
                                                                           String lastName) {
        PersonPageableFilter filter = new PersonPageableFilter();
        filter.setSize(size);
        filter.setPage(page);
        filter.setFirstName(firstName);
        filter.setLastName(lastName);
        return new PaginatedOut<>(getAllEntities(filter, null)
                .stream()
                .map(PersonDtoOut::new)
                .collect(Collectors.toList()), countEntities(filter));
    }

    public List<PersonEntity> fetchByFilter(String firstName, String lastName) {
        PersonFilter personFilter = new PersonFilter();
        personFilter.setFirstName(firstName);
        personFilter.setLastName(lastName);
        return getAllEntities(personFilter, null);
    }

    public void validateNationalCode(String code) {
        if (!NationalCodeValidation.isValidationNationalCode(code)) {
            throw new CustomException("Invalid nationalCode : " + code, HttpStatus.BAD_REQUEST, "50271");
        }
    }

    public void checkDuplicatedNationalCode(String code) {
        PersonFilter personFilter = new PersonFilter();
        personFilter.setNationalCode(code);
        if (exist(personFilter)) {
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
        searchCondition.addEqualCondition("id", filter.getId());
        searchCondition.addLikeCondition("firstName", filter.getFirstName());
        searchCondition.addLikeCondition("lastName", filter.getLastName());
        searchCondition.addEqualCondition("nationalCode", filter.getNationalCode());
        searchFilter.setSearchCondition(searchCondition);
        return searchFilter;
    }
}