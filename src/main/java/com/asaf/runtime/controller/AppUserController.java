package com.asaf.runtime.controller;

import com.asaf.runtime.model.AppUser;
import com.asaf.runtime.request.AppUserCreate;
import com.asaf.runtime.request.AppUserFilter;
import com.asaf.runtime.request.AppUserUpdate;
import com.asaf.runtime.response.PaginationResponse;
import com.asaf.runtime.security.UserSecurityContext;
import com.asaf.runtime.service.AppUserService;
import com.asaf.runtime.validation.Create;
import com.asaf.runtime.validation.Update;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("AppUser")
@Tag(name = "AppUser")
public class AppUserController {

  @Autowired private AppUserService appUserService;

  @PostMapping("getAllAppUsers")
  @Operation(summary = "getAllAppUsers", description = "lists AppUsers")
  public PaginationResponse<AppUser> getAllAppUsers(
      @Valid @RequestBody AppUserFilter appUserFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return appUserService.getAllAppUsers(appUserFilter, securityContext);
  }

  @PostMapping("createAppUser")
  @Operation(summary = "createAppUser", description = "Creates AppUser")
  public AppUser createAppUser(
      @Validated(Create.class) @RequestBody AppUserCreate appUserCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return appUserService.createAppUser(appUserCreate, securityContext);
  }

  @PutMapping("updateAppUser")
  @Operation(summary = "updateAppUser", description = "Updates AppUser")
  public AppUser updateAppUser(
      @Validated(Update.class) @RequestBody AppUserUpdate appUserUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return appUserService.updateAppUser(appUserUpdate, securityContext);
  }
}
