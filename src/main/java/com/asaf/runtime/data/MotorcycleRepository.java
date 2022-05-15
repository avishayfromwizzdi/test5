package com.asaf.runtime.data;

import com.asaf.runtime.model.Motorcycle;
import com.asaf.runtime.request.MotorcycleFilter;
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
public class MotorcycleRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private VehicleRepository vehicleRepository;

  /**
   * @param motorcycleFilter Object Used to List Motorcycle
   * @param securityContext
   * @return List of Motorcycle
   */
  public List<Motorcycle> listAllMotorcycles(
      MotorcycleFilter motorcycleFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Motorcycle> q = cb.createQuery(Motorcycle.class);
    Root<Motorcycle> r = q.from(Motorcycle.class);
    List<Predicate> preds = new ArrayList<>();
    addMotorcyclePredicate(motorcycleFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<Motorcycle> query = em.createQuery(q);

    if (motorcycleFilter.getPageSize() != null
        && motorcycleFilter.getCurrentPage() != null
        && motorcycleFilter.getPageSize() > 0
        && motorcycleFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(motorcycleFilter.getPageSize() * motorcycleFilter.getCurrentPage())
          .setMaxResults(motorcycleFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends Motorcycle> void addMotorcyclePredicate(
      MotorcycleFilter motorcycleFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    vehicleRepository.addVehiclePredicate(motorcycleFilter, cb, q, r, preds, securityContext);
  }
  /**
   * @param motorcycleFilter Object Used to List Motorcycle
   * @param securityContext
   * @return count of Motorcycle
   */
  public Long countAllMotorcycles(
      MotorcycleFilter motorcycleFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<Motorcycle> r = q.from(Motorcycle.class);
    List<Predicate> preds = new ArrayList<>();
    addMotorcyclePredicate(motorcycleFilter, cb, q, r, preds, securityContext);
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
