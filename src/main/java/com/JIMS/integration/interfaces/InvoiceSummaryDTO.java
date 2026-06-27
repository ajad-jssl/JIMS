package com.JIMS.integration.interfaces;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceSummaryDTO {
    public InvoiceSummaryDTO(String string, long longValue, double doubleValue, long longValue2, double doubleValue2,
			long longValue3, double doubleValue3, long longValue4, double doubleValue4) {
		// TODO Auto-generated constructor stub
	}
	private String invoiceType;
    private Long totalCount;
    private Double totalAmount;
    private Long releaseCount;
    private Double releaseAmount;
    private Long cancelCount;
    private Double cancelAmount;
    private Long verifiedCount;
    private Double verifiedAmount;
}