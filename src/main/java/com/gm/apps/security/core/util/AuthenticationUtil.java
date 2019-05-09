package com.gm.apps.security.core.util;

import java.io.Serializable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.gm.apps.security.core.authentication.AuthUserPrincipal;
import com.gm.apps.security.core.dto.UserDto;

public final class AuthenticationUtil {
  private AuthenticationUtil() {}

  /**
   * Gets the current-user
   */
  public static <K extends Serializable> UserDto<K> currentUser() {
    Authentication auth = SecurityContextHolder
        .getContext().getAuthentication();
    return AuthenticationUtil.currentUser(auth);
  }

  public static <K extends Serializable> UserDto<K> currentUser(Authentication auth) {
    if (auth != null) {
      Object principal = auth.getPrincipal();
      if (principal instanceof AuthUserPrincipal<?>) {
        @SuppressWarnings("unchecked")
        AuthUserPrincipal<K> authUserPrincipal = (AuthUserPrincipal<K>) principal;
        return authUserPrincipal.currentUser();
      }
    }
    return null;
  }

  /**
   * Throws AccessDeniedException is not authorized
   *
   * @param authorized
   * @param message
   */
  public static void ensureAuthority(boolean authorized, String message) {
    if (!authorized) {
      throw new AccessDeniedException(message);
    }
  }

}
