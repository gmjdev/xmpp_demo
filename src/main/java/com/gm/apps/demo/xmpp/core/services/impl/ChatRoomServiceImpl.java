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

import javax.ws.rs.core.Response;

import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.MUCRoomEntities;
import org.igniterealtime.restclient.entity.MUCRoomEntity;
import org.igniterealtime.restclient.entity.ParticipantEntities;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.gm.apps.demo.xmpp.core.dto.ChatParticipantDetail;
import com.gm.apps.demo.xmpp.core.dto.ChatRoomDetail;
import com.gm.apps.demo.xmpp.core.services.ChatRoomService;

public class ChatRoomServiceImpl implements ChatRoomService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ChatRoomServiceImpl.class);
  private RestApiClient xmppApiClient;
  private ModelMapper modelMapper;

  public ChatRoomServiceImpl(RestApiClient xmppApiClient, ModelMapper modelMapper) {
    this.xmppApiClient = xmppApiClient;
    this.modelMapper = modelMapper;
  }

  @Override
  public List<ChatRoomDetail> findAllRooms() {
    MUCRoomEntities entities = xmppApiClient.getChatRooms();
    if (null != entities && null != entities.getMucRooms() && !entities.getMucRooms().isEmpty()) {
      return entities.getMucRooms().stream().map(s -> modelMapper.map(s, ChatRoomDetail.class))
          .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  @Override
  public ChatRoomDetail getRoomDetail(String room) {
    MUCRoomEntity roomEntity = xmppApiClient.getChatRoom(room);
    return modelMapper.map(roomEntity, ChatRoomDetail.class);
  }

  @Override
  public List<ChatParticipantDetail> getRoomParticipants(String room) {
    ParticipantEntities participantEntities = xmppApiClient.getChatRoomParticipants(room);
    if (null != participantEntities && null != participantEntities.getParticipants()
        && !participantEntities.getParticipants().isEmpty()) {
      return participantEntities.getParticipants().stream().map(s -> modelMapper.map(s, ChatParticipantDetail.class))
          .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  @Override
  public void addOwnerToRoom(String room, String ownerId) {
    Response response = xmppApiClient.addOwner(room, ownerId);
    LOGGER.info("Add owner to room : {}", response.getEntity());
    HttpStatus status = HttpStatus.resolve(response.getStatus());
    if (!status.is2xxSuccessful()) {
      LOGGER.error("Add owner to room");
    }
  }

  @Override
  public void addAdminToRoom(String room, String adminId) {
    Response response = xmppApiClient.addAdmin(room, adminId);
    LOGGER.info("Add admin to room : {}", response.getEntity());
    HttpStatus status = HttpStatus.resolve(response.getStatus());
    if (!status.is2xxSuccessful()) {
      LOGGER.error("Add Admin to room");
    }
  }

  @Override
  public void addParticipant(String room, String participant) {
    Response response = xmppApiClient.addMember(room, participant);
    LOGGER.info("Add participant : {}", response.getEntity());
    HttpStatus status = HttpStatus.resolve(response.getStatus());
    if (!status.is2xxSuccessful()) {
      LOGGER.error("Add participant");
    }
  }

  @Override
  public void deleteRoom(String room) {
    Response response = xmppApiClient.deleteChatRoom(room);
    LOGGER.info("Delete Room : {}", response.getEntity());
    HttpStatus status = HttpStatus.resolve(response.getStatus());
    if (!status.is2xxSuccessful()) {
      LOGGER.error("Delete room");
    }
  }

  @Override
  public void removeParticipant(String room, String participant) {
    Response response = xmppApiClient.deleteMember(room, participant);
    LOGGER.info("Remove Participants : {}", response.getEntity());
    HttpStatus status = HttpStatus.resolve(response.getStatus());
    if (!status.is2xxSuccessful()) {
      LOGGER.error("Remove participant to room");
    }
  }

  @Override
  public void removeRoomAdmin(String room, String admin) {
    Response response = xmppApiClient.deleteAdmin(room, admin);
    LOGGER.info("Remove Participants : {}", response.getEntity());
    HttpStatus status = HttpStatus.resolve(response.getStatus());
    if (!status.is2xxSuccessful()) {
      LOGGER.error("Remove Participants Admin");
    }
  }

  @Override
  public void removeRoomOwner(String room, String owner) {
    Response response = xmppApiClient.deleteOwner(room, owner);
    LOGGER.info("Remove Participants : {}", response.getEntity());
    HttpStatus status = HttpStatus.resolve(response.getStatus());
    if (!status.is2xxSuccessful()) {
      LOGGER.error("Remove Participants Room Owner");
    }
  }

  @Override
  public void create(ChatRoomDetail room) {
    MUCRoomEntity roomEntity = modelMapper.map(room, MUCRoomEntity.class);
    Response response = xmppApiClient.createChatRoom(roomEntity);
    HttpStatus status = HttpStatus.resolve(response.getStatus());
    if (!status.is2xxSuccessful()) {
      LOGGER.error("Error while creating room: {}", response.getEntity());
    }
  }

  @Override
  public void update(ChatRoomDetail room) {
    MUCRoomEntity roomEntity = modelMapper.map(room, MUCRoomEntity.class);
    Response response = xmppApiClient.createChatRoom(roomEntity);
    HttpStatus status = HttpStatus.resolve(response.getStatus());
    if (!status.is2xxSuccessful()) {
      LOGGER.error("Error while updating room: {}", response.getEntity());
    }
  }
}
