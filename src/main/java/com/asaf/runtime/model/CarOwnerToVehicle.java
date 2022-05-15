package com.asaf.runtime.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CarOwnerToVehicle {

  @ManyToOne(targetEntity = CarOwner.class)
  private CarOwner carOwner;

  @ManyToOne(targetEntity = Vehicle.class)
  private Vehicle vehicle;

  @Id private String id;

  /** @return carOwner */
  @ManyToOne(targetEntity = CarOwner.class)
  public CarOwner getCarOwner() {
    return this.carOwner;
  }

  /**
   * @param carOwner carOwner to set
   * @return CarOwnerToVehicle
   */
  public <T extends CarOwnerToVehicle> T setCarOwner(CarOwner carOwner) {
    this.carOwner = carOwner;
    return (T) this;
  }

  /** @return vehicle */
  @ManyToOne(targetEntity = Vehicle.class)
  public Vehicle getVehicle() {
    return this.vehicle;
  }

  /**
   * @param vehicle vehicle to set
   * @return CarOwnerToVehicle
   */
  public <T extends CarOwnerToVehicle> T setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
    return (T) this;
  }

  /** @return id */
  @Id
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return CarOwnerToVehicle
   */
  public <T extends CarOwnerToVehicle> T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
