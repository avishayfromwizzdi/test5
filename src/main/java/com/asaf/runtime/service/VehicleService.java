package com.asaf.runtime.service;

import com.asaf.runtime.data.VehicleRepository;
import com.asaf.runtime.model.Vehicle;
import com.asaf.runtime.request.VehicleCreate;
import com.asaf.runtime.request.VehicleFilter;
import com.asaf.runtime.request.VehicleUpdate;
import com.asaf.runtime.response.PaginationResponse;
import com.asaf.runtime.security.UserSecurityContext;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VehicleService {

  @Autowired private VehicleRepository repository;

  /**
   * @param vehicleCreate Object Used to Create Vehicle
   * @param securityContext
   * @return created Vehicle
   */
  public Vehicle createVehicle(VehicleCreate vehicleCreate, UserSecurityContext securityContext) {
    Vehicle vehicle = createVehicleNoMerge(vehicleCreate, securityContext);
    this.repository.merge(vehicle);
    return vehicle;
  }

  /**
   * @param vehicleCreate Object Used to Create Vehicle
   * @param securityContext
   * @return created Vehicle unmerged
   */
  public Vehicle createVehicleNoMerge(
      VehicleCreate vehicleCreate, UserSecurityContext securityContext) {
    Vehicle vehicle = new Vehicle();
    vehicle.setId(UUID.randomUUID().toString());
    updateVehicleNoMerge(vehicle, vehicleCreate);

    if (securityContext != null) {
      vehicle.setCreator(securityContext.getUser());
    }

    return vehicle;
  }

  /**
   * @param vehicleCreate Object Used to Create Vehicle
   * @param vehicle
   * @return if vehicle was updated
   */
  public boolean updateVehicleNoMerge(Vehicle vehicle, VehicleCreate vehicleCreate) {
    boolean update = false;

    if (vehicleCreate.getName() != null && (!vehicleCreate.getName().equals(vehicle.getName()))) {
      vehicle.setName(vehicleCreate.getName());
      update = true;
    }

    if (vehicleCreate.getDescription() != null
        && (!vehicleCreate.getDescription().equals(vehicle.getDescription()))) {
      vehicle.setDescription(vehicleCreate.getDescription());
      update = true;
    }

    return update;
  }
  /**
   * @param vehicleUpdate
   * @param securityContext
   * @return vehicle
   */
  public Vehicle updateVehicle(VehicleUpdate vehicleUpdate, UserSecurityContext securityContext) {
    Vehicle vehicle = vehicleUpdate.getVehicle();
    if (updateVehicleNoMerge(vehicle, vehicleUpdate)) {
      this.repository.merge(vehicle);
    }
    return vehicle;
  }

  /**
   * @param vehicleFilter Object Used to List Vehicle
   * @param securityContext
   * @return PaginationResponse<Vehicle> containing paging information for Vehicle
   */
  public PaginationResponse<Vehicle> getAllVehicles(
      VehicleFilter vehicleFilter, UserSecurityContext securityContext) {
    List<Vehicle> list = listAllVehicles(vehicleFilter, securityContext);
    long count = this.repository.countAllVehicles(vehicleFilter, securityContext);
    return new PaginationResponse<>(list, vehicleFilter.getPageSize(), count);
  }

  /**
   * @param vehicleFilter Object Used to List Vehicle
   * @param securityContext
   * @return List of Vehicle
   */
  public List<Vehicle> listAllVehicles(
      VehicleFilter vehicleFilter, UserSecurityContext securityContext) {
    return this.repository.listAllVehicles(vehicleFilter, securityContext);
  }

  public <T, I> List<T> listByIds(Class<T> c, SingularAttribute<T, I> idField, Set<I> ids) {
    return repository.listByIds(c, idField, ids);
  }

  public <T, I> T getByIdOrNull(Class<T> c, SingularAttribute<T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public void merge(java.lang.Object base) {
    this.repository.merge(base);
  }

  public void massMerge(List<?> toMerge) {
    this.repository.massMerge(toMerge);
  }
}
