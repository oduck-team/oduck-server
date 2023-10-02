package io.oduck.api.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
  private int status;

    public CustomException(int status, String message) {
      super(message);
      this.status = status;
    }

  public CustomException(String message) {
    super(message);
  }
}