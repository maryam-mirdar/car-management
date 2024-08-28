package org.mirdar.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.mirdar.api.model.dto.in.PersonDtoIn;
import org.mirdar.api.model.dto.in.PersonNewDtoIn;
import org.mirdar.api.model.dto.in.PersonUpdateIn;
import org.mirdar.api.model.dto.out.PersonDtoOut;
import org.mirdar.api.service.PersonService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public List<PersonDtoOut> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDtoOut getPersonById(@PathVariable("id") Long id) {
        return personService.getPersonById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody PersonDtoIn personDTO) {
        personService.save(personDTO);
    }

    @PostMapping("/person-with-car")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody PersonNewDtoIn personNewDtoIn){
        personService.save(personNewDtoIn);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("id") Long id, @RequestBody PersonUpdateIn personUpdateIn) {
        personService.update(id, personUpdateIn);
    }

    @PostMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String delete(@PathVariable("id") Long id) {
        personService.delete(id);
        return "Deleted Successfully!";
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<PersonDtoOut> fetchPersonWithFilteringAndSorting(@RequestParam(defaultValue = "") String firstNameFilter,
                                                                 @RequestParam(defaultValue = "") String lastNameFilter,
                                                                 @RequestParam(defaultValue = "") List<String> sortList,
                                                                 @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        return personService.fetchPersonDataWithFilteringAndSorting(firstNameFilter, lastNameFilter, sortList, sortOrder.toString());
    }
}