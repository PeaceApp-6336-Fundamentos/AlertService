package com.upc.pre.peaceapp.alerts.application.internal.outboundservices;

import com.upc.pre.peaceapp.alerts.infrastructure.external.clients.ReportServiceClient;
import com.upc.pre.peaceapp.alerts.infrastructure.external.dto.ReportDto;
import org.springframework.stereotype.Service;

@Service
public class ExternalReportService {

    private final ReportServiceClient reportServiceClient;

    public ExternalReportService(ReportServiceClient reportServiceClient) {
        this.reportServiceClient = reportServiceClient;
    }

    public boolean existsById(Long reportId) {
        return reportServiceClient.reportExists(reportId);
    }

    public ReportDto fetchById(Long reportId) {
        return reportServiceClient.getReportById(reportId);
    }
}
