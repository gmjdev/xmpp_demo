package com.gm.apps.security.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gm.apps.security.data.entities.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

}
