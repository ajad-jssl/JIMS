package com.JIMS.MIS.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.PieceStage;

@Repository
public interface PieceStageRepository extends JpaRepository<PieceStage, Integer> { 
	@Query(value = "  select * from PieceStage where piecestage_id =:Id ",nativeQuery = true)
	List<PieceStage>  findbyId(@Param("Id") Integer id);

}

 