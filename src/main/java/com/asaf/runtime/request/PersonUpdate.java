package com.asaf.runtime.request;

import com.asaf.runtime.model.Person;
import com.fasterxml.jackson.annotation.JsonIgnore;

/** Object Used to Update Person */
@com.asaf.runtime.validation.IdValid.List({
  @com.asaf.runtime.validation.IdValid(
      targetField = "person",
      field = "id",
      fieldType = com.asaf.runtime.model.Person.class,
      groups = {com.asaf.runtime.validation.Update.class})
})
public class PersonUpdate extends PersonCreate {

  private String id;

  @JsonIgnore private Person person;

  /** @return id */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return PersonUpdate
   */
  public <T extends PersonUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }

  /** @return person */
  @JsonIgnore
  public Person getPerson() {
    return this.person;
  }

  /**
   * @param person person to set
   * @return PersonUpdate
   */
  public <T extends PersonUpdate> T setPerson(Person person) {
    this.person = person;
    return (T) this;
  }
}
