package com.asaf.runtime.controller;

import com.asaf.runtime.model.CarOwner;
import com.asaf.runtime.request.CarOwnerCreate;
import com.asaf.runtime.request.CarOwnerFilter;
import com.asaf.runtime.request.CarOwnerUpdate;
import com.asaf.runtime.response.PaginationResponse;
import com.asaf.runtime.security.UserSecurityContext;
import com.asaf.runtime.service.CarOwnerService;
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
@RequestMapping("CarOwner")
@Tag(name = "CarOwner")
public class CarOwnerController {

  @Autowired private CarOwnerService carOwnerService;

  @PostMapping("getAllCarOwners")
  @Operation(summary = "getAllCarOwners", description = "lists CarOwners")
  public PaginationResponse<CarOwner> getAllCarOwners(
      @Valid @RequestBody CarOwnerFilter carOwnerFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return carOwnerService.getAllCarOwners(carOwnerFilter, securityContext);
  }

  @PutMapping("updateCarOwner")
  @Operation(summary = "updateCarOwner", description = "Updates CarOwner")
  public CarOwner updateCarOwner(
      @Validated(Update.class) @RequestBody CarOwnerUpdate carOwnerUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return carOwnerService.updateCarOwner(carOwnerUpdate, securityContext);
  }

  @PostMapping("createCarOwner")
  @Operation(summary = "createCarOwner", description = "Creates CarOwner")
  public CarOwner createCarOwner(
      @Validated(Create.class) @RequestBody CarOwnerCreate carOwnerCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return carOwnerService.createCarOwner(carOwnerCreate, securityContext);
  }
}
