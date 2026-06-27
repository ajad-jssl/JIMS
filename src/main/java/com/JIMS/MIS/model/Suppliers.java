package com.JIMS.MIS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "Suppliers")
public class Suppliers {
	   @Id
	    @Column(name = "supplier_id")
	    private Long supplierId;

	    @Column(name = "code")
	    private String code;

	    @Column(name = "name")
	    private String name;

	    @Column(name = "suppcat_id")
	    private Integer suppcatId;

		public Long getSupplierId() {
			return supplierId;
		}

		public void setSupplierId(Long supplierId) {
			this.supplierId = supplierId;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getSuppcatId() {
			return suppcatId;
		}

		public void setSuppcatId(Integer suppcatId) {
			this.suppcatId = suppcatId;
		}
}
