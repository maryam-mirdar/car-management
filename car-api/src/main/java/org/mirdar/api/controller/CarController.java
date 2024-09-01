package org.mirdar.api.controller;

import com.mwga.common.shared.util.PaginatedOut;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.mirdar.api.model.dto.filter.CarPageableFilter;
import org.mirdar.api.model.dto.in.CarDtoIn;
import org.mirdar.api.model.dto.in.CarUpdateIn;
import org.mirdar.api.model.dto.out.CarDtoOut;
import org.mirdar.api.model.dto.out.PersonCarOut;
import org.mirdar.api.service.CarService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PaginatedOut<CarDtoOut>> getAllCars(@Valid CarPageableFilter filter) {
        return new ResponseEntity<>(carService.getAll(filter), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CarDtoOut> getCarById(@PathVariable("id")  String id) {
        return new ResponseEntity<>(carService.getById(id), HttpStatus.OK);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody @Valid CarDtoIn carDTO) {
        carService.save(carDTO);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("id") String id, @RequestBody CarUpdateIn carUpdateIn) {
        carService.update(id, carUpdateIn);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        carService.delete(id);
        return new ResponseEntity<>("Deleted Successfully!", HttpStatus.NO_CONTENT);
    }

    @GetMapping()
    public ResponseEntity<List<CarDtoOut>> fetchCarWithFilteringAndSorting(
            @RequestParam(defaultValue = "", required = false) String modelFilter,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        return new ResponseEntity<>(carService.fetchDataWithFilteringAndSorting(modelFilter,
                sortList,
                sortOrder.toString()),
                HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PaginatedOut<CarDtoOut>> fetchByFilterAndSortAndPagination(
            CarPageableFilter filter) {
        return new ResponseEntity<>(carService.fetchByFilterAndSortingAndPagination(filter.getSize(),
                filter.getPage(),
                filter.getModel()),
                HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PaginatedOut<PersonCarOut>> fetchByModelAndOwnerName(
              CarPageableFilter filter) {
        return new ResponseEntity<>(carService.fetchByModelAndOwnerFirstName(filter), HttpStatus.OK);
    }
}