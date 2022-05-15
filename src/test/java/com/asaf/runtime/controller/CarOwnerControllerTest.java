package com.asaf.runtime.controller;

import com.asaf.runtime.AppInit;
import com.asaf.runtime.model.CarOwner;
import com.asaf.runtime.request.CarOwnerCreate;
import com.asaf.runtime.request.CarOwnerFilter;
import com.asaf.runtime.request.CarOwnerUpdate;
import com.asaf.runtime.request.LoginRequest;
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
public class CarOwnerControllerTest {

  private CarOwner testCarOwner;
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
  public void testCarOwnerCreate() {
    CarOwnerCreate request = new CarOwnerCreate();

    ResponseEntity<CarOwner> response =
        this.restTemplate.postForEntity("/CarOwner/createCarOwner", request, CarOwner.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testCarOwner = response.getBody();
    assertCarOwner(request, testCarOwner);
  }

  @Test
  @Order(2)
  public void testListAllCarOwners() {
    CarOwnerFilter request = new CarOwnerFilter();
    ParameterizedTypeReference<PaginationResponse<CarOwner>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<CarOwner>> response =
        this.restTemplate.exchange(
            "/CarOwner/getAllCarOwners", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<CarOwner> body = response.getBody();
    Assertions.assertNotNull(body);
    List<CarOwner> CarOwners = body.getList();
    Assertions.assertNotEquals(0, CarOwners.size());
    Assertions.assertTrue(CarOwners.stream().anyMatch(f -> f.getId().equals(testCarOwner.getId())));
  }

  public void assertCarOwner(CarOwnerCreate request, CarOwner testCarOwner) {
    Assertions.assertNotNull(testCarOwner);
  }

  @Test
  @Order(3)
  public void testCarOwnerUpdate() {
    CarOwnerUpdate request = new CarOwnerUpdate().setId(testCarOwner.getId());
    ResponseEntity<CarOwner> response =
        this.restTemplate.exchange(
            "/CarOwner/updateCarOwner", HttpMethod.PUT, new HttpEntity<>(request), CarOwner.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testCarOwner = response.getBody();
    assertCarOwner(request, testCarOwner);
  }
}
