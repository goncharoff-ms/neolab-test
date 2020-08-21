package ru.goncharov.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class DataParserException extends RuntimeException {
    public DataParserException(JsonProcessingException e) {
        super(e);
    }
}
