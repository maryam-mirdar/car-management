package org.mirdar.api.service;

import com.mwga.common.shared.util.PaginatedOut;
import com.mwga.storage.orm.jpa.GenericFilterableService;
import com.mwga.storage.orm.model.SearchCondition;
import com.mwga.storage.orm.model.SearchFilter;
import lombok.RequiredArgsConstructor;
import org.mirdar.api.exception.CustomException;
import org.mirdar.api.model.dto.CarFilter;
import org.mirdar.api.model.dto.in.CarDtoIn;
import org.mirdar.api.model.dto.in.CarUpdateIn;
import org.mirdar.api.model.dto.out.CarDtoOut;
import org.mirdar.api.model.entity.CarEntity;
import org.mirdar.api.repository.CarRepository;
import org.mirdar.api.util.SortUtil;
import org.mirdar.api.validation.CarLicenseValidation;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService extends GenericFilterableService<CarEntity, CarFilter> {
    private final CarRepository carRepository;

    public List<CarDtoOut> getAllCars(CarFilter filter) {
        return getAllEntities(filter, new String[]{"person"}).stream()
                .map(CarDtoOut::new)
                .collect(Collectors.toList());
    }

    public CarDtoOut getCarById(long id) {
        CarEntity carEntity = getEntityById(id , new String[]{"person"});
        return new CarDtoOut(carEntity);
    }

    public void save(CarDtoIn carDtoIn) {
        CarEntity carEntity = carDtoIn.mapToEntity();
        if (carDtoIn.getPersonId() == null) {
            throw new CustomException("A car cannot be created without an owner.", HttpStatus.BAD_REQUEST, "50262");
        }
        validateLicensePlate(carDtoIn.getLicensePlate());
        checkDuplicateLicensePlate(carDtoIn.getLicensePlate(), null);
        createEntity(carEntity);
       // carRepository.save(carEntity);
    }

    public void update(long id, CarUpdateIn carUpdateIn) {
        CarEntity carEntity = getEntityById(id , null);
        if (carUpdateIn.getLicensePlate() != null) {
            validateLicensePlate(carUpdateIn.getLicensePlate());
            checkDuplicateLicensePlate(carUpdateIn.getLicensePlate(), carEntity.getLicensePlate());
            carEntity.setLicensePlate(carUpdateIn.getLicensePlate());
        }
        carEntity.setModel(Optional.ofNullable(carUpdateIn.getModel()).orElse(carEntity.getModel()));
        carEntity.setPersonId(Optional.ofNullable(carUpdateIn.getPersonId()).orElse(carEntity.getPersonId()));
        updateEntity(carEntity);
       // carRepository.save(carEntity);
    }

    public void delete(long id) {
        getEntityById(id , null);
        //carRepository.deleteById(id);
        deleteEntityById(id);
    }

    public List<CarDtoOut> fetchCarDataWithFilteringAndSorting(String modelFilter,
                                                               List<String> sortList,
                                                               String sortOrder) {
        Sort sort = Sort.by(SortUtil.createSortOrder(sortList, sortOrder));
        List<CarEntity> carEntities = carRepository.findByModelLike(modelFilter, sort);
        return carEntities.stream()
                .map(CarDtoOut::new)
                .collect(Collectors.toList());
    }

    public void validateLicensePlate(String licensePlate) {
        if (!CarLicenseValidation.isValidationLicensePlate(licensePlate)) {
            throw new CustomException("Invalid licensePlate : " + licensePlate, HttpStatus.BAD_REQUEST, "50263");
        }
    }

    public void checkDuplicateLicensePlate(String newLicensePlate, String currentLicensePlate) {
        if (!newLicensePlate.equals(currentLicensePlate) && carRepository.existsByLicensePlate(newLicensePlate)) {
            throw new CustomException("Duplicate licensePlate : " + newLicensePlate, HttpStatus.BAD_REQUEST, "50264");
        }
    }

    public List<CarEntity> findByPersonId(Long personId) {
        return carRepository.findByPersonId(personId);
    }

    public boolean existsByLicensePlate(String licensePlate) {
        return carRepository.existsByLicensePlate(licensePlate);
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
        searchFilter.setSearchCondition(searchCondition);
        return searchFilter;
    }
}