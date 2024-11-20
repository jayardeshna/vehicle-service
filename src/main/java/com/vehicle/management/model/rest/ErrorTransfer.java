package com.vehicle.management.model.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorTransfer {

    private String errorMessage;
    private String errorCode;
    private String developerMessage;
    private String defaultMessage;
    private Map<String, String> defaultMessageParamMap;
}
