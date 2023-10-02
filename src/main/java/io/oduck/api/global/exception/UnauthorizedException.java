package io.oduck.api.global.exception;

public class UnauthorizedException extends CustomException{
    public UnauthorizedException(String message) {
        super(401, message);
    }
}
