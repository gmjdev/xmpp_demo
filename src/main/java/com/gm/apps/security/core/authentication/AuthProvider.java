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

package com.gm.apps.security.core.authentication;

import java.util.stream.Stream;

public enum AuthProvider {
  CUSTOM(-1), LOCAL(0), GOOGLE(1), GITHUB(2), KNOWMAIL(3), FACEBOOK(4);

  private final int value;

  private AuthProvider(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static AuthProvider fromValue(String provider) {
    return Stream.of(values()).filter(s -> provider.equalsIgnoreCase(s.name())).findFirst().orElse(CUSTOM);
  }
}
