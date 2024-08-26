package org.mirdar.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.mirdar.api.model.dto.in.CarDtoIn;
import org.mirdar.api.model.dto.out.CarDtoOut;
import org.mirdar.api.service.CarService;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;

    @GetMapping()
    public List<CarDtoOut> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public CarDtoOut getCarById(@PathVariable("id") Long id) {
        return carService.getCarById(id);
    }

    @PostMapping()
    public CarDtoOut add(@RequestBody CarDtoIn carDTO) {
        return carService.save(carDTO);
    }

    @PostMapping("/{id}")
    public CarDtoOut update(@PathVariable("id") Long id, @RequestBody CarDtoIn updatedCarDTO) {
        return carService.update(id, updatedCarDTO);
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        carService.delete(id);
        return "Deleted Successfully!";
    }

    @GetMapping()
    public List<CarDtoOut> fetchCarWithFilteringAndSorting(@RequestParam(defaultValue = "") String modelFilter,
                                                           @RequestParam(defaultValue = "") List<String> sortList,
                                                           @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        return carService.fetchCarDataWithFilteringAndSorting(modelFilter, sortList, sortOrder.toString());
    }
}