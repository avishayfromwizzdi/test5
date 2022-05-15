package com.asaf.runtime.controller;

import com.asaf.runtime.model.CarOwnerToVehicle;
import com.asaf.runtime.request.CarOwnerToVehicleCreate;
import com.asaf.runtime.request.CarOwnerToVehicleFilter;
import com.asaf.runtime.request.CarOwnerToVehicleUpdate;
import com.asaf.runtime.response.PaginationResponse;
import com.asaf.runtime.security.UserSecurityContext;
import com.asaf.runtime.service.CarOwnerToVehicleService;
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
@RequestMapping("CarOwnerToVehicle")
@Tag(name = "CarOwnerToVehicle")
public class CarOwnerToVehicleController {

  @Autowired private CarOwnerToVehicleService carOwnerToVehicleService;

  @PostMapping("getAllCarOwnerToVehicles")
  @Operation(summary = "getAllCarOwnerToVehicles", description = "lists CarOwnerToVehicles")
  public PaginationResponse<CarOwnerToVehicle> getAllCarOwnerToVehicles(
      @Valid @RequestBody CarOwnerToVehicleFilter carOwnerToVehicleFilter,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return carOwnerToVehicleService.getAllCarOwnerToVehicles(
        carOwnerToVehicleFilter, securityContext);
  }

  @PutMapping("updateCarOwnerToVehicle")
  @Operation(summary = "updateCarOwnerToVehicle", description = "Updates CarOwnerToVehicle")
  public CarOwnerToVehicle updateCarOwnerToVehicle(
      @Validated(Update.class) @RequestBody CarOwnerToVehicleUpdate carOwnerToVehicleUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return carOwnerToVehicleService.updateCarOwnerToVehicle(
        carOwnerToVehicleUpdate, securityContext);
  }

  @PostMapping("createCarOwnerToVehicle")
  @Operation(summary = "createCarOwnerToVehicle", description = "Creates CarOwnerToVehicle")
  public CarOwnerToVehicle createCarOwnerToVehicle(
      @Validated(Create.class) @RequestBody CarOwnerToVehicleCreate carOwnerToVehicleCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return carOwnerToVehicleService.createCarOwnerToVehicle(
        carOwnerToVehicleCreate, securityContext);
  }
}
