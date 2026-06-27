package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.gpms_itemsrecieved;


@Repository
public interface gpms_itemsrecievedeinterface extends JpaRepository<gpms_itemsrecieved, Integer> {

}
