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

package com.gm.apps.demo.xmpp.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gm.apps.demo.xmpp.core.dto.ChatParticipantDetail;
import com.gm.apps.demo.xmpp.core.dto.ChatRoomDetail;
import com.gm.apps.demo.xmpp.core.dto.ChatRoomParticipant;
import com.gm.apps.demo.xmpp.core.services.ChatRoomService;

@RestController
@RequestMapping("/chat-rooms")
public class ChatRoomController {
  @Autowired
  private ChatRoomService chatRoomService;

  @GetMapping
  public List<ChatRoomDetail> findAllRooms() {
    return chatRoomService.findAllRooms();
  }

  @GetMapping("/{room}")
  public ChatRoomDetail getRoomDetail(@PathVariable String room) {
    return chatRoomService.getRoomDetail(room);
  }

  @GetMapping("/{room}/participants")
  public List<ChatParticipantDetail> getRoomParticipants(@PathVariable String room) {
    return chatRoomService.getRoomParticipants(room);
  }

  @PostMapping("/owner-participant")
  public void addOwnerToRoom(@Valid @RequestBody ChatRoomParticipant owner) {
    chatRoomService.addOwnerToRoom(owner.getRoom(), owner.getParticipant());
  }

  @DeleteMapping("/{room}/{owner}")
  public void removeRoomOwner(@PathVariable String room, @PathVariable String owner) {
    chatRoomService.removeRoomOwner(room, owner);
  }

  @PostMapping("/admin-participant")
  public void addAdminToRoom(@Valid @RequestBody ChatRoomParticipant participant) {
    chatRoomService.addAdminToRoom(participant.getRoom(), participant.getParticipant());
  }

  @DeleteMapping("/{room}/{admin}")
  public void removeRoomAdmin(@PathVariable String room, @PathVariable String admin) {
    chatRoomService.removeRoomAdmin(room, admin);
  }

  @PostMapping("/participant")
  public void addParticipant(@Valid @RequestBody ChatRoomParticipant participant) {
    chatRoomService.addParticipant(participant.getRoom(), participant.getParticipant());
  }

  @DeleteMapping("/{room}/{participant}")
  public void removeParticipant(@PathVariable String room, @PathVariable String participant) {
    chatRoomService.removeParticipant(room, participant);
  }

  @DeleteMapping("/{room}")
  public void deleteRoom(@PathVariable String room) {
    chatRoomService.deleteRoom(room);
  }

  @PostMapping
  public void createRoom(@Valid @RequestBody ChatRoomDetail chatRoomDetail) {
    chatRoomService.create(chatRoomDetail);
  }

  @PutMapping("/{room}")
  public void createRoom(@PathVariable String room, @Valid @RequestBody ChatRoomDetail chatRoomDetail) {
    chatRoomService.create(chatRoomDetail);
  }
}
