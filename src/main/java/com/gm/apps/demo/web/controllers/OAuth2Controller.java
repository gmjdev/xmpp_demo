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

package com.gm.apps.demo.web.controllers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {

  @GetMapping("/oauth2/redirect")
  @ResponseBody
  public ResponseEntity<?> authenticateUserClient(
      @RequestParam(required = false) String error) {
    if (StringUtils.hasText(error)) {
      Map<String, Object> err = new HashMap<>();
      err.put("success", false);
      err.put("message", error);
      return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }
    return ResponseEntity.ok("Hello Success Authentication");
  }
}
