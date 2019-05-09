/******************************************************************************
 * Copyright (C) 2019 Girish Mahajan
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * @Written by Girish
 * @Project demo
 * @Date May 5, 2019
 ******************************************************************************/

package com.gm.apps.security.core.jwt;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import com.gm.apps.security.core.util.AuthenticationUtil;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWEDecryptionKeySelector;
import com.nimbusds.jose.proc.JWEKeySelector;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

public class JwtTokenServiceImpl implements JwtTokenService {
  private JwtPropeties jwtPropeties;
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenServiceImpl.class);
  private DirectEncrypter encrypter;
  private JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);
  private ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor;

  public JwtTokenServiceImpl(JwtPropeties jwtPropeties) {
    this.jwtPropeties = jwtPropeties;
    configureJwt();
  }

  private void configureJwt() {
    byte[] secretKey = jwtPropeties.getSecret().getBytes();
    try {
      encrypter = new DirectEncrypter(secretKey);
    } catch (KeyLengthException e) {
      LOGGER.error("Failed to initialize JWT token service", e);
    }
    jwtProcessor = new DefaultJWTProcessor<>();
    JWKSource<SimpleSecurityContext> jweKeySource = new ImmutableSecret<>(secretKey);
    JWEKeySelector<SimpleSecurityContext> jweKeySelector =
        new JWEDecryptionKeySelector<>(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256,
            jweKeySource);
    jwtProcessor.setJWEKeySelector(jweKeySelector);
  }

  @Override
  public boolean isValidToken(String token) {
    try {
      return null != parseToken(token);
    } catch (Exception ex) {
      LOGGER.error("Failed to parse JWT token: {}", token, ex);
    }
    return false;
  }

  /**
   * Creates a token
   */
  private String createToken(String aud, String subject, Long expirationMillis, Map<String, Object> claimMap) {
    JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
    builder
        .expirationTime(new Date(System.currentTimeMillis() + expirationMillis))
        .audience(aud)
        .subject(subject)
        .claim(APP_JWT_IAT, System.currentTimeMillis());

    claimMap.forEach(builder::claim);
    JWTClaimsSet claims = builder.build();
    Payload payload = new Payload(claims.toJSONObject());
    JWEObject jweObject = new JWEObject(header, payload);
    try {
      jweObject.encrypt(encrypter);
    } catch (JOSEException e) {
      throw new IllegalStateException(e);
    }
    // Serialize to compact JOSE
    return jweObject.serialize();
  }

  private String createToken(String audience, String subject, Long expirationMillis) {
    return createToken(audience, subject, expirationMillis, new HashMap<>());
  }

  @Override
  public String createToken(String username) {
    return createToken(AUTH_AUDIENCE,
        username,
        jwtPropeties.getExpirationInMsec());
  }

  @Override
  public String createShortLivedToken(String username) {
    return createToken(AUTH_AUDIENCE,
        username,
        jwtPropeties.getShortLivedExpirationInMillis());
  }

  /**
   * Parses a token
   */
  @Override
  public JWTClaimsSet parseToken(String token) {
    return parseToken(token, JwtTokenService.AUTH_AUDIENCE);
  }

  private JWTClaimsSet parseToken(String token, String audience) {
    try {
      JWTClaimsSet claims = jwtProcessor.process(token, null);
      AuthenticationUtil.ensureAuthority(audience != null &&
          claims.getAudience().contains(audience), "Mismatched audience");
      AuthenticationUtil.ensureAuthority(claims.getExpirationTime().after(new Date()),
          "Token expired");
      return claims;
    } catch (ParseException | BadJOSEException | JOSEException e) {
      throw new BadCredentialsException(e.getMessage());
    }
  }

  /**
   * Parses a token
   */
  private JWTClaimsSet parseToken(String token, String audience, long issuedAfter) {
    JWTClaimsSet claims = parseToken(token);
    AuthenticationUtil.ensureAuthority(audience != null &&
        claims.getAudience().contains(audience), "Mismatched audience");
    long issueTime = (long) claims.getClaim(JwtTokenService.APP_JWT_IAT);
    AuthenticationUtil.ensureAuthority(issueTime >= issuedAfter,
        "Token is obsolete");
    return claims;
  }

  @Override
  public String getSubject(String token) {
    JWTClaimsSet claims = parseToken(token, JwtTokenService.AUTH_AUDIENCE);
    return claims.getSubject();

  }
}
