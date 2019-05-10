package com.gm.apps.security.data.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "userSessions")
@Getter
@Setter
public class UserSession extends BaseEntity<Long> {
  @ManyToOne
  private User user;
  @Column(length = 512, nullable = false)
  private String token;
  private byte tokenType;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "tokenIssuedAt", nullable = false)
  private Date tokenIssuedAt;
}
