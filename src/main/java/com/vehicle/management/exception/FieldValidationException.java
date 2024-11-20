package com.vehicle.management.exception;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
public class FieldValidationException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;

    private final Set<? extends ConstraintViolation<?>> violations;

    public FieldValidationException() {
        this(null, null);
    }

    public FieldValidationException(String message) {
        this(message, null);
    }

    public FieldValidationException(Set<? extends ConstraintViolation<?>> violations) {
        this(null, violations);
    }

    public FieldValidationException(String message, Set<? extends ConstraintViolation<?>> violations) {
        super(message);
        this.message = message;
        this.violations = violations;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Set<? extends ConstraintViolation<?>> getViolations() {
        return violations;
    }
}