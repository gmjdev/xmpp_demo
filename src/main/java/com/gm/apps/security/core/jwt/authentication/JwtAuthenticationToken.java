package com.gm.apps.security.core.jwt.authentication;

import java.util.Collection;
import java.util.Objects;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
  private static final long serialVersionUID = 8916820305731825460L;
  private UserDetails principal;
  private String jwtToken;

  public JwtAuthenticationToken(String token) {
    super(AuthorityUtils.NO_AUTHORITIES);
    jwtToken = token;
  }

  public JwtAuthenticationToken(UserDetails principal, String jwtToken,
      Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    this.jwtToken = jwtToken;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return jwtToken;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(jwtToken, principal);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof JwtAuthenticationToken)) {
      return false;
    }
    JwtAuthenticationToken other = (JwtAuthenticationToken) obj;
    return Objects.equals(jwtToken, other.jwtToken) &&
        Objects.equals(principal, other.principal);
  }
}
