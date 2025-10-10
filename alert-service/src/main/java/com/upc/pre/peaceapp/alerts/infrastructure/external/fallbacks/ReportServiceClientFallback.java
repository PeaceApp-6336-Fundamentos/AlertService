package com.upc.pre.peaceapp.alerts.infrastructure.external.fallbacks;

import com.upc.pre.peaceapp.alerts.infrastructure.external.clients.ReportServiceClient;
import com.upc.pre.peaceapp.alerts.infrastructure.external.dto.ReportDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportServiceClientFallback implements ReportServiceClient {

    @Override
    public ReportDto getReportById(Long id) {
        log.warn("Fallback: getReportById called for report ID: {}", id);
        return new ReportDto(
                id,
                "Unknown Report",
                "No description available",
                "Unknown location",
                "UNKNOWN_TYPE",
                0L,
                "no-image.png",
                "0.0",
                "0.0"
        );
    }

    @Override
    public Boolean reportExists(Long id) {
        log.warn("Fallback: reportExists called for report ID: {}", id);
        return false;
    }
}
