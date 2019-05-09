package com.gm.apps.security.data.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.gm.apps.security.data.repository.UserRepository;
import com.gm.apps.security.data.repository.UserSessionRepository;
import com.gm.apps.security.data.service.UserService;
import com.gm.apps.security.data.service.UserServiceImpl;
import com.gm.apps.security.data.service.UserSessionService;
import com.gm.apps.security.data.service.UserSessionServiceImpl;

@Configuration
@EnableJpaRepositories(basePackages = {"com.gm.apps.security.data.repository"})
@EntityScan(basePackages = {"com.gm.apps.security.data.entities"})
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class SecurityDataConfig {

  @Bean
  public UserService userService(UserRepository repository) {
    return new UserServiceImpl(repository);
  }

  @Bean
  public UserSessionService userSessionService(UserSessionRepository repository) {
    return new UserSessionServiceImpl(repository);
  }

}
