package io.oduck.api.global.advice;

import io.oduck.api.global.common.ErrorResponse;
import io.oduck.api.global.exception.BadRequestException;
import io.oduck.api.global.exception.ConflictException;
import io.oduck.api.global.exception.CustomException;
import io.oduck.api.global.exception.ForbiddenException;
import io.oduck.api.global.exception.NotFoundException;
import io.oduck.api.global.exception.UnauthorizedException;
import io.oduck.api.global.webHook.WebHookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RequiredArgsConstructor
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {
  private final WebHookService webHookService;

  // 요청 바디 필드 유효성 검증 예외 처리
  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {

    return ErrorResponse.of(e.getBindingResult());
  }

  // 경로 변수 유효성 검증 예외 처리
  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleConstraintViolationException(
      ConstraintViolationException e) {

    return ErrorResponse.of(e.getConstraintViolations());
  }

  // converter가 변환하지 못했을 경우(enum type에 존재하지 않는 값)
  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {

    return ErrorResponse.of(e.getPropertyName() + " Type Mismatched");
  }

  // url에 대해 지원하지 않는 http method 일 때 예외 처리
  @ExceptionHandler
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ErrorResponse handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {

    return ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED);
  }

  // HTTP Body를 제대로 파싱하지 못했을 때 예외 처리
  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {

    // 메시지 수정 필요
    return ErrorResponse.of("Required request body is missing");
  }

  // 요청 시 쿼리 파라미터가 결여됐을 때 예외 처리
  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMissingServletRequestParameterException(
      MissingServletRequestParameterException e) {

    return ErrorResponse.of(e.getMessage());
  }

  // 요청 쿠키가 null 값이거나 빈 값일 경우
  // 수정 필요
  @ExceptionHandler(MissingRequestCookieException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorResponse handleMissingRequestCookieException(MissingRequestCookieException e) {
    return ErrorResponse.of(e.getMessage());
  }

  // 일반적인 예외 처리
  @ExceptionHandler({
        BadRequestException.class,
        UnauthorizedException.class,
        ForbiddenException.class,
        NotFoundException.class,
        ConflictException.class,
        CustomException.class
  })
  public ResponseEntity<?> handleCustomException(CustomException e) {
    final ErrorResponse response = ErrorResponse.of(e.getMessage());

    return new ResponseEntity<>(response, HttpStatus.valueOf(e.getStatus()));
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected ErrorResponse handleNullPointerException(HttpServletRequest req, NullPointerException e) {
    log.error("handleNullPointerException", e);
    // Discord WebHook에 보낼 때 "가 있으면 json 파싱 에러가 발생함.
    // NPE 메시지에서 "를 사용함. -> Discord WebHook에 보낼 때 " -> \" 로 치환
    webHookService.sendMsg(new NullPointerException(e.getMessage().replace("\"", "\\\"")), req);
    return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // 위에서 지정한 예외 외의 서버 로직 예외에 대한 예외 처리.
  // 예상하지 못한 서버 예외
  // 운영에 치명적일 수 있음.
  // 반드시 로그를 기록하고, 관리자에게 알림을 줄 것.
  @ExceptionHandler
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleException(HttpServletRequest req, Exception e) {
    log.error("# Uncaught exceptions, which can be fatal to the server", e);
    webHookService.sendMsg(e, req);
    return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
