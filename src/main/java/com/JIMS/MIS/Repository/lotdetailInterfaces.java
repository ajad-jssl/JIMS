package com.JIMS.MIS.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.lotdetail;

@Repository
public interface lotdetailInterfaces extends JpaRepository<lotdetail, Integer>{


   @Query(value = "EXEC spTRA_GetPiecesInLoad :tLoadId", nativeQuery = true)
    List<lotdetail> getPiecesInLoadByTLoadId(int tLoadId);
}
