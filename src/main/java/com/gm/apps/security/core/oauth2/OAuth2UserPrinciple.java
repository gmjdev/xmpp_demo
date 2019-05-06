/******************************************************************************
 * Copyright (C) 2019 Girish Mahajan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 * @Written by Girish
 * @Project demo
 * @Date May 5, 2019
 ******************************************************************************/

package com.gm.apps.security.core.oauth2;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2UserPrinciple implements OAuth2User, UserDetails {
  @Getter(value = AccessLevel.NONE)
  @Setter(value = AccessLevel.NONE)
  private static final long serialVersionUID = -1370417330832751406L;
  private Long id;
  private String email;
  private String passPhrase;
  private Collection<? extends GrantedAuthority> authorities;
  private Map<String, Object> attributes;

  public OAuth2UserPrinciple(Long id, String email, String passPhrase,
      Collection<? extends GrantedAuthority> authorities) {
    super();
    this.id = id;
    this.email = email;
    this.passPhrase = passPhrase;
    this.authorities = authorities;
  }

  @Override
  public String getName() {
    return email;
  }

  @Override
  public String getPassword() {
    return "[PROTECTED]";
  }

  @SuppressWarnings({ "squid:S4144" })
  @Override
  public String getUsername() {
    return email;
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
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.unmodifiableCollection(authorities);
  }

  @Override
  public Map<String, Object> getAttributes() {
    return Collections.unmodifiableMap(attributes);
  }

}
