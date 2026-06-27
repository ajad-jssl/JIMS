package com.JIMS.MIS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.AMS_Contracts;



@Repository
public interface AMS_Contract_List_Repo extends JpaRepository<AMS_Contracts, Integer>  {

}
