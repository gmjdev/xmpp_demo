package com.gm.apps.security.core.oauth2.user;

import java.util.Collections;
import java.util.Map;

public class BasicOAuth2UserInfo extends OAuth2UserInfo {

  public BasicOAuth2UserInfo() {
    super(Collections.emptyMap());
  }

  public BasicOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getId() {
    return (String) attributes.get("id");
  }

  @Override
  public String getName() {
    return (String) attributes.get("name");
  }

  @Override
  public String getEmail() {
    return (String) attributes.get("email");
  }

  @SuppressWarnings("unchecked")
  @Override
  public String getImageUrl() {
    if (attributes.containsKey("picture")) {
      Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
      if (pictureObj.containsKey("data")) {
        Map<String, Object> dataObj = (Map<String, Object>) pictureObj.get("data");
        if (dataObj.containsKey("url")) {
          return (String) dataObj.get("url");
        }
      }
    }
    return null;
  }

  public static BasicOAuth2UserInfo userFromAttributes(Map<String, Object> attributes) {
    return new BasicOAuth2UserInfo(attributes);
  }
}
