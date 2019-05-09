package com.gm.apps.security.core.authentication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import com.gm.apps.security.core.dto.UserDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthUserPrincipal<K extends Serializable> implements OidcUser, UserDetails, CredentialsContainer {
  private static final String ROLE_STR_PREFIX = "ROLE_";
  private static final long serialVersionUID = 5061565933014329607L;
  @Getter(AccessLevel.NONE)
  private final UserDto<K> userDto;
  private Map<String, Object> attributes;
  private String name;
  private Map<String, Object> claims;
  private OidcUserInfo userInfo;
  private OidcIdToken idToken;

  public UserDto<K> currentUser() {
    return userDto;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<String> roles = userDto.getRoles();
    if (null != roles && !roles.isEmpty()) {
      return roles.stream()
          .map(role -> new SimpleGrantedAuthority(ROLE_STR_PREFIX + role))
          .collect(Collectors.toCollection(() -> new ArrayList<>(roles.size() + 2)));
    }
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return userDto.getPassword();
  }

  @Override
  public String getUsername() {
    return userDto.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void eraseCredentials() {
    userDto.setPassword(null);
    attributes = null;
    claims = null;
    userInfo = null;
    idToken = null;
  }
}
