package com.asaf.runtime.controller;

import com.asaf.runtime.model.Vehicle;
import com.asaf.runtime.request.VehicleCreate;
import com.asaf.runtime.request.VehicleFilter;
import com.asaf.runtime.request.VehicleUpdate;
import com.asaf.runtime.response.PaginationResponse;
import com.asaf.runtime.security.UserSecurityContext;
import com.asaf.runtime.service.VehicleService;
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
@RequestMapping("Vehicle")
@Tag(name = "Vehicle")
public class VehicleController {

  @Autowired private VehicleService vehicleService;

  @PostMapping("getAllVehicles")
  @Operation(summary = "getAllVehicles", description = "lists Vehicles")
  public PaginationResponse<Vehicle> getAllVehicles(
      @Valid @RequestBody VehicleFilter vehicleFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return vehicleService.getAllVehicles(vehicleFilter, securityContext);
  }

  @PostMapping("createVehicle")
  @Operation(summary = "createVehicle", description = "Creates Vehicle")
  public Vehicle createVehicle(
      @Validated(Create.class) @RequestBody VehicleCreate vehicleCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return vehicleService.createVehicle(vehicleCreate, securityContext);
  }

  @PutMapping("updateVehicle")
  @Operation(summary = "updateVehicle", description = "Updates Vehicle")
  public Vehicle updateVehicle(
      @Validated(Update.class) @RequestBody VehicleUpdate vehicleUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return vehicleService.updateVehicle(vehicleUpdate, securityContext);
  }
}
