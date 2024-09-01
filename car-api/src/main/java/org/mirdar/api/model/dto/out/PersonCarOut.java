package org.mirdar.api.model.dto.out;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.mirdar.api.model.entity.CarEntity;

@Getter
@Setter
public class PersonCarOut {
    private String model;
    private String licensePlate;
    private String personFirstName;
    private String personLastName;
    private String personNationalCode;

    public PersonCarOut(CarEntity car) {
        this.model = car.getModel();
        this.licensePlate = car.getLicensePlate();
        if (Hibernate.isInitialized(car.getPerson())) {
            this.personFirstName = car.getPerson().getFirstName();
            this.personLastName = car.getPerson().getLastName();
            this.personNationalCode = car.getPerson().getNationalCode();
        }
    }
}
