package com.vehicle.management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends RuntimeException {

    private final int status;
    private final String errorKey;
    private final String errorCode;
    private final String errorMessage;
    private final String developerMessage;
    private final String defaultMessage;
    private final Map<String, String> defaultMessageParamMap;

}
