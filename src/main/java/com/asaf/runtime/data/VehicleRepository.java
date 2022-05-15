package com.asaf.runtime.data;

import com.asaf.runtime.model.Vehicle;
import com.asaf.runtime.model.Vehicle_;
import com.asaf.runtime.request.VehicleFilter;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class VehicleRepository {
  @PersistenceContext private EntityManager em;

  /**
   * @param vehicleFilter Object Used to List Vehicle
   * @param securityContext
   * @return List of Vehicle
   */
  public List<Vehicle> listAllVehicles(
      VehicleFilter vehicleFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Vehicle> q = cb.createQuery(Vehicle.class);
    Root<Vehicle> r = q.from(Vehicle.class);
    List<Predicate> preds = new ArrayList<>();
    addVehiclePredicate(vehicleFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<Vehicle> query = em.createQuery(q);

    if (vehicleFilter.getPageSize() != null
        && vehicleFilter.getCurrentPage() != null
        && vehicleFilter.getPageSize() > 0
        && vehicleFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(vehicleFilter.getPageSize() * vehicleFilter.getCurrentPage())
          .setMaxResults(vehicleFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends Vehicle> void addVehiclePredicate(
      VehicleFilter vehicleFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    if (vehicleFilter.getName() != null && !vehicleFilter.getName().isEmpty()) {
      preds.add(r.get(Vehicle_.name).in(vehicleFilter.getName()));
    }

    if (vehicleFilter.getDescription() != null && !vehicleFilter.getDescription().isEmpty()) {
      preds.add(r.get(Vehicle_.description).in(vehicleFilter.getDescription()));
    }

    if (vehicleFilter.getId() != null && !vehicleFilter.getId().isEmpty()) {
      preds.add(r.get(Vehicle_.id).in(vehicleFilter.getId()));
    }
  }
  /**
   * @param vehicleFilter Object Used to List Vehicle
   * @param securityContext
   * @return count of Vehicle
   */
  public Long countAllVehicles(VehicleFilter vehicleFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<Vehicle> r = q.from(Vehicle.class);
    List<Predicate> preds = new ArrayList<>();
    addVehiclePredicate(vehicleFilter, cb, q, r, preds, securityContext);
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
