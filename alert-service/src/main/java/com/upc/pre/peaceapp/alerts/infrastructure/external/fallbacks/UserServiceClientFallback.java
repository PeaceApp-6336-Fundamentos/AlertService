package com.upc.pre.peaceapp.alerts.infrastructure.external.fallbacks;

import com.upc.pre.peaceapp.alerts.infrastructure.external.clients.UserServiceClient;
import com.upc.pre.peaceapp.alerts.infrastructure.external.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public UserDto getUserById(Long id) {
        log.warn("Fallback: getUserById called for user ID: {}", id);
        return new UserDto(
                id,
                "Unknown",
                "Unknown",
                "unknown@example.com",
                "000000000",
                "N/A",
                "default.png"
        );
    }

    @Override
    public Boolean userExists(Long id) {
        log.warn("Fallback: userExists called for user ID: {}", id);
        return false;
    }
}
