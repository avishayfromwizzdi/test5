package com.asaf.runtime.service;

import com.asaf.runtime.data.PersonRepository;
import com.asaf.runtime.model.Person;
import com.asaf.runtime.request.PersonCreate;
import com.asaf.runtime.request.PersonFilter;
import com.asaf.runtime.request.PersonUpdate;
import com.asaf.runtime.response.PaginationResponse;
import com.asaf.runtime.security.UserSecurityContext;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonService {

  @Autowired private PersonRepository repository;

  /**
   * @param personCreate Object Used to Create Person
   * @param securityContext
   * @return created Person
   */
  public Person createPerson(PersonCreate personCreate, UserSecurityContext securityContext) {
    Person person = createPersonNoMerge(personCreate, securityContext);
    this.repository.merge(person);
    return person;
  }

  /**
   * @param personCreate Object Used to Create Person
   * @param securityContext
   * @return created Person unmerged
   */
  public Person createPersonNoMerge(
      PersonCreate personCreate, UserSecurityContext securityContext) {
    Person person = new Person();
    person.setId(UUID.randomUUID().toString());
    updatePersonNoMerge(person, personCreate);

    if (securityContext != null) {
      person.setCreator(securityContext.getUser());
    }

    return person;
  }

  /**
   * @param personCreate Object Used to Create Person
   * @param person
   * @return if person was updated
   */
  public boolean updatePersonNoMerge(Person person, PersonCreate personCreate) {
    boolean update = false;

    if (personCreate.getDescription() != null
        && (!personCreate.getDescription().equals(person.getDescription()))) {
      person.setDescription(personCreate.getDescription());
      update = true;
    }

    if (personCreate.getName() != null && (!personCreate.getName().equals(person.getName()))) {
      person.setName(personCreate.getName());
      update = true;
    }

    return update;
  }
  /**
   * @param personUpdate
   * @param securityContext
   * @return person
   */
  public Person updatePerson(PersonUpdate personUpdate, UserSecurityContext securityContext) {
    Person person = personUpdate.getPerson();
    if (updatePersonNoMerge(person, personUpdate)) {
      this.repository.merge(person);
    }
    return person;
  }

  /**
   * @param personFilter Object Used to List Person
   * @param securityContext
   * @return PaginationResponse<Person> containing paging information for Person
   */
  public PaginationResponse<Person> getAllPersons(
      PersonFilter personFilter, UserSecurityContext securityContext) {
    List<Person> list = listAllPersons(personFilter, securityContext);
    long count = this.repository.countAllPersons(personFilter, securityContext);
    return new PaginationResponse<>(list, personFilter.getPageSize(), count);
  }

  /**
   * @param personFilter Object Used to List Person
   * @param securityContext
   * @return List of Person
   */
  public List<Person> listAllPersons(
      PersonFilter personFilter, UserSecurityContext securityContext) {
    return this.repository.listAllPersons(personFilter, securityContext);
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
