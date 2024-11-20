package com.vehicle.management.exception;

import ch.qos.logback.core.status.ErrorStatus;
import com.vehicle.management.model.rest.ErrorTransfer;
import com.vehicle.management.model.rest.ValidationError;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static String shortenedStackTrace(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String[] lines = writer.toString().split("\n");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(lines.length, 3); i++) {
            sb.append(lines[i]).append("\n");
        }
        return sb.toString();
    }

    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ErrorTransfer webApplicationException(BaseException baseException, HttpServletResponse response, Authentication auth) {
        log.error("baseException ", baseException);
        baseException.printStackTrace();
        if (log.isDebugEnabled())
            log.warn(shortenedStackTrace(baseException));
        response.setStatus(baseException.getStatus());
        return createErrorTransfer(baseException);
    }




    private ErrorTransfer createErrorTransfer(BaseException baseException ) {
        String errorMessage = baseException.getErrorMessage();
        String developerMessage = baseException.getDeveloperMessage();
        String defaultMessage = baseException.getDefaultMessage();


        return new ErrorTransfer(
                errorMessage != null ? errorMessage : baseException.getErrorMessage(),
                baseException.getErrorCode(),
                developerMessage != null ? developerMessage : baseException.getDeveloperMessage(),
                defaultMessage != null ? defaultMessage : baseException.getDefaultMessage(),
                baseException.getDefaultMessageParamMap()
        );
    }

    private String getMessageFromBundle(String messageKey, ResourceBundle resourceBundle) {
        if (messageKey != null && resourceBundle != null) {
            return resourceBundle.getString(messageKey);
        }
        return messageKey;
    }

    //field validation exception
    @ExceptionHandler(FieldValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorStatus handleFieldValidationException(FieldValidationException ex) {
        log.error("FieldValidationException occurred {}", ex.getMessage());
        List<ValidationError> errors = new ArrayList<>();
        String messageKey = "validation.error";
        for (ConstraintViolation<?> constraintViolation : ex.getViolations()) {
            ValidationError error = new ValidationError();
            error.setField(constraintViolation.getPropertyPath().toString());
            error.setMessage(constraintViolation.getMessage());
            errors.add(error);
        }
        return new ErrorStatus(messageKey, errors);
    }

    //file size exception
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
    public Map<String, Object> handleMultipartException(MaxUploadSizeExceededException ex) {
        log.error("MaxUploadSizeExceededException", ex);
        Map<String, Object> responseData = new HashMap<>();
            responseData.put("error", ex.getMessage());
        return responseData;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, Object> processValidationError(MethodArgumentTypeMismatchException ex) {
        log.warn("processValidationError", ex);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("error", ex.getMessage());
        log.warn("MethodArgumentTypeMismatchException for field {}, class {}, method{}, error message {}",
                ex.getName(), ex.getParameter().getContainingClass().getName(), Objects.requireNonNull(ex.getParameter().getMethod()).getName(), ex.getMessage());
        return responseData;
    }

    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public Map<String, Object> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.warn("HttpRequestMethodNotSupportedException", exception);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("error", exception.getMessage());
        return responseData;
    }

    @ExceptionHandler(value = { HttpMediaTypeNotSupportedException.class })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Map<String, Object> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        log.warn("httpMediaTypeNotSupportedException", exception);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("error", exception.getMessage());
        log.error(exception.getMessage());
        log.warn(exception.getMessage());
        return responseData;
    }

    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public Map<String, Object> httpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.warn("httpMessageNotReadableException", exception);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("error", "Not Valid Json Format");
        log.warn(exception.getMessage());
        return responseData;
    }

    //global exception
    @ExceptionHandler(value = { IOException.class, InvocationTargetException.class, NoSuchMethodException.class,
            Exception.class })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> exceptionHandle(Exception exception) {
        log.warn("exceptionHandle", exception);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("message", exception.getMessage());
        log.error(exception.getMessage());
        return responseData;
    }

    @ExceptionHandler(ClientAbortException.class)
    public void handleClientAbortException(ClientAbortException clientAbortException) {
        log.warn(clientAbortException.getMessage());
    }

    @ExceptionHandler(value = { HttpMediaTypeNotAcceptableException.class })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public Map<String, Object> httpMediaTypeNotAcceptableException(HttpMediaTypeNotSupportedException exception) {
        log.warn("httpMediaTypeNotAcceptableException", exception);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("error", exception.getMessage());
        log.warn(exception.getMessage());
        return responseData;
    }

    @ExceptionHandler(value = { MissingServletRequestParameterException.class })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public Map<String, Object> httpMediaTypeNotAcceptableException(MissingServletRequestParameterException exception) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("error", exception.getMessage());
        log.warn( "MissingServletRequestParameterException message {}" ,exception.getMessage());
        return responseData;
    }

    @ExceptionHandler(value = { MissingServletRequestPartException.class })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public Map<String, Object> httpMissingServletRequestPart(MissingServletRequestParameterException exception) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("error", exception.getMessage());
        log.warn( "MissingServletRequestPart Exception message {}" ,exception.getMessage());
        return responseData;
    }
}
