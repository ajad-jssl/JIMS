package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Module_Master")
public class Modules {

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name="module_id")
	    private Integer id;
		
		
		@Column(name="module_name")
		private String moduleName;
		
		@Column(name = "created_by")
	    private String createdBy;

	    @Column(name = "created_date")
	    private LocalDateTime createdDate;

	    @Column(name = "modified_by")
	    private String modifiedBy;

	    @Column(name = "modified_date")
	    private LocalDateTime modifiedDate;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getModuleName() {
			return moduleName;
		}

		public void setModuleName(String moduleName) {
			this.moduleName = moduleName;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public LocalDateTime getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(LocalDateTime createdDate) {
			this.createdDate = createdDate;
		}

		public String getModifiedBy() {
			return modifiedBy;
		}

		public void setModifiedBy(String modifiedBy) {
			this.modifiedBy = modifiedBy;
		}

		public LocalDateTime getModifiedDate() {
			return modifiedDate;
		}

		public void setModifiedDate(LocalDateTime modifiedDate) {
			this.modifiedDate = modifiedDate;
		}
	    
	    
	    
	 
}
