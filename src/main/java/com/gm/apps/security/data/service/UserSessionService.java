package com.gm.apps.security.data.service;

import java.util.List;
import com.gm.apps.security.data.entities.UserSession;

public interface UserSessionService {
  void deleteInBatch(List<UserSession> sessions);
}
