package com.JIMS.integration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "FACTORY_MASTER")
public class Factory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "factory_name")
	private String factory_name;

	@Column(name = "is_delete")
	private boolean is_delete;
	
	
	@Column(name="GST_No")
	private String gstno;
	
	
	@Column(name="StateCode")
	private String StateCode;
	
	
	@Column(name="address")
	private String address;

}


