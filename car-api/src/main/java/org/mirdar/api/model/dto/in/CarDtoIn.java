package org.mirdar.api.model.dto.in;

import lombok.Getter;
import lombok.Setter;
import org.mirdar.api.model.entity.CarEntity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CarDtoIn {
    @NotEmpty
    private String model;
    @NotEmpty
    private String licensePlate;
    @NotNull
    private Long personId;

    public CarEntity mapToEntity() {
        CarEntity carEntity = new CarEntity();
        carEntity.setModel(this.model);
        carEntity.setLicensePlate(this.licensePlate);
        carEntity.setPersonId(this.personId);
        return carEntity;
    }
}