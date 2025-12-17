package com.fuse.ai.server.web.exception;

import com.simply.common.core.exception.BaseException;
import com.simply.common.core.exception.error.SystemErrorType;

public class SysException extends BaseException {

    public SysException(SystemErrorType systemErrorType) {
        this(systemErrorType, systemErrorType.getMessage());
    }

    public SysException(SystemErrorType systemErrorType, String message) {
        super(systemErrorType, message);
    }
}