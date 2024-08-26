package org.mirdar.api.model.dto.out;

import lombok.Getter;
import lombok.Setter;
import org.mirdar.api.model.entity.CarEntity;

@Getter
@Setter
public class CarDtoOut {
    private Long id;
    private String model;
    private String licensePlate;
    private Long personId;

    public CarDtoOut(CarEntity car) {
        this.id = car.getId();
        this.model = car.getModel();
        this.licensePlate = car.getLicensePlate();
        this.personId = car.getPerson().getId();
    }
}
