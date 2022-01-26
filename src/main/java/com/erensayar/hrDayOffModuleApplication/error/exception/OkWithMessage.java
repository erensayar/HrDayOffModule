package com.erensayar.hrDayOffModuleApplication.error.exception;

import static com.erensayar.hrDayOffModuleApplication.error.exception.ExceptionConstants.OK_CODE;
import static com.erensayar.hrDayOffModuleApplication.error.exception.ExceptionConstants.OK_MESSAGE;

public class OkWithMessage extends BaseException {

    private static final String ERROR_CODE = OK_CODE;
    private static final String ERROR_MESSAGE = OK_MESSAGE;

    public OkWithMessage() {
        super(ERROR_CODE, ERROR_MESSAGE);
    }

    public OkWithMessage(final String errorMessage) {
        super(OK_CODE, errorMessage);
    }

}