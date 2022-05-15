package com.asaf.runtime.request;

import com.asaf.runtime.model.Car;
import com.fasterxml.jackson.annotation.JsonIgnore;

/** Object Used to Update Car */
@com.asaf.runtime.validation.IdValid.List({
  @com.asaf.runtime.validation.IdValid(
      targetField = "car",
      field = "id",
      fieldType = com.asaf.runtime.model.Car.class,
      groups = {com.asaf.runtime.validation.Update.class})
})
public class CarUpdate extends CarCreate {

  @JsonIgnore private Car car;

  private String id;

  /** @return car */
  @JsonIgnore
  public Car getCar() {
    return this.car;
  }

  /**
   * @param car car to set
   * @return CarUpdate
   */
  public <T extends CarUpdate> T setCar(Car car) {
    this.car = car;
    return (T) this;
  }

  /** @return id */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return CarUpdate
   */
  public <T extends CarUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
