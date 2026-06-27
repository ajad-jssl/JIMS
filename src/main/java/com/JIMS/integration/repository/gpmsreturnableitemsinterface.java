package com.JIMS.integration.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.GPMSreturnableitems;


@Repository
public interface gpmsreturnableitemsinterface extends JpaRepository<GPMSreturnableitems, Integer> {

}
