package com.gm.apps.security.knowmail.oauth2.user;

import java.util.Collections;
import java.util.Map;
import com.gm.apps.security.core.oauth2.user.OAuth2UserInfo;

public class KnowmailOAuth2UserInfo extends OAuth2UserInfo {
  private final String email;

  public KnowmailOAuth2UserInfo(String email, Map<String, Object> attributes) {
    super(Collections.unmodifiableMap(attributes));
    this.email = email;
  }

  @Override
  public String getId() {
    return null;
  }

  @Override
  public String getName() {
    return email;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public String getImageUrl() {
    return "";
  }

  public Map<String, Object> getAttributes() {
    return attributes;
  }

}
