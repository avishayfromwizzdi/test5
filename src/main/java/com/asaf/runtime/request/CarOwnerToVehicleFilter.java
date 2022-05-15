package com.asaf.runtime.request;

import com.asaf.runtime.model.CarOwner;
import com.asaf.runtime.model.Vehicle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.Min;

/** Object Used to List CarOwnerToVehicle */
@com.asaf.runtime.validation.IdValid.List({
  @com.asaf.runtime.validation.IdValid(
      targetField = "carOwner",
      field = "carOwnerIds",
      fieldType = com.asaf.runtime.model.CarOwner.class),
  @com.asaf.runtime.validation.IdValid(
      targetField = "vehicle",
      field = "vehicleIds",
      fieldType = com.asaf.runtime.model.Vehicle.class)
})
public class CarOwnerToVehicleFilter {

  @JsonIgnore private List<CarOwner> carOwner;

  private Set<String> carOwnerIds;

  @Min(value = 0)
  private Integer currentPage;

  private Set<String> id;

  @Min(value = 1)
  private Integer pageSize;

  @JsonIgnore private List<Vehicle> vehicle;

  private Set<String> vehicleIds;

  /** @return carOwner */
  @JsonIgnore
  public List<CarOwner> getCarOwner() {
    return this.carOwner;
  }

  /**
   * @param carOwner carOwner to set
   * @return CarOwnerToVehicleFilter
   */
  public <T extends CarOwnerToVehicleFilter> T setCarOwner(List<CarOwner> carOwner) {
    this.carOwner = carOwner;
    return (T) this;
  }

  /** @return carOwnerIds */
  public Set<String> getCarOwnerIds() {
    return this.carOwnerIds;
  }

  /**
   * @param carOwnerIds carOwnerIds to set
   * @return CarOwnerToVehicleFilter
   */
  public <T extends CarOwnerToVehicleFilter> T setCarOwnerIds(Set<String> carOwnerIds) {
    this.carOwnerIds = carOwnerIds;
    return (T) this;
  }

  /** @return currentPage */
  public Integer getCurrentPage() {
    return this.currentPage;
  }

  /**
   * @param currentPage currentPage to set
   * @return CarOwnerToVehicleFilter
   */
  public <T extends CarOwnerToVehicleFilter> T setCurrentPage(Integer currentPage) {
    this.currentPage = currentPage;
    return (T) this;
  }

  /** @return id */
  public Set<String> getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return CarOwnerToVehicleFilter
   */
  public <T extends CarOwnerToVehicleFilter> T setId(Set<String> id) {
    this.id = id;
    return (T) this;
  }

  /** @return pageSize */
  public Integer getPageSize() {
    return this.pageSize;
  }

  /**
   * @param pageSize pageSize to set
   * @return CarOwnerToVehicleFilter
   */
  public <T extends CarOwnerToVehicleFilter> T setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return (T) this;
  }

  /** @return vehicle */
  @JsonIgnore
  public List<Vehicle> getVehicle() {
    return this.vehicle;
  }

  /**
   * @param vehicle vehicle to set
   * @return CarOwnerToVehicleFilter
   */
  public <T extends CarOwnerToVehicleFilter> T setVehicle(List<Vehicle> vehicle) {
    this.vehicle = vehicle;
    return (T) this;
  }

  /** @return vehicleIds */
  public Set<String> getVehicleIds() {
    return this.vehicleIds;
  }

  /**
   * @param vehicleIds vehicleIds to set
   * @return CarOwnerToVehicleFilter
   */
  public <T extends CarOwnerToVehicleFilter> T setVehicleIds(Set<String> vehicleIds) {
    this.vehicleIds = vehicleIds;
    return (T) this;
  }
}
