package com.asaf.runtime.request;

import com.asaf.runtime.model.CarOwner;
import com.fasterxml.jackson.annotation.JsonIgnore;

/** Object Used to Update CarOwner */
@com.asaf.runtime.validation.IdValid.List({
  @com.asaf.runtime.validation.IdValid(
      targetField = "carOwner",
      field = "id",
      fieldType = com.asaf.runtime.model.CarOwner.class,
      groups = {com.asaf.runtime.validation.Update.class})
})
public class CarOwnerUpdate extends CarOwnerCreate {

  @JsonIgnore private CarOwner carOwner;

  private String id;

  /** @return carOwner */
  @JsonIgnore
  public CarOwner getCarOwner() {
    return this.carOwner;
  }

  /**
   * @param carOwner carOwner to set
   * @return CarOwnerUpdate
   */
  public <T extends CarOwnerUpdate> T setCarOwner(CarOwner carOwner) {
    this.carOwner = carOwner;
    return (T) this;
  }

  /** @return id */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return CarOwnerUpdate
   */
  public <T extends CarOwnerUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
