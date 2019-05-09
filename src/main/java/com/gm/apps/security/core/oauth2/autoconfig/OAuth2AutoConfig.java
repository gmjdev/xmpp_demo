package com.gm.apps.security.core.oauth2.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.gm.apps.security.core.authorization.oauth2.CookieOAuth2AuthorizationRequestRepository;

@Configuration
public class OAuth2AutoConfig {
  @ConditionalOnMissingBean
  @Bean
  public CookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
    return new CookieOAuth2AuthorizationRequestRepository();
  }
}
