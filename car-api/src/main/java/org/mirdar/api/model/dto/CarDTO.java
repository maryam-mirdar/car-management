package org.mirdar.api.model.dto;

import lombok.Data;

@Data
public class CarDTO {
    private String model;
    private String licensePlate;
    private Long personId;
}
