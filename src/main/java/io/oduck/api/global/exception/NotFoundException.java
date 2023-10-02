package io.oduck.api.global.exception;

public class NotFoundException extends CustomException{

    public NotFoundException(String value) {
        super(404, value + " Not Found");
    }
}
