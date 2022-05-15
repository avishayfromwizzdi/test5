package com.asaf.runtime.request;

import com.asaf.runtime.model.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

/** Object Used to Update AppUser */
@com.asaf.runtime.validation.IdValid.List({
  @com.asaf.runtime.validation.IdValid(
      targetField = "appUser",
      field = "id",
      fieldType = com.asaf.runtime.model.AppUser.class,
      groups = {com.asaf.runtime.validation.Update.class})
})
public class AppUserUpdate extends AppUserCreate {

  @JsonIgnore private AppUser appUser;

  private String id;

  /** @return appUser */
  @JsonIgnore
  public AppUser getAppUser() {
    return this.appUser;
  }

  /**
   * @param appUser appUser to set
   * @return AppUserUpdate
   */
  public <T extends AppUserUpdate> T setAppUser(AppUser appUser) {
    this.appUser = appUser;
    return (T) this;
  }

  /** @return id */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return AppUserUpdate
   */
  public <T extends AppUserUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
