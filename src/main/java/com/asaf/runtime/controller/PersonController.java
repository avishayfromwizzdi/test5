package com.asaf.runtime.controller;

import com.asaf.runtime.model.Person;
import com.asaf.runtime.request.PersonCreate;
import com.asaf.runtime.request.PersonFilter;
import com.asaf.runtime.request.PersonUpdate;
import com.asaf.runtime.response.PaginationResponse;
import com.asaf.runtime.security.UserSecurityContext;
import com.asaf.runtime.service.PersonService;
import com.asaf.runtime.validation.Create;
import com.asaf.runtime.validation.Update;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("Person")
@Tag(name = "Person")
public class PersonController {

  @Autowired private PersonService personService;

  @PostMapping("getAllPersons")
  @Operation(summary = "getAllPersons", description = "lists Persons")
  public PaginationResponse<Person> getAllPersons(
      @Valid @RequestBody PersonFilter personFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return personService.getAllPersons(personFilter, securityContext);
  }

  @PostMapping("createPerson")
  @Operation(summary = "createPerson", description = "Creates Person")
  public Person createPerson(
      @Validated(Create.class) @RequestBody PersonCreate personCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return personService.createPerson(personCreate, securityContext);
  }

  @PutMapping("updatePerson")
  @Operation(summary = "updatePerson", description = "Updates Person")
  public Person updatePerson(
      @Validated(Update.class) @RequestBody PersonUpdate personUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return personService.updatePerson(personUpdate, securityContext);
  }
}
