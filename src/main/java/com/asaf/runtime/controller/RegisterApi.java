package com.asaf.runtime.controller;

import com.asaf.runtime.model.AppUser;
import com.asaf.runtime.request.AppUserCreate;
import com.asaf.runtime.service.AppUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "RegisterApi")
@RestController
public class RegisterApi {

  private final AppUserService appUserService;

  public RegisterApi(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @SecurityRequirements
  @PostMapping("register")
  public AppUser register(
      @RequestBody
          @org.springframework.validation.annotation.Validated(
              com.asaf.runtime.validation.Create.class)
          AppUserCreate appUserCreate) {
    return appUserService.createAppUser(appUserCreate, null);
  }
}
