package com.gm.apps.security.core.jwt;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

public final class JwtUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private JwtUtil() {
    // private constructor
  }

  public static DecodedJwtInfo decodeJwtToken(String jwt) {
    LOGGER.debug("Decoding JWT token");
    if (StringUtils.isEmpty(jwt)) {
      return null;
    }

    String[] jwtTokens = jwt.split("\\.");

    if (jwtTokens.length < 3) {
      throw new IllegalArgumentException("Invalid JWT token provided for decoding.");
    }

    String base64EncodedHeader = jwtTokens[0];
    String base64EncodedBody = jwtTokens[1];
    Base64 base64Url = new Base64(true);
    String header = new String(base64Url.decode(base64EncodedHeader));
    LOGGER.debug("JWT Header : {}", header);
    try {
      String body = new String(base64Url.decode(base64EncodedBody));
      @SuppressWarnings("unchecked")
      Map<String, Object> jwtBody = OBJECT_MAPPER.readValue(body, Map.class);
      LOGGER.debug("JWT Body : {}", body);
      DecodedJwtInfo decodedJwtInfo = new DecodedJwtInfo();
      setJwtBodyDetails(jwtBody, decodedJwtInfo);
      return decodedJwtInfo;
    } catch (IOException e) {
      LOGGER.warn("Failed to decode JWT", e);
    }
    return null;
  }

  private static void setJwtBodyDetails(Map<String, Object> jwtBody, DecodedJwtInfo decodedJwtInfo) {
    if (null != decodedJwtInfo) {
      String uniqueName = jwtBody.getOrDefault("unique_name", "").toString();
      decodedJwtInfo.setUniqueName(uniqueName);
      decodedJwtInfo.setIssuer(jwtBody.getOrDefault("iss", "").toString());
      decodedJwtInfo.setAudience(jwtBody.getOrDefault("aud", "").toString());
      decodedJwtInfo.setExpiration(Long.parseLong(jwtBody.get("exp").toString()));
      decodedJwtInfo.setNotBefore(Long.parseLong(jwtBody.get("nbf").toString()));
      jwtBody.put("email", uniqueName);
      decodedJwtInfo.setAttributes(Collections.unmodifiableMap(jwtBody));
    }
  }

  @Getter
  @Setter
  public static class DecodedJwtInfo {
    private String uniqueName;
    private String issuer;
    private String audience;
    private long expiration;
    private long notBefore;
    private Map<String, Object> attributes;
  }
}

