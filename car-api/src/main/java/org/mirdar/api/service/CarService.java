package org.mirdar.api.service;

import lombok.RequiredArgsConstructor;
import org.mirdar.api.exception.CustomException;
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
public class CarService {
    private final CarRepository carRepository;

    public List<CarDtoOut> getAllCars() {
        List<CarEntity> carEntities = carRepository.findAll();
        return carEntities.stream()
                .map(CarDtoOut::new)
                .collect(Collectors.toList());
    }

    public CarEntity findPersonByIdOrThrow(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new CustomException("Car with id = " + id + " does not exist.",
                        HttpStatus.NOT_FOUND, "50260"));
    }

    public CarDtoOut getCarById(long id) {
        CarEntity carEntity = carRepository.findByIdWithPerson(id)
                .orElseThrow(() -> new CustomException("Car with id = " + id + " does not exist.",
                        HttpStatus.NOT_FOUND, "50261"));
        return new CarDtoOut(carEntity);
    }

    public void save(CarDtoIn carDtoIn) {
        CarEntity carEntity = carDtoIn.mapToEntity();
        if (carDtoIn.getPersonId() == null) {
            throw new CustomException("A car cannot be created without an owner.", HttpStatus.BAD_REQUEST, "50262");
        }
        validateLicensePlate(carDtoIn.getLicensePlate());
        checkDuplicateLicensePlate(carDtoIn.getLicensePlate(), null);
        carRepository.save(carEntity);
    }

    public void update(long id, CarUpdateIn carUpdateIn) {
        CarEntity carEntity = findPersonByIdOrThrow(id);
        if (carUpdateIn.getLicensePlate() != null) {
            validateLicensePlate(carUpdateIn.getLicensePlate());
            checkDuplicateLicensePlate(carUpdateIn.getLicensePlate(), carEntity.getLicensePlate());
            carEntity.setLicensePlate(carUpdateIn.getLicensePlate());
        }
        carEntity.setModel(Optional.ofNullable(carUpdateIn.getModel()).orElse(carEntity.getModel()));
        carEntity.setPersonId(Optional.ofNullable(carUpdateIn.getPersonId()).orElse(carEntity.getPersonId()));
        carRepository.save(carEntity);
    }

    public void delete(long id) {
        findPersonByIdOrThrow(id);
        carRepository.deleteById(id);
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

    public boolean existsByLicensePlate(String licensePlate){
        return carRepository.existsByLicensePlate(licensePlate);
    }
}