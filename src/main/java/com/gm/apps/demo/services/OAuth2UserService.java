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

package com.gm.apps.demo.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.gm.apps.security.core.authentication.AuthProvider;
import com.gm.apps.security.core.authentication.AuthUserPrincipal;
import com.gm.apps.security.core.dto.UserDto;
import com.gm.apps.security.core.jwt.JwtUtil;
import com.gm.apps.security.core.oauth2.user.BasicOAuth2UserInfo;
import com.gm.apps.security.core.oauth2.user.OAuth2UserInfo;
import com.gm.apps.security.data.entities.User;
import com.gm.apps.security.data.entities.UserSession;
import com.gm.apps.security.data.service.UserService;
import com.gm.apps.security.data.service.UserSessionService;
import com.gm.apps.security.knowmail.oauth2.user.KnowmailOAuth2UserInfo;

public class OAuth2UserService extends DefaultOAuth2UserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2UserService.class);
  @Autowired
  private UserService userService;
  @Autowired
  private UserSessionService userSessionService;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private ModelMapper modelMapper;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
    LOGGER.debug("Loading oauth2 user...");
    String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
    AuthProvider provider = AuthProvider.fromValue(registrationId);

    if (AuthProvider.KNOWMAIL == provider || provider == AuthProvider.CUSTOM) {
      // throw new IllegalArgumentException("Illlllllllllll");
      return processCustomOAuth2Provider(oAuth2UserRequest);
    }

    // default OAuth2 client implementation
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
    try {
      OAuth2UserInfo oAuth2UserInfo = BasicOAuth2UserInfo.userFromAttributes(oAuth2User.getAttributes());
      AuthUserPrincipal<Long> principal = buildPrincipal(oAuth2UserInfo, oAuth2UserRequest);
      principal.setAttributes(oAuth2User.getAttributes());
      return principal;
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private OAuth2User processCustomOAuth2Provider(OAuth2UserRequest oAuth2UserRequest) {
    Assert.notNull(oAuth2UserRequest, "userRequest cannot be null");
    JwtUtil.DecodedJwtInfo decodedJwt = JwtUtil.decodeJwtToken(oAuth2UserRequest.getAccessToken().getTokenValue());
    KnowmailOAuth2UserInfo knowmailOAuth2UserInfo =
        new KnowmailOAuth2UserInfo(decodedJwt.getUniqueName(), decodedJwt.getAttributes());
    AuthUserPrincipal<Long> principal = buildPrincipal(knowmailOAuth2UserInfo, oAuth2UserRequest);
    principal.setAttributes(knowmailOAuth2UserInfo.getAttributes());
    return principal;
  }

  /**
   * Builds the security principal from the given userReqest. Registers the user if not already
   * registered.
   *
   * @param oAuth2AccessToken
   *
   */
  public AuthUserPrincipal<Long> buildPrincipal(OAuth2UserInfo oAuth2UserInfo,
      OAuth2UserRequest request) {
    String registrationId = request.getClientRegistration().getRegistrationId();
    String email = oAuth2UserInfo.getEmail();
    if (StringUtils.isEmpty(email)) {
      LOGGER.warn("Unable to get email address from OAuth2 Response creating custom user email");
      email = oAuth2UserInfo.getName().concat("@").concat(registrationId);
    }

    LOGGER.debug("Checking existing of existing user with email: {}", email);
    User user = createOrGetUser(registrationId, email, request.getAccessToken(), oAuth2UserInfo);
    updateUserSession(request, user);

    @SuppressWarnings("unchecked")
    UserDto<Long> userDto = modelMapper.map(user, UserDto.class);
    userDto.setUsername(email);
    userDto.setEmail(email);
    AuthUserPrincipal<Long> principal = new AuthUserPrincipal<>(userDto);
    principal.setName(userDto.getName());
    return principal;
  }

  private void updateUserSession(OAuth2UserRequest request, User user) {
    if (CollectionUtils.isEmpty(user.getUserSessions())) {
      LOGGER.debug("Session does not exists for user: {}", user.getEmail());
      createSessionForUser(request, user);
    } else {
      LOGGER.debug("Session already exists for user");
      final Instant currentTokenDate = request.getAccessToken().getIssuedAt();
      List<UserSession> obsolateSessions =
          user.getUserSessions().stream().filter(s -> isObsolateSessions(s, currentTokenDate))
              .collect(Collectors.toList());
      int totalUserSessions = null == obsolateSessions || obsolateSessions.isEmpty() ? 0 : obsolateSessions.size();
      boolean createSession = false;
      if (!CollectionUtils.isEmpty(obsolateSessions)) {
        LOGGER.info("Found multiple obsolete usersessions, count: {}", obsolateSessions.size());
        userSessionService.deleteInBatch(obsolateSessions);
        createSession = totalUserSessions - obsolateSessions.size() < 1;
      }
      if (createSession) {
        createSessionForUser(request, user);
      }
    }
  }

  private void createSessionForUser(OAuth2UserRequest request, User user) {
    UserSession userSession = new UserSession();
    userSession.setToken(request.getAccessToken().getTokenValue());
    userSession.setTokenIssuedAt(java.util.Date.from(request.getAccessToken().getIssuedAt()));
    user.addSession(userSession);
    userService.updateUser(user);
  }

  /**
   * Find obsolete user sessions.
   *
   * @param session existing user session
   * @param currentTokenDate current token issued date
   * @return
   */
  private boolean isObsolateSessions(UserSession session, Instant currentTokenDate) {
    return session.getTokenIssuedAt().before(java.util.Date.from(currentTokenDate));
  }

  /**
   * Create Or Get user from database
   *
   * @param oAuth2User
   *
   * @param registrationId - OAuth2 Client Id
   * @param oAuth2UserInfo - OAuth2 User info
   * @param email - email address
   * @return Instance of User object
   */
  private User createOrGetUser(String registrationId, String email, OAuth2AccessToken accessToken,
      OAuth2UserInfo oAuth2UserInfo) {
    return userService.findByEmail(email).orElseGet(() -> {
      AuthProvider provider = AuthProvider.fromValue(registrationId);
      LOGGER.info("User does not exists with email: {} creating new user", email);
      // register a new user
      User newUser = new User();
      newUser.setImageUrl(oAuth2UserInfo.getImageUrl());
      newUser.setEmail(email);
      newUser.setName(oAuth2UserInfo.getName());
      newUser.setProvider(provider);
      newUser.setProviderId(registrationId);
      newUser.setPassPhrase(passwordEncoder.encode(UUID.randomUUID().toString()));

      UserSession userSession = new UserSession();
      userSession.setTokenIssuedAt(java.util.Date.from(accessToken.getIssuedAt()));
      userSession.setToken(accessToken.getTokenValue());
      userSession.setTokenType((byte) provider.getValue());
      newUser.addSession(userSession);
      return userService.createUser(newUser);
    });
  }
}
