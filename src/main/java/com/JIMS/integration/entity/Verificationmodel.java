package com.JIMS.integration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Invoice_No",schema = "dbo")
public class Verificationmodel {
	@Id
	@Column(name="invoice_id")
	private int invoice_id;
	
	@Column(name="verification")
	private int verfication;

	public int getVerfication() {
		return verfication;
	}

	public void setVerfication(int verfication) {
		this.verfication = verfication;
	}

	public int getInvoice_id() {
		return invoice_id;
	}

	public void setInvoice_id(int invoice_id) {
		this.invoice_id = invoice_id;
	}
	
}
