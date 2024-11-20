package com.vehicle.management.exception;

public class BadRequestException extends BaseException {
    public BadRequestException(String applicationMessage) {
        super(400, null,"4000101", "Bad Request", applicationMessage, null, null);
    }
}
