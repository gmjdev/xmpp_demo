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

package com.gm.apps.demo.xmpp.core.services;

import java.util.List;

import com.gm.apps.demo.xmpp.core.dto.ChatParticipantDetail;
import com.gm.apps.demo.xmpp.core.dto.ChatRoomDetail;

public interface ChatRoomService {
  void create(ChatRoomDetail room);

  void update(ChatRoomDetail room);

  List<ChatRoomDetail> findAllRooms();

  ChatRoomDetail getRoomDetail(String room);

  List<ChatParticipantDetail> getRoomParticipants(String room);

  void addOwnerToRoom(String room, String owner);

  void removeRoomOwner(String room, String owner);

  void addAdminToRoom(String room, String admin);

  void removeRoomAdmin(String room, String admin);

  void addParticipant(String room, String participant);

  void removeParticipant(String room, String participant);

  void deleteRoom(String room);

}
