package org.mirdar.api.model.dto.in;

import lombok.Getter;
import lombok.Setter;
import org.mirdar.api.exception.CustomException;
import org.mirdar.api.model.entity.CarEntity;
import org.mirdar.api.validation.CarLicenseValidation;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Getter
@Setter
public class CarUpdateIn {
    private String model;
    private String licensePlate;
    private Long personId;
}
