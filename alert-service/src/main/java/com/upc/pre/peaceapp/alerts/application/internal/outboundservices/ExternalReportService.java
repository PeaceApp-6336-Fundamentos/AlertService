package com.upc.pre.peaceapp.alerts.application.internal.outboundservices;

import com.upc.pre.peaceapp.alerts.infrastructure.external.clients.ReportServiceClient;
import com.upc.pre.peaceapp.alerts.infrastructure.external.dto.ReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalReportService {

    private final ReportServiceClient reportServiceClient;

    public boolean existsById(Long reportId) {
        if (reportId == null) return false;
        try {
            return reportServiceClient.reportExists(reportId);
        } catch (Exception e) {
            // Si el microservicio de reportes está caído, devolvemos false
            return false;
        }
    }

    public ReportDto fetchById(Long reportId) {
        if (reportId == null) return null;
        try {
            return reportServiceClient.getReportById(reportId);
        } catch (Exception e) {
            // Manejamos el fallback en caso de error
            return null;
        }
    }
}
