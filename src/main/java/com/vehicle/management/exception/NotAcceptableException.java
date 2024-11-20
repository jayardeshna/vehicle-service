package com.vehicle.management.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
public class NotAcceptableException extends BaseException {

    public NotAcceptableException(NotAcceptableExceptionMSG notAcceptableExceptionMSG) {
        super(406, notAcceptableExceptionMSG.getErrorKey(), notAcceptableExceptionMSG.getStatusCode(), notAcceptableExceptionMSG.getErrorMessage(),
                notAcceptableExceptionMSG.getDeveloperMessage(), notAcceptableExceptionMSG.getDefaultMessage(), notAcceptableExceptionMSG.getDefaultMessageParamMap());
    }

    @Getter
    public enum NotAcceptableExceptionMSG {
        PASSWORD_DOES_NOT_MATCH("4060001", "Password does not match.", "Password does not match."),
        EMAIL_REQUIRE("4060002", "Email is require", "Email is require"),
        USER_ALREADY_EXIST("4060003", "User Already Exist", "User Already Exist"),
        INVALID_ACCESS_TOKEN("4060004", "Invalid Access Token", "Invalid Access Token"),
        USER_NOT_VERIFIED("4060005", "Your email address has not been verified yet. Please verify it to proceed.", "Your email address has not been verified yet. Please verify it to proceed.");
        private String errorKey;
        private final String statusCode;
        @Setter
        private String errorMessage;
        @Setter
        private String developerMessage;
        @Setter
        private String defaultMessage;
        @Setter
        private Map<String, String> defaultMessageParamMap;

        NotAcceptableExceptionMSG(String statusCode, String errorMessage, String developerMessage) {
            this.statusCode = statusCode;
            this.errorMessage = errorMessage;
            this.developerMessage = developerMessage;
        }
        NotAcceptableExceptionMSG(String errorKey, String statusCode, String errorMessage, String developerMessage) {
            this.errorKey = errorKey;
            this.statusCode = statusCode;
            this.errorMessage = errorMessage;
            this.developerMessage = developerMessage;
        }
    }


}
