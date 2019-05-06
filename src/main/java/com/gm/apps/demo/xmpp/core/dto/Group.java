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

import java.util.List;
import lombok.ToString;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@ToString
public class Group {
  private String name;
  private String description;
  private List<String> admins;
  private List<String> members;
}
