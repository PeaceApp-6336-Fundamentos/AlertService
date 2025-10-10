package com.upc.pre.peaceapp.alerts.infrastructure.external.clients;

import com.upc.pre.peaceapp.alerts.infrastructure.external.dto.ReportDto;
import com.upc.pre.peaceapp.alerts.infrastructure.external.fallbacks.ReportServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "report-service", fallback = ReportServiceClientFallback.class)
public interface ReportServiceClient {

    @GetMapping("/api/v1/reports/{id}")
    ReportDto getReportById(@PathVariable("id") Long id);

    @GetMapping("/api/v1/reports/{id}/exists")
    Boolean reportExists(@PathVariable("id") Long id);
}
