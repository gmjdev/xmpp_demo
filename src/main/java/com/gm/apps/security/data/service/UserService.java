package com.gm.apps.security.data.service;

import java.util.Optional;
import com.gm.apps.security.core.authentication.AuthUserService;
import com.gm.apps.security.data.entities.User;

public interface UserService extends AuthUserService {
  User createUser(User user);

  void updateUser(User user);

  Optional<User> findByEmail(String email);
}
