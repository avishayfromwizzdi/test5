package com.asaf.runtime.controller;

import com.asaf.runtime.AppInit;
import com.asaf.runtime.model.Person;
import com.asaf.runtime.request.LoginRequest;
import com.asaf.runtime.request.PersonCreate;
import com.asaf.runtime.request.PersonFilter;
import com.asaf.runtime.request.PersonUpdate;
import com.asaf.runtime.response.PaginationResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AppInit.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class PersonControllerTest {

  private Person testPerson;
  @Autowired private TestRestTemplate restTemplate;

  @BeforeAll
  private void init() {
    ResponseEntity<Object> authenticationResponse =
        this.restTemplate.postForEntity(
            "/login",
            new LoginRequest().setUsername("admin@flexicore.com").setPassword("admin"),
            Object.class);
    String authenticationKey =
        authenticationResponse.getHeaders().get(HttpHeaders.AUTHORIZATION).stream()
            .findFirst()
            .orElse(null);
    restTemplate
        .getRestTemplate()
        .setInterceptors(
            Collections.singletonList(
                (request, body, execution) -> {
                  request.getHeaders().add("Authorization", "Bearer " + authenticationKey);
                  return execution.execute(request, body);
                }));
  }

  @Test
  @Order(1)
  public void testPersonCreate() {
    PersonCreate request = new PersonCreate();

    request.setDescription("test-string");

    request.setName("test-string");

    ResponseEntity<Person> response =
        this.restTemplate.postForEntity("/Person/createPerson", request, Person.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testPerson = response.getBody();
    assertPerson(request, testPerson);
  }

  @Test
  @Order(2)
  public void testListAllPersons() {
    PersonFilter request = new PersonFilter();
    ParameterizedTypeReference<PaginationResponse<Person>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Person>> response =
        this.restTemplate.exchange(
            "/Person/getAllPersons", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Person> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Person> Persons = body.getList();
    Assertions.assertNotEquals(0, Persons.size());
    Assertions.assertTrue(Persons.stream().anyMatch(f -> f.getId().equals(testPerson.getId())));
  }

  public void assertPerson(PersonCreate request, Person testPerson) {
    Assertions.assertNotNull(testPerson);

    if (request.getDescription() != null) {
      Assertions.assertEquals(request.getDescription(), testPerson.getDescription());
    }

    if (request.getName() != null) {
      Assertions.assertEquals(request.getName(), testPerson.getName());
    }
  }

  @Test
  @Order(3)
  public void testPersonUpdate() {
    PersonUpdate request = new PersonUpdate().setId(testPerson.getId());
    ResponseEntity<Person> response =
        this.restTemplate.exchange(
            "/Person/updatePerson", HttpMethod.PUT, new HttpEntity<>(request), Person.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testPerson = response.getBody();
    assertPerson(request, testPerson);
  }
}
