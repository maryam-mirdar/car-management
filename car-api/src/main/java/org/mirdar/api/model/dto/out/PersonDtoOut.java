package org.mirdar.api.model.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.mirdar.api.model.entity.PersonEntity;

@Getter
@Setter
@AllArgsConstructor
public class PersonDtoOut {
    private String id;
    private String firstName;
    private String lastName;
    private String nationalCode;

    public PersonDtoOut(PersonEntity person){
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.nationalCode = person.getNationalCode();
    }
}