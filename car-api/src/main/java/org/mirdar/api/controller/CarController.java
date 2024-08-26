package org.mirdar.api.controller;

import org.mirdar.api.model.dto.CarDTO;
import org.mirdar.api.model.entity.Car;
import org.mirdar.api.service.CarService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.mirdar.api.service.impl.PersonServiceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@RequestMapping("/car")
public class CarController {
    final CarService carService;

    @GetMapping("/getAll")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/getById/{Id}")
    public Car getCarById(@PathVariable("Id") Long id) {
        return carService.getCarById(id);
    }

    @PostMapping("/add")
    public Car add(@RequestBody CarDTO carDTO) {
        return carService.save(carDTO);
    }

    @PutMapping("/update/{Id}")
    public Car update(@PathVariable("Id") Long id, @RequestBody CarDTO updatedCarDTO) {
        return carService.update(id, updatedCarDTO);
    }

    @DeleteMapping("/delete/{Id}")
    public String delete(@PathVariable("Id") Long id) {
        carService.delete(id);
        return "Deleted Successfully!";
    }

    @GetMapping("/fetchData")
    public List<Car> fetchCarWithFilteringAndSorting(@RequestParam(defaultValue = "") String modelFilter,
                                                     @RequestParam(defaultValue = "") List<String> sortList,
                                                     @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        return carService.fetchCarDataWithFilteringAndSorting(modelFilter, sortList, sortOrder.toString());
    }
}