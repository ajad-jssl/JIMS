package com.JIMS.integration.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DrawingEntryDTO {

    Integer getId();

    Integer getUserId();

    String getEmpId();

    Integer getShiftId();

    Integer getContractId();

    Integer getPhaseId();

    String getTxtDate(); // Stored as nvarchar(10) in SQL

    Integer getYear();

    Integer getWeek();

    Integer getTypeId();

    Integer getActivityCategoryId();

    BigDecimal getDhHours();

    BigDecimal getIhHours();

    BigDecimal getEhHours();

    String getRework(); // char(1) in SQL, use String

    BigDecimal getRhHours();

    BigDecimal getTotalHours();

    BigDecimal getGrandTotal();

    Integer getReasonId();

    Integer getEcNo();

    String getCreatedBy();
    
    Integer getMonth();

    LocalDateTime getCreatedDate();

    String getModifiedBy();

    LocalDateTime getModifiedDate();
    
    Integer getisDeleted();
}