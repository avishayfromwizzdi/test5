package com.asaf.runtime.security;

import com.asaf.runtime.model.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserSecurityContext implements UserDetails {
  private final AppUser appUser;

  public UserSecurityContext(AppUser appUser) {
    this.appUser = appUser;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @JsonIgnore
  @Override
  public String getPassword() {
    return appUser.getPassword();
  }

  @Override
  public String getUsername() {
    return appUser.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public String getId() {
    return appUser.getId();
  }

  public AppUser getUser() {
    return appUser;
  }
}
