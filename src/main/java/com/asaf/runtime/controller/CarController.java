package com.asaf.runtime.controller;

import com.asaf.runtime.model.Car;
import com.asaf.runtime.request.CarCreate;
import com.asaf.runtime.request.CarFilter;
import com.asaf.runtime.request.CarUpdate;
import com.asaf.runtime.response.PaginationResponse;
import com.asaf.runtime.security.UserSecurityContext;
import com.asaf.runtime.service.CarService;
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
@RequestMapping("Car")
@Tag(name = "Car")
public class CarController {

  @Autowired private CarService carService;

  @PostMapping("getAllCars")
  @Operation(summary = "getAllCars", description = "lists Cars")
  public PaginationResponse<Car> getAllCars(
      @Valid @RequestBody CarFilter carFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return carService.getAllCars(carFilter, securityContext);
  }

  @PostMapping("createCar")
  @Operation(summary = "createCar", description = "Creates Car")
  public Car createCar(
      @Validated(Create.class) @RequestBody CarCreate carCreate, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return carService.createCar(carCreate, securityContext);
  }

  @PutMapping("updateCar")
  @Operation(summary = "updateCar", description = "Updates Car")
  public Car updateCar(
      @Validated(Update.class) @RequestBody CarUpdate carUpdate, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return carService.updateCar(carUpdate, securityContext);
  }
}
