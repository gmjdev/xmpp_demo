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
 * @Date 19-Apr-2019
 ******************************************************************************/

package com.gm.apps.demo.xmpp.core.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.UserEntities;
import org.igniterealtime.restclient.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gm.apps.demo.xmpp.core.dto.User;
import com.gm.apps.demo.xmpp.core.services.ChatUserService;

public class ChatUserServiceImpl implements ChatUserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ChatGroupServiceImpl.class);
  private RestApiClient xmppApiClient;
  private ModelMapper modelMapper;

  public ChatUserServiceImpl(RestApiClient xmppApiClient, ModelMapper modelMapper) {
    this.xmppApiClient = xmppApiClient;
    this.modelMapper = modelMapper;
  }

  @Override
  public void createUser(User user) {
    UserEntity userEntity = modelMapper.map(user, UserEntity.class);
    xmppApiClient.createUser(userEntity);
  }

  @Override
  public void deleteUser(String user) {
    xmppApiClient.deleteUser(user);
  }

  @Override
  public void updateUser(User user) {
    UserEntity entity = modelMapper.map(user, UserEntity.class);
    xmppApiClient.updateUser(entity);
  }

  @Override
  public List<User> findAllUsers() {
    UserEntities entities = xmppApiClient.getUsers();
    if (null != entities && null != entities.getUsers() && !entities.getUsers().isEmpty()) {
      return entities.getUsers().stream().map(x -> modelMapper.map(x, User.class)).collect(Collectors.toList());
    }
    return Collections.emptyList();
  }
}
