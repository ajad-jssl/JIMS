package com.JIMS.integration.entity;



import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "MAINTENANCE_WORKER_DESIGNATION")
public class MaintenanceWorkerDesignation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORKER_DESIG_ID")
    private Integer workerDesigId;

    @Column(name = "DESIGNATION_ID")
    private Integer designationId;

    @Column(name = "WORKER_MAPPING_ID")
    private Long workerMappingId;

    @Column(name = "FACTORY_ID")
    private Integer factoryId;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;

    @Column(name = "STATUS")
    private Boolean status;

	public Integer getWorkerDesigId() {
		return workerDesigId;
	}

	public void setWorkerDesigId(Integer workerDesigId) {
		this.workerDesigId = workerDesigId;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public Long getWorkerMappingId() {
		return workerMappingId;
	}

	public void setWorkerMappingId(Long workerMappingId) {
		this.workerMappingId = workerMappingId;
	}

	public Integer getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
    
}