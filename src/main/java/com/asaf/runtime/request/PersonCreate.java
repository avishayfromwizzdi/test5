package com.asaf.runtime.request;

/** Object Used to Create Person */
public class PersonCreate {

  private String description;

  private String name;

  /** @return description */
  public String getDescription() {
    return this.description;
  }

  /**
   * @param description description to set
   * @return PersonCreate
   */
  public <T extends PersonCreate> T setDescription(String description) {
    this.description = description;
    return (T) this;
  }

  /** @return name */
  public String getName() {
    return this.name;
  }

  /**
   * @param name name to set
   * @return PersonCreate
   */
  public <T extends PersonCreate> T setName(String name) {
    this.name = name;
    return (T) this;
  }
}
