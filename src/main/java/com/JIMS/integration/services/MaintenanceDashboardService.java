package com.JIMS.integration.services;

import com.JIMS.integration.entity.MaintenanceMachineSummaryDTO;
import com.JIMS.integration.entity.Maintenancecategorysummarydto;
import com.JIMS.integration.entity.TopBrokeCauseDTO;
import com.JIMS.integration.entity.MachineListDTO;
import com.JIMS.integration.entity.MachineTicketDetailDTO;
import com.JIMS.integration.entity.MachinecategoryListDTO;
import com.JIMS.integration.repository.MaintenanceDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MaintenanceDashboardService {

    @Autowired
    private MaintenanceDashboardRepository repository;

    public List<MaintenanceMachineSummaryDTO> getSummaryByMachine(
            Integer factoryId, String machineSubcode, String dateFrom, String dateTo) {
        return repository.getSummaryByMachine(factoryId, machineSubcode, dateFrom, dateTo);
    }

    public MaintenanceMachineSummaryDTO getGrandTotal(
            Integer factoryId, String machineSubcode, String dateFrom, String dateTo) {
        return repository.getGrandTotal(factoryId, machineSubcode, dateFrom, dateTo);
    }

    public List<Maintenancecategorysummarydto> getSummaryByCategory(
            Integer factoryId, String machineSubcode, String dateFrom, String dateTo) {
        return repository.getSummaryByCategory(factoryId, machineSubcode, dateFrom, dateTo);
    }

    public List<MachineListDTO> getMachineList(Integer factoryId, String category) {
        return repository.getMachineList(factoryId, category);
    }

    public List<MachinecategoryListDTO> getcategoryList(Integer factoryId) {
        return repository.getMachinecategoryList(factoryId);
    }

    public List<MachineTicketDetailDTO> getTicketsByMachine(
            String machineSubcode, Integer factoryId, String dateFrom, String dateTo) {
        return repository.getTicketsByMachine(machineSubcode, factoryId, dateFrom, dateTo);
    }
    public Map<String, List<TopBrokeCauseDTO>> getTopBrokeCauses(
            Integer factoryId, String dateFrom, String dateTo) {
        return repository.getTopBrokeCauses(factoryId, dateFrom, dateTo);
    }
}
