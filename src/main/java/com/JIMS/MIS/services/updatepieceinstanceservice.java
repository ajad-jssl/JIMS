package com.JIMS.MIS.services;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.updatepieceinstanceInterfaces;

import jakarta.transaction.Transactional;

@Service
public class updatepieceinstanceservice {
	
    @Autowired
    private updatepieceinstanceInterfaces pieceInstanceRepository;

    public String updatePieceInstance(int qty,String lblLoadId, String contractId, String phaseLoadId, String chkLoadItemValue) {
        int rowsAffected = pieceInstanceRepository.updatePieceInstance(qty,lblLoadId, contractId, phaseLoadId, chkLoadItemValue);
        if (rowsAffected > 0) {
            return "Update successful!";
        } else {
            return "No rows were updated.";
        }
    }
    

    @Autowired
    @Qualifier("misDataSource")
    private DataSource misDataSource;

    @Autowired
    @Qualifier("misDataSource2")
    private DataSource misDataSource2;

   
    public int updatePieceInstanceFactoryWise(
            int qty,
            String tloadId,
            String contractId,
            String phaseLoadId,
            String chkLoadItemValue,
            int factoryId
    ) {

        DataSource ds = (factoryId == 1) ? misDataSource : misDataSource2;

        String sql = """
            UPDATE TOP (%d) Piece_Instance
            SET tload_id = ?
            WHERE load_id <> 0
              AND tload_id = 0
              AND load_id = ?
              AND pieces_id = ?
              AND fab_id = 1
              AND instance_id IN (
                    SELECT i.instance_id
                    FROM Piece_Instance i
                    INNER JOIN Pieces p
                        ON i.pieces_id = p.pieces_id
                       AND i.revision = p.revision
                    WHERE p.comp_id = 0
                      AND p.contract_id = ?
                )
              AND instance_id IN (
                    SELECT pieceinstance_id
                    FROM PieceStage
                    WHERE piecestages_id = 3
                )
              AND instance_id NOT IN (
                    SELECT pieceinstance_id
                    FROM PieceRework
                    WHERE reworkdate IS NULL
                )
            """.formatted(qty);

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int idx = 1;
            ps.setString(idx++, tloadId);
            ps.setString(idx++, phaseLoadId);
            ps.setString(idx++, chkLoadItemValue);
            ps.setString(idx++, contractId);

            return ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Error updating Piece_Instance", e);
        }
    }

}
