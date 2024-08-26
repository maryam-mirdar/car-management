package org.mirdar.api.service;

import org.mirdar.api.model.dto.PersonDTO;
import org.mirdar.api.model.entity.Person;

import java.util.List;

public interface PersonService {

    Person save(PersonDTO personDTO);

    Person update(long id, PersonDTO updatedPersonDTO);

    void delete(long id);

    List<Person> getAllPersons();

    Person getPersonById(long id);

    List<Person> fetchPersonDataWithFilteringAndSorting(String firstName, String lastName, List<String> sortList, String sortOrder);
}