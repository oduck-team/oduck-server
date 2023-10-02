package io.oduck.api.global.exception;

public class ConflictException extends CustomException{
    public ConflictException(String value) {
        super(409, "Duplicate" + value);
    }
}
