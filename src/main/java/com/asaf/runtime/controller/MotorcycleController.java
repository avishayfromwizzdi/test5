package com.asaf.runtime.controller;

import com.asaf.runtime.model.Motorcycle;
import com.asaf.runtime.request.MotorcycleCreate;
import com.asaf.runtime.request.MotorcycleFilter;
import com.asaf.runtime.request.MotorcycleUpdate;
import com.asaf.runtime.response.PaginationResponse;
import com.asaf.runtime.security.UserSecurityContext;
import com.asaf.runtime.service.MotorcycleService;
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
@RequestMapping("Motorcycle")
@Tag(name = "Motorcycle")
public class MotorcycleController {

  @Autowired private MotorcycleService motorcycleService;

  @PostMapping("getAllMotorcycles")
  @Operation(summary = "getAllMotorcycles", description = "lists Motorcycles")
  public PaginationResponse<Motorcycle> getAllMotorcycles(
      @Valid @RequestBody MotorcycleFilter motorcycleFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return motorcycleService.getAllMotorcycles(motorcycleFilter, securityContext);
  }

  @PutMapping("updateMotorcycle")
  @Operation(summary = "updateMotorcycle", description = "Updates Motorcycle")
  public Motorcycle updateMotorcycle(
      @Validated(Update.class) @RequestBody MotorcycleUpdate motorcycleUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return motorcycleService.updateMotorcycle(motorcycleUpdate, securityContext);
  }

  @PostMapping("createMotorcycle")
  @Operation(summary = "createMotorcycle", description = "Creates Motorcycle")
  public Motorcycle createMotorcycle(
      @Validated(Create.class) @RequestBody MotorcycleCreate motorcycleCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return motorcycleService.createMotorcycle(motorcycleCreate, securityContext);
  }
}
