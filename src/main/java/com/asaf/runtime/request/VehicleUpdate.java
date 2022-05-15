package com.asaf.runtime.request;

import com.asaf.runtime.model.Vehicle;
import com.fasterxml.jackson.annotation.JsonIgnore;

/** Object Used to Update Vehicle */
@com.asaf.runtime.validation.IdValid.List({
  @com.asaf.runtime.validation.IdValid(
      targetField = "vehicle",
      field = "id",
      fieldType = com.asaf.runtime.model.Vehicle.class,
      groups = {com.asaf.runtime.validation.Update.class})
})
public class VehicleUpdate extends VehicleCreate {

  private String id;

  @JsonIgnore private Vehicle vehicle;

  /** @return id */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return VehicleUpdate
   */
  public <T extends VehicleUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }

  /** @return vehicle */
  @JsonIgnore
  public Vehicle getVehicle() {
    return this.vehicle;
  }

  /**
   * @param vehicle vehicle to set
   * @return VehicleUpdate
   */
  public <T extends VehicleUpdate> T setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
    return (T) this;
  }
}
