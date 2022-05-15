package com.asaf.runtime.validation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class IdValidator implements ConstraintValidator<IdValid, Object> {

  private static final Logger logger = LoggerFactory.getLogger(IdValidator.class);
  private String field;
  private Class<?> fieldType;
  private String targetField;
  @PersistenceContext @Lazy private EntityManager em;

  private final Map<String, PropertyDescriptor> idGettersCache = new ConcurrentHashMap<>();

  @Override
  public void initialize(IdValid constraintAnnotation) {
    this.field = constraintAnnotation.field();
    this.fieldType = constraintAnnotation.fieldType();
    this.targetField = constraintAnnotation.targetField();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
    BeanWrapperImpl objectWrapper = new BeanWrapperImpl(value);

    Object fieldValue = objectWrapper.getPropertyValue(field);
    if (fieldValue instanceof Collection) {
      Collection<?> collection = (Collection<?>) fieldValue;
      Set<String> ids =
          collection.stream()
              .filter(f -> f instanceof String)
              .map(f -> (String) f)
              .collect(Collectors.toSet());
      if (!ids.isEmpty()) {
        Map<String, ?> basicMap = listByIds(fieldType, ids);

        ids.removeAll(basicMap.keySet());
        if (!ids.isEmpty()) {
          constraintValidatorContext
              .buildConstraintViolationWithTemplate(
                  "cannot find " + fieldType.getCanonicalName() + " with ids \"" + ids + "\"")
              .addPropertyNode(field)
              .addConstraintViolation();
          return false;
        }
        objectWrapper.setPropertyValue(targetField, new ArrayList<>(basicMap.values()));
      }

    } else {
      if (fieldValue instanceof String) {
        String id = (String) fieldValue;

        Object basic = getByById(fieldType, id);
        if (basic == null) {
          constraintValidatorContext
              .buildConstraintViolationWithTemplate(
                  "cannot find " + fieldType.getCanonicalName() + " with id \"" + id + "\"")
              .addPropertyNode(field)
              .addConstraintViolation();
          return false;
        }
        objectWrapper.setPropertyValue(targetField, basic);
      }
    }
    return true;
  }

  private PropertyDescriptor getIdPropertyDescriptor(Class<?> c) throws NoSuchMethodException {
    String canonicalName = c.getCanonicalName();
    PropertyDescriptor propertyDescriptor = idGettersCache.get(canonicalName);
    if (propertyDescriptor == null) {
      for (Field field : c.getDeclaredFields()) {
        if (field.isAnnotationPresent(Id.class)) {
          Method method = c.getMethod("get" + StringUtils.capitalize(field.getName()));
          propertyDescriptor = new PropertyDescriptor(method, field);
          break;
        }
      }
      idGettersCache.put(canonicalName, propertyDescriptor);
    }
    return propertyDescriptor;
  }

  private <T> T getByById(Class<T> c, String id) {
    return listByIds(c, Collections.singleton(id)).get(id);
  }

  private <T> Map<String, T> listByIds(Class<T> c, Set<String> ids) {
    try {
      PropertyDescriptor idGetter = getIdPropertyDescriptor(c);
      if (idGetter != null) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(c);
        Root<T> r = q.from(c);
        q.select(r).where(r.get(idGetter.field.getName()).in(ids));
        return toMap(em.createQuery(q).getResultList(), idGetter.getter);
      }
    } catch (Exception e) {
      logger.error("failed getting " + c.getCanonicalName() + " by ids " + ids);
    }
    return Collections.emptyMap();
  }

  private <T> Map<String, T> toMap(List<T> resultList, Method idGetter)
      throws InvocationTargetException, IllegalAccessException {
    Map<String, T> map = new HashMap<>();
    for (T o : resultList) {

      map.put((String) idGetter.invoke(o), o);
    }
    return map;
  }

  private static class PropertyDescriptor {
    final Method getter;
    final Field field;

    public PropertyDescriptor(Method getter, Field field) {
      this.getter = getter;
      this.field = field;
    }
  }
}
