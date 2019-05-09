package com.gm.apps.security.core.jwt.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.gm.apps.security.core.authentication.AuthUserPrincipal;
import com.gm.apps.security.core.dto.UserDto;
import com.gm.apps.security.core.jwt.JwtTokenService;

public class JwtAuthenticationProvider implements AuthenticationProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationProvider.class);
  private JwtTokenService jwtTokenService;
  private UserDetailsService userDetailsService;

  public JwtAuthenticationProvider(JwtTokenService jwtTokenService,
      UserDetailsService userDetailsService) {
    this.jwtTokenService = jwtTokenService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    LOGGER.debug("Processing user authentication....");
    String token = (String) authentication.getCredentials();

    String username = jwtTokenService.getSubject(token);
    UserDetails user = userDetailsService.loadUserByUsername(username);

    if (null == user) {
      LOGGER.info("User is null");
    }

    AuthUserPrincipal<Long> principal = new AuthUserPrincipal<>(new UserDto<>());
    return new JwtAuthenticationToken(principal, token, principal.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
