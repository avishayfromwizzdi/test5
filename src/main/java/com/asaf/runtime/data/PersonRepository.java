package com.asaf.runtime.data;

import com.asaf.runtime.model.Person;
import com.asaf.runtime.model.Person_;
import com.asaf.runtime.request.PersonFilter;
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
public class PersonRepository {
  @PersistenceContext private EntityManager em;

  /**
   * @param personFilter Object Used to List Person
   * @param securityContext
   * @return List of Person
   */
  public List<Person> listAllPersons(
      PersonFilter personFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Person> q = cb.createQuery(Person.class);
    Root<Person> r = q.from(Person.class);
    List<Predicate> preds = new ArrayList<>();
    addPersonPredicate(personFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<Person> query = em.createQuery(q);

    if (personFilter.getPageSize() != null
        && personFilter.getCurrentPage() != null
        && personFilter.getPageSize() > 0
        && personFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(personFilter.getPageSize() * personFilter.getCurrentPage())
          .setMaxResults(personFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends Person> void addPersonPredicate(
      PersonFilter personFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    if (personFilter.getDescription() != null && !personFilter.getDescription().isEmpty()) {
      preds.add(r.get(Person_.description).in(personFilter.getDescription()));
    }

    if (personFilter.getName() != null && !personFilter.getName().isEmpty()) {
      preds.add(r.get(Person_.name).in(personFilter.getName()));
    }

    if (personFilter.getId() != null && !personFilter.getId().isEmpty()) {
      preds.add(r.get(Person_.id).in(personFilter.getId()));
    }
  }
  /**
   * @param personFilter Object Used to List Person
   * @param securityContext
   * @return count of Person
   */
  public Long countAllPersons(PersonFilter personFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<Person> r = q.from(Person.class);
    List<Predicate> preds = new ArrayList<>();
    addPersonPredicate(personFilter, cb, q, r, preds, securityContext);
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
