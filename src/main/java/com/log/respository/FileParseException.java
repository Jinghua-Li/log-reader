package com.log.respository;

import com.log.exception.SystemExceptions;

public class FileParseException extends SystemExceptions {
    public FileParseException(String message) {
        super(message);
    }
}