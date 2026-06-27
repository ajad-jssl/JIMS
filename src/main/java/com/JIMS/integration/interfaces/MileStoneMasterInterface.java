package com.JIMS.integration.interfaces;

public interface MileStoneMasterInterface {
	
	String getMilestone_id();
	String getMilestone_code();
	String getMilestone_name();
	String getMilestone_desc();
	String getCreated_date();
	String getModified_date();
	String getCreated_by();
	String getModified_by();
	String getFactory_id();
	String getMilestone_concat();
	

}
/*

milestone_id INT PRIMARY KEY,  -- Defines milestone_id as the primary key
milestone_code VARCHAR(50) NOT NULL,
milestone_name VARCHAR(50) NOT NULL,
milestone_desc VARCHAR(MAX),   -- Use VARCHAR(MAX) for large text fields in SQL Server
created_by VARCHAR(100) NULL,
created_date DATETIME NULL,
modified_by VARCHAR(100) NULL,
modified_date DATETIME NULL,
statuses VARCHAR(50) NOT NULL,
is_delete BIT DEFAULT 0        -- Sets a default value of 0 for is_delete
);
*/