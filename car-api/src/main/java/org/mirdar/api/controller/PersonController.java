package org.mirdar.api.controller;

import org.mirdar.api.model.dto.PersonDTO;
import org.mirdar.api.model.entity.Person;
import org.mirdar.api.service.PersonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@RequestMapping("/person")
public class PersonController {
    final PersonService personService;

    @GetMapping("/getAll")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/getById/{Id}")
    public Person getPersonById(@PathVariable("Id") Long id) {
        return personService.getPersonById(id);
    }

    @PostMapping("/add")
    public Person add(@RequestBody PersonDTO personDTO) {
        return personService.save(personDTO);
    }

    @PutMapping("/update/{Id}")
    public Person update(@PathVariable("Id") Long id, @RequestBody PersonDTO personDTO) {
        return personService.update(id, personDTO);
    }

    @DeleteMapping("/delete/{Id}")
    public String delete(@PathVariable("Id") Long id) {
        personService.delete(id);
        return "Deleted Successfully!";
    }

    @GetMapping("/fetchData")
    public List<Person> fetchPersonWithFilteringAndSorting(@RequestParam(defaultValue = "") String firstNameFilter,
                                                           @RequestParam(defaultValue = "") String lastNameFilter,
                                                           @RequestParam(defaultValue = "") List<String> sortList,
                                                           @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        return personService.fetchPersonDataWithFilteringAndSorting(firstNameFilter, lastNameFilter, sortList, sortOrder.toString());
    }
}