package com.JIMS.MIS.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.updatepiecestounload;

import jakarta.transaction.Transactional;

@Repository
public interface updatepiecestounloadInterfaces  extends JpaRepository<updatepiecestounload, Integer>{
	 @Modifying
	    @Transactional
    @Query(value="SET ROWCOUNT :qty UPDATE Piece_Instance  SET tload_id=0 WHERE pieces_id = :piecesId AND tload_id = :tloadId AND load_id = :loadId AND fab_id = 1 AND supplier_id = :supplierId AND locfg_id = :locfgId",nativeQuery = true)
    int updatePiecestounload(
    		@Param("qty") int qty,
            @Param("tloadId") int tloadId,
            @Param("piecesId") int piecesId,
            @Param("loadId") int loadId,
            @Param("supplierId") int supplierId,
            @Param("locfgId") int locfgId
    );
}
