package com.asaf.runtime.request;

import com.asaf.runtime.model.CarOwner;
import com.asaf.runtime.model.Vehicle;
import com.fasterxml.jackson.annotation.JsonIgnore;

/** Object Used to Create CarOwnerToVehicle */
@com.asaf.runtime.validation.IdValid.List({
  @com.asaf.runtime.validation.IdValid(
      targetField = "carOwner",
      field = "carOwnerId",
      fieldType = com.asaf.runtime.model.CarOwner.class,
      groups = {
        com.asaf.runtime.validation.Update.class,
        com.asaf.runtime.validation.Create.class
      }),
  @com.asaf.runtime.validation.IdValid(
      targetField = "vehicle",
      field = "vehicleId",
      fieldType = com.asaf.runtime.model.Vehicle.class,
      groups = {com.asaf.runtime.validation.Update.class, com.asaf.runtime.validation.Create.class})
})
public class CarOwnerToVehicleCreate {

  @JsonIgnore private CarOwner carOwner;

  private String carOwnerId;

  @JsonIgnore private Vehicle vehicle;

  private String vehicleId;

  /** @return carOwner */
  @JsonIgnore
  public CarOwner getCarOwner() {
    return this.carOwner;
  }

  /**
   * @param carOwner carOwner to set
   * @return CarOwnerToVehicleCreate
   */
  public <T extends CarOwnerToVehicleCreate> T setCarOwner(CarOwner carOwner) {
    this.carOwner = carOwner;
    return (T) this;
  }

  /** @return carOwnerId */
  public String getCarOwnerId() {
    return this.carOwnerId;
  }

  /**
   * @param carOwnerId carOwnerId to set
   * @return CarOwnerToVehicleCreate
   */
  public <T extends CarOwnerToVehicleCreate> T setCarOwnerId(String carOwnerId) {
    this.carOwnerId = carOwnerId;
    return (T) this;
  }

  /** @return vehicle */
  @JsonIgnore
  public Vehicle getVehicle() {
    return this.vehicle;
  }

  /**
   * @param vehicle vehicle to set
   * @return CarOwnerToVehicleCreate
   */
  public <T extends CarOwnerToVehicleCreate> T setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
    return (T) this;
  }

  /** @return vehicleId */
  public String getVehicleId() {
    return this.vehicleId;
  }

  /**
   * @param vehicleId vehicleId to set
   * @return CarOwnerToVehicleCreate
   */
  public <T extends CarOwnerToVehicleCreate> T setVehicleId(String vehicleId) {
    this.vehicleId = vehicleId;
    return (T) this;
  }
}
