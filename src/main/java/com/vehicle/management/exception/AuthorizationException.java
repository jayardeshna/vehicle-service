package com.vehicle.management.exception;

public class AuthorizationException extends BaseException {

    /**
     *
     */
    private static final long serialVersionUID = 1119066419517920757L;

    /**
     *
     */

    public AuthorizationException(String applicationMessage) {
        super(401, null, "4010101", "Not authorized", applicationMessage, null, null);
    }

    public AuthorizationException(String errorMessage, String applicationMessage) {
        super(401, null, "4010102", errorMessage, applicationMessage, null, null);
    }

}