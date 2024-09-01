package org.mirdar.api.controller;

import com.mwga.common.shared.util.PaginatedOut;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.mirdar.api.model.dto.filter.PersonPageableFilter;
import org.mirdar.api.model.dto.in.PersonDtoIn;
import org.mirdar.api.model.dto.in.PersonNewDtoIn;
import org.mirdar.api.model.dto.in.PersonUpdateIn;
import org.mirdar.api.model.dto.out.PersonDtoOut;
import org.mirdar.api.service.PersonService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@RequestMapping("/persons")
@Validated
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public ResponseEntity<PaginatedOut<PersonDtoOut>> getAllPersons(@RequestBody PersonPageableFilter filter) {
        return new ResponseEntity<>(personService.getAll(filter), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDtoOut> getPersonById(@PathVariable("id") String id) {
        return new ResponseEntity<>(personService.getById(id), HttpStatus.OK);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody PersonDtoIn personDTO) {
        personService.save(personDTO);
    }

    @PostMapping("/person-with-car")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody PersonNewDtoIn personNewDtoIn) {
        personService.save(personNewDtoIn);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("id") String id, @RequestBody PersonUpdateIn personUpdateIn) {
        personService.update(id, personUpdateIn);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        personService.delete(id);
        return new ResponseEntity<>("Deleted Successfully!", HttpStatus.NO_CONTENT);
    }

    @GetMapping()
    public ResponseEntity<List<PersonDtoOut>> fetchPersonWithFilteringAndSorting(
            @RequestParam(defaultValue = "") String firstNameFilter,
            @RequestParam(defaultValue = "") String lastNameFilter,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        return new ResponseEntity<>(personService.fetchDataWithFilteringAndSorting(firstNameFilter,
                lastNameFilter,
                sortList,
                sortOrder.toString()), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PaginatedOut<PersonDtoOut>> fetchByFilterAndSortingAndPagination(
            @RequestParam(defaultValue = "") Integer size,
            @RequestParam(defaultValue = "") Integer page,
            @RequestParam(defaultValue = "") String firstName,
            @RequestParam(defaultValue = "") String lastName) {
        return new ResponseEntity<>(personService.fetchByFilterAndSortingAndPagination(size,page,firstName, lastName)
                , HttpStatus.OK);
    }
}