package org.mirdar.api.model.dto.in;

import lombok.Getter;
import lombok.Setter;
import org.mirdar.api.model.entity.PersonEntity;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PersonDtoIn {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String nationalCode;

    public PersonEntity mapToEntity() {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setFirstName(this.firstName);
        personEntity.setLastName(this.lastName);
        personEntity.setNationalCode(this.nationalCode);
        return personEntity;
    }
}