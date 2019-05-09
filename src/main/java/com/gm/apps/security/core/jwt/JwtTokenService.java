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

import com.nimbusds.jwt.JWTClaimsSet;

public interface JwtTokenService {
  String APP_JWT_IAT = "lemon-iat";
  String AUTH_AUDIENCE = "auth";
  String VERIFY_AUDIENCE = "verify";
  String FORGOT_CREDENTIALS_AUDIENCE = "forgot-creadentials";
  String CHANGE_EMAIL_AUDIENCE = "change-email";

  String createToken(String username);

  String createShortLivedToken(String username);

  boolean isValidToken(String token);

  JWTClaimsSet parseToken(String token);

  String getSubject(String token);
}
