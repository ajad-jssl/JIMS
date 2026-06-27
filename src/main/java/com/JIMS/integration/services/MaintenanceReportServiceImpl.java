package com.JIMS.integration.services;

import com.JIMS.integration.entity.MaintenanceReportDTO;
import com.JIMS.integration.interfaces.MaintenanceReportService;
import com.JIMS.integration.repository.MaintenanceReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceReportServiceImpl implements MaintenanceReportService {

    @Autowired
    private MaintenanceReportRepository maintenanceReportRepository;

    @Override
    public List<MaintenanceReportDTO> getAllReports() {
        return maintenanceReportRepository.fetchAllReports();
    }

    @Override
    public List<MaintenanceReportDTO> getReportsByDateRange(String fromDate, String toDate) {
        return maintenanceReportRepository.fetchReportsByDateRange(fromDate, toDate);
    }
}
