package org.mirdar.api.model.dto.in;

import lombok.Getter;
import lombok.Setter;
import org.mirdar.api.model.entity.CarEntity;
import org.mirdar.api.model.entity.PersonEntity;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PersonNewDtoIn {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String nationalCode;
    @NotEmpty
    private String model;
    @NotEmpty
    private String licensePlate;

    public PersonEntity mapToEntity(){
        PersonEntity personEntity = new PersonEntity();
        personEntity.setFirstName(this.firstName);
        personEntity.setLastName(this.lastName);
        personEntity.setNationalCode(this.nationalCode);

        CarEntity carEntity = new CarEntity();
        carEntity.setModel(this.model);
        carEntity.setLicensePlate(this.licensePlate);
        carEntity.setPerson(personEntity);

        personEntity.getCars().add(carEntity);
        return personEntity;
    }
}