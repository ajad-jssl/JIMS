package com.JIMS.integration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

	 @Autowired
	    private JdbcTemplate jdbcTemplate;

    public void performDailyTask() {
    	  String sql = "UPDATE SERIES_MASTER SET status = CASE " +
                  "WHEN GETDATE() > end_date THEN 'Closed' " +
                  "ELSE 'Open' END";

     int rowsUpdated = jdbcTemplate.update(sql);
     System.out.println(rowsUpdated + " rows updated in the database.");
    }
}
