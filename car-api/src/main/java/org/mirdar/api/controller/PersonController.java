package org.mirdar.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.mirdar.api.model.dto.in.PersonDtoIn;
import org.mirdar.api.model.dto.out.PersonDtoOut;
import org.mirdar.api.service.PersonService;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public List<PersonDtoOut> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/{id}")
    public PersonDtoOut getPersonById(@PathVariable("id") Long id) {
        return personService.getPersonById(id);
    }

    @PostMapping()
    public void add(@RequestBody PersonDtoIn personDTO) {
        personService.save(personDTO);
    }

    @PostMapping("/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody PersonDtoIn personDTO) {
        personService.update(id, personDTO);
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        personService.delete(id);
        return "Deleted Successfully!";
    }

    @GetMapping()
    public List<PersonDtoOut> fetchPersonWithFilteringAndSorting(@RequestParam(defaultValue = "") String firstNameFilter,
                                                                 @RequestParam(defaultValue = "") String lastNameFilter,
                                                                 @RequestParam(defaultValue = "") List<String> sortList,
                                                                 @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        return personService.fetchPersonDataWithFilteringAndSorting(firstNameFilter, lastNameFilter, sortList, sortOrder.toString());
    }
}