package org.mirdar.api.service.impl;

import lombok.AllArgsConstructor;
import org.mirdar.api.exception.CannotBeCreatedWithOutAnOwnerException;
import org.mirdar.api.exception.DuplicateLicensePlateException;
import org.mirdar.api.exception.NoSuchEntityExistsException;
import org.mirdar.api.exception.ValidationLicensePlateException;
import org.mirdar.api.model.dto.CarDTO;
import org.mirdar.api.model.entity.Car;
import org.mirdar.api.model.entity.Person;
import org.mirdar.api.repository.CarRepository;
import org.mirdar.api.repository.PersonRepository;
import org.mirdar.api.service.CarService;
import org.mirdar.api.util.SortUtil;
import org.mirdar.api.validation.CarLicenseValidation;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {
    final CarRepository carRepository;
    final PersonRepository personRepository;

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Car getCarById(long id) {
        return carRepository.findById(id).orElseThrow(() -> new NoSuchEntityExistsException("Car", id));
    }

    @Override
    public Car save(CarDTO carDTO) {
        Car car = new Car();
        if (carDTO.getPersonId() == null) {
            throw new CannotBeCreatedWithOutAnOwnerException();
        }
        Person person = personRepository.findById(carDTO.getPersonId()).orElseThrow(() -> new NoSuchEntityExistsException("Person", carDTO.getPersonId()));
        car.setPerson(person);
        String licensePlate = carDTO.getLicensePlate();
        if (!CarLicenseValidation.isValidationLicensePlate(licensePlate)) {
            throw new ValidationLicensePlateException(licensePlate);
        }
        if (carRepository.existsByLicensePlate(licensePlate)) {
            throw new DuplicateLicensePlateException(licensePlate);
        }
        car.setLicensePlate(licensePlate);
        car.setModel(carDTO.getModel());
        return carRepository.save(car);
    }

    @Override
    public Car update(long id, CarDTO updatedCarDTO) {
        Car car = carRepository.findById(id).orElseThrow(() -> new NoSuchEntityExistsException("Car", id));
        car.setModel(Optional.ofNullable(updatedCarDTO.getModel()).orElse(car.getModel()));
        if (updatedCarDTO.getLicensePlate() != null) {
            String licensePlate = updatedCarDTO.getLicensePlate();
            if (!CarLicenseValidation.isValidationLicensePlate(licensePlate)) {
                throw new ValidationLicensePlateException(licensePlate);
            }
            if (!licensePlate.equals(car.getLicensePlate()) && carRepository.existsByLicensePlate(licensePlate)) {
                throw new DuplicateLicensePlateException(licensePlate);
            }
            car.setLicensePlate(licensePlate);
        }
        if (updatedCarDTO.getPersonId() != null) {
            Person person = personRepository.findById(updatedCarDTO.getPersonId()).orElseThrow(() -> new NoSuchEntityExistsException("Person", updatedCarDTO.getPersonId()));
            car.setPerson(person);
        }
        carRepository.save(car);
        return car;
    }

    @Override
    public void delete(long id) {
        carRepository.findById(id).orElseThrow(() -> new NoSuchEntityExistsException("Car", id));
        carRepository.deleteById(id);
    }

    @Override
    public List<Car> fetchCarDataWithFilteringAndSorting(String modelFilter, List<String> sortList, String sortOrder) {
        Sort sort = Sort.by(SortUtil.createSortOrder(sortList, sortOrder));
        return carRepository.findByModelLike(modelFilter, sort);
    }
}