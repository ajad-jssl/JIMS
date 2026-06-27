package com.JIMS.integration.interfaces;

public interface ServiceCodeMasterInterface {

	String getServicecode_id();
	String getService_type();
	String getService_description();
	String getService_code();
	String getCreated_by();
	String getCreated_date();
	String getFactory_id();
}
/*
ServiceCodeId INT,
ServiceType VARCHAR(110),
ServiceDescription VARCHAR(MAX),
ServiceCode VARCHAR(50),
Status VARCHAR(50),
created_by VARCHAR(100) NOT NULL,
created_date DATETIME NOT NULL,
modified_by VARCHAR(100),
*/