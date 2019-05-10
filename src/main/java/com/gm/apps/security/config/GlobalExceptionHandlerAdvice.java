package com.gm.apps.security.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {Exception.class, IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> handleException(
      RuntimeException ex, WebRequest request) {
    String error = request.getParameter("error");
    Map<String, Object> errResponse = new HashMap<>();
    if (StringUtils.hasText(error)) {
      errResponse.put("success", false);
      errResponse.put("message", error);
    }
    return handleExceptionInternal(ex, errResponse,
        new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  @ExceptionHandler(value = OAuth2AuthenticationException.class)
  protected ResponseEntity<Object> handleOAuth2Authentication(
      OAuth2AuthenticationException ex, WebRequest request) {
    String error = request.getParameter("error");
    Map<String, Object> errResponse = new HashMap<>();
    if (StringUtils.hasText(error)) {
      errResponse.put("success", false);
      errResponse.put("message", error);
    }
    return handleExceptionInternal(ex, errResponse,
        new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
  }
}
