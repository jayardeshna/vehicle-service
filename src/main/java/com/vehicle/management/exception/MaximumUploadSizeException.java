package com.vehicle.management.exception;

import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartException;

public class MaximumUploadSizeException extends MultipartException {

    public MaximumUploadSizeException(long maxUploadSize) {
        this(maxUploadSize, null);
    }

    public MaximumUploadSizeException(long maxUploadSize, @Nullable Throwable ex) {
        super("Maximum upload size " + (maxUploadSize >= 0L ? "of " + maxUploadSize + "MB " : "") + "exceeded", ex);
    }
}
