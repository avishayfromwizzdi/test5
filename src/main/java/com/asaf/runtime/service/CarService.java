package com.asaf.runtime.service;

import com.asaf.runtime.data.CarRepository;
import com.asaf.runtime.model.Car;
import com.asaf.runtime.request.CarCreate;
import com.asaf.runtime.request.CarFilter;
import com.asaf.runtime.request.CarUpdate;
import com.asaf.runtime.response.PaginationResponse;
import com.asaf.runtime.security.UserSecurityContext;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarService {

  @Autowired private CarRepository repository;

  @Autowired private VehicleService vehicleService;

  /**
   * @param carCreate Object Used to Create Car
   * @param securityContext
   * @return created Car
   */
  public Car createCar(CarCreate carCreate, UserSecurityContext securityContext) {
    Car car = createCarNoMerge(carCreate, securityContext);
    this.repository.merge(car);
    return car;
  }

  /**
   * @param carCreate Object Used to Create Car
   * @param securityContext
   * @return created Car unmerged
   */
  public Car createCarNoMerge(CarCreate carCreate, UserSecurityContext securityContext) {
    Car car = new Car();
    car.setId(UUID.randomUUID().toString());
    updateCarNoMerge(car, carCreate);

    if (securityContext != null) {
      car.setCreator(securityContext.getUser());
    }

    return car;
  }

  /**
   * @param carCreate Object Used to Create Car
   * @param car
   * @return if car was updated
   */
  public boolean updateCarNoMerge(Car car, CarCreate carCreate) {
    boolean update = vehicleService.updateVehicleNoMerge(car, carCreate);

    return update;
  }
  /**
   * @param carUpdate
   * @param securityContext
   * @return car
   */
  public Car updateCar(CarUpdate carUpdate, UserSecurityContext securityContext) {
    Car car = carUpdate.getCar();
    if (updateCarNoMerge(car, carUpdate)) {
      this.repository.merge(car);
    }
    return car;
  }

  /**
   * @param carFilter Object Used to List Car
   * @param securityContext
   * @return PaginationResponse<Car> containing paging information for Car
   */
  public PaginationResponse<Car> getAllCars(
      CarFilter carFilter, UserSecurityContext securityContext) {
    List<Car> list = listAllCars(carFilter, securityContext);
    long count = this.repository.countAllCars(carFilter, securityContext);
    return new PaginationResponse<>(list, carFilter.getPageSize(), count);
  }

  /**
   * @param carFilter Object Used to List Car
   * @param securityContext
   * @return List of Car
   */
  public List<Car> listAllCars(CarFilter carFilter, UserSecurityContext securityContext) {
    return this.repository.listAllCars(carFilter, securityContext);
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
