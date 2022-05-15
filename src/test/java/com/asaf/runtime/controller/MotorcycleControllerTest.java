package com.asaf.runtime.controller;

import com.asaf.runtime.AppInit;
import com.asaf.runtime.model.Motorcycle;
import com.asaf.runtime.request.LoginRequest;
import com.asaf.runtime.request.MotorcycleCreate;
import com.asaf.runtime.request.MotorcycleFilter;
import com.asaf.runtime.request.MotorcycleUpdate;
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
public class MotorcycleControllerTest {

  private Motorcycle testMotorcycle;
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
  public void testMotorcycleCreate() {
    MotorcycleCreate request = new MotorcycleCreate();

    ResponseEntity<Motorcycle> response =
        this.restTemplate.postForEntity("/Motorcycle/createMotorcycle", request, Motorcycle.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testMotorcycle = response.getBody();
    assertMotorcycle(request, testMotorcycle);
  }

  @Test
  @Order(2)
  public void testListAllMotorcycles() {
    MotorcycleFilter request = new MotorcycleFilter();
    ParameterizedTypeReference<PaginationResponse<Motorcycle>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Motorcycle>> response =
        this.restTemplate.exchange(
            "/Motorcycle/getAllMotorcycles", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Motorcycle> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Motorcycle> Motorcycles = body.getList();
    Assertions.assertNotEquals(0, Motorcycles.size());
    Assertions.assertTrue(
        Motorcycles.stream().anyMatch(f -> f.getId().equals(testMotorcycle.getId())));
  }

  public void assertMotorcycle(MotorcycleCreate request, Motorcycle testMotorcycle) {
    Assertions.assertNotNull(testMotorcycle);
  }

  @Test
  @Order(3)
  public void testMotorcycleUpdate() {
    MotorcycleUpdate request = new MotorcycleUpdate().setId(testMotorcycle.getId());
    ResponseEntity<Motorcycle> response =
        this.restTemplate.exchange(
            "/Motorcycle/updateMotorcycle",
            HttpMethod.PUT,
            new HttpEntity<>(request),
            Motorcycle.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testMotorcycle = response.getBody();
    assertMotorcycle(request, testMotorcycle);
  }
}
