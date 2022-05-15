package com.asaf.runtime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AppUser {

  @Id private String id;

  @JsonIgnore private String password;

  private String username;

  /** @return id */
  @Id
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return AppUser
   */
  public <T extends AppUser> T setId(String id) {
    this.id = id;
    return (T) this;
  }

  /** @return password */
  @JsonIgnore
  public String getPassword() {
    return this.password;
  }

  /**
   * @param password password to set
   * @return AppUser
   */
  public <T extends AppUser> T setPassword(String password) {
    this.password = password;
    return (T) this;
  }

  /** @return username */
  public String getUsername() {
    return this.username;
  }

  /**
   * @param username username to set
   * @return AppUser
   */
  public <T extends AppUser> T setUsername(String username) {
    this.username = username;
    return (T) this;
  }
}
