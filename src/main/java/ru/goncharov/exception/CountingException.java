package ru.goncharov.exception;
/*
  @author : Maksim Goncharov
  @since : 19.08.2020, ср
*/

public class CountingException extends Exception {
    public CountingException(String message, Throwable cause) {
        super(message, cause);
    }
}
