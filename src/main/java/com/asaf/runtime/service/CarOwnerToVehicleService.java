package com.asaf.runtime.service;

import com.asaf.runtime.data.CarOwnerToVehicleRepository;
import com.asaf.runtime.model.CarOwnerToVehicle;
import com.asaf.runtime.request.CarOwnerToVehicleCreate;
import com.asaf.runtime.request.CarOwnerToVehicleFilter;
import com.asaf.runtime.request.CarOwnerToVehicleUpdate;
import com.asaf.runtime.response.PaginationResponse;
import com.asaf.runtime.security.UserSecurityContext;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarOwnerToVehicleService {

  @Autowired private CarOwnerToVehicleRepository repository;

  /**
   * @param carOwnerToVehicleCreate Object Used to Create CarOwnerToVehicle
   * @param securityContext
   * @return created CarOwnerToVehicle
   */
  public CarOwnerToVehicle createCarOwnerToVehicle(
      CarOwnerToVehicleCreate carOwnerToVehicleCreate, UserSecurityContext securityContext) {
    CarOwnerToVehicle carOwnerToVehicle =
        createCarOwnerToVehicleNoMerge(carOwnerToVehicleCreate, securityContext);
    this.repository.merge(carOwnerToVehicle);
    return carOwnerToVehicle;
  }

  /**
   * @param carOwnerToVehicleCreate Object Used to Create CarOwnerToVehicle
   * @param securityContext
   * @return created CarOwnerToVehicle unmerged
   */
  public CarOwnerToVehicle createCarOwnerToVehicleNoMerge(
      CarOwnerToVehicleCreate carOwnerToVehicleCreate, UserSecurityContext securityContext) {
    CarOwnerToVehicle carOwnerToVehicle = new CarOwnerToVehicle();
    carOwnerToVehicle.setId(UUID.randomUUID().toString());
    updateCarOwnerToVehicleNoMerge(carOwnerToVehicle, carOwnerToVehicleCreate);

    return carOwnerToVehicle;
  }

  /**
   * @param carOwnerToVehicleCreate Object Used to Create CarOwnerToVehicle
   * @param carOwnerToVehicle
   * @return if carOwnerToVehicle was updated
   */
  public boolean updateCarOwnerToVehicleNoMerge(
      CarOwnerToVehicle carOwnerToVehicle, CarOwnerToVehicleCreate carOwnerToVehicleCreate) {
    boolean update = false;

    if (carOwnerToVehicleCreate.getVehicle() != null
        && (carOwnerToVehicle.getVehicle() == null
            || !carOwnerToVehicleCreate
                .getVehicle()
                .getId()
                .equals(carOwnerToVehicle.getVehicle().getId()))) {
      carOwnerToVehicle.setVehicle(carOwnerToVehicleCreate.getVehicle());
      update = true;
    }

    if (carOwnerToVehicleCreate.getCarOwner() != null
        && (carOwnerToVehicle.getCarOwner() == null
            || !carOwnerToVehicleCreate
                .getCarOwner()
                .getId()
                .equals(carOwnerToVehicle.getCarOwner().getId()))) {
      carOwnerToVehicle.setCarOwner(carOwnerToVehicleCreate.getCarOwner());
      update = true;
    }

    return update;
  }
  /**
   * @param carOwnerToVehicleUpdate
   * @param securityContext
   * @return carOwnerToVehicle
   */
  public CarOwnerToVehicle updateCarOwnerToVehicle(
      CarOwnerToVehicleUpdate carOwnerToVehicleUpdate, UserSecurityContext securityContext) {
    CarOwnerToVehicle carOwnerToVehicle = carOwnerToVehicleUpdate.getCarOwnerToVehicle();
    if (updateCarOwnerToVehicleNoMerge(carOwnerToVehicle, carOwnerToVehicleUpdate)) {
      this.repository.merge(carOwnerToVehicle);
    }
    return carOwnerToVehicle;
  }

  /**
   * @param carOwnerToVehicleFilter Object Used to List CarOwnerToVehicle
   * @param securityContext
   * @return PaginationResponse<CarOwnerToVehicle> containing paging information for
   *     CarOwnerToVehicle
   */
  public PaginationResponse<CarOwnerToVehicle> getAllCarOwnerToVehicles(
      CarOwnerToVehicleFilter carOwnerToVehicleFilter, UserSecurityContext securityContext) {
    List<CarOwnerToVehicle> list =
        listAllCarOwnerToVehicles(carOwnerToVehicleFilter, securityContext);
    long count =
        this.repository.countAllCarOwnerToVehicles(carOwnerToVehicleFilter, securityContext);
    return new PaginationResponse<>(list, carOwnerToVehicleFilter.getPageSize(), count);
  }

  /**
   * @param carOwnerToVehicleFilter Object Used to List CarOwnerToVehicle
   * @param securityContext
   * @return List of CarOwnerToVehicle
   */
  public List<CarOwnerToVehicle> listAllCarOwnerToVehicles(
      CarOwnerToVehicleFilter carOwnerToVehicleFilter, UserSecurityContext securityContext) {
    return this.repository.listAllCarOwnerToVehicles(carOwnerToVehicleFilter, securityContext);
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
