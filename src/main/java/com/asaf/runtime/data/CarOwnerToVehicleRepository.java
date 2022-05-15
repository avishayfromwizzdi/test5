package com.asaf.runtime.data;

import com.asaf.runtime.model.CarOwner;
import com.asaf.runtime.model.CarOwnerToVehicle;
import com.asaf.runtime.model.CarOwnerToVehicle_;
import com.asaf.runtime.model.CarOwner_;
import com.asaf.runtime.model.Vehicle;
import com.asaf.runtime.model.Vehicle_;
import com.asaf.runtime.request.CarOwnerToVehicleFilter;
import com.asaf.runtime.security.UserSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CarOwnerToVehicleRepository {
  @PersistenceContext private EntityManager em;

  /**
   * @param carOwnerToVehicleFilter Object Used to List CarOwnerToVehicle
   * @param securityContext
   * @return List of CarOwnerToVehicle
   */
  public List<CarOwnerToVehicle> listAllCarOwnerToVehicles(
      CarOwnerToVehicleFilter carOwnerToVehicleFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<CarOwnerToVehicle> q = cb.createQuery(CarOwnerToVehicle.class);
    Root<CarOwnerToVehicle> r = q.from(CarOwnerToVehicle.class);
    List<Predicate> preds = new ArrayList<>();
    addCarOwnerToVehiclePredicate(carOwnerToVehicleFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<CarOwnerToVehicle> query = em.createQuery(q);

    if (carOwnerToVehicleFilter.getPageSize() != null
        && carOwnerToVehicleFilter.getCurrentPage() != null
        && carOwnerToVehicleFilter.getPageSize() > 0
        && carOwnerToVehicleFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(
              carOwnerToVehicleFilter.getPageSize() * carOwnerToVehicleFilter.getCurrentPage())
          .setMaxResults(carOwnerToVehicleFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends CarOwnerToVehicle> void addCarOwnerToVehiclePredicate(
      CarOwnerToVehicleFilter carOwnerToVehicleFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    if (carOwnerToVehicleFilter.getVehicle() != null
        && !carOwnerToVehicleFilter.getVehicle().isEmpty()) {
      Set<String> ids =
          carOwnerToVehicleFilter.getVehicle().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Vehicle> join = r.join(CarOwnerToVehicle_.vehicle);
      preds.add(join.get(Vehicle_.id).in(ids));
    }

    if (carOwnerToVehicleFilter.getCarOwner() != null
        && !carOwnerToVehicleFilter.getCarOwner().isEmpty()) {
      Set<String> ids =
          carOwnerToVehicleFilter.getCarOwner().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, CarOwner> join = r.join(CarOwnerToVehicle_.carOwner);
      preds.add(join.get(CarOwner_.id).in(ids));
    }

    if (carOwnerToVehicleFilter.getId() != null && !carOwnerToVehicleFilter.getId().isEmpty()) {
      preds.add(r.get(CarOwnerToVehicle_.id).in(carOwnerToVehicleFilter.getId()));
    }
  }
  /**
   * @param carOwnerToVehicleFilter Object Used to List CarOwnerToVehicle
   * @param securityContext
   * @return count of CarOwnerToVehicle
   */
  public Long countAllCarOwnerToVehicles(
      CarOwnerToVehicleFilter carOwnerToVehicleFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<CarOwnerToVehicle> r = q.from(CarOwnerToVehicle.class);
    List<Predicate> preds = new ArrayList<>();
    addCarOwnerToVehiclePredicate(carOwnerToVehicleFilter, cb, q, r, preds, securityContext);
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
