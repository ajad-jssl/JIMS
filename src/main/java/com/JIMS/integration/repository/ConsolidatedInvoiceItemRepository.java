package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.ConsolidatedInvoiceItem;

@Repository
public interface ConsolidatedInvoiceItemRepository
        extends JpaRepository<ConsolidatedInvoiceItem, Integer> {

    List<ConsolidatedInvoiceItem> findByConpnid(Integer conpnid);
}
