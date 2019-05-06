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
 * @Date May 5, 2019
 ******************************************************************************/

package com.gm.apps.web.utils;

import java.util.Base64;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.SerializationUtils;

public final class CookieUtil {
  private CookieUtil() {
  }

  public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();

    if (null != cookies && cookies.length > 0) {
      return Stream.of(cookies).filter(c -> name.equals(c.getName())).findFirst();
    }

    return Optional.empty();
  }

  public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, boolean secure) {
    Cookie cookie = new Cookie(name, value);
    cookie.setSecure(secure);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }

  public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
    Cookie[] cookies = request.getCookies();
    Optional<Cookie> cookie = Optional.empty();
    if (null != cookies && cookies.length > 0) {
      cookie = Stream.of(cookies).filter(c -> name.equals(c.getName())).findFirst();
    }
    
    if (cookie.isPresent()) {
      cookie.get().setValue("");
      cookie.get().setPath("/");
      cookie.get().setMaxAge(0);
      response.addCookie(cookie.get());
    }
  }

  public static String serialize(Object object) {
    return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
  }

  public static <T> T deserialize(Cookie cookie, Class<T> cls) {
    return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
  }
}
