package com.gm.apps.security.data.service;

import java.util.Optional;
import javax.transaction.Transactional;
import com.gm.apps.security.data.entities.User;
import com.gm.apps.security.data.repository.UserRepository;

@Transactional
public class UserServiceImpl implements UserService {
  private final UserRepository repository;

  public UserServiceImpl(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public User createUser(User user) {
    return repository.save(user);
  }

  @Override
  public void updateUser(User user) {
    Optional<User> existingUsr = repository.findById(user.getId());
    if (!existingUsr.isPresent()) {
      return;
    }
    user.setId(existingUsr.get().getId());
    repository.save(user);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return repository.findByEmail(email);
  }
}
