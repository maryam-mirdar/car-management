package org.mirdar.api.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdviceResponse {
    private String message;
    private String status;
    private String code;
}