package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.User;
import com.JIMS.integration.interfaces.ExportTitleInterface;
import com.JIMS.integration.interfaces.GSTStateMasterInterface;
import com.JIMS.integration.interfaces.InvoiceTypeInterface;
import com.JIMS.integration.interfaces.MileStoneMasterInterface;
import com.JIMS.integration.interfaces.OtherTypeMasterInterface;
import com.JIMS.integration.interfaces.RemarksInvoiceTypeInterfaces;
import com.JIMS.integration.interfaces.ServiceCodeMasterInterface;
import com.JIMS.integration.interfaces.ShipmentDeliveryConditionInterfaces;
import com.JIMS.integration.interfaces.TypeMasterInterface;
import com.JIMS.integration.interfaces.UOMMasterInterface;
import com.JIMS.integration.interfaces.VendorForScrapMasterInterface;
import com.JIMS.integration.interfaces.WorkOrderMasterInterface;

import jakarta.transaction.Transactional;

public interface MasterRepository extends JpaRepository<User, Integer> {

	/* GST MASTER -> START */

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO GST_MASTER (sap_bucode, state_code, state_gstno, created_by, created_date,factory_id) VALUES (:sapBuCode, :stateCode, :stateGstNo, :createdBy, GETDATE(), :factory_id)", nativeQuery = true)
	int insertGSTMasterRecord(String sapBuCode, String stateCode, String stateGstNo, String createdBy,
			String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE GST_MASTER SET sap_bucode = :sapBuCode, state_code = :stateCode, state_gstno = :stateGstNo, modified_by = :modifiedBy, modified_date = GETDATE() WHERE bu_id = :buId", nativeQuery = true)
	int updateGSTMasterRecord(String sapBuCode, String stateCode, String stateGstNo, String modifiedBy, String buId);

	@Query(value = "select  gm.*, sm.id as state_id, sm.state_code,sm.state_id, sm.state_name from GST_MASTER gm\r\n"
			+ "inner  join STATE_MASTER sm on sm.id = gm.state_code where \r\n"
			+ "(:factory_id IS NULL OR gm.factory_id = :factory_id) and  gm.is_delete = 0", nativeQuery = true)
	List<GSTStateMasterInterface> listGSTMasterRecord(String factory_id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO GST_MASTER_HISTORY (bu_id, sap_bucode, state_code, state_gstno, created_by, created_date, modified_by, modified_date, action, transaction_date,factory_id) "
			+ "	", nativeQuery = true)
	int insertGSTMasterToHistory(String buId, String modifiedBy);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO GST_MASTER_HISTORY (bu_id, sap_bucode, state_code, state_gstno, created_by, created_date, modified_by, modified_date, action,transaction_date, deleted_date, deleted_by) "
			+ "SELECT bu_id, sap_bucode, state_code, state_gstno, created_by, created_date, modified_by, modified_date, 'DELETE', GETDATE(), GETDATE(), :modified_by "
			+ "FROM GST_MASTER " + "WHERE bu_id = :bu_id", nativeQuery = true)
	int deleteGSTMasterToHistory(String modified_by, String bu_id);

	@Query(value = "select TOP 1 gm.*, sm.id as state_id, sm.state_code, sm.state_name from GST_MASTER gm\r\n"
			+ "inner  join STATE_MASTER sm on sm.id = gm.state_code where gm.bu_id = :buId  and gm.is_delete = 0", nativeQuery = true)
	GSTStateMasterInterface searchGSTById(String buId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE GST_MASTER SET modified_by = :modifiedBy, modified_date = GETDATE(), is_delete = 1 WHERE bu_id = :buId", nativeQuery = true)
	int deleteGSTMasterRecord(String modifiedBy, String buId);
	/* GST MASTER -> END */

	/* MILESTONE MASTER -> START */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO MILESTONE_MASTER (milestone_code, milestone_name, milestone_desc, created_by, created_date,factory_id) VALUES (:milestoneCode, :milestoneName, :milestoneDesc, :createdBy, GETDATE(), :factory_id)", nativeQuery = true)
	int insertMilestoneRecord(String milestoneCode, String milestoneName, String milestoneDesc, String createdBy,
			String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE MILESTONE_MASTER SET milestone_code = :milestoneCode, milestone_name = :milestoneName, milestone_desc = :milestoneDesc, modified_by = :modifiedBy, modified_date = GETDATE() WHERE milestone_id = :milestoneId", nativeQuery = true)
	int updateMilestoneRecord(String milestoneCode, String milestoneName, String milestoneDesc, String modifiedBy,
			String milestoneId);

	@Query(value = "SELECT mm.*,CONCAT(mm.milestone_code, ' - ', mm.milestone_name) AS milestone_concat  FROM MILESTONE_MASTER mm where mm.is_delete = 0  and (:factory_id IS NULL OR mm.factory_id = :factory_id)", nativeQuery = true)
	List<MileStoneMasterInterface> listMilestoneRecords(String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE MILESTONE_MASTER SET modified_by = :modifiedBy, modified_date = GETDATE(), is_delete = 1 WHERE milestone_id = :milestoneId", nativeQuery = true)
	int deleteMilestoneRecord(String modifiedBy, String milestoneId);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO MILESTONE_MASTER_HISTORY (milestone_id, milestone_code, milestone_name, milestone_desc, created_by, created_date, modified_by, modified_date, statuses, action,transaction_date,factory_id)\r\n"
			+ "SELECT milestone_id, milestone_code, milestone_name, milestone_desc, created_by, created_date, :modifiedBy, GETDATE(), statuses, 'UPDATE', GETDATE(),factory_id FROM MILESTONE_MASTER WHERE milestone_id = :milestoneId;", nativeQuery = true)
	int insertMileStoneToHistory(String milestoneId, String modifiedBy);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO MILESTONE_MASTER_HISTORY (milestone_id, milestone_code, milestone_name, milestone_desc, created_by, created_date, modified_by, modified_date, statuses, action,transaction_date,deleted_date,deleted_by)\r\n"
			+ "SELECT milestone_id, milestone_code, milestone_name, milestone_desc, created_by, created_date, modified_by, modified_date, statuses, 'DELETE',GETDATE(),GETDATE(), :modifiedBy \r\n"
			+ "FROM MILESTONE_MASTER\r\n" + "WHERE milestone_id = :milestoneId;", nativeQuery = true)
	int deleteMileStoneToHistory(String milestoneId, String modifiedBy);

	@Query(value = "select * from MILESTONE_MASTER where milestone_id = :milestoneId  and is_delete = 0", nativeQuery = true)
	MileStoneMasterInterface searchMileStoneById(String milestoneId);
	/* MILESTONE MASTER -> END */

	/* OTHERTYPE MASTER -> START */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO OTHERTYPE_MASTER (type, created_by, created_date,factory_id) VALUES (:type, :createdBy, GETDATE(), :factory_id)", nativeQuery = true)
	int insertOtherType(String type, String createdBy, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE OTHERTYPE_MASTER SET type = :type, modified_by = :modifiedBy, modified_date = GETDATE() WHERE othertype_id = :othertypeId", nativeQuery = true)
	int updateOtherType(String type, String modifiedBy, String othertypeId);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO OTHERTYPE_MASTER_HISTORY (othertype_id, type, created_by, created_date, modified_by, modified_date, action,transaction_date,factory_id)\r\n"
			+ "SELECT othertype_id, type, created_by, created_date, :modifiedBy, GETDATE(), 'UPDATE',GETDATE(),factory_id FROM OTHERTYPE_MASTER WHERE othertype_id = :othertypeId", nativeQuery = true)
	int insertOtherTypeToHistory(String othertypeId, String modifiedBy);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO OTHERTYPE_MASTER_HISTORY (othertype_id, type, created_by, created_date, modified_by, modified_date, action,transaction_date,deleted_date,deleted_by)\r\n"
			+ "SELECT othertype_id, type, created_by, created_date, modified_by, modified_date, 'DELETE',GETDATE(),GETDATE(),:modifiedBy \r\n"
			+ " FROM OTHERTYPE_MASTER\r\n" + "WHERE othertype_id = :othertypeId", nativeQuery = true)
	int deleteOtherTypeToHistory(String othertypeId, String modifiedBy);

