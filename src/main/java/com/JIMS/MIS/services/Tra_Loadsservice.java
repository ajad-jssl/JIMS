package com.JIMS.MIS.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.JIMS.MIS.Repository.tra_loadsInterfaces;
import com.JIMS.MIS.model.Tra_Loads;

import javax.sql.DataSource;

@Service
public class Tra_Loadsservice {
    private static final Logger logger = LogManager.getLogger(Tra_Loadsservice.class); 	
    private final tra_loadsInterfaces tra_loadsrepository;

    public Tra_Loadsservice(tra_loadsInterfaces tra_loadsrepository) {
        this.tra_loadsrepository = tra_loadsrepository;
    }

    @Transactional("misTransactionManager")
    public Map<String, Object> addtra_loads(Tra_Loads tra_loads) {
        logger.info("In addtra_loads()....");
        Map<String, Object> response = new HashMap<>();
        Tra_Loads obj = null;
        try {
            if (tra_loads != null) {
                obj = tra_loadsrepository.save(tra_loads);
            }
            response.put("Message", (obj != null) ? "Lot Created Successfully" : "Lot Creation Failure");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.put("Message", e.getMessage());
        }
        response.put("Action", "CREATE");
        response.put("status", (obj != null) ? "Success" : "Failure");
        return response;
    }

    public List<Tra_Loads> findbycontractId(Integer contractId) {
        return tra_loadsrepository.findbycontractId(contractId);
    }

    @Transactional("misTransactionManager")
    public Map<String, Object> updatetra_loads(Tra_Loads tra_loads) {
        logger.info("In updatetra_loads()....");
        Map<String, Object> response = new HashMap<>();
        Tra_Loads obj = null;
        try {
            if (tra_loads != null) {
                obj = tra_loadsrepository.save(tra_loads);
            }
            response.put("message", (obj != null) ? "Lot Updated Successfully" : "Update Failed");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.put("Message", e.getMessage());
        }
        response.put("Status", (obj != null) ? "Success" : "Failure");
        response.put("Action", "UPDATE");
        return response;
    }
    
    @Autowired
    @Qualifier("misDataSource")
    private DataSource misDataSource;

    @Autowired
    @Qualifier("misDataSource2")
    private DataSource misDataSource2;

    public Map<String, Object> addTraLoadsFactoryWise(
            Tra_Loads t, int factoryId) {

        Map<String, Object> response = new HashMap<>();

        String sql = """
                INSERT INTO Tra_Loads
                (
                    loadno,
                    contract_id,
                    trailerref,
                    trackingref,
                    created,
                    wb_weight,
                    exciseinvoice,
                    descr,
                    factory_id,
                    supid,
                    createddate,
                    createdby
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        DataSource selectedDS =
                (factoryId == 1) ? misDataSource : misDataSource2;

        try (Connection con = selectedDS.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

        	ps.setString(1, t.getLoadno());
        	ps.setInt(2, t.getContract_id());
        	ps.setString(3, t.getTrailerref());
        	ps.setString(4, t.getTrackingref());
        	ps.setObject(5, t.getCreated());           // from frontend ISO
        	ps.setBigDecimal(6, t.getWb_weight());
        	ps.setString(7, t.getExciseinvoice());
        	ps.setString(8, t.getDescr());
        	ps.setInt(9, factoryId);                   // from URL
        	ps.setInt(10, t.getSupid());
        	ps.setObject(11, t.getCreateddate());      // backend or frontend
        	ps.setString(12, t.getCreatedby());        // backend or frontend


            ps.executeUpdate();

            response.put("Message", "Lot Created Successfully");
            response.put("status", "Success");

        } catch (SQLException e) {
            response.put("Message", e.getMessage());
            response.put("status", "Failure");
        }

        response.put("Action", "CREATE");
        return response;
    }
}
