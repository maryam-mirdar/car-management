package org.mirdar.api.model.dto.in;

import lombok.Data;

@Data
public class CarDtoIn {
    private String model;
    private String licensePlate;
    private Long personId;
}
