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
 * @Date May 1, 2019
 ******************************************************************************/

package com.gm.apps.demo.xmpp.core.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.GroupEntities;
import org.igniterealtime.restclient.entity.GroupEntity;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.gm.apps.demo.xmpp.core.dto.Group;
import com.gm.apps.demo.xmpp.core.services.ChatGroupService;

public class ChatGroupServiceImpl implements ChatGroupService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ChatGroupServiceImpl.class);
  private RestApiClient xmppApiClient;
  private ModelMapper modelMapper;

  public ChatGroupServiceImpl(RestApiClient xmppApiClient, ModelMapper modelMapper) {
    this.xmppApiClient = xmppApiClient;
    this.modelMapper = modelMapper;
  }

  @Override
  public void createGroup(Group group) {
    GroupEntity entity = modelMapper.map(group, GroupEntity.class);
    try (Response response = xmppApiClient.createGroup(entity)) {
      LOGGER.info("Create group : {}", response.getEntity());
      HttpStatus status = HttpStatus.resolve(response.getStatus());
      if (!status.is2xxSuccessful()) {
        LOGGER.error("Create group");
      }
    }
  }

  @Override
  public void deleteGroup(String group) {
    try (Response response = xmppApiClient.deleteGroup(group)) {
      LOGGER.info("Delete Group : {}", response.getEntity());
      HttpStatus status = HttpStatus.resolve(response.getStatus());
      if (!status.is2xxSuccessful()) {
        LOGGER.error("Delete Group");
      }
    }
  }

  @Override
  public Group getGroup(String groupName) {
    GroupEntity groupEntity = xmppApiClient.getGroup(groupName);
    return modelMapper.map(groupEntity, Group.class);
  }

  @Override
  public void updateGroup(Group group) {
    GroupEntity updatedGroupEntity = modelMapper.map(group, GroupEntity.class);
    try (Response response = xmppApiClient.updateGroup(updatedGroupEntity)) {
      LOGGER.info("Update group : {}", response.getEntity());
      HttpStatus status = HttpStatus.resolve(response.getStatus());
      if (!status.is2xxSuccessful()) {
        LOGGER.error("Update Group");
      }
    }
  }

  @Override
  public List<Group> findAllGroups() {
    GroupEntities groups = xmppApiClient.getGroups();
    if (null != groups && null != groups.getGroups() && !groups.getGroups().isEmpty()) {
      return groups.getGroups().stream().map(s -> modelMapper.map(s, Group.class)).collect(Collectors.toList());
    }
    return Collections.emptyList();
  }
}
