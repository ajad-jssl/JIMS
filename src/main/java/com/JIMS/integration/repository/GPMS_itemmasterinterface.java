package com.JIMS.integration.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.GPMS_itemmaster;

@Repository
public interface GPMS_itemmasterinterface extends JpaRepository<GPMS_itemmaster, Integer> {
    // JpaRepository already provides basic CRUD operations like save(), findById(), deleteById(), etc.
}
