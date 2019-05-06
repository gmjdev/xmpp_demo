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

package com.gm.apps.demo.xmpp.core.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatRoomDetail {
  private String roomName;
  private String description;
  private String password;
  private String subject;
  private String naturalName;

  private int maxUsers;

  private Date creationDate;
  private Date modificationDate;

  private boolean persistent;
  private boolean publicRoom;
  private boolean registrationEnabled;
  private boolean canAnyoneDiscoverJID;
  private boolean canOccupantsChangeSubject;
  private boolean canOccupantsInvite;
  private boolean canChangeNickname;
  private boolean logEnabled;
  private boolean loginRestrictedToNickname;
  private boolean membersOnly;
  private boolean moderated;

  private List<String> broadcastPresenceRoles;

  private List<String> owners;
  private List<String> ownerGroups;

  private List<String> admins;
  private List<String> adminGroups;

  private List<String> members;
  private List<String> memberGroups;

  private List<String> outcasts;
  private List<String> outcastGroups;
}
