package com.gm.apps.security.core.oauth2.exception;

public class OAuth2AuthenticationProcessingException extends RuntimeException {
  private static final long serialVersionUID = 5319317511858077001L;

  public OAuth2AuthenticationProcessingException(String message, Throwable cause) {
    super(message, cause);
  }

  public OAuth2AuthenticationProcessingException(String message) {
    super(message);
  }

}
