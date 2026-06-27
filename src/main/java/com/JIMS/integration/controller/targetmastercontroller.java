package com.JIMS.integration.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.TargetMaster;
import com.JIMS.integration.entity.TargetMonthlyDetails;
import com.JIMS.integration.repository.TargetMasterRepository;
import com.JIMS.integration.repository.TargetMonthlyDetailsRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl/master")
public class targetmastercontroller {

    @Autowired
    private TargetMasterRepository targetrepo;
    
    @Autowired
    private TargetMonthlyDetailsRepository targetmonthlyrepo;

    @PostMapping("/addtargetmaster")
    public @ResponseBody Map<String, Object> addTargetMaster(

            @RequestParam String financial_year,
            @RequestParam String target_type,

            @RequestParam(required = false)
            Integer contract_id,

            @RequestParam Double target_production_ih,
            @RequestParam Double target_production_sc,

            @RequestParam Double target_dispatch_ih,
            @RequestParam Double target_dispatch_sc,

            @RequestParam Double target_erection_ih,
            @RequestParam Double target_erection_sc,

            @RequestParam Double target_manhours,

            @RequestParam Integer created_by,

            @RequestParam Integer[] month_no,

            @RequestParam Double[] prod_ih,
            @RequestParam Double[] prod_sc,

            @RequestParam Double[] disp_ih,
            @RequestParam Double[] disp_sc,

            @RequestParam Double[] erec_ih,
            @RequestParam Double[] erec_sc

    ) {
    	
        Map<String, Object> response = new HashMap<>();
        try {
        	
    	TargetMaster target = new TargetMaster();

    	target.setFinancialYear(financial_year);
    	target.setTargetType(target_type);
    	target.setContractId(contract_id);

    	target.setTargetProductionIh(target_production_ih);
    	target.setTargetProductionSc(target_production_sc);

    	target.setTargetDispatchIh(target_dispatch_ih);
    	target.setTargetDispatchSc(target_dispatch_sc);

    	target.setTargetErectionIh(target_erection_ih);
    	target.setTargetErectionSc(target_erection_sc);

    	target.setTargetManhours(target_manhours);

    	target.setCreatedBy(created_by);
    	target.setCreatedDate(LocalDateTime.now());

    	target = targetrepo.save(target);
    	for (int i = 0; i < month_no.length; i++) {

    	    TargetMonthlyDetails monthly =
    	            new TargetMonthlyDetails();

    	    monthly.setTargetId(
    	            target.getTargetId());

    	    monthly.setMonthNo(
    	            month_no[i]);

    	    monthly.setProdIh(
    	            prod_ih[i]);

    	    monthly.setProdSc(
    	            prod_sc[i]);

    	    monthly.setDispIh(
    	            disp_ih[i]);

    	    monthly.setDispSc(
    	            disp_sc[i]);

    	    monthly.setErecIh(
    	            erec_ih[i]);

    	    monthly.setErecSc(
    	            erec_sc[i]);

    	    monthly.setCreatedBy(
    	            created_by);

    	    monthly.setCreatedDate(
    	            LocalDateTime.now());

    	    targetmonthlyrepo
    	            .save(monthly);
    	    
    	       response.put("status", true);
    	        response.put("message", "Success");
    	}
        } catch (Exception e) {

            response.put("status", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
    @GetMapping("/searchtargetmaster")
    public @ResponseBody Map<String, Object> searchTargetMaster(
            @RequestParam Integer targetId) {

        Map<String, Object> response = new HashMap<>();

        try {

            Optional<TargetMaster> target =
                    targetrepo.findById(targetId);

            if (target.isPresent()) {

                List<TargetMonthlyDetails> monthlyDetails =
                        targetmonthlyrepo.findByTargetId(targetId);

                response.put("status", true);

                response.put("targetMaster",
                        target.get());

                response.put("monthlyDetails",
                        monthlyDetails);

            } else {

                response.put("status", false);
                response.put("message", "Record Not Found");
            }

        } catch (Exception e) {

            response.put("status", false);
            response.put("message", e.getMessage());
        }

        return response;
    }

    @PostMapping("/updatetargetmaster")
    public @ResponseBody Map<String, Object> updateTargetMaster(

            @RequestParam Integer target_id,

            @RequestParam String financial_year,
            @RequestParam String target_type,

            @RequestParam(required = false)
            Integer contract_id,

            @RequestParam Double target_production_ih,
            @RequestParam Double target_production_sc,

            @RequestParam Double target_dispatch_ih,
            @RequestParam Double target_dispatch_sc,

            @RequestParam Double target_erection_ih,
            @RequestParam Double target_erection_sc,

            @RequestParam Double target_manhours,

            @RequestParam Integer modified_by,

            @RequestParam Integer[] month_no,

            @RequestParam Double[] prod_ih,
            @RequestParam Double[] prod_sc,

            @RequestParam Double[] disp_ih,
            @RequestParam Double[] disp_sc,

            @RequestParam Double[] erec_ih,
            @RequestParam Double[] erec_sc

    ) {

        Map<String, Object> response = new HashMap<>();

        try {

            Optional<TargetMaster> optional =
                    targetrepo.findById(target_id);

            if (!optional.isPresent()) {

                response.put("status", false);
                response.put("message", "Record Not Found");

                return response;
            }

            TargetMaster target = optional.get();

            target.setFinancialYear(financial_year);
            target.setTargetType(target_type);
            target.setContractId(contract_id);

            target.setTargetProductionIh(target_production_ih);
            target.setTargetProductionSc(target_production_sc);

            target.setTargetDispatchIh(target_dispatch_ih);
            target.setTargetDispatchSc(target_dispatch_sc);

            target.setTargetErectionIh(target_erection_ih);
            target.setTargetErectionSc(target_erection_sc);

            target.setTargetManhours(target_manhours);

            target.setModifiedBy(modified_by);
            target.setModifiedDate(LocalDateTime.now());

            targetrepo.save(target);

      

            for (int i = 0; i < month_no.length; i++) {

                Optional<TargetMonthlyDetails> optionalMonthly =
                        targetmonthlyrepo.findByTargetIdAndMonthNo(
                                target_id,
                                month_no[i]);

                TargetMonthlyDetails monthly;

                if (optionalMonthly.isPresent()) {

                    monthly = optionalMonthly.get();

                } else {

                    monthly = new TargetMonthlyDetails();

                    monthly.setTargetId(target_id);
                    monthly.setMonthNo(month_no[i]);

                    monthly.setCreatedBy(modified_by);
                    monthly.setCreatedDate(LocalDateTime.now());
                }

                monthly.setProdIh(prod_ih[i]);
                monthly.setProdSc(prod_sc[i]);

                monthly.setDispIh(disp_ih[i]);
                monthly.setDispSc(disp_sc[i]);

                monthly.setErecIh(erec_ih[i]);
                monthly.setErecSc(erec_sc[i]);

                targetmonthlyrepo.save(monthly);
            }
            response.put("status", true);
            response.put("message", "Success");

        } catch (Exception e) {

            response.put("status", false);
            response.put("message", e.getMessage());
        }

        return response;
    }

    @GetMapping("/listtargetmaster")
    public @ResponseBody Map<String, Object> listTargetMaster() {

        Map<String, Object> response = new HashMap<>();

        try {

            List<Object[]> rows =
                    targetrepo.getTargetMasterDetails();

            List<Map<String, Object>> data =
                    new ArrayList<>();

            for (Object[] row : rows) {

                Map<String, Object> map =
                        new HashMap<>();

                map.put("targetId", row[0]);
                map.put("financialYear", row[1]);
                map.put("targetType", row[2]);
                map.put("contractId", row[3]);

                map.put("targetProductionIh", row[4]);
                map.put("targetProductionSc", row[5]);

                map.put("targetDispatchIh", row[6]);
                map.put("targetDispatchSc", row[7]);

                map.put("targetErectionIh", row[8]);
                map.put("targetErectionSc", row[9]);

                map.put("targetManhours", row[10]);

                map.put("createdBy", row[11]);
                map.put("createdDate", row[12]);

                map.put("modifiedBy", row[13]);
                map.put("modifiedDate", row[14]);

                map.put("jobCode", row[15]);

                data.add(map);
            }

            response.put("status", true);
            response.put("data", data);

        } catch (Exception e) {

            response.put("status", false);
            response.put("message", e.getMessage());
        }

        return response;
    }
}