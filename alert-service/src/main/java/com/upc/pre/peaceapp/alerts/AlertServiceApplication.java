package com.upc.pre.peaceapp.alerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {
        "com.upc.pre.peaceapp.alerts",
        "com.upc.pre.peaceapp.shared.documentation"
})
@EnableDiscoveryClient
@EnableJpaAuditing
@EnableFeignClients(basePackages = "com.upc.pre.peaceapp.alerts.infrastructure.external.clients")
public class AlertServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlertServiceApplication.class, args);
    }
}
