package com.asaf.runtime.data;

import com.asaf.runtime.model.Car;
import com.asaf.runtime.request.CarFilter;
import com.asaf.runtime.security.UserSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CarRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private VehicleRepository vehicleRepository;

  /**
   * @param carFilter Object Used to List Car
   * @param securityContext
   * @return List of Car
   */
  public List<Car> listAllCars(CarFilter carFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Car> q = cb.createQuery(Car.class);
    Root<Car> r = q.from(Car.class);
    List<Predicate> preds = new ArrayList<>();
    addCarPredicate(carFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<Car> query = em.createQuery(q);

    if (carFilter.getPageSize() != null
        && carFilter.getCurrentPage() != null
        && carFilter.getPageSize() > 0
        && carFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(carFilter.getPageSize() * carFilter.getCurrentPage())
          .setMaxResults(carFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends Car> void addCarPredicate(
      CarFilter carFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    vehicleRepository.addVehiclePredicate(carFilter, cb, q, r, preds, securityContext);
  }
  /**
   * @param carFilter Object Used to List Car
   * @param securityContext
   * @return count of Car
   */
  public Long countAllCars(CarFilter carFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<Car> r = q.from(Car.class);
    List<Predicate> preds = new ArrayList<>();
    addCarPredicate(carFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public <T, I> List<T> listByIds(Class<T> c, SingularAttribute<T, I> idField, Set<I> ids) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<T> q = cb.createQuery(c);
    Root<T> r = q.from(c);
    q.select(r).where(r.get(idField).in(ids));
    return em.createQuery(q).getResultList();
  }

  public <T, I> T getByIdOrNull(Class<T> c, SingularAttribute<T, I> idField, I id) {
    return listByIds(c, idField, Collections.singleton(id)).stream().findFirst().orElse(null);
  }

  @Transactional
  public void merge(java.lang.Object base) {
    em.merge(base);
  }

  @Transactional
  public void massMerge(List<?> toMerge) {
    for (Object o : toMerge) {
      em.merge(o);
    }
  }
}
