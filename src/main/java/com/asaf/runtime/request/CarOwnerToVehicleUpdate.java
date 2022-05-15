package com.asaf.runtime.request;

import com.asaf.runtime.model.CarOwnerToVehicle;
import com.fasterxml.jackson.annotation.JsonIgnore;

/** Object Used to Update CarOwnerToVehicle */
@com.asaf.runtime.validation.IdValid.List({
  @com.asaf.runtime.validation.IdValid(
      targetField = "carOwnerToVehicle",
      field = "id",
      fieldType = com.asaf.runtime.model.CarOwnerToVehicle.class,
      groups = {com.asaf.runtime.validation.Update.class})
})
public class CarOwnerToVehicleUpdate extends CarOwnerToVehicleCreate {

  @JsonIgnore private CarOwnerToVehicle carOwnerToVehicle;

  private String id;

  /** @return carOwnerToVehicle */
  @JsonIgnore
  public CarOwnerToVehicle getCarOwnerToVehicle() {
    return this.carOwnerToVehicle;
  }

  /**
   * @param carOwnerToVehicle carOwnerToVehicle to set
   * @return CarOwnerToVehicleUpdate
   */
  public <T extends CarOwnerToVehicleUpdate> T setCarOwnerToVehicle(
      CarOwnerToVehicle carOwnerToVehicle) {
    this.carOwnerToVehicle = carOwnerToVehicle;
    return (T) this;
  }

  /** @return id */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return CarOwnerToVehicleUpdate
   */
  public <T extends CarOwnerToVehicleUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
