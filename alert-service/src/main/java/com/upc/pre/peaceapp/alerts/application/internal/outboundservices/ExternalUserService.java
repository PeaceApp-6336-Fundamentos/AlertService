package com.upc.pre.peaceapp.alerts.application.internal.outboundservices;

import com.upc.pre.peaceapp.alerts.infrastructure.external.clients.UserServiceClient;
import com.upc.pre.peaceapp.alerts.infrastructure.external.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class ExternalUserService {

    private final UserServiceClient userServiceClient;

    public ExternalUserService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    public boolean existsById(Long userId) {
        return userServiceClient.userExists(userId);
    }

    public UserDto fetchById(Long userId) {
        return userServiceClient.getUserById(userId);
    }
}
