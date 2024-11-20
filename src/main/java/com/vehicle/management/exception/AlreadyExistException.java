package com.vehicle.management.exception;

import lombok.Getter;

public class AlreadyExistException extends BaseException {

    public AlreadyExistException(AlreadyExist alreadyExist) {
        super(409,alreadyExist.getErrorKey(), alreadyExist.getStatusCode(),
                alreadyExist.getErrorMessage(), alreadyExist.getDeveloperMessage(), null, null);
    }

    @Getter
    public enum AlreadyExist {
        USER_ALREADY_EXIST("4090100", "User with email already exist", "User with email already exist");

        private String errorKey;
        private final String statusCode;
        private final String errorMessage;
        private final String developerMessage;

        AlreadyExist(String statusCode, String errorMessage, String developerMessage) {
            this.statusCode = statusCode;
            this.errorMessage = errorMessage;
            this.developerMessage = developerMessage;
        }

    }
}
