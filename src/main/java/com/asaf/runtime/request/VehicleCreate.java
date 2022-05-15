package com.asaf.runtime.request;

/** Object Used to Create Vehicle */
public class VehicleCreate {

  private String description;

  private String name;

  /** @return description */
  public String getDescription() {
    return this.description;
  }

  /**
   * @param description description to set
   * @return VehicleCreate
   */
  public <T extends VehicleCreate> T setDescription(String description) {
    this.description = description;
    return (T) this;
  }

  /** @return name */
  public String getName() {
    return this.name;
  }

  /**
   * @param name name to set
   * @return VehicleCreate
   */
  public <T extends VehicleCreate> T setName(String name) {
    this.name = name;
    return (T) this;
  }
}
