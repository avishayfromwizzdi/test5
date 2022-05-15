package com.asaf.runtime.controller;

import com.asaf.runtime.AppInit;
import com.asaf.runtime.model.Vehicle;
import com.asaf.runtime.request.LoginRequest;
import com.asaf.runtime.request.VehicleCreate;
import com.asaf.runtime.request.VehicleFilter;
import com.asaf.runtime.request.VehicleUpdate;
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
public class VehicleControllerTest {

  private Vehicle testVehicle;
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
  public void testVehicleCreate() {
    VehicleCreate request = new VehicleCreate();

    request.setName("test-string");

    request.setDescription("test-string");

    ResponseEntity<Vehicle> response =
        this.restTemplate.postForEntity("/Vehicle/createVehicle", request, Vehicle.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testVehicle = response.getBody();
    assertVehicle(request, testVehicle);
  }

  @Test
  @Order(2)
  public void testListAllVehicles() {
    VehicleFilter request = new VehicleFilter();
    ParameterizedTypeReference<PaginationResponse<Vehicle>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Vehicle>> response =
        this.restTemplate.exchange(
            "/Vehicle/getAllVehicles", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Vehicle> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Vehicle> Vehicles = body.getList();
    Assertions.assertNotEquals(0, Vehicles.size());
    Assertions.assertTrue(Vehicles.stream().anyMatch(f -> f.getId().equals(testVehicle.getId())));
  }

  public void assertVehicle(VehicleCreate request, Vehicle testVehicle) {
    Assertions.assertNotNull(testVehicle);

    if (request.getName() != null) {
      Assertions.assertEquals(request.getName(), testVehicle.getName());
    }

    if (request.getDescription() != null) {
      Assertions.assertEquals(request.getDescription(), testVehicle.getDescription());
    }
  }

  @Test
  @Order(3)
  public void testVehicleUpdate() {
    VehicleUpdate request = new VehicleUpdate().setId(testVehicle.getId());
    ResponseEntity<Vehicle> response =
        this.restTemplate.exchange(
            "/Vehicle/updateVehicle", HttpMethod.PUT, new HttpEntity<>(request), Vehicle.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testVehicle = response.getBody();
    assertVehicle(request, testVehicle);
  }
}
