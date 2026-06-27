package com.JIMS.MIS.services;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.updatepiecestounloadInterfaces;

@Service
public class piecestounloadservice {
    @Autowired
    private updatepiecestounloadInterfaces pieceInstanceRepository;

    public String updatePiecestounload(int qty,int tloadId, int piecesId, int loadId, int supplierId, int locfgId) {
    	 int rowsAffected = pieceInstanceRepository.updatePiecestounload(qty,tloadId, piecesId, loadId, supplierId, locfgId);
         if (rowsAffected > 0) {
             return "unload successful!";
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

  
    public int updatePiecestounload(
            int qty,
            int tloadId,
            int piecesId,
            int loadId,
            int supplierId,
            int locfgId,
            int factoryId
    ) {

        if (qty <= 0) {
            return 0;
        }

        DataSource ds = (factoryId == 1) ? misDataSource : misDataSource2;

        String sql = """
            UPDATE TOP (%d) Piece_Instance
            SET tload_id = 0
            WHERE pieces_id = ?
              AND tload_id = ?
              AND load_id = ?
              AND fab_id = 1
              AND supplier_id = ?
              AND locfg_id = ?
            """.formatted(qty);

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int idx = 1;
            ps.setInt(idx++, piecesId);
            ps.setInt(idx++, tloadId);
            ps.setInt(idx++, loadId);
            ps.setInt(idx++, supplierId);
            ps.setInt(idx++, locfgId);

            return ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Error unloading pieces", e);
        }
    }
}
