package com.gm.apps.security.core.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.gm.apps.security.core.authentication.AuthProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto<K extends Serializable> {
  private K id;
  @ToString.Exclude
  private String username;
  @ToString.Exclude
  private String password;
  private Set<String> roles = new HashSet<>();
  private Serializable tag;
  private boolean unverified = false;
  private boolean blocked = false;
  private boolean admin = false;
  private String name;
  private String email;
  private String imageUrl;
  private Boolean emailVerified = false;
  private String passPhrase;
  private AuthProvider provider;
  private String providerId;
}
