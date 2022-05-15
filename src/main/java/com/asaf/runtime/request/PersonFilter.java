package com.asaf.runtime.request;

import java.util.Set;
import javax.validation.constraints.Min;

/** Object Used to List Person */
public class PersonFilter {

  @Min(value = 0)
  private Integer currentPage;

  private Set<String> description;

  private Set<String> id;

  private Set<String> name;

  @Min(value = 1)
  private Integer pageSize;

  /** @return currentPage */
  public Integer getCurrentPage() {
    return this.currentPage;
  }

  /**
   * @param currentPage currentPage to set
   * @return PersonFilter
   */
  public <T extends PersonFilter> T setCurrentPage(Integer currentPage) {
    this.currentPage = currentPage;
    return (T) this;
  }

  /** @return description */
  public Set<String> getDescription() {
    return this.description;
  }

  /**
   * @param description description to set
   * @return PersonFilter
   */
  public <T extends PersonFilter> T setDescription(Set<String> description) {
    this.description = description;
    return (T) this;
  }

  /** @return id */
  public Set<String> getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return PersonFilter
   */
  public <T extends PersonFilter> T setId(Set<String> id) {
    this.id = id;
    return (T) this;
  }

  /** @return name */
  public Set<String> getName() {
    return this.name;
  }

  /**
   * @param name name to set
   * @return PersonFilter
   */
  public <T extends PersonFilter> T setName(Set<String> name) {
    this.name = name;
    return (T) this;
  }

  /** @return pageSize */
  public Integer getPageSize() {
    return this.pageSize;
  }

  /**
   * @param pageSize pageSize to set
   * @return PersonFilter
   */
  public <T extends PersonFilter> T setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return (T) this;
  }
}
