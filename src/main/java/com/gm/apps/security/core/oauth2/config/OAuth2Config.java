package com.gm.apps.security.core.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import com.gm.apps.security.core.jwt.JwtTokenService;
import com.gm.apps.security.core.oauth2.authentication.OAuth2AuthenticationFailureHandler;
import com.gm.apps.security.core.oauth2.authentication.OAuth2AuthenticationSuccessHandler;

@Configuration
public class OAuth2Config {
  @Autowired
  private AuthorizationRequestRepository<OAuth2AuthorizationRequest> httpCookieOAuth2AuthorizationRequestRepository;

  @Bean
  public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler(JwtTokenService jwtTokenService) {
    return new OAuth2AuthenticationSuccessHandler(jwtTokenService, httpCookieOAuth2AuthorizationRequestRepository);
  }

  @Bean
  public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
    return new OAuth2AuthenticationFailureHandler(httpCookieOAuth2AuthorizationRequestRepository);
  }
}
