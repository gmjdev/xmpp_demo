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

package com.gm.apps.security.core.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtTokenServiceImpl implements JwtTokenService {
  private JwtPropeties jwtPropeties;
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenServiceImpl.class);

  public JwtTokenServiceImpl(JwtPropeties jwtPropeties) {
    this.jwtPropeties = jwtPropeties;
  }

  @Override
  public String createToken(Authentication authentication) {
    Date expiryDate = new Date(System.currentTimeMillis() + jwtPropeties.getExpirationInMsec());
    //@formatter:off
    return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtPropeties.getSecret())
                .compact();
    //@formatter:on
  }

  @Override
  public boolean isValidToken(String token) {
    try {
      Jwts.parser().setSigningKey(jwtPropeties.getSecret()).parseClaimsJws(token);
      return true;
    } catch (SignatureException ex) {
      LOGGER.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      LOGGER.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      LOGGER.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      LOGGER.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      LOGGER.error("JWT claims string is empty.");
    }
    return false;
  }

  public Long getUserIdFromToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(jwtPropeties.getSecret()).parseClaimsJws(token).getBody();
    return Long.parseLong(claims.getSubject());
  }
}
