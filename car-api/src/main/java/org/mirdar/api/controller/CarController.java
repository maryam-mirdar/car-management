package org.mirdar.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.mirdar.api.model.dto.in.CarDtoIn;
import org.mirdar.api.model.dto.in.CarUpdateIn;
import org.mirdar.api.model.dto.out.CarDtoOut;
import org.mirdar.api.service.CarService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@RequestMapping("/cars")
@Validated
public class CarController {
    private final CarService carService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<CarDtoOut> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarDtoOut getCarById(@PathVariable("id") Long id) {
        return carService.getCarById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody @Valid CarDtoIn carDTO) {
        carService.save(carDTO);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("id") Long id, @RequestBody CarUpdateIn carUpdateIn) {
        carService.update(id, carUpdateIn);
    }

    @PostMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String delete(@PathVariable("id") Long id) {
        carService.delete(id);
        return "Deleted Successfully!";
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<CarDtoOut> fetchCarWithFilteringAndSorting(
            @RequestParam(defaultValue = "", required = false) String modelFilter,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        return carService.fetchCarDataWithFilteringAndSorting(modelFilter, sortList, sortOrder.toString());
    }
}