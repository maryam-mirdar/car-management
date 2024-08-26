package org.mirdar.api.service;

import lombok.AllArgsConstructor;
import org.mirdar.api.exception.CannotBeCreatedWithOutAnOwnerException;
import org.mirdar.api.exception.DuplicateLicensePlateException;
import org.mirdar.api.exception.NoSuchEntityExistsException;
import org.mirdar.api.exception.ValidationLicensePlateException;
import org.mirdar.api.model.dto.in.CarDtoIn;
import org.mirdar.api.model.dto.out.CarDtoOut;
import org.mirdar.api.model.entity.CarEntity;
import org.mirdar.api.model.entity.PersonEntity;
import org.mirdar.api.repository.CarRepository;
import org.mirdar.api.repository.PersonRepository;
import org.mirdar.api.util.SortUtil;
import org.mirdar.api.validation.CarLicenseValidation;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final PersonService personService;
    public List<CarDtoOut> getAllCars() {
        List<CarEntity> carEntities = carRepository.findAll();
        return carEntities.stream()
                .map(CarDtoOut::new)
                .collect(Collectors.toList());
    }

    public CarDtoOut getCarById(long id) {
        CarEntity carEntity = carRepository.findById(id).orElseThrow(() -> new NoSuchEntityExistsException("Car", id));
        return new CarDtoOut(carEntity);
    }

    public CarDtoOut save(CarDtoIn carDtoIn) {
        CarEntity carEntity = new CarEntity();
        if (carDtoIn.getPersonId() == null) {
            throw new CannotBeCreatedWithOutAnOwnerException();
        }
        PersonEntity person = personService.getPersonEntityById(carDtoIn.getPersonId());
        carEntity.setPerson(person);
        String licensePlate = carDtoIn.getLicensePlate();
        if (!CarLicenseValidation.isValidationLicensePlate(licensePlate)) {
            throw new ValidationLicensePlateException(licensePlate);
        }
        if (carRepository.existsByLicensePlate(licensePlate)) {
            throw new DuplicateLicensePlateException(licensePlate);
        }
        carEntity.setLicensePlate(licensePlate);
        carEntity.setModel(carDtoIn.getModel());
        CarEntity savedCar = carRepository.save(carEntity);
        return new CarDtoOut(savedCar);
    }

    public CarDtoOut update(long id, CarDtoIn carDtoIn) {
        CarEntity carEntity = carRepository.findById(id).orElseThrow(() -> new NoSuchEntityExistsException("Car", id));
        carEntity.setModel(Optional.ofNullable(carDtoIn.getModel()).orElse(carEntity.getModel()));
        if (carDtoIn.getLicensePlate() != null) {
            String licensePlate = carDtoIn.getLicensePlate();
            if (!CarLicenseValidation.isValidationLicensePlate(licensePlate)) {
                throw new ValidationLicensePlateException(licensePlate);
            }
            if (!licensePlate.equals(carEntity.getLicensePlate()) && carRepository.existsByLicensePlate(licensePlate)) {
                throw new DuplicateLicensePlateException(licensePlate);
            }
            carEntity.setLicensePlate(licensePlate);
        }
        if (carDtoIn.getPersonId() != null) {
            PersonEntity personEntity = personService.getPersonEntityById(carDtoIn.getPersonId());
            carEntity.setPerson(personEntity);
        }
        CarEntity updatedCar = carRepository.save(carEntity);
        return new CarDtoOut(updatedCar);
    }

    public void delete(long id) {
        carRepository.findById(id).orElseThrow(() -> new NoSuchEntityExistsException("Car", id));
        carRepository.deleteById(id);
    }

    public List<CarDtoOut> fetchCarDataWithFilteringAndSorting(String modelFilter, List<String> sortList, String sortOrder) {
        Sort sort = Sort.by(SortUtil.createSortOrder(sortList, sortOrder));
        List<CarEntity> carEntities = carRepository.findByModelLike(modelFilter, sort);
        return carEntities.stream()
                .map(CarDtoOut::new)
                .collect(Collectors.toList());
    }

    public List<CarEntity> findByPerson(PersonEntity personEntity) {
        return carRepository.findByPerson(personEntity);
    }
}