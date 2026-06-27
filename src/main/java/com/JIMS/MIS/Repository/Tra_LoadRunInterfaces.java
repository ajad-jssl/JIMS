package com.JIMS.MIS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.JIMS.MIS.model.Tra_LoadRuns;

public interface Tra_LoadRunInterfaces extends JpaRepository<Tra_LoadRuns, Integer> {
	@Query(value = "select * from [dbo].[Tra-LoadRuns] where [run-id]=:id",nativeQuery = true)
	int findbyrunId(int id);

}
