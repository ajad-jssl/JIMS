package com.JIMS.integration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
 @Entity
 @Table (name="GPMS_tabRetGatePass")
public class GpmsGPNGPidmaster {
	 @Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private int gpid;
		private String gp_no;
		private Integer GP_type;
		public int getGpid() {
			return gpid;
		}
		public void setGpid(int gpid) {
			this.gpid = gpid;
		}
		public String getGp_no() {
			return gp_no;
		}
		public void setGp_no(String gp_no) {
			this.gp_no = gp_no;
		}
		public Integer getGP_type() {
			return GP_type;
		}
		public void setGP_type(Integer gP_type) {
			GP_type = gP_type;
		}
 }