	@Query(value = "SELECT * FROM OTHERTYPE_MASTER where is_delete = 0 AND (:factory_id IS NULL OR factory_id = :factory_id)", nativeQuery = true)
	List<OtherTypeMasterInterface> listOtherTypes(String factory_id);

	@Query(value = "select * from OTHERTYPE_MASTER where othertype_id = :otherTypeId  and is_delete = 0", nativeQuery = true)
	OtherTypeMasterInterface searchOtherTypeById(String otherTypeId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE OTHERTYPE_MASTER SET modified_by = :modifiedBy, modified_date = GETDATE(), is_delete = 1 WHERE othertype_id = :othertypeId", nativeQuery = true)
	int deleteOtherType(String modifiedBy, String othertypeId);

	/* OTHERTYPE MASTER -> END */

	/* SERVICECODE MASTER -> START */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERVICECODE_MASTER (service_type, service_description, service_code, created_by, created_date,is_active,factory_id) VALUES (:serviceType, :serviceDescription, :serviceCode, :createdBy, GETDATE(), 1,:factory_id)", nativeQuery = true)
	int insertServiceCodeRecord(String serviceType, String serviceDescription, String serviceCode, String createdBy,
			String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERVICECODE_MASTER SET service_type = :serviceType, service_description = :serviceDescription, service_code = :serviceCode, modified_by = :modifiedBy, modified_date = GETDATE() WHERE servicecode_id = :servicecodeId", nativeQuery = true)
	int updateServiceCodeRecord(String serviceType, String serviceDescription, String serviceCode, String modifiedBy,
			String servicecodeId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERVICECODE_MASTER SET modified_by = :modifiedBy, modified_date = GETDATE(), is_active = 0 WHERE servicecode_id = :servicecodeId", nativeQuery = true)
	int updateServiceCodeInActiveRecord(String modifiedBy, String servicecodeId);

	@Query(value = "SELECT * FROM SERVICECODE_MASTER where service_type = 'SERVICE CODE' and is_delete = 0 and factory_id = :factory_id", nativeQuery = true)
	List<ServiceCodeMasterInterface> listServiceCodeRecords(String factory_id);

	@Query(value = "SELECT * FROM SERVICECODE_MASTER where service_type = 'HSN CODE' and  is_delete = 0 AND (:factory_id IS NULL OR factory_id = :factory_id)", nativeQuery = true)
	List<ServiceCodeMasterInterface> listHSNCodeRecords(String factory_id);

	@Query(value = """
			  SELECT *
			  FROM SERVICECODE_MASTER
			  WHERE is_delete = 0
			    AND service_type IN ('SERVICE CODE', 'HSN CODE')
			    AND (:factory_id IS NULL OR factory_id = :factory_id)
			""", nativeQuery = true)
	List<ServiceCodeMasterInterface> listServiceAndHSNCodes(String factory_id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERVICECODE_MASTER_HISTORY (servicecode_id, service_type, service_description, service_code, status, created_by, created_date, modified_by, modified_date, action,transaction_date,factory_id)\r\n"
			+ "SELECT servicecode_id, service_type, service_description, service_code, status, created_by, created_date, :modifiedBy, GETDATE(), 'UPDATE',GETDATE(),factory_id FROM SERVICECODE_MASTER WHERE servicecode_id = :servicecodeId", nativeQuery = true)
	int insertServiceCodeToHistory(String servicecodeId, String modifiedBy);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERVICECODE_MASTER_HISTORY (servicecode_id, service_type, service_description, service_code, status, created_by, created_date, modified_by, modified_date, action,transaction_date,deleted_date,deleted_by)\r\n"
			+ "SELECT servicecode_id, service_type, service_description, service_code, status, created_by, created_date, modified_by, modified_date, 'UPDATE',GETDATE(),GETDATE(),:modifiedBy\r\n"
			+ "FROM SERVICECODE_MASTER\r\n" + "WHERE servicecode_id = :servicecodeId", nativeQuery = true)
	int deleteServiceCodeToHistory(String servicecodeId, String modifiedBy);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERVICECODE_MASTER SET modified_by = :modifiedBy, modified_date = GETDATE(), is_delete = 1 WHERE servicecode_id = :servicecodeId", nativeQuery = true)
	int deleteServiceCodeRecord(String modifiedBy, String servicecodeId);

	@Query(value = "SELECT * FROM SERVICECODE_MASTER where servicecode_id = :servicecodeId and is_delete = 0", nativeQuery = true)
	ServiceCodeMasterInterface searchServiceById(String servicecodeId);

	/* SERVICECODE MASTER -> END */

	/* TYPEMASTER -> START */

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SCRAPTYPE_MASTER (scrap_type, scrap_name, created_by, created_date,factory_id) VALUES (:scrapType, :scrapName, :createdBy, GETDATE(), :factory_id)", nativeQuery = true)
	int insertScrapTypeRecord(String scrapType, String scrapName, String createdBy, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPTYPE_MASTER SET scrap_type = :scrapType, scrap_name = :scrapName, modified_by = :modifiedBy, modified_date = GETDATE() WHERE type_id = :typeId", nativeQuery = true)
	int updateScrapTypeRecord(String scrapType, String scrapName, String modifiedBy, String typeId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPTYPE_MASTER SET modified_by = :modifiedBy, modified_date = GETDATE(), is_delete = 1 WHERE type_id = :typeId", nativeQuery = true)
	int deleteScrapTypeRecord(String modifiedBy, String typeId);

	@Query(value = "SELECT * FROM SCRAPTYPE_MASTER where is_delete = 0 AND (:factory_id IS NULL OR factory_id = :factory_id)", nativeQuery = true)
	List<TypeMasterInterface> listScrapTypeRecords(String factory_id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SCRAPTYPE_MASTER_HISTORY(type_id,scrap_type,scrap_name, created_by, created_date, modified_by, modified_date,action,transaction_date,factory_id)\r\n"
			+ "select type_id,scrap_type,scrap_name,created_by,created_date, :modifiedBy, GETDATE(), 'UPDATE', GETDATE(),factory_id FROM SCRAPTYPE_MASTER WHERE type_id = :typeId", nativeQuery = true)
	int insertTypeMasteToHistory(String typeId, String modifiedBy);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SCRAPTYPE_MASTER_HISTORY(type_id,scrap_type,scrap_name, created_by, created_date, modified_by, modified_date,action,transaction_date,\r\n"
			+ "deleted_date,deleted_by) select type_id,scrap_type,scrap_name,created_by,created_date, modified_by, modified_date, 'DELETE', GETDATE(),GETDATE(),:modifiedBy FROM SCRAPTYPE_MASTER WHERE\r\n"
			+ "type_id = :typeId\r\n" + "", nativeQuery = true)
	int deleteTypeMasteToHistory(String typeId, String modifiedBy);

	@Query(value = "SELECT * FROM SCRAPTYPE_MASTER where type_id = :typeId and is_delete = 0", nativeQuery = true)
	TypeMasterInterface searchTypeMasterId(String typeId);

	/* TYPEMASTER -> END */

	/* UOM_MASTER MASTER -> START */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO UOM_MASTER ( unit_name, description,created_by, created_date,factory_id) VALUES (:unitName, :description,:createdBy, GETDATE(),:factory_id)", nativeQuery = true)
	int insertInvoiceUnitRecord(String unitName, String description, String createdBy, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE UOM_MASTER SET  unit_name = :unitName,description = :description, modified_by = :modifiedBy, modified_date = GETDATE() WHERE unit_id = :unitId", nativeQuery = true)
	int updateInvoiceUnitRecord(String unitName, String description, String modifiedBy, String unitId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE UOM_MASTER SET is_delete = 1, modified_by = :modifiedBy, modified_date = GETDATE() WHERE unit_id = :unitId", nativeQuery = true)
	int deleteInvoiceUnitRecord(String modifiedBy, String unitId);

	@Query(value = "SELECT * FROM UOM_MASTER where is_delete = 0 AND (:factory_id IS NULL OR factory_id = :factory_id)", nativeQuery = true)
	List<UOMMasterInterface> listInvoiceUnitRecords(String factory_id);

	@Query(value = "SELECT * FROM UOM_MASTER where unit_id = :unitId and is_delete = 0", nativeQuery = true)
	UOMMasterInterface searchUOMMasterId(String unitId);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO UOM_MASTER_HISTORY(unit_id, unit_name,created_by,created_date, modified_by, modified_date,action,transaction_date,factory_id) \r\n"
			+ "select unit_id, unit_name,created_by,created_date, :modifiedBy, GETDATE(), 'UPDATE', GETDATE(),factory_id FROM UOM_MASTER WHERE\r\n"
			+ "unit_id = :unitId", nativeQuery = true)
	int insertUOMMasteToHistory(String unitId, String modifiedBy);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO UOM_MASTER_HISTORY(unit_id, unit_name,created_by,created_date, modified_by, modified_date,action,transaction_date,deleted_date,deleted_by) \r\n"
			+ "select unit_id, unit_name,created_by,created_date, :modifiedBy, GETDATE(), 'DELETE', GETDATE(),GETDATE(),:modifiedBy FROM UOM_MASTER WHERE\r\n"
			+ "unit_id = :unitId", nativeQuery = true)
	int deleteUOMMasteToHistory(String unitId, String modifiedBy);
	/* UOM_MASTER MASTER -> END */

	/* VENDORFORSCRAP MASTER -> START */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SCRAPVENDOR_MASTER ( vendor_name,vendor_state_id,vendor_city,vendor_desc, created_by, created_date,factory_id) VALUES \r\n"
			+ "(:vendor_name,:vendor_state_id,:vendor_city,:vendor_desc, :created_by, GETDATE(),:factory_id)", nativeQuery = true)
	int insertScrapVendorRecord(String vendor_name, String vendor_state_id, String vendor_city, String vendor_desc,
			String created_by, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPVENDOR_MASTER SET  vendor_name = :vendor_name, vendor_state_id = :vendor_state_id, vendor_city = :vendor_city, vendor_desc = :vendor_desc, modified_by = :modified_by,modified_date = GETDATE() WHERE ven_id = :ven_id", nativeQuery = true)
	int updateScrapVendorRecord(String vendor_name, String vendor_state_id, String vendor_city, String vendor_desc,
			String modified_by, String ven_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPVENDOR_MASTER SET is_delete = 1, modified_by = :modified_by, modified_date = GETDATE() WHERE ven_id = :ven_id", nativeQuery = true)
	int deleteScrapVendorRecord(String modified_by, String ven_id);

	@Query(value = "SELECT * FROM SCRAPVENDOR_MASTER where is_delete = 0 AND (:factory_id IS NULL OR factory_id = :factory_id)", nativeQuery = true)
	List<VendorForScrapMasterInterface> listScrapVendorRecord(String factory_id);

	@Query(value = "SELECT vm.*, sm.state_name FROM SCRAPVENDOR_MASTER vm\r\n"
			+ "inner join STATE_MASTER sm on sm.id = vm.vendor_state_id\r\n"
			+ "where vm.ven_id = :ven_id and vm.is_delete = 0", nativeQuery = true)
	VendorForScrapMasterInterface searchScrapVendorRecordById(String ven_id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SCRAPVENDOR_MASTER_HISTORY(ven_id,vendor_name,vendor_state_id,vendor_city,vendor_desc, created_by,created_date, modified_by, modified_date,action,transaction_date,factory_id) \r\n"
			+ "select ven_id,vendor_name,vendor_state_id,vendor_city,vendor_desc, created_by,created_date, :modified_by, GETDATE(), 'UPDATE', GETDATE(),factory_id FROM SCRAPVENDOR_MASTER WHERE\r\n"
			+ "ven_id = :ven_id", nativeQuery = true)
	int insertScrapVendorToHistory(String ven_id, String modified_by);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SCRAPVENDOR_MASTER_HISTORY(ven_id,vendor_name,vendor_state_id,vendor_city,vendor_desc, created_by,created_date, modified_by, modified_date,action,transaction_date,deleted_date,deleted_by) \r\n"
			+ "select ven_id,vendor_name,vendor_state_id,vendor_city,vendor_desc, created_by,created_date, :modified_by, GETDATE(), 'DELETE', GETDATE(),GETDATE(),:modified_by FROM SCRAPVENDOR_MASTER WHERE\r\n"
			+ "ven_id = :ven_id", nativeQuery = true)
	int deleteScrapVendoreToHistory(String ven_id, String modified_by);
	/* VENDORFORSCRAP MASTER -> END */

	/* WORKORDERMASTER MASTER -> START */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO WORKORDER_MASTER ( workorder_no,work_date,created_by, created_date,factory_id) VALUES \r\n"
			+ "(:workorder_no,:work_date,:created_by, GETDATE(),:factory_id)", nativeQuery = true)
	int insertWorkOrderRecord(String workorder_no, String work_date, String created_by, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE WORKORDER_MASTER SET  workorder_no = :workorder_no,  work_date = :work_date, modified_by = :modified_by,modified_date = GETDATE() WHERE work_id = :work_id", nativeQuery = true)
	int updateWorkOrderRecord(String workorder_no, String work_date, String modified_by, String work_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE WORKORDER_MASTER SET is_delete = 1, modified_by = :modified_by, modified_date = GETDATE() WHERE work_id = :work_id", nativeQuery = true)
	int deleteWorkOrderRecord(String modified_by, String work_id);

	@Query(value = "SELECT * FROM WORKORDER_MASTER where is_delete = 0 AND (:factory_id IS NULL OR factory_id = :factory_id)", nativeQuery = true)
	List<WorkOrderMasterInterface> listWorkOrderRecords(String factory_id);

	@Query(value = "SELECT * FROM WORKORDER_MASTER where work_id = :work_id and is_delete = 0", nativeQuery = true)
	WorkOrderMasterInterface searchWorkOrderById(String work_id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO WORKORDER_MASTER_HISTORY(work_id,workorder_no,docket_no,work_date,dock_date, created_by,created_date, modified_by, modified_date,action,transaction_date,factory_id) \r\n"
			+ "select work_id,workorder_no,docket_no,work_date,dock_date, created_by,created_date, :modified_by, GETDATE(), 'UPDATE', GETDATE(),factory_id FROM WORKORDER_MASTER WHERE\r\n"
			+ "work_id = :work_id", nativeQuery = true)
	int insertWorkOrderToHistory(String work_id, String modified_by);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO WORKORDER_MASTER_HISTORY(work_id,workorder_no,docket_no,work_date,dock_date, created_by,created_date, modified_by, modified_date,action,transaction_date,deleted_date,deleted_by) \r\n"
			+ "select work_id,workorder_no,docket_no,work_date,dock_date, created_by,created_date, :modified_by, GETDATE(), 'DELETE', GETDATE(),GETDATE(),:modified_by FROM WORKORDER_MASTER WHERE\r\n"
			+ "work_id = :work_id", nativeQuery = true)
	int deleteWorkOrderToHistory(String work_id, String modified_by);

	/* WORKORDERMASTER MASTER -> END */

	/* DELIVERY CONDITION MASTER -> START */

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SHIPMENT_DELIVERY_CONDITION (shipment_mode,delivery_condition,created_by,created_date,factory_id) VALUES (:shipment_mode,:delivery_condition,:created_by,GETDATE(), :factory_id)", nativeQuery = true)
	int insertShipmentDelivery(String shipment_mode, String delivery_condition, String created_by, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SHIPMENT_DELIVERY_CONDITION (shipment_mode,delivery_condition,created_by,created_date,factory_id) VALUES (:shipment_mode,:delivery_condition,:created_by,GETDATE(), :factory_id)", nativeQuery = true)
	int insertShipmentDeliveryNew(String shipment_mode, String delivery_condition, String created_by,
			String factory_id);

	@Query(value = "select *  from SHIPMENT_DELIVERY_CONDITION where si_id = :si_id", nativeQuery = true)
	ShipmentDeliveryConditionInterfaces searchShipmentDeliveryId(String si_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SHIPMENT_DELIVERY_CONDITION SET shipment_mode =:shipment_mode, delivery_condition =:delivery_condition, modified_by=:modified_by, modified_date = GETDATE() where si_id = :si_id", nativeQuery = true)
	int updateShipmentDeliveryRecord(String shipment_mode, String delivery_condition, String modified_by, String si_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SHIPMENT_DELIVERY_CONDITION SET is_delete = 1,modified_by=:modified_by, modified_date = GETDATE() where si_id = :si_id", nativeQuery = true)
	int deleteshipmentdelivery(String modified_by, String si_id);

	@Query(value = "select * from SHIPMENT_DELIVERY_CONDITION where is_delete = 0  AND (:factory_id IS NULL OR factory_id = :factory_id)", nativeQuery = true)
	List<ShipmentDeliveryConditionInterfaces> listShipmentDeliveryRecord(String factory_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SHIPMENT_DELIVERY_CONDITION_HISTORY (si_id,shipment_mode,delivery_condition,created_by,created_date,modified_by,modified_date,action,transaction_date,factory_id)"
			+ " select si_id, shipment_mode, delivery_condition, created_by, created_date, :modified_by, GETDATE() , 'UPDATE',GETDATE(),factory_id from SHIPMENT_DELIVERY_CONDITION where si_id =:si_id", nativeQuery = true)
	void insertShipmentDeliveryHistory(String modified_by, String si_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SHIPMENT_DELIVERY_CONDITION_HISTORY (si_id,shipment_mode,delivery_condition,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date)"
			+ " select si_id, shipment_mode, delivery_condition, created_by, created_date, modified_by, modified_date, 'DELETE',GETDATE(),:modified_by,GETDATE() from SHIPMENT_DELIVERY_CONDITION where si_id =:si_id", nativeQuery = true)
	void updateShipmentDeliveryHistory(String modified_by, String si_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where shipment_mode_id = :shipment_mode_id ", nativeQuery = true)
	int checkShipmentConditionIdPresentInContractMaster(String shipment_mode_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where delivery_condition_id = :delivery_condition_id ", nativeQuery = true)
	int checkDeliveryConditionIdPresentInContractMaster(String delivery_condition_id);
	
	
	
	
	@Query(value = """
		    SELECT CASE 
		        WHEN EXISTS (SELECT 1 FROM CONTRACT_MASTER 
		                     WHERE shipment_mode_id = :shipment_mode_id)

		          OR EXISTS (SELECT 1 FROM CONTRACT_MASTER 
		                     WHERE delivery_condition_id = :delivery_condition_id)

		          OR EXISTS (SELECT 1 FROM Others_invoice_master 
		                     WHERE ship_mode_id = :shipment_mode_id 
		                        OR deliverycondition_id = :delivery_condition_id)

		        THEN 1 ELSE 0 END
		    """, nativeQuery = true)
		int checkShipmentOrDeliveryUsed(String shipment_mode_id, String delivery_condition_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where work_id = :work_id and is_locked = 1", nativeQuery = true)
	int checkWorkIdPresentInContractMaster(String work_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where s_code = :servicecode_id", nativeQuery = true)
	int checkServiceCodeIdPresentInContractMaster(String servicecode_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where h_code = :servicecode_id", nativeQuery = true)
	int checkHSNCodeIdPresentInContractMaster(String servicecode_id);
	
	
	
	@Query(value = """
		    SELECT CASE 
		        WHEN EXISTS (
		            SELECT 1 FROM CONTRACT_MASTER 
		            WHERE h_code = :servicecode_id 
		               OR s_code = :servicecode_id
		        )
		        OR EXISTS (
		            SELECT 1 FROM Others_invoice_master 
		            WHERE servicecode_id = :servicecode_id 
		               OR HSNcode_id = :servicecode_id
		        )
		        THEN 1 ELSE 0 END
		    """, nativeQuery = true)
		int checkServiceCodeUsed(String servicecode_id);
	
	
	/* DELIVERY CONDITION MASTER -> END */

	/* MASTER -> START */

	/* MASTER -> END */

	/* INVOICE_TYPE_REMARKS_MASTER -> ADD */
	@Query(value = "select * from INVOICE_TYPE_REMARKS_MASTER where is_delete = 0 AND (:factory_id IS NULL OR factory_id = :factory_id)", nativeQuery = true)
	List<RemarksInvoiceTypeInterfaces> listInvoiceRemarksType(String factory_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_TYPE_REMARKS_MASTER (remarks_type,created_by,created_date,factory_id) VALUES (:remarks_type,:created_by,GETDATE(), :factory_id)", nativeQuery = true)
	int insertRemarksType(String remarks_type, String created_by, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE INVOICE_TYPE_REMARKS_MASTER SET remarks_type=:remarks_type,modified_by=:modified_by,modified_date=GETDATE() where slno =:slno", nativeQuery = true)
	int updateInvoiceTypeRemarks(String remarks_type, String modified_by, String slno);

	@Query(value = "select * from INVOICE_TYPE_REMARKS_MASTER where slno = :slno ", nativeQuery = true)
	RemarksInvoiceTypeInterfaces searchInvoiceRemarksById(String slno);

	@Transactional
	@Modifying
	@Query(value = "UPDATE INVOICE_TYPE_REMARKS_MASTER SET is_delete = 1,modified_by=:modified_by,modified_date=GETDATE() where slno =:slno", nativeQuery = true)
	int deleteInvoiceRemarksById(String modified_by, String slno);

	/* INVOICE_TYPE_REMARKS_MASTER -> END */

	/* INVOICE_TYPE_MASTER -> ADD */
	@Query(value = "select * from INVOICE_TYPE_MASTER where is_delete = 0 AND (:factory_id IS NULL OR factory_id = :factory_id)", nativeQuery = true)
	List<InvoiceTypeInterface> listInvoiceType(String factory_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_TYPE_MASTER (type,created_by,created_date,factory_id) VALUES (:type,:created_by,GETDATE(), :factory_id)", nativeQuery = true)
	int insertInvoiceType(String type, String created_by, String factory_id);

	@Query(value = "select count(*) from INVOICE_TYPE_MASTER where type =:type_name",nativeQuery = true)
	int checkDuplicationInvoiceType(String type_name);
	
	
	@Query(value="select count (*)  from DEBIT_CREDIT_INVOICE_MASTER where invoice_type_id =:type_id",nativeQuery = true)
	int checkinvoicetypetranscation(String type_id);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE INVOICE_TYPE_MASTER SET type=:type,modified_by=:modified_by,modified_date=GETDATE() where type_no =:type_no", nativeQuery = true)
	int updateInvoiceType(String type, String modified_by, String type_no);

	@Query(value = "select * from INVOICE_TYPE_MASTER where type_no = :type_no ", nativeQuery = true)
	InvoiceTypeInterface searchInvoiceTypeById(String type_no);

	@Transactional
	@Modifying
	@Query(value = "UPDATE INVOICE_TYPE_MASTER SET is_delete = 1,modified_by=:modified_by,modified_date=GETDATE() where type_no =:type_no", nativeQuery = true)
	int deleteInvoiceTypeById(String modified_by, String type_no);

	@Query(value = "SELECT * FROM SERVICECODE_MASTER where service_type = 'SERVICE CODE' and is_delete = 0 AND (:factory_id IS NULL OR factory_id = :factory_id) ", nativeQuery = true)
	List<ServiceCodeMasterInterface> listServiceCodeRecordsNew(String factory_id);

	// BGTPE START
	@Query(value = "select * from BGTYPE_MASTER where is_delete = 0 ", nativeQuery = true)
	List<RemarksInvoiceTypeInterfaces> listBGType();

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO BGTYPE_MASTER (description,created_by,created_date) VALUES (:description,:created_by,GETDATE())", nativeQuery = true)
	int insertBGType(String description, String created_by);

	@Transactional
	@Modifying
	@Query(value = "UPDATE BGTYPE_MASTER SET description=:description,modified_by=:modified_by,modified_date=GETDATE() where bgid =:bgid", nativeQuery = true)
	int updateBGType(String description, String modified_by, String bgid);

	@Query(value = "select * from BGTYPE_MASTER where bgid = :bgid ", nativeQuery = true)
	RemarksInvoiceTypeInterfaces searchBGTypeByBgId(String bgid);

	@Transactional
	@Modifying
	@Query(value = "UPDATE BGTYPE_MASTER SET is_delete = 1,modified_by=:modified_by,modified_date=GETDATE() where bgid =:bgid", nativeQuery = true)
	int deleteBGTypebyBGId(String modified_by, String bgid);

	@Query(value = "select CASE\r\n" + //
			"when (SELECT COUNT(*) FROM QSPACKING_ITEM_MASTER where UOM_id = :unit_id) > 0\r\n" + //
			"THEN 'Update failed, UOM is used in QSPACKING_ITEM_MASTER'\r\n" + //
			"WHEN (SELECT COUNT(*) FROM SALE_ORDER_ITEM_LEVEL_ENTRY where uom_id = :unit_id) > 0\r\n" + //
			"THEN 'Update failed, UOM is used in SCRAP SALE order'\r\n" + //
			"WHEN (SELECT COUNT(*) FROM UOM_MASTER where unit_name = :unit_name and description= :description and  unit_id = :unit_id) > 0\r\n"
			+ //
			"THEN 'No Modification Done, Update Failed' \r\n" + //
			"END as message", nativeQuery = true)
	String checkWhetherUOMIsUsedInTransactionsBeforeUpdate(String unit_name, String description, String unit_id);
	
	
	@Query(value = """
		    SELECT CASE 
		        WHEN EXISTS (SELECT 1 FROM QSPACKING_ITEM_MASTER WHERE uom_id = :uom_id)
		          OR EXISTS (SELECT 1 FROM QSADVANCE_PACKINGNOTEITEM_MASTER WHERE uom_id = :uom_id)
		          OR EXISTS (SELECT 1 FROM QSCHALLAN_PACKINGNOTEITEM_MASTER WHERE uom_id = :uom_id)
		          OR EXISTS (SELECT 1 FROM other_packingnote_items WHERE uom_id = :uom_id)
		          OR EXISTS (SELECT 1 FROM SCRAP_PACKING_NOTE_ITEMS WHERE uom_id = :uom_id)
		          OR EXISTS (SELECT 1 FROM SALE_ORDER_ITEM_LEVEL_ENTRY WHERE uom_id = :uom_id)
		        THEN 1 ELSE 0 END
		    """, nativeQuery = true)
		int checkUomUsedInTransactions(@Param("uom_id") String uomId);
	
	

	@Query(value = "select case\r\n"
			+ "when (SELECT COUNT(*) FROM QSPACKING_ITEM_MASTER where UOM_id = :unit_id) > 0 \r\n"
			+ "THEN 'Update failed, UOM is used in QSPACKING_ITEM_MASTER'\r\n"
			+ "WHEN (SELECT COUNT(*) FROM SALE_ORDER_ITEM_LEVEL_ENTRY where uom_id = :unit_id) > 0\r\n"
			+ "THEN 'Update failed, UOM is used in SCRAP SALE order'\r\n" + "END as message", nativeQuery = true)
	String checkWhetherUOMIsUsedInTransactionsBeforeDelete(String unit_id);

	@Query(value = "SELECT COUNT(*) FROM UOM_MASTER where unit_name = :unit_name OR description = :description", nativeQuery = true)
	int checkWhetheUomExists(String unit_name, String description);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO EXPORT_TITLE_MASTER (export_title, created_by, created_date) VALUES "
			+ "(:export_title, :created_by, GETDATE() )", nativeQuery = true)
	int addExportTitle(String export_title, String created_by);

	@Transactional
	@Modifying
	@Query(value = "UPDATE EXPORT_TITLE_MASTER set export_title = :export_title, modified_by = :modified_by, "
			+ "modified_date = GETDATE() where id = :id", nativeQuery = true)
	int updateExportTitle(String export_title, String modified_by, String id);

	@Query(value = "SELECT * from EXPORT_TITLE_MASTER where is_delete = 0", nativeQuery = true)
	List<ExportTitleInterface> getAllExportTitle();

	@Query(value = "SELECT * from EXPORT_TITLE_MASTER where id = :id and  is_delete = 0", nativeQuery = true)
	ExportTitleInterface getAllExportTitleBasedOnId(String id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE EXPORT_TITLE_MASTER set is_delete = 1, modified_by = :modified_by where id = :id ", nativeQuery = true)
	int deleteExportTitle(String modified_by, String id);

	@Query(value = "SELECT COUNT(*) from MILESTONE_MASTER where milestone_code = :milestone_code or milestone_name = :milestone_name", nativeQuery = true)
	int checkMilestoneExistsOrNot(String milestone_code, String milestone_name);

	@Query(value = "select case\r\n" + //
			"WHEN (select count(*) from MILESTONE_ASSGIN_CONTRACT_MASTER where milestone_id = :milestone_id) > 0\r\n" + //
			"THEN 'The transaction already exist for MileStone'\r\n" + //
			"WHEN (SELECT COUNT(*) from MILESTONE_MASTER where ( milestone_code = :milestone_code or milestone_name = :milestone_name ) \r\n"
			+ //
			"and milestone_id <> :milestone_id ) > 0\r\n" + //
			"THEN ' MileStone Already Exists'\r\n" + //
			"END as message", nativeQuery = true)
	String checkTransactionForMilestone(String milestone_code, String milestone_name,
			String milestone_id);

	@Query(value = "select case\r\n"
			+ "WHEN (select count(*) from MILESTONE_ASSGIN_CONTRACT_MASTER where milestone_id = :milestone_id) > 0\r\n"
			+ "THEN ' Milestone info is already used in Transactions'\r\n"
			+ "END as message", nativeQuery = true)
	String checkTransactionForMilestoneBeforeDelete(String milestone_id);

	@Query(value = "select count(*) from GST_MASTER where sap_bucode = :sap_bucode and state_gstno =:state_gstno", nativeQuery = true)
	int checkDuplicateGSTMasterRecord(String sap_bucode, String state_gstno);

	@Query(value = "select case\r\n" + "WHEN (select count(*) from BUSINESS_UNITS where gst_number =:bu_id )> 0\r\n"
			+ "THEN 'The transaction already exist for GST Master'\r\n" + "END as message", nativeQuery = true)
	String checkGstInTransactions(String bu_id);

	@Query(value = "select count(*) from EXPORT_TITLE_MASTER where export_title = :export_title and is_delete = 0", nativeQuery = true)
	int checkDuplicatesExportTitle(String export_title);

	@Query(value = "select CASE\r\n"
			+ "WHEN(select count(*) from CONTRACT_MASTER where export_title_text = :id) > 0\r\n"

			+ "THEN 'The transaction already exist for Export Title '\r\n"
			+ "END as message", nativeQuery = true)
	String checkExportTitleinContactorMaster(String id);

	@Query(value = "select CASE \r\n" + "WHEN (SELECT COUNT(*) FROM INVOICE_MASTER WHERE bg_type = :bgid) > 0\r\n"
			+ "THEN ' BGTYPE USED IN INVOICE GENERATION'\r\n" + "END AS MESSAGE", nativeQuery = true)
	String checkBGTypePresentInContractMaster(String bgid);
	
	
	
	
	
	
	@Query(value = """
		    SELECT CASE 
		        WHEN EXISTS (SELECT 1 FROM INVOICE_MASTER WHERE bg_no = :bg_no)
		          OR EXISTS (SELECT 1 FROM DEBIT_CREDIT_INVOICE_MASTER WHERE bg_no = :bg_no)
		          OR EXISTS (SELECT 1 FROM SCRAP_INVOICE_MASTER WHERE bg_no = :bg_no)
		        THEN 1 ELSE 0 END
		    """, nativeQuery = true)
		int checkBgNoUsed(@Param("bg_no") String bgNo);

	@Query(value = "SELECT count(*) from  SCRAPTYPE_MASTER where scrap_name = :scrap_name", nativeQuery = true)
	int checkForDuplicateScrapType(String scrap_name);

	@Query(value = "SELECT COUNT(*) from INVOICE_TYPE_REMARKS_MASTER where remarks_type = :remarks_type", nativeQuery = true)
	int checkForDuplicateRemarksType(String remarks_type);

	@Query(value = "SELECT COUNT(*) from SHIPMENT_DELIVERY_CONDITION where delivery_condition = :delivery_condition or shipment_mode = :shipment_mode", nativeQuery = true)
	int checkDuplicateShipmentDeliveryConditionAndShipmentMode(String delivery_condition, String shipment_mode);

	@Query(value = "SELECT COUNT(*) from SERVICECODE_MASTER where service_code = :service_code", nativeQuery = true)
	int checkDUplicateServiceCode(String service_code);

	@Query(value = "SELECT COUNT(*) from WORKORDER_MASTER where workorder_no = :workorder_no", nativeQuery = true)
	int checkForDuplicate(String workorder_no);

	@Query(value = "SELECT CASE\r\n" + "WHEN(SELECT COUNT(*) FROM CONTRACT_MASTER where work_id = :work_id) > 0\r\n"
			+ "THEN 'The transaction already exist for WorkOder'\r\n"
			+"WHEN(select count(*) from Others_invoice_master where workorder_id= :work_id) > 0\r\n"
			+ "THEN 'The transaction already exist for WorkOder'\r\n"
			+ "END as message", nativeQuery = true)
	String checkWorkOrderInTransactions(String work_id);

	@Query(value = "SELECT CASE\r\n" + "WHEN(SELECT COUNT(*) FROM CONTRACT_MASTER where work_id = :work_id) > 0\r\n"
			+ "THEN 'Delete Failed, WorkOrder number is used in Transactions'\r\n"
			+ "END as message", nativeQuery = true)
	String checkWorkOrderInTransactionsDuringDelete(String work_id);

	@Query(value = "select COUNT(*) from QSPACKING_ITEM_MASTER where inc_type = :slno", nativeQuery = true)
	int checkremarksIdPresentInQspackingItemMaster(String slno);
	
	@Query(value = "select count(*) from QSCHALLAN_PACKINGNOTE_MASTER where invoice_type_id =:slno",nativeQuery = true)
	int checkinvoiceremarkInDeliveryChallan(String slno);
	
	
	
	@Query(value = "select count(*) from INVOICE_MASTER where bg_type = :bgid", nativeQuery = true)
	int checkBGTypeUsedInInvoice(String bgid);
	
	
	
	
	@Query(value = """
			SELECT CASE 
			    WHEN EXISTS (SELECT 1 FROM QSPACKING_ITEM_MASTER WHERE type_id = :type_id)
			      OR EXISTS (SELECT 1 FROM QSADVANCE_PACKINGNOTEITEM_MASTER WHERE type_id = :type_id)
			      OR EXISTS (SELECT 1 FROM QSCHALLAN_PACKINGNOTEITEM_MASTER WHERE type_id = :type_id)
			      OR EXISTS (SELECT 1 FROM other_packingnote_items WHERE type_id = :type_id)
			    THEN 1 ELSE 0 END
			""", nativeQuery = true)
	int checkUsedInQSPackingItemMaster(String type_id);

//	@Query(value = "select COUNT(*) from QSPACKING_ITEM_MASTER where type_id =  :type_id", nativeQuery = true)
//	int checkUsedInQSPackingItemMaster(String type_id);
//	
//	@Query(value = "select COUNT(*) from QSADVANCE_PACKINGNOTEITEM_MASTER where type_id =  :type_id", nativeQuery = true)
//	int checkUsedInAdvancePacking(String type_id);
//	
//	@Query(value = "select COUNT(*) from QSCHALLAN_PACKINGNOTEITEM_MASTER where type_id =  :type_id", nativeQuery = true)
//	int checkUsedInQSCHALLAN_PACKINGNOTEITEM_MASTER(String type_id);
	
	
	

	@Query(value = "SELECT COUNT(*) from SHIPMENT_DELIVERY_CONDITION where delivery_condition = :delivery_condition or shipment_mode = :shipment_mode and si_id != :si_id", nativeQuery = true)
	int checkDuplicateShipmentDeliveryConditionAndShipmentModeWithId(String delivery_condition, String shipment_mode,
			String si_id);

	@Query(value = "select count(*) from GST_MASTER where sap_bucode = :sap_bucode and state_gstno =:state_gstno and bu_id != :bu_id", nativeQuery = true)
	int checkDuplicateGSTMasterRecordWithId(String sap_bucode, String state_gstno, String bu_id);

	@Query(value = "SELECT COUNT(*) from MILESTONE_MASTER where milestone_code = :milestone_code and milestone_name = :milestone_name and milestone_id != :milestone_id", nativeQuery = true)
	int checkMilestoneExistsOrNotWithId(String milestone_code, String milestone_name, String milestone_id);

	@Query(value = "SELECT COUNT(*) from SERVICECODE_MASTER where service_code = :service_code and servicecode_id != :servicecode_id", nativeQuery = true)
	int checkDUplicateServiceCodeWithId(String service_code, String servicecode_id);

	@Query(value = "SELECT count(*) from  SCRAPTYPE_MASTER where scrap_name = :scrap_name and type_id != :type_id", nativeQuery = true)
	int checkForDuplicateScrapTypeWithId(String scrap_name, String type_id);

	@Query(value = """
		    SELECT COUNT(*) 
		    FROM UOM_MASTER 
		    WHERE (unit_name = :unit_name OR description = :description)
		      AND unit_id <> :unit_id
		""", nativeQuery = true)
	int checkWhetheUomExist(String unit_name, String description, String unit_id);
	
	

	@Query(value = "select COUNT(*) from SCRAPVENDOR_MASTER where vendor_name = :vendor_name", nativeQuery = true)
	int checkUniqueNameScrapVendor(String vendor_name);

	@Query(value = "select COUNT(*) from SCRAPVENDOR_MASTER where vendor_name = :vendor_name and ven_id != :ven_id", nativeQuery = true)
	int checkUniqueNameScrapVendorWithId(String vendor_name, String ven_id);

	@Query(value = "select COUNT(*) from WORKORDER_MASTER where workorder_no = :workorder_no and work_id != :work_id", nativeQuery = true)
	int checkForDuplicateWithId(String workorder_no, String work_id);

	@Query(value = "SELECT COUNT(*) from INVOICE_TYPE_REMARKS_MASTER where remarks_type = :remarks_type and slno != :slno", nativeQuery = true)
	int checkForDuplicateRemarksTypeWithId(String remarks_type, String slno);

	@Query(value = "SELECT COUNT(*) from BGTYPE_MASTER where description = :description", nativeQuery = true)
	int checkDuplicateBgTYpeDesc(String description);
	

	

	@Query(value = "SELECT COUNT(*) from BGTYPE_MASTER where description = :description and bgid != :bgid", nativeQuery = true)
	int checkDuplicateBgTYpeDescWithId(String description, String bgid);

}
