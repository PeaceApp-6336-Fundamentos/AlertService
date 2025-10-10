package com.upc.pre.peaceapp.alerts.infrastructure.external.clients;

import com.upc.pre.peaceapp.alerts.infrastructure.external.dto.UserDto;
import com.upc.pre.peaceapp.alerts.infrastructure.external.fallbacks.UserServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        fallback = UserServiceClientFallback.class
)
public interface UserServiceClient {

    @GetMapping("/api/v1/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/v1/users/{id}/exists")
    Boolean userExists(@PathVariable("id") Long id);
}
