package com.gm.apps.security.data.service;

import java.util.List;
import javax.transaction.Transactional;
import com.gm.apps.security.data.entities.UserSession;
import com.gm.apps.security.data.repository.UserSessionRepository;

@Transactional
public class UserSessionServiceImpl implements UserSessionService {
  private UserSessionRepository userSessionRepository;

  public UserSessionServiceImpl(UserSessionRepository userSessionRepository) {
    this.userSessionRepository = userSessionRepository;
  }

  @Override
  public void deleteInBatch(List<UserSession> sessions) {
    userSessionRepository.deleteAll(sessions);
  }
}
