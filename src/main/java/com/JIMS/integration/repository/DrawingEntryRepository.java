package com.JIMS.integration.repository;

import com.JIMS.integration.entity.Drawing_Entry_Entity;
import com.JIMS.integration.interfaces.DrawingEntryDTO;
import com.JIMS.integration.interfaces.DrawingReportDTO;
import com.JIMS.integration.interfaces.MonthlyTotalDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface DrawingEntryRepository extends JpaRepository<Drawing_Entry_Entity, Integer> {

	@Query("SELECT d.id AS id, " +
		       "d.userId AS userId, " +
		       "d.empId AS empId, " +
		       "d.shiftId AS shiftId, " +
		       "d.contractId AS contractId, " +
		       "d.phaseId AS phaseId, " +
		       "d.txtDate AS txtDate, " +
		       "d.year AS year, " +
		       "d.week AS week, " +
		       "d.typeId AS typeId, " +
		       "d.activityCategoryId AS activityCategoryId, " +
		       "d.dhHours AS dhHours, " +
		       "d.ihHours AS ihHours, " +
		       "d.ehHours AS ehHours, " +
		       "d.rework AS rework, " +
		       "d.rhHours AS rhHours, " +
		       "d.totalHours AS totalHours, " +
		       "d.grandTotal AS grandTotal, " +
		       "d.reasonId AS reasonId, " +
		       "d.month AS month,"+
		       "d.ecNo AS ecNo, " +
		       "d.createdBy AS createdBy, " +
		       "d.createdDate AS createdDate, " +
		       "d.modifiedBy AS modifiedBy, " +
		       "d.modifiedDate AS modifiedDate " +
		       "FROM Drawing_Entry_Entity d " +  
		       "WHERE d.userId = :userId " +
		       "AND d.txtDate = :txtDate " +
		       "AND d.year = :year AND (d.isDeleted IS NULL OR d.isDeleted = 0)")
		List<DrawingEntryDTO> findByUserIdAndTxtDateAndYear(Integer userId, String txtDate, Integer year);
	
	
	
	@Query(
		    value = """
		        SELECT COALESCE(SUM(GRAND_TOTAL), 0)
		        FROM Jc_Drawntxn
		        WHERE TXT_DATE = :txtDate
		          AND YEAR = :year
		          AND USER_ID = :userId AND (is_deleted IS NULL OR is_deleted = 0)
		    """,
		    nativeQuery = true
		)
		BigDecimal getTotalGrandTotalNative(
		        @Param("txtDate") String txtDate,
		        @Param("year") Integer year,
		        @Param("userId") Integer userId
		);
	
	@Query(value = """
			SELECT
			    SUM(CASE WHEN MONTH = '1' THEN GRAND_TOTAL ELSE 0 END) AS jan,
			    SUM(CASE WHEN MONTH = '2' THEN GRAND_TOTAL ELSE 0 END) AS feb,
			    SUM(CASE WHEN MONTH = '3' THEN GRAND_TOTAL ELSE 0 END) AS mar,
			    SUM(CASE WHEN MONTH = '4' THEN GRAND_TOTAL ELSE 0 END) AS apr,
			    SUM(CASE WHEN MONTH = '5' THEN GRAND_TOTAL ELSE 0 END) AS may,
			    SUM(CASE WHEN MONTH = '6' THEN GRAND_TOTAL ELSE 0 END) AS jun,
			    SUM(CASE WHEN MONTH = '7' THEN GRAND_TOTAL ELSE 0 END) AS jul,
			    SUM(CASE WHEN MONTH = '8' THEN GRAND_TOTAL ELSE 0 END) AS aug,
			    SUM(CASE WHEN MONTH = '9' THEN GRAND_TOTAL ELSE 0 END) AS sep,
			    SUM(CASE WHEN MONTH = '10' THEN GRAND_TOTAL ELSE 0 END) AS oct,
			    SUM(CASE WHEN MONTH = '11' THEN GRAND_TOTAL ELSE 0 END) AS nov,
			    SUM(CASE WHEN MONTH = '12' THEN GRAND_TOTAL ELSE 0 END) AS dec
			FROM Jc_Drawntxn
			WHERE YEAR = :year
			  AND USER_ID = :userId AND (is_deleted IS NULL OR is_deleted = 0)
			""", nativeQuery = true)
			Map<String, Object> getMonthlySummary(int year, int userId);

	 @Query(value = "SELECT " +
	            "jc.TXT_DATE as txtDate, " +
	            "jc.YEAR as year, " +
	            "con.JobCode as jobCode, " +
	            "con.cname as cname, " +
	            "jc.PHASE_ID as phaseId, " +
	            "sf.CODE as shiftCode, " +
	            "ty.TYPE_NAME as typeName, " +
	            "act.ACTIVITY_NAME as activityName, " +
	            "jc.DH_HOURS as dhHours, " +
	            "jc.IH_HOURS as ihHours, " +
	            "jc.RH_HOURS as rhHours, " +
	            "jc.TOTAL_HOURS as totalHours, " +
	            "mon.MONTH_NAME as monthName " +
	            "FROM JC_DRAWNTXN jc " +
	            "INNER JOIN SHIFT_MASTER sf ON jc.SHIFT_ID = sf.ID " +
	            "INNER JOIN TYPE_SELECTION ty ON jc.TYPE_ID = ty.TYPE_ID " +
	            "INNER JOIN ACTIVITY_CATEGORY_MASTER act ON jc.ACTIVITY_CATEGORY_ID = act.ACTIVITY_ID " +
	            "INNER JOIN [MIS].[dbo].[Contracts] con ON jc.CONTRACT_ID = con.contract_id " +
	            "INNER JOIN MONTH_MASTER mon ON jc.MONTH = mon.ID " +
	            "WHERE jc.YEAR = :year AND jc.MONTH = :month AND jc.USER_ID = :userId  AND (jc.is_deleted = 0 OR jc.is_deleted IS NULL)",
	            nativeQuery = true)
	    List<DrawingReportDTO> getDrawingReportByUser(@Param("year") Integer year,
	                                                   @Param("month") Integer month,
	                                                   @Param("userId") Integer userId);
	 
	 
	 

	 @Query(value = "SELECT " +
	            "jc.TXT_DATE as txtDate, " +
	            "jc.YEAR as year, " +
	            "con.JobCode as jobCode, " +
	            "con.cname as cname, " +
	            "jc.PHASE_ID as phaseId, " +
	            "sf.CODE as shiftCode, " +
	            "ty.TYPE_NAME as typeName, " +
	            "act.ACTIVITY_NAME as activityName, " +
	            "jc.DH_HOURS as dhHours, " +
	            "jc.IH_HOURS as ihHours, " +
	            "jc.RH_HOURS as rhHours, " +
	            "jc.TOTAL_HOURS as totalHours, " +
	            "mon.MONTH_NAME as monthName " +
	            "FROM JC_DRAWNTXN jc " +
	            "INNER JOIN SHIFT_MASTER sf ON jc.SHIFT_ID = sf.ID " +
	            "INNER JOIN TYPE_SELECTION ty ON jc.TYPE_ID = ty.TYPE_ID " +
	            "INNER JOIN ACTIVITY_CATEGORY_MASTER act ON jc.ACTIVITY_CATEGORY_ID = act.ACTIVITY_ID " +
	            "INNER JOIN [MIS].[dbo].[Contracts] con ON jc.CONTRACT_ID = con.contract_id " +
	            "INNER JOIN MONTH_MASTER mon ON jc.MONTH = mon.ID " +
	            "WHERE jc.YEAR = :year  AND jc.USER_ID = :userId  AND (jc.is_deleted = 0 OR jc.is_deleted IS NULL)",
	            nativeQuery = true)
	    List<DrawingReportDTO> getDrawingReportByUserByyear(@Param("year") Integer year,
	                                                  
	                                                   @Param("userId") Integer userId);
	 
	 @Query(value = """
			   	SELECT
			        con.JobCode,
			        um.User_Id,
			        um.UserName,
			        um.Location,
			          (SUM(jc.TOTAL_HOURS)) AS final_total
			    FROM Jc_Drawntxn AS jc
			    INNER JOIN JIMS.dbo.UserFactoryMappingJIMS  um
			        ON um.User_Id = jc.USER_ID
			    INNER JOIN [MIS].[dbo].[Contracts] con
			        ON jc.CONTRACT_ID = con.contract_id
			    WHERE
			        (jc.is_deleted = 0 OR jc.is_deleted IS NULL)
			        AND (jc.USER_ID      = :userId      OR :userId      IS NULL)
			        AND (jc.CONTRACT_ID  = :contractId  OR :contractId  IS NULL)
			        AND (CAST(jc.CREATED_DATE AS DATE) >= CAST(:fromDate AS DATE)  OR :fromDate IS NULL)
			        AND (CAST(jc.CREATED_DATE AS DATE) <= CAST(:toDate   AS DATE)  OR :toDate   IS NULL)
			    GROUP BY
			        con.JobCode,
			        um.User_Id,
			        um.UserName,
			         um.Location
			    ORDER BY
			        con.JobCode, um.User_Id
			    """, nativeQuery = true)
			List<Object[]> getHours(
			    @Param("userId")     Integer userId,
			    @Param("contractId") Integer contractId,
			    @Param("fromDate")   String fromDate,     // pass as "yyyy-MM-dd" or null
			    @Param("toDate")     String toDate
			);
 
}
