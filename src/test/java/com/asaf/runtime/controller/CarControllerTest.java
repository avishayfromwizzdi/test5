package com.asaf.runtime.controller;

import com.asaf.runtime.AppInit;
import com.asaf.runtime.model.Car;
import com.asaf.runtime.request.CarCreate;
import com.asaf.runtime.request.CarFilter;
import com.asaf.runtime.request.CarUpdate;
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
public class CarControllerTest {

  private Car testCar;
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
  public void testCarCreate() {
    CarCreate request = new CarCreate();

    ResponseEntity<Car> response =
        this.restTemplate.postForEntity("/Car/createCar", request, Car.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testCar = response.getBody();
    assertCar(request, testCar);
  }

  @Test
  @Order(2)
  public void testListAllCars() {
    CarFilter request = new CarFilter();
    ParameterizedTypeReference<PaginationResponse<Car>> t = new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Car>> response =
        this.restTemplate.exchange(
            "/Car/getAllCars", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Car> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Car> Cars = body.getList();
    Assertions.assertNotEquals(0, Cars.size());
    Assertions.assertTrue(Cars.stream().anyMatch(f -> f.getId().equals(testCar.getId())));
  }

  public void assertCar(CarCreate request, Car testCar) {
    Assertions.assertNotNull(testCar);
  }

  @Test
  @Order(3)
  public void testCarUpdate() {
    CarUpdate request = new CarUpdate().setId(testCar.getId());
    ResponseEntity<Car> response =
        this.restTemplate.exchange(
            "/Car/updateCar", HttpMethod.PUT, new HttpEntity<>(request), Car.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testCar = response.getBody();
    assertCar(request, testCar);
  }
}
