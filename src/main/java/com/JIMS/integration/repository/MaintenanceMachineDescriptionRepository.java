package com.JIMS.integration.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.MaintenanceMachineDescription;

import jakarta.transaction.Transactional;


@Repository
public interface MaintenanceMachineDescriptionRepository
        extends JpaRepository<MaintenanceMachineDescription, Integer> {

    boolean existsByMachineDescriptionIgnoreCase(String machineDescription);
    
    
    @Query(value =
    	    "SELECT DEPT_ID, DEPT_NAME " +
    	    "FROM MAINTENANCE_DEPT " +
    	    "WHERE STATUS = 1 " +
    	    "AND FACTORY_ID = ?1 " +
    	    "ORDER BY DEPT_NAME",
    	    nativeQuery = true)
    	List<Map<String, Object>> getDepartmentsByFactory(String factoryId);
    
    @Query(value =
    	    "SELECT COUNT(*) " +
    	    "FROM MAINTENANCE_MACHINE_LIST " +
    	    "WHERE MACHINE_DESCRIPTION = :machineDescId",
    	    nativeQuery = true)
    	int countMachineTransactions(
    	        @Param("machineDescId") Integer machineDescId);

    @Query(value =
    	    "SELECT COUNT(*) " +
    	    "FROM MAINTENANCE_DEPT " +
    	    "WHERE UPPER(DEPT_NAME) = UPPER(?1) " +
    	    "AND FACTORY_ID = ?2",
    	    nativeQuery = true)
    	int checkDepartmentExists(String deptName, String factoryId);

        @Query(value =
            "SELECT * FROM MAINTENANCE_DEPT WHERE DEPT_ID=?1",
            nativeQuery = true)
        Map<String,Object> getDepartmentById(Integer deptId);

        @Modifying
        @Transactional
        @Query(value =
            "INSERT INTO MAINTENANCE_DEPT " +
            "(DEPT_NAME,STATUS,CREATED_BY,CREATED_DATE,FACTORY_ID) " +
            "VALUES (?1,1,?2,GETDATE(),?3)",
            nativeQuery = true)
        void createDepartment(String deptName,Integer createdBy,String factory_id);

        @Modifying
        @Transactional
        @Query(value =
            "UPDATE MAINTENANCE_DEPT " +
            "SET DEPT_NAME=?2, " +
            "MODIFIED_BY=?3, " +
            "MODIFIED_DATE=GETDATE() " +
            "WHERE DEPT_ID=?1",
            nativeQuery = true)
        void updateDepartment(
                Integer deptId,
                String deptName,
                Integer modifiedBy);
        
        
        @Modifying
        @Transactional
        @Query(value =
                "INSERT INTO MAINTENANCE_MACHINE_LIST " +
                "(MACHINE_CODE, MACHINE_SUBCODE, MACHINE_DESCRIPTION, DEPT_ID, " +
                "MODEL_NO, SERIAL_NO, MANUFACTURER_NAME, " +
                "PO_NUMBER, PURCHASE_DATE, WARRANTY_START_DATE, WARRANTY_END_DATE, " +
                "MANUFACTURE_DATE, COUNTRY_ID, " +
                "STATUS, CREATED_BY, CREATED_DATE, FACTORY_ID,MACHINE_DESCRIPTION_NAME) " +
                "VALUES " +
                "(:machineCode, :machineSubCode, :machineDescriptionId, :deptId, " +
                ":modelNo, :serialNo, :manufacturerName, " +
                ":poNumber, :purchaseDate, :warrantyStartDate, :warrantyEndDate, " +
                ":manufactureDate, :countryId, " +
                "1, :createdBy, GETDATE(), :factory_id,:machDes)",
                nativeQuery = true)
        int createMachine(

                @Param("machineCode") String machineCode,
                @Param("machineSubCode") String machineSubCode,
                @Param("machineDescriptionId") Integer machineDescriptionId,
                @Param("deptId") Integer deptId,
                @Param("modelNo") String modelNo,
                @Param("serialNo") String serialNo,
                @Param("manufacturerName") String manufacturerName,

                @Param("poNumber") String poNumber,
                @Param("purchaseDate") String purchaseDate,
                @Param("warrantyStartDate") String warrantyStartDate,
                @Param("warrantyEndDate") String warrantyEndDate,
                @Param("manufactureDate") String manufactureDate,
                @Param("countryId") Integer countryId,

                @Param("createdBy") Integer createdBy,
                @Param("factory_id") Integer factory_id,
                @Param("machDes") String machDes
                
        );

        
        @Query(value =
        	    "SELECT TOP 1 'Machine Code already exists' " +
        	    "FROM MAINTENANCE_MACHINE_LIST " +
        	    "WHERE FACTORY_ID  = :factoryId " +
        	    "AND MACHINE_CODE  = :machineCode",
        	    nativeQuery = true)
        	String checkDuplicateMachineCode(
        	        @Param("machineCode") String  machineCode,
        	        @Param("factoryId")   Integer factoryId);
        	 
        	// ── CHECK 2: machineSubCode ─────────────────────────────────────
        	@Query(value =
        	    "SELECT TOP 1 'Machine Sub Code already exists' " +
        	    "FROM MAINTENANCE_MACHINE_LIST " +
        	    "WHERE FACTORY_ID    = :factoryId " +
        	    "AND MACHINE_SUBCODE = :machineSubCode",
        	    nativeQuery = true)
        	String checkDuplicateMachineSubCode(
        	        @Param("machineSubCode") String  machineSubCode,
        	        @Param("factoryId")      Integer factoryId);
        	 
        	// ── CHECK 3: serialNo ───────────────────────────────────────────
        	@Query(value =
        	    "SELECT TOP 1 'Serial No already exists' " +
        	    "FROM MAINTENANCE_MACHINE_LIST " +
        	    "WHERE FACTORY_ID = :factoryId " +
        	    "AND SERIAL_NO    = :serialNo",
        	    nativeQuery = true)
        	String checkDuplicateSerialNo(
        	        @Param("serialNo")  String  serialNo,
        	        @Param("factoryId") Integer factoryId);
        
        
        	@Modifying
        	@Transactional
        	@Query(value =
        	        "UPDATE MAINTENANCE_MACHINE_LIST SET " +
        	        "MACHINE_CODE = :machineCode, " +
        	        "MACHINE_SUBCODE = :machineSubCode, " +
        	        "MACHINE_DESCRIPTION = :machineDescriptionId, " +
        	        "DEPT_ID = :deptId, " +
        	        "MODEL_NO = :modelNo, " +
        	        "SERIAL_NO = :serialNo, " +
        	        "MANUFACTURER_NAME = :manufacturerName, " +
        	        "PO_NUMBER = :poNumber, " +
        	        "PURCHASE_DATE = :purchaseDate, " +
        	        "WARRANTY_START_DATE = :warrantyStartDate, " +
        	        "WARRANTY_END_DATE = :warrantyEndDate, " +
        	        "MANUFACTURE_DATE = :manufactureDate, " +
        	        "COUNTRY_ID = :countryId, " +
        	        "MODIFIED_BY = :modifiedBy, " +
        	        "MODIFIED_DATE = GETDATE() ,MACHINE_DESCRIPTION_NAME=:machDes " +
        	        "WHERE MACHINE_ID = :machineId",
        	        nativeQuery = true)
        	int updateMachine(
        	        @Param("machineId")           Integer machineId,
        	        @Param("machineCode")         String machineCode,
        	        @Param("machineSubCode")      String machineSubCode,
        	        @Param("machineDescriptionId")Integer machineDescriptionId,
        	        @Param("deptId")              Integer deptId,
        	        @Param("modelNo")             String modelNo,
        	        @Param("serialNo")            String serialNo,
        	        @Param("manufacturerName")    String manufacturerName,
        	        @Param("poNumber")            String poNumber,
        	        @Param("purchaseDate")        String purchaseDate,
        	        @Param("warrantyStartDate")   String warrantyStartDate,
        	        @Param("warrantyEndDate")     String warrantyEndDate,
        	        @Param("manufactureDate")     String manufactureDate,
        	        @Param("countryId")           Integer countryId,
        	        @Param("modifiedBy")          Integer modifiedBy,
        	        @Param("machDes")  String machDes);


        @Query(value =
                "SELECT " +
                "M.MACHINE_ID, " +
                "M.MACHINE_CODE, " +
                "M.MACHINE_SUBCODE, " +
                "MD.MACHINE_DESCRIPTION, " +
                "D.DEPT_NAME, " +
                "M.MODEL_NO, " +
                "M.SERIAL_NO, " +
                "M.MANUFACTURER_NAME " +
                "FROM MAINTENANCE_MACHINE_LIST M " +
                "LEFT JOIN MAINTENANCE_MACHINE_DESCRIPTION MD " +
                "ON M.MACHINE_DESCRIPTION = MD.MACHINE_DESC_ID " +
                "LEFT JOIN MAINTENANCE_DEPT D " +
                "ON M.DEPT_ID = D.DEPT_ID " +
                "WHERE M.STATUS = 1 " +
                "AND M.FACTORY_ID = ?1",
                nativeQuery = true)
        List<Map<String, Object>> getAllMachines(String factoryId);


        @Query(value =
                "SELECT * " +
                "FROM MAINTENANCE_MACHINE_LIST " +
                "WHERE MACHINE_ID = :machineId",
                nativeQuery = true)
        Map<String,Object> getMachineById(
                @Param("machineId") Integer machineId);
        
        
        
        @Query(value =
                "SELECT mml.MACHINE_ID, " +
                "mml.MACHINE_SUBCODE, " +
                "CASE WHEN EXISTS ( " +
                "SELECT 1 FROM MAINTENANCE_TICKET mt " +
                "WHERE mt.MACHINE_ID = mml.MACHINE_ID " +
                "AND mt.STATUS IN (1,2,3,4)) " +
                "THEN 1 ELSE 0 END AS IS_DOWN " +
                "FROM MAINTENANCE_MACHINE_LIST mml " +
                "WHERE mml.MACHINE_DESCRIPTION = :machineDescriptionId  and mml.FACTORY_ID= :factoryId  " +
                "AND mml.STATUS = 1",
                nativeQuery = true)
        List<Map<String,Object>> getMachinesByDescriptionId(
                @Param("machineDescriptionId") Integer machineDescriptionId, @Param("factoryId") String  factoryId);

        
        @Query(value =
        	       "SELECT TOP 1 MACHINE_ID " +
        	       "FROM MAINTENANCE_MACHINE_LIST " +
        	       "WHERE MACHINE_CODE = :machineCode " +
        	       "AND FACTORY_ID = :factoryId " +
        	       "ORDER BY MACHINE_ID DESC",
        	       nativeQuery = true)
        	Integer getLatestMachineId(
        	        @Param("machineCode") String machineCode,
        	        @Param("factoryId") Integer factoryId);
        
        @Modifying
        @Transactional
        @Query(value =
               "INSERT INTO MAINTENANCE_MACHINE_ITEMS " +
               "(machine_id, items_id, factory_id, created_by, created_date, status) " +
               "VALUES (:machineId, :itemId, :factoryId, :createdBy, GETDATE(), 1)",
               nativeQuery = true)
        int insertMachineItem(
                @Param("machineId") Integer machineId,
                @Param("itemId") Integer itemId,
                @Param("factoryId") Integer factoryId,
                @Param("createdBy") Integer createdBy);

        @Query(value =
        	    "SELECT * " +
        	    "FROM MAINTENANCE_MACHINE_ITEMS " +
        	    "WHERE MACHINE_ID = ?1 and status=1",
        	    nativeQuery = true)
        	List<Map<String,Object>> getMachineItemsByMachineId(Integer machineId);
        
        @Modifying
        @Transactional
        @Query(value =
            "UPDATE MAINTENANCE_MACHINE_ITEMS " +
            "SET STATUS = 0 " +
            "WHERE MACHINE_ID = :machineId",
            nativeQuery = true)
        void deactivateMachineItems(
                @Param("machineId") Integer machineId);
        
        @Modifying
        @Transactional
        @Query(value =
            "UPDATE MAINTENANCE_MACHINE_ITEMS " +
            "SET STATUS = 1 " +
            "WHERE MACHINE_ID = :machineId " +
            "AND ITEMS_ID = :itemId",
            nativeQuery = true)
        int activateMachineItem(
                @Param("machineId") Integer machineId,
                @Param("itemId") Integer itemId);
        
        
        @Query(value = """
        	    SELECT COUNT(*)
        	    FROM MAINTENANCE_MACHINE_DESCRIPTION
        	    WHERE UPPER(LTRIM(RTRIM(MACHINE_DESCRIPTION))) =
        	          UPPER(LTRIM(RTRIM(:machineDescription)))
        	      AND MACHINE_DESC_ID <> :machineDescId
        	    """, nativeQuery = true)
        	int countDuplicateMachineDescription(
        	        @Param("machineDescription") String machineDescription,
        	        @Param("machineDescId") Integer machineDescId);
        
        
        
        @Query(value =
        	    "SELECT COUNT(*) " +
        	    "FROM MAINTENANCE_DEPT d1 " +
        	    "WHERE UPPER(LTRIM(RTRIM(d1.DEPT_NAME))) = UPPER(LTRIM(RTRIM(:departmentName))) " +
        	    "AND d1.FACTORY_ID = (SELECT FACTORY_ID FROM MAINTENANCE_DEPT WHERE DEPT_ID = :deptId) " +
        	    "AND d1.DEPT_ID <> :deptId",
        	    nativeQuery = true)
        	int checkDuplicateDepartmentForUpdate(
        	        @Param("departmentName") String departmentName,
        	        @Param("deptId") Integer deptId);
        @Query(value =
        	    "SELECT COUNT(*) " +
        	    "FROM MAINTENANCE_MACHINE_LIST " +
        	    "WHERE DEPT_ID = :deptId",
        	    nativeQuery = true)
        	int countDepartmentTransactions(
        	        @Param("deptId") Integer deptId);
        
        
        
     // Check duplicate MACHINE_CODE (excluding current machine)
        @Query(value =
            "SELECT COUNT(*) FROM MAINTENANCE_MACHINE_LIST " +
            "WHERE MACHINE_CODE = :machineCode " +
            "AND FACTORY_ID = :factoryId " +
            "AND MACHINE_ID <> :machineId " +
            "AND STATUS = 1",
            nativeQuery = true)
        int checkDuplicateMachineCode(
            @Param("machineCode") String machineCode,
            @Param("factoryId") Integer factoryId,
            @Param("machineId") Integer machineId);

        // Check duplicate MACHINE_SUBCODE
        @Query(value =
            "SELECT COUNT(*) FROM MAINTENANCE_MACHINE_LIST " +
            "WHERE MACHINE_SUBCODE = :machineSubCode " +
            "AND FACTORY_ID = :factoryId " +
            "AND MACHINE_ID <> :machineId " +
            "AND STATUS = 1",
            nativeQuery = true)
        int checkDuplicateMachineSubCode(
            @Param("machineSubCode") String machineSubCode,
            @Param("factoryId") Integer factoryId,
            @Param("machineId") Integer machineId);

        // Check duplicate SERIAL_NO
        @Query(value =
            "SELECT COUNT(*) FROM MAINTENANCE_MACHINE_LIST " +
            "WHERE SERIAL_NO = :serialNo " +
            "AND FACTORY_ID = :factoryId " +
            "AND MACHINE_ID <> :machineId " +
            "AND STATUS = 1",
            nativeQuery = true)
        int checkDuplicateSerialNo(
            @Param("serialNo") String serialNo,
            @Param("factoryId") Integer factoryId,
            @Param("machineId") Integer machineId);
}

