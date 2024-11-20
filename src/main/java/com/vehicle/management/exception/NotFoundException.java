package com.vehicle.management.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
public class NotFoundException extends BaseException {

    public NotFoundException(NotFoundType notFoundType) {
        super(404, notFoundType.getErrorKey(), notFoundType.getStatusCode(),  notFoundType.getErrorMessage(),
                notFoundType.getDeveloperMessage(), notFoundType.getDefaultMessage(), notFoundType.getDefaultMessageParamMap());
    }

    @Getter
    public enum NotFoundType {
        VEHICLE_NOT_FOUND("4040001", "Vehicle not found", "Vehicle not found"),
        GEOFENCE_NOT_FOUND("4040002", "Geofence not found", "Geofence not found");

        private String errorKey;
        private final String statusCode;
        private final String errorMessage;
        private final String developerMessage;
        private String defaultMessage;
        private Map<String, String> defaultMessageParamMap;

        NotFoundType(String statusCode, String errorMessage, String developerMessage) {
            this.statusCode = statusCode;
            this.errorMessage = errorMessage;
            this.developerMessage = developerMessage;
        }
    }






}
