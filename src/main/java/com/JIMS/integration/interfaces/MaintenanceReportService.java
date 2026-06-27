package com.JIMS.integration.interfaces;

import com.JIMS.integration.entity.MaintenanceReportDTO;

import java.util.List;

public interface MaintenanceReportService {

    /**
     * Return every maintenance report record.
     */
    List<MaintenanceReportDTO> getAllReports();

    /**
     * Return records where REPORTED_DATE falls within [fromDate, toDate].
     * Dates should be in "yyyy-MM-dd" format.
     */
    List<MaintenanceReportDTO> getReportsByDateRange(String fromDate, String toDate);
}
