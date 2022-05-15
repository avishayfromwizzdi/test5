package com.asaf.runtime.service;

import com.asaf.runtime.data.CarOwnerRepository;
import com.asaf.runtime.model.CarOwner;
import com.asaf.runtime.request.CarOwnerCreate;
import com.asaf.runtime.request.CarOwnerFilter;
import com.asaf.runtime.request.CarOwnerUpdate;
import com.asaf.runtime.response.PaginationResponse;
import com.asaf.runtime.security.UserSecurityContext;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarOwnerService {

  @Autowired private CarOwnerRepository repository;

  @Autowired private PersonService personService;

  /**
   * @param carOwnerCreate Object Used to Create CarOwner
   * @param securityContext
   * @return created CarOwner
   */
  public CarOwner createCarOwner(
      CarOwnerCreate carOwnerCreate, UserSecurityContext securityContext) {
    CarOwner carOwner = createCarOwnerNoMerge(carOwnerCreate, securityContext);
    this.repository.merge(carOwner);
    return carOwner;
  }

  /**
   * @param carOwnerCreate Object Used to Create CarOwner
   * @param securityContext
   * @return created CarOwner unmerged
   */
  public CarOwner createCarOwnerNoMerge(
      CarOwnerCreate carOwnerCreate, UserSecurityContext securityContext) {
    CarOwner carOwner = new CarOwner();
    carOwner.setId(UUID.randomUUID().toString());
    updateCarOwnerNoMerge(carOwner, carOwnerCreate);

    if (securityContext != null) {
      carOwner.setCreator(securityContext.getUser());
    }

    return carOwner;
  }

  /**
   * @param carOwnerCreate Object Used to Create CarOwner
   * @param carOwner
   * @return if carOwner was updated
   */
  public boolean updateCarOwnerNoMerge(CarOwner carOwner, CarOwnerCreate carOwnerCreate) {
    boolean update = personService.updatePersonNoMerge(carOwner, carOwnerCreate);

    return update;
  }
  /**
   * @param carOwnerUpdate
   * @param securityContext
   * @return carOwner
   */
  public CarOwner updateCarOwner(
      CarOwnerUpdate carOwnerUpdate, UserSecurityContext securityContext) {
    CarOwner carOwner = carOwnerUpdate.getCarOwner();
    if (updateCarOwnerNoMerge(carOwner, carOwnerUpdate)) {
      this.repository.merge(carOwner);
    }
    return carOwner;
  }

  /**
   * @param carOwnerFilter Object Used to List CarOwner
   * @param securityContext
   * @return PaginationResponse<CarOwner> containing paging information for CarOwner
   */
  public PaginationResponse<CarOwner> getAllCarOwners(
      CarOwnerFilter carOwnerFilter, UserSecurityContext securityContext) {
    List<CarOwner> list = listAllCarOwners(carOwnerFilter, securityContext);
    long count = this.repository.countAllCarOwners(carOwnerFilter, securityContext);
    return new PaginationResponse<>(list, carOwnerFilter.getPageSize(), count);
  }

  /**
   * @param carOwnerFilter Object Used to List CarOwner
   * @param securityContext
   * @return List of CarOwner
   */
  public List<CarOwner> listAllCarOwners(
      CarOwnerFilter carOwnerFilter, UserSecurityContext securityContext) {
    return this.repository.listAllCarOwners(carOwnerFilter, securityContext);
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
