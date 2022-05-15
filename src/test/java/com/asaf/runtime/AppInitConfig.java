package com.asaf.runtime;

import com.asaf.runtime.model.CarOwner;
import com.asaf.runtime.model.Vehicle;
import com.asaf.runtime.request.AppUserCreate;
import com.asaf.runtime.request.CarOwnerCreate;
import com.asaf.runtime.request.VehicleCreate;
import com.asaf.runtime.security.UserSecurityContext;
import com.asaf.runtime.service.AppUserService;
import com.asaf.runtime.service.CarOwnerService;
import com.asaf.runtime.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppInitConfig {

  @Autowired private CarOwnerService carOwnerService;

  @Autowired private VehicleService vehicleService;

  @Autowired
  @Qualifier("adminSecurityContext")
  private UserSecurityContext securityContext;

  @Bean
  public CarOwner carOwner() {
    CarOwnerCreate carOwnerCreate = new CarOwnerCreate();
    return carOwnerService.createCarOwner(carOwnerCreate, securityContext);
  }

  @Bean
  public Vehicle vehicle() {
    VehicleCreate vehicleCreate = new VehicleCreate();
    return vehicleService.createVehicle(vehicleCreate, securityContext);
  }

  @Configuration
  public static class UserConfig {
    @Bean
    @Qualifier("adminSecurityContext")
    public UserSecurityContext adminSecurityContext(AppUserService appUserService) {
      com.asaf.runtime.model.AppUser admin =
          appUserService.createAppUser(
              new AppUserCreate().setUsername("admin@flexicore.com").setPassword("admin"), null);
      return new UserSecurityContext(admin);
    }
  }
}
