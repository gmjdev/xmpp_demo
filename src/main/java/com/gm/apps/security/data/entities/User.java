package com.gm.apps.security.data.entities;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gm.apps.security.core.authentication.AuthProvider;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
public class User extends BaseEntity<Long> {
  @Column(nullable = false)
  private String name;
  @Email
  @Column(nullable = false)
  private String email;
  private String imageUrl;
  @Column(nullable = false)
  private Boolean emailVerified = false;
  @JsonIgnore
  private String passPhrase;
  @NotNull
  @Enumerated(EnumType.STRING)
  private AuthProvider provider;
  private String providerId;
  @OneToMany(
      mappedBy = "user",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private Set<UserSession> userSessions = new HashSet<>();

  public void addSession(UserSession userSession) {
    userSessions.add(userSession);
    userSession.setUser(this);
  }
}
