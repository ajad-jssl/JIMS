package com.JIMS.MIS.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.updatepieceinstance;

import jakarta.transaction.Transactional;
@Repository
public interface updatepieceinstanceInterfaces extends JpaRepository<updatepieceinstance, Integer> {
	   @Modifying
	    @Transactional
	@Query(value = "SET ROWCOUNT :qty UPDATE Piece_Instance SET tload_id = :tloadId " +
            "WHERE load_id <> 0 " +
            "AND tload_id = 0 " +
            "AND instance_id IN (SELECT i.instance_id " +
                                "FROM piece_instance i " +
                                "INNER JOIN pieces p ON i.pieces_id = p.pieces_id " +
                                "AND i.revision = p.revision " +
                                "WHERE p.comp_id = 0 " +
                                "AND contract_id = :contractId) " +
            "AND load_id = :phaseLoadId " +
            "AND pieces_id = :chkLoadItemValue " +
            "AND instance_id IN (SELECT pieceinstance_id FROM PieceStage WHERE piecestages_id = 3) " +
            "AND instance_id NOT IN (SELECT pieceinstance_id FROM PieceRework WHERE reworkdate IS NULL) " +
            "AND fab_id = 1", 
    nativeQuery = true)
int updatePieceInstance(@Param("qty") int qty,
					 @Param("tloadId") String tloadId,
                     @Param("contractId") String contractId,
                     @Param("phaseLoadId") String phaseLoadId,
                     @Param("chkLoadItemValue") String chkLoadItemValue);
}

