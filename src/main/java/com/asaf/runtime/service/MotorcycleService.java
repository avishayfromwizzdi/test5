package com.asaf.runtime.service;

import com.asaf.runtime.data.MotorcycleRepository;
import com.asaf.runtime.model.Motorcycle;
import com.asaf.runtime.request.MotorcycleCreate;
import com.asaf.runtime.request.MotorcycleFilter;
import com.asaf.runtime.request.MotorcycleUpdate;
import com.asaf.runtime.response.PaginationResponse;
import com.asaf.runtime.security.UserSecurityContext;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MotorcycleService {

  @Autowired private MotorcycleRepository repository;

  @Autowired private VehicleService vehicleService;

  /**
   * @param motorcycleCreate Object Used to Create Motorcycle
   * @param securityContext
   * @return created Motorcycle
   */
  public Motorcycle createMotorcycle(
      MotorcycleCreate motorcycleCreate, UserSecurityContext securityContext) {
    Motorcycle motorcycle = createMotorcycleNoMerge(motorcycleCreate, securityContext);
    this.repository.merge(motorcycle);
    return motorcycle;
  }

  /**
   * @param motorcycleCreate Object Used to Create Motorcycle
   * @param securityContext
   * @return created Motorcycle unmerged
   */
  public Motorcycle createMotorcycleNoMerge(
      MotorcycleCreate motorcycleCreate, UserSecurityContext securityContext) {
    Motorcycle motorcycle = new Motorcycle();
    motorcycle.setId(UUID.randomUUID().toString());
    updateMotorcycleNoMerge(motorcycle, motorcycleCreate);

    if (securityContext != null) {
      motorcycle.setCreator(securityContext.getUser());
    }

    return motorcycle;
  }

  /**
   * @param motorcycleCreate Object Used to Create Motorcycle
   * @param motorcycle
   * @return if motorcycle was updated
   */
  public boolean updateMotorcycleNoMerge(Motorcycle motorcycle, MotorcycleCreate motorcycleCreate) {
    boolean update = vehicleService.updateVehicleNoMerge(motorcycle, motorcycleCreate);

    return update;
  }
  /**
   * @param motorcycleUpdate
   * @param securityContext
   * @return motorcycle
   */
  public Motorcycle updateMotorcycle(
      MotorcycleUpdate motorcycleUpdate, UserSecurityContext securityContext) {
    Motorcycle motorcycle = motorcycleUpdate.getMotorcycle();
    if (updateMotorcycleNoMerge(motorcycle, motorcycleUpdate)) {
      this.repository.merge(motorcycle);
    }
    return motorcycle;
  }

  /**
   * @param motorcycleFilter Object Used to List Motorcycle
   * @param securityContext
   * @return PaginationResponse<Motorcycle> containing paging information for Motorcycle
   */
  public PaginationResponse<Motorcycle> getAllMotorcycles(
      MotorcycleFilter motorcycleFilter, UserSecurityContext securityContext) {
    List<Motorcycle> list = listAllMotorcycles(motorcycleFilter, securityContext);
    long count = this.repository.countAllMotorcycles(motorcycleFilter, securityContext);
    return new PaginationResponse<>(list, motorcycleFilter.getPageSize(), count);
  }

  /**
   * @param motorcycleFilter Object Used to List Motorcycle
   * @param securityContext
   * @return List of Motorcycle
   */
  public List<Motorcycle> listAllMotorcycles(
      MotorcycleFilter motorcycleFilter, UserSecurityContext securityContext) {
    return this.repository.listAllMotorcycles(motorcycleFilter, securityContext);
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
