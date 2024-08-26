package org.mirdar.api.exceptionHandler;

import org.mirdar.api.exception.*;
import org.mirdar.api.model.dto.AdviceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler({CannotBeCreatedWithOutAnOwnerException.class, CarAlreadyHasOwnerException.class,
            PersonHasCarException.class, ValidationLicensePlateException.class, ValidationNationalCodeException.class,
            DuplicateLicensePlateException.class, DuplicateNationalCodeException.class})
    public ResponseEntity<AdviceResponse> handleBadRequestStatusException(RuntimeException ex) {
        AdviceResponse adviceResponse = AdviceResponse.builder()
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(adviceResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoSuchEntityExistsException.class})
    public ResponseEntity<AdviceResponse> handleNotFoundStatusException(RuntimeException ex) {
        AdviceResponse adviceResponse = AdviceResponse.builder()
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(adviceResponse, HttpStatus.NOT_FOUND);
    }
}
