package com.gm.apps.security.core.jwt.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.gm.apps.security.core.jwt.JwtPropeties;
import com.gm.apps.security.core.jwt.JwtTokenService;
import com.gm.apps.security.core.jwt.JwtTokenServiceImpl;
import com.gm.apps.security.core.jwt.authentication.JwtAuthenticationProvider;
import com.gm.apps.security.core.jwt.filter.JwtAuthenticationFilter;

@Configuration
public class JwtAutoConfiguration {

  @ConditionalOnBean(name = "jwtPropeties")
  @ConditionalOnMissingBean
  @Bean
  public JwtPropeties jwtPropeties() {
    return new JwtPropeties();
  }

  @ConditionalOnBean(value = JwtPropeties.class)
  @ConditionalOnMissingBean
  @Bean
  public JwtTokenService jwtTokenService(JwtPropeties jwtPropeties) {
    return new JwtTokenServiceImpl(jwtPropeties);
  }

  @ConditionalOnBean(value = {JwtTokenService.class})
  @ConditionalOnMissingBean
  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenService jwtTokenService) {
    return new JwtAuthenticationFilter(jwtTokenService);
  }

  @ConditionalOnMissingBean
  @Bean
  public JwtAuthenticationProvider jwtAuthenticationProvider(JwtTokenService jwtTokenService,
      UserDetailsService userDetailsService) {
    return new JwtAuthenticationProvider(jwtTokenService, userDetailsService);
  }
}
