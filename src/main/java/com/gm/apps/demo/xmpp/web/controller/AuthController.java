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

package com.gm.apps.demo.xmpp.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gm.apps.demo.xmpp.web.dto.LoginRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private OAuth2AuthorizedClientService authorizedClientService;
  // @Autowired
  // private AuthenticationManager authenticationManagerBean;

  // @Autowired
  // private JwtTokenService jwtTokenService;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    // Authentication authentication = authenticationManagerBean
    // .authenticate(new
    // UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
    // loginRequest.getPassPhrase()));
    //
    // SecurityContextHolder.getContext().setAuthentication(authentication);
    //
    // String token = jwtTokenService.createToken(authentication);
    return ResponseEntity.ok("Hello");
  }

  @GetMapping("/login/oauth2/success")
  public String getLoginInfo(OAuth2AuthenticationToken authentication) {
    OAuth2AuthorizedClient client = authorizedClientService
        .loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
    String accessToken = client.getAccessToken().getTokenValue();
    String refreshToken = client.getRefreshToken().getTokenValue();
    return "loginSuccess";
  }

  @GetMapping("/login/oauth2/error")
  public String getLoginInfoErr() {
    return "loginSuccessError";
  }

  @GetMapping("/oauth2/callback/{client}")
  public ResponseEntity<?> authenticateUserClient(@PathVariable String client,
      @Valid @RequestBody LoginRequest loginRequest) {
    // Authentication authentication = authenticationManagerBean
    // .authenticate(new
    // UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
    // loginRequest.getPassPhrase()));
    //
    // SecurityContextHolder.getContext().setAuthentication(authentication);
    //
    // String token = jwtTokenService.createToken(authentication);
    return ResponseEntity.ok("Hello " + client);
  }
}
