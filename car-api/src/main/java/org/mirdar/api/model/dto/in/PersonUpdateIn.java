package org.mirdar.api.model.dto.in;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonUpdateIn {
    private String firstName;
    private String lastName;
    private String nationalCode;
}
