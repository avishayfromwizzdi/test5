package com.asaf.runtime.controller;

import com.asaf.runtime.AppInit;
import com.asaf.runtime.model.CarOwner;
import com.asaf.runtime.model.CarOwnerToVehicle;
import com.asaf.runtime.model.Vehicle;
import com.asaf.runtime.request.CarOwnerToVehicleCreate;
import com.asaf.runtime.request.CarOwnerToVehicleFilter;
import com.asaf.runtime.request.CarOwnerToVehicleUpdate;
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
public class CarOwnerToVehicleControllerTest {

  private CarOwnerToVehicle testCarOwnerToVehicle;
  @Autowired private TestRestTemplate restTemplate;

  @Autowired private CarOwner carOwner;

  @Autowired private Vehicle vehicle;

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
  public void testCarOwnerToVehicleCreate() {
    CarOwnerToVehicleCreate request = new CarOwnerToVehicleCreate();

    request.setVehicleId(this.vehicle.getId());

    request.setCarOwnerId(this.carOwner.getId());

    ResponseEntity<CarOwnerToVehicle> response =
        this.restTemplate.postForEntity(
            "/CarOwnerToVehicle/createCarOwnerToVehicle", request, CarOwnerToVehicle.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testCarOwnerToVehicle = response.getBody();
    assertCarOwnerToVehicle(request, testCarOwnerToVehicle);
  }

  @Test
  @Order(2)
  public void testListAllCarOwnerToVehicles() {
    CarOwnerToVehicleFilter request = new CarOwnerToVehicleFilter();
    ParameterizedTypeReference<PaginationResponse<CarOwnerToVehicle>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<CarOwnerToVehicle>> response =
        this.restTemplate.exchange(
            "/CarOwnerToVehicle/getAllCarOwnerToVehicles",
            HttpMethod.POST,
            new HttpEntity<>(request),
            t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<CarOwnerToVehicle> body = response.getBody();
    Assertions.assertNotNull(body);
    List<CarOwnerToVehicle> CarOwnerToVehicles = body.getList();
    Assertions.assertNotEquals(0, CarOwnerToVehicles.size());
    Assertions.assertTrue(
        CarOwnerToVehicles.stream().anyMatch(f -> f.getId().equals(testCarOwnerToVehicle.getId())));
  }

  public void assertCarOwnerToVehicle(
      CarOwnerToVehicleCreate request, CarOwnerToVehicle testCarOwnerToVehicle) {
    Assertions.assertNotNull(testCarOwnerToVehicle);

    if (request.getVehicleId() != null) {

      Assertions.assertNotNull(testCarOwnerToVehicle.getVehicle());
      Assertions.assertEquals(request.getVehicleId(), testCarOwnerToVehicle.getVehicle().getId());
    }

    if (request.getCarOwnerId() != null) {

      Assertions.assertNotNull(testCarOwnerToVehicle.getCarOwner());
      Assertions.assertEquals(request.getCarOwnerId(), testCarOwnerToVehicle.getCarOwner().getId());
    }
  }

  @Test
  @Order(3)
  public void testCarOwnerToVehicleUpdate() {
    CarOwnerToVehicleUpdate request =
        new CarOwnerToVehicleUpdate().setId(testCarOwnerToVehicle.getId());
    ResponseEntity<CarOwnerToVehicle> response =
        this.restTemplate.exchange(
            "/CarOwnerToVehicle/updateCarOwnerToVehicle",
            HttpMethod.PUT,
            new HttpEntity<>(request),
            CarOwnerToVehicle.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testCarOwnerToVehicle = response.getBody();
    assertCarOwnerToVehicle(request, testCarOwnerToVehicle);
  }
}
