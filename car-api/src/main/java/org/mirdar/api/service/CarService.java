package org.mirdar.api.service;

import com.mwga.common.shared.util.PaginatedOut;
import com.mwga.storage.orm.jpa.GenericFilterableService;
import com.mwga.storage.orm.model.SearchCondition;
import com.mwga.storage.orm.model.SearchFilter;
import com.mwga.storage.orm.model.SearchJoinCondition;
import lombok.RequiredArgsConstructor;
import org.mirdar.api.exception.CustomException;
import org.mirdar.api.model.dto.filter.CarFilter;
import org.mirdar.api.model.dto.filter.CarPageableFilter;
import org.mirdar.api.model.dto.in.CarDtoIn;
import org.mirdar.api.model.dto.in.CarUpdateIn;
import org.mirdar.api.model.dto.out.CarDtoOut;
import org.mirdar.api.model.dto.out.PersonCarOut;
import org.mirdar.api.model.entity.CarEntity;
import org.mirdar.api.repository.CarRepository;
import org.mirdar.api.util.SortUtil;
import org.mirdar.api.validation.CarLicenseValidation;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.JoinType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService extends GenericFilterableService<CarEntity, CarFilter> {
    private final CarRepository carRepository;

    public PaginatedOut<CarDtoOut> getAll(CarFilter filter) {
        return new PaginatedOut<>(getAllEntities(filter, new String[]{"person"})
                .stream()
                .map(CarDtoOut::new)
                .collect(Collectors.toList()), countEntities(filter));
    }

    public CarDtoOut getById(String id) {
        CarEntity entity = getEntityById(id, new String[]{"person"});
        return new CarDtoOut(entity);
    }

    public void save(CarDtoIn carDtoIn) {
        CarEntity entity = carDtoIn.mapToEntity();
        if (carDtoIn.getPersonId() == null) {
            throw new CustomException("A car cannot be created without an owner.", HttpStatus.BAD_REQUEST, "50262");
        }
        validateLicensePlate(carDtoIn.getLicensePlate());
        checkDuplicateLicensePlate(carDtoIn.getLicensePlate(), null);
        createEntity(entity);
    }

    public void update(String id, CarUpdateIn carUpdateIn) {
        CarEntity entity = getEntityById(id, null);
        if (carUpdateIn.getLicensePlate() != null) {
            validateLicensePlate(carUpdateIn.getLicensePlate());
            checkDuplicateLicensePlate(carUpdateIn.getLicensePlate(), entity.getLicensePlate());
            entity.setLicensePlate(carUpdateIn.getLicensePlate());
        }
        entity.setModel(Optional.ofNullable(carUpdateIn.getModel()).orElse(entity.getModel()));
        entity.setPersonId(Optional.ofNullable(carUpdateIn.getPersonId()).orElse(entity.getPersonId()));
        updateEntity(entity);
    }

    public void delete(String id) {
        deleteEntity(getEntityById(id, null));
    }

    public List<CarDtoOut> fetchDataWithFilteringAndSorting(String modelFilter,
                                                            List<String> sortList,
                                                            String sortOrder) {
        Sort sort = Sort.by(SortUtil.createSortOrder(sortList, sortOrder));
        List<CarEntity> carEntities = carRepository.findByModelLike(modelFilter, sort);
        return carEntities.stream()
                .map(CarDtoOut::new)
                .collect(Collectors.toList());
    }

    public PaginatedOut<CarDtoOut> fetchByFilterAndSortingAndPagination(Integer size,
                                                                        Integer page,
                                                                        String model) {
        CarPageableFilter filter = new CarPageableFilter();
        filter.setSize(size);
        filter.setPage(page);
        filter.setModel(model);
        return new PaginatedOut<>(getAllEntities(filter, null)
                .stream()
                .map(CarDtoOut::new)
                .collect(Collectors.toList()), countEntities(filter));
    }

    public PaginatedOut<PersonCarOut> fetchByModelAndOwnerFirstName(CarFilter filter) {

        if (!StringUtils.hasText(filter.getModel()) || !StringUtils.hasText(filter.getPersonFirstName())){
            throw new CustomException(" ", HttpStatus.BAD_REQUEST, "50269");
        }
        return new PaginatedOut<>(getAllEntities(filter, new String[]{"person"})
                .stream()
                .map(PersonCarOut::new)
                .collect(Collectors.toList()), countEntities(filter));
    }

    public void validateLicensePlate(String licensePlate) {
        if (!CarLicenseValidation.isValidationLicensePlate(licensePlate)) {
            throw new CustomException("Invalid licensePlate : " + licensePlate, HttpStatus.BAD_REQUEST, "50263");
        }
    }

    public void checkDuplicateLicensePlate(String newLicensePlate, String currentLicensePlate) {
        if (!newLicensePlate.equals(currentLicensePlate) && existsByLicensePlate(newLicensePlate)) {
            throw new CustomException("Duplicate licensePlate : " + newLicensePlate, HttpStatus.BAD_REQUEST, "50264");
        }
    }

    public boolean existsByLicensePlate(String licensePlate) {
        CarFilter filter = new CarFilter();
        filter.setLicensePlate(licensePlate);
        return exist(filter);
    }

    @Override
    public SearchFilter filterChain(CarFilter filter) {
        SearchFilter searchFilter = new SearchFilter();
        setPagination(filter, searchFilter);
        searchFilter.setSortCriteria(filter.getSortList());
        searchFilter.setDistinct(filter.isDistinct());
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.addEqualCondition("id", filter.getId());
        searchCondition.addLikeCondition("model", filter.getModel());
        searchCondition.addLikeCondition("licensePlate", filter.getLicensePlate());
        searchCondition.addEqualCondition("personId", filter.getPersonId());
        if (StringUtils.hasText(filter.getPersonLastName()) || StringUtils.hasText(filter.getPersonFirstName())) {
            SearchJoinCondition personJoinCondition = new SearchJoinCondition(false,
                    JoinType.INNER, "person");
            personJoinCondition.addLikeCondition("firstName", filter.getPersonFirstName());
            personJoinCondition.addLikeCondition("lastName", filter.getPersonLastName());
            searchCondition.addJoinCondition(personJoinCondition);
        }
        searchFilter.setSearchCondition(searchCondition);
        return searchFilter;
    }
}