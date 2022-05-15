package com.asaf.runtime.request;

import com.asaf.runtime.model.Motorcycle;
import com.fasterxml.jackson.annotation.JsonIgnore;

/** Object Used to Update Motorcycle */
@com.asaf.runtime.validation.IdValid.List({
  @com.asaf.runtime.validation.IdValid(
      targetField = "motorcycle",
      field = "id",
      fieldType = com.asaf.runtime.model.Motorcycle.class,
      groups = {com.asaf.runtime.validation.Update.class})
})
public class MotorcycleUpdate extends MotorcycleCreate {

  private String id;

  @JsonIgnore private Motorcycle motorcycle;

  /** @return id */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return MotorcycleUpdate
   */
  public <T extends MotorcycleUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }

  /** @return motorcycle */
  @JsonIgnore
  public Motorcycle getMotorcycle() {
    return this.motorcycle;
  }

  /**
   * @param motorcycle motorcycle to set
   * @return MotorcycleUpdate
   */
  public <T extends MotorcycleUpdate> T setMotorcycle(Motorcycle motorcycle) {
    this.motorcycle = motorcycle;
    return (T) this;
  }
}
