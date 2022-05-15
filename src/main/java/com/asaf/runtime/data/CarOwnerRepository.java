package com.asaf.runtime.data;

import com.asaf.runtime.model.CarOwner;
import com.asaf.runtime.request.CarOwnerFilter;
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
public class CarOwnerRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private PersonRepository personRepository;

  /**
   * @param carOwnerFilter Object Used to List CarOwner
   * @param securityContext
   * @return List of CarOwner
   */
  public List<CarOwner> listAllCarOwners(
      CarOwnerFilter carOwnerFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<CarOwner> q = cb.createQuery(CarOwner.class);
    Root<CarOwner> r = q.from(CarOwner.class);
    List<Predicate> preds = new ArrayList<>();
    addCarOwnerPredicate(carOwnerFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<CarOwner> query = em.createQuery(q);

    if (carOwnerFilter.getPageSize() != null
        && carOwnerFilter.getCurrentPage() != null
        && carOwnerFilter.getPageSize() > 0
        && carOwnerFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(carOwnerFilter.getPageSize() * carOwnerFilter.getCurrentPage())
          .setMaxResults(carOwnerFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends CarOwner> void addCarOwnerPredicate(
      CarOwnerFilter carOwnerFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    personRepository.addPersonPredicate(carOwnerFilter, cb, q, r, preds, securityContext);
  }
  /**
   * @param carOwnerFilter Object Used to List CarOwner
   * @param securityContext
   * @return count of CarOwner
   */
  public Long countAllCarOwners(
      CarOwnerFilter carOwnerFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<CarOwner> r = q.from(CarOwner.class);
    List<Predicate> preds = new ArrayList<>();
    addCarOwnerPredicate(carOwnerFilter, cb, q, r, preds, securityContext);
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
