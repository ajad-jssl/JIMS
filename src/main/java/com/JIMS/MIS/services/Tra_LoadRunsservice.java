package com.JIMS.MIS.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.Tra_LoadRunInterfaces;
import com.JIMS.MIS.model.Tra_LoadRuns;


@Service
public class Tra_LoadRunsservice {
	
	private static final Logger logger =LogManager.getLogger(Tra_LoadRunsservice.class);
			private final Tra_LoadRunInterfaces tra_loadrunsrepository; 
	
	public Tra_LoadRunsservice(Tra_LoadRunInterfaces tra_loadrunsrepository) {
		super();
		this.tra_loadrunsrepository=tra_loadrunsrepository;
	}
	
	 public Map<String,Object> addtra_loadruns(Tra_LoadRuns tra_loadruns){
		 logger.info("In addtra_loads()....");
			Map<String, Object> tra_loadrunsresponse = new HashMap<String, Object>();
			Tra_LoadRuns obj =null;
			try {
				if(tra_loadruns!=null) {
					obj = tra_loadrunsrepository.save(tra_loadruns);
				}
				tra_loadrunsresponse.put("Message",(obj != null)? "run Created Sucessfully":"run Creation Failure");
			}catch (Exception e) {
				logger.info(e.getMessage());
				tra_loadrunsresponse.put("Message", e.getMessage());
				
			}
			tra_loadrunsresponse .put("Action", "CREATE");
			tra_loadrunsresponse.put("status", (obj != null) ? "Success":"Failure");
			return tra_loadrunsresponse;
	 }
	 
	    @Autowired
	    @Qualifier("misDataSource")
	    private DataSource misDataSource;

	    @Autowired
	    @Qualifier("misDataSource2")
	    private DataSource misDataSource2;
	 public Map<String, Object> addTraLoadRunsFactoryWise(
		        Tra_LoadRuns t, int factoryId) {

		    Map<String, Object> response = new HashMap<>();

		    String sql = """
		        INSERT INTO Tra_LoadRuns
		        (
		            created,
		            tload_id,
		            haulier_id,
		            destination_id,
		            reqddate,
		            reqdtime,
		            notes,
		            trate_id,
		            loadtype_id,
		            tablerate,
		            adjustment,
		            fuelpc,
		            fuelcost,
		            totalcost,
		            invoiceno,
		            costnotes,
		            despatchdate,
		            arrivaldate,
		            despatchtime,
		            arrivaltime,
		            delaycost,
		            length,
		            width,
		            runstatus_id,
		            leftdestdate,
		            leftdesttime,
		            createdby,
		            createddate,
		            updatedby,
		            updateddate
		        )
		        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
		        """;

		    DataSource selectedDS = (factoryId == 1) ? misDataSource : misDataSource2;

		    try (Connection con = selectedDS.getConnection();
		    		   PreparedStatement ps = con.prepareStatement(sql)) {

		        ps.setObject(1, t.getCreated());
		        ps.setInt(2, t.getTload_id());
		        ps.setInt(3, t.getHaulier_id());
		        ps.setInt(4, t.getDestination_id());
		        ps.setObject(5, t.getReqddate());
		        ps.setString(6, t.getReqdtime());
		        ps.setString(7, t.getNotes());
		        ps.setInt(8, t.getTrate_id());
		        ps.setInt(9, t.getLoadtype_id());
		        ps.setBigDecimal(10, t.getTablerate());
		        ps.setBigDecimal(11, t.getAdjustment());
		        ps.setBigDecimal(12, t.getFuelpc());
		        ps.setBigDecimal(13, t.getFuelcost());
		        ps.setBigDecimal(14, t.getTotalcost());
		        ps.setString(15, t.getInvoiceno());
		        ps.setString(16, t.getCostnotes());
		        ps.setObject(17, t.getDespatchdate());
		        ps.setObject(18, t.getArrivaldate());
		        ps.setString(19, t.getDespatchtime());
		        ps.setString(20, t.getArrivaltime());
		        ps.setBigDecimal(21, t.getDelaycost());
		        ps.setString(22, t.getLength());
		        ps.setString(23, t.getWidth());
		        ps.setInt(24, t.getRunstatus_id());
		        ps.setObject(25, t.getLeftdestdate());
		        ps.setString(26, t.getLeftdesttime());
		        ps.setInt(27, t.getCreatedby());
		        ps.setObject(28, t.getCreateddate());
		        ps.setString(29, t.getUpdatedby());
		        ps.setObject(30, t.getUpdateddate());

		        ps.executeUpdate();

		 
		        response.put("Message", "LoadRun Created Successfully");
		        response.put("status", "Success");

		    } catch (SQLException e) {
		        response.put("Message", e.getMessage());
		        response.put("status", "Failure");
		    }

		    response.put("Action", "CREATE");
		    return response;
		}

	 	public Map<String,Object> listtra_loadruns(){
	 		logger.info("In listtra_loadruns()....");
			Map<String, Object> tra_loadrunsresponse = new HashMap<String, Object>();
			List<Tra_LoadRuns> user = null ;
			try {
				user = tra_loadrunsrepository.findAll();
				  if (user != null && !user.isEmpty()) {
					  tra_loadrunsresponse.put("Data",(user != null)?user:"null");
					  tra_loadrunsresponse.put("Message", (user != null)?"run Listing SuccessFully":"run Listing No Data");
			        }
				} catch (Exception e) {
					logger.info(e.getMessage());
					tra_loadrunsresponse.put("Message",e.getMessage());
				}
			tra_loadrunsresponse.put("Action", "FETCH");
			tra_loadrunsresponse.put("status", (user != null) ? "Success":"Failure");
			return tra_loadrunsresponse;
	 	}
	 	
		public Map<String,Object> updatetra_loadruns(Tra_LoadRuns tra_loadsruns){
			logger.info("In updatetra_loadruns()....");
			Map<String, Object> tra_loadrunsresponse = new HashMap<String, Object>();
			Tra_LoadRuns obj = null;
			try {
				if(tra_loadsruns != null) {
					obj = tra_loadrunsrepository.save(tra_loadsruns);
				}
				tra_loadrunsresponse.put("message",(obj != null) ? "Lot Updated Sucessfully":"Already Used in Transaction not able to Update");
			} catch (Exception e) {
				logger.info(e.getMessage());
				tra_loadrunsresponse.put("Message",e.getMessage());
			}
			tra_loadrunsresponse.put("Status",(obj != null) ? "Success":"Failure");
			tra_loadrunsresponse.put("Action", "UPDATE");		
			return tra_loadrunsresponse;
			
		}
	

		public void deletetra_loadruns(int id)   
		{  
		tra_loadrunsrepository.deleteById(id);  
		}  

}
