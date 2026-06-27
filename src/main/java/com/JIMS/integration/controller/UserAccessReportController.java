package com.JIMS.integration.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.repository.UserPermissionTabsRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class UserAccessReportController {

	
	  @GetMapping("/user-access")
	    public List<Map<String, Object>> getUserAccesscon(@RequestParam String userId) {
	        return getUserAccess(userId);
	    }
	
	@Autowired
	private UserPermissionTabsRepository repo;
	

    public List<Map<String, Object>> getUserAccess(String userId) {

        List<Object[]> results = repo.getUserAccessReport(userId);
        List<Map<String, Object>> list = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();

            map.put("userId", row[0]);
            map.put("moduleName", row[1]);
            map.put("moduleId", row[2]);
            map.put("pageName", row[3]);
            map.put("tabName", row[4]);
            map.put("subPageName", row[5]);
            map.put("actions", row[6]);

            list.add(map);
        }

        return list;
    }
    
    @GetMapping("/AllUser")
    public List<Map<String, Object>> getAllUsers() {
    	List<Object[]>result = repo.getAllUser();
    	  List<Map<String, Object>> list = new ArrayList<>();
    	  for (Object[] row : result) {
              Map<String, Object> map = new HashMap<>();

              map.put("Id", row[0]);
              map.put("userId", row[1]);
              map.put("status",  row[2]);
              map.put("department", row[3]);

              list.add(map);
          }
    	  
    	  return list;
    }
  
    
    @GetMapping("/getHistoryReport")
    public ResponseEntity<?> getHistoryReport(

            @RequestParam String userId,

            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date fromDate,

            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date toDate

    ) {

        List<Object[]> data =
                repo.getHistoryReport(userId, fromDate, toDate);

        return ResponseEntity.ok(data);
    }
}
