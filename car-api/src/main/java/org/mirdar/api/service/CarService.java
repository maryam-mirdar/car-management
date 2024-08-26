package org.mirdar.api.service;

import org.mirdar.api.model.dto.CarDTO;
import org.mirdar.api.model.entity.Car;

import java.util.List;

public interface CarService {

    Car save(CarDTO carDTO);

    Car update(long id, CarDTO updatedCarDTO);

    void delete(long id);

    List<Car> getAllCars();

    Car getCarById(long id);

    List<Car> fetchCarDataWithFilteringAndSorting(String model, List<String> sortList, String sortOrder);
}