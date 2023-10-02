package io.oduck.api.global.common;

import jakarta.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

@Getter
public class ErrorResponse {
  private String message;
  private List<FieldError> fieldErrors;
  private List<ConstraintViolationError> violationErrors;

  /**
   * http 상태 코드와 예외 메시지를 받는 생성자 <br/>
   * ErrorResponse("임시 에러")
   **/
  private ErrorResponse(String message) {
    this.message = message;
  }

  /**
   * 바디 필드 예외 및 경로 변수 예외를 받는 생성자
   * 
   * <pre>
   * List<FieldError> fieldErrors = [
   *    { "field: "name", "rejectedValue : "", "reason" : "이름이 공백이 아니어야 합니다." }
   *    { "field: "email", "rejectedValue : "", "reason" : "이메일이 공백이 아니어야 합니다." }
   * ];
   *
   * List<ConstraintViolationError> violationErrors = [
   *    { "propertyPath" : "article-id", "message" : "1 이상이어야 합니다", "invalidValue" : -1 }
   * ];
   * </pre>
   *
   * ErrorResponse(fieldErrors, violationErrors)
   **/
  private ErrorResponse(final List<FieldError> fieldErrors,
      final List<ConstraintViolationError> violationErrors) {
    this.fieldErrors = fieldErrors;
    this.violationErrors = violationErrors;
  }

  // 바디 필드 예외에 해당하는 에러 응답 생성 메소드
  public static ErrorResponse of(BindingResult bindingResult) {
    return new ErrorResponse(FieldError.of(bindingResult), null);
  }

  // 경로 변수 예외에 해당하는 에러 응답 생성 메소드
  public static ErrorResponse of(Set<ConstraintViolation<?>> violations) {
    return new ErrorResponse(null, ConstraintViolationError.of(violations));
  }

  // HttpStatus에 해당하는 에러 응답 생성 메소드
  public static ErrorResponse of(HttpStatus httpStatus) {
    return new ErrorResponse(httpStatus.getReasonPhrase());
  }

  // HttpStatus에 커스텀 메시지를 넣는 에러 응답 생성 메소드
  public static ErrorResponse of(String message) {
    return new ErrorResponse(message);
  }

  // 요청 바디 필드 유효성 검증 예외
  @Getter
  @AllArgsConstructor
  public static class FieldError {
    private String field;
    private Object rejectedValue;
    private String reason;

    public static List<FieldError> of(BindingResult bindingResult) {
      final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
      return fieldErrors.stream()
          .map(error -> new FieldError(
              error.getField(),
              error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
              error.getDefaultMessage()))
          .collect(Collectors.toList());
    }
  }

  // 경로 변수 유효성 검증 예외
  @Getter
  @AllArgsConstructor
  public static class ConstraintViolationError {
    private String propertyPath;
    private Object rejectedValue;
    private String reason;

    public static List<ConstraintViolationError> of(
        Set<ConstraintViolation<?>> constraintViolations) {
      return constraintViolations.stream()
          .map(constraintViolation -> new ConstraintViolationError(
              constraintViolation.getPropertyPath().toString(),
              constraintViolation.getInvalidValue().toString(),
              constraintViolation.getMessage()))
          .collect(Collectors.toList());
    }
  }
}