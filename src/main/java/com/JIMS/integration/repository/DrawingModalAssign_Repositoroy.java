package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.DrawingModalAssign;


@Repository
public interface DrawingModalAssign_Repositoroy extends JpaRepository<DrawingModalAssign,Integer> {

	
//	 boolean existsByUserId(Integer userId);
	 
	  boolean existsByUserIdAndTypeId(Integer userId, Integer typeId);
	  
	  
	  
	  @Query(value=" select count(*) from Jc_Drawntxn where USER_ID=:userId and TYPE_ID=:typeId",nativeQuery = true)
	  public Integer isUsedmodal(@Param("userId") Integer userId,@Param("typeId") Integer typeId);
}
