package com.asaf.runtime.service;

import com.asaf.runtime.data.AppUserRepository;
import com.asaf.runtime.model.AppUser;
import com.asaf.runtime.request.AppUserCreate;
import com.asaf.runtime.request.AppUserFilter;
import com.asaf.runtime.request.AppUserUpdate;
import com.asaf.runtime.response.PaginationResponse;
import com.asaf.runtime.security.UserSecurityContext;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.metamodel.SingularAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AppUserService {

  @Autowired private AppUserRepository repository;

  @Autowired private PasswordEncoder passwordEncoder;

  /**
   * @param appUserCreate Object Used to Create AppUser
   * @param securityContext
   * @return created AppUser
   */
  public AppUser createAppUser(AppUserCreate appUserCreate, UserSecurityContext securityContext) {
    AppUser appUser = createAppUserNoMerge(appUserCreate, securityContext);
    this.repository.merge(appUser);
    return appUser;
  }

  /**
   * @param appUserCreate Object Used to Create AppUser
   * @param securityContext
   * @return created AppUser unmerged
   */
  public AppUser createAppUserNoMerge(
      AppUserCreate appUserCreate, UserSecurityContext securityContext) {
    AppUser appUser = new AppUser();
    appUser.setId(UUID.randomUUID().toString());
    updateAppUserNoMerge(appUser, appUserCreate);

    return appUser;
  }

  /**
   * @param appUserCreate Object Used to Create AppUser
   * @param appUser
   * @return if appUser was updated
   */
  public boolean updateAppUserNoMerge(AppUser appUser, AppUserCreate appUserCreate) {
    boolean update = false;

    if (appUserCreate.getUsername() != null
        && (!appUserCreate.getUsername().equals(appUser.getUsername()))) {
      appUser.setUsername(appUserCreate.getUsername());
      update = true;
    }

    if (appUserCreate.getPassword() != null
        && (!passwordEncoder.matches(appUserCreate.getPassword(), appUser.getPassword()))) {
      appUser.setPassword(passwordEncoder.encode(appUserCreate.getPassword()));
      update = true;
    }

    return update;
  }
  /**
   * @param appUserUpdate
   * @param securityContext
   * @return appUser
   */
  public AppUser updateAppUser(AppUserUpdate appUserUpdate, UserSecurityContext securityContext) {
    AppUser appUser = appUserUpdate.getAppUser();
    if (updateAppUserNoMerge(appUser, appUserUpdate)) {
      this.repository.merge(appUser);
    }
    return appUser;
  }

  /**
   * @param appUserFilter Object Used to List AppUser
   * @param securityContext
   * @return PaginationResponse<AppUser> containing paging information for AppUser
   */
  public PaginationResponse<AppUser> getAllAppUsers(
      AppUserFilter appUserFilter, UserSecurityContext securityContext) {
    List<AppUser> list = listAllAppUsers(appUserFilter, securityContext);
    long count = this.repository.countAllAppUsers(appUserFilter, securityContext);
    return new PaginationResponse<>(list, appUserFilter.getPageSize(), count);
  }

  /**
   * @param appUserFilter Object Used to List AppUser
   * @param securityContext
   * @return List of AppUser
   */
  public List<AppUser> listAllAppUsers(
      AppUserFilter appUserFilter, UserSecurityContext securityContext) {
    return this.repository.listAllAppUsers(appUserFilter, securityContext);
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
