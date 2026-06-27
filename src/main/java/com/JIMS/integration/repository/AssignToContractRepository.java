package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.JIMS.integration.entity.User;
import com.JIMS.integration.interfaces.AssignToContract;
import com.JIMS.integration.interfaces.BankMasterInterface;
import com.JIMS.integration.interfaces.BusinessUnitInterface;
import com.JIMS.integration.interfaces.ContractG2Interfaces;
import com.JIMS.integration.interfaces.ContractListFromContractInterfaces;
import com.JIMS.integration.interfaces.InvoiceConsigneeAddressInterface;
import com.JIMS.integration.interfaces.LoadsG2List;
import com.JIMS.integration.interfaces.MileStoneAssignedContractListInterfaces;
import com.JIMS.integration.interfaces.ServiceCodeMasterInterface;
import com.JIMS.integration.interfaces.ShipmentDeliveryConditionInterfaces;
import com.JIMS.integration.interfaces.TaxMasterInterface;
import com.JIMS.integration.interfaces.WorkOrderMasterInterface;

import jakarta.transaction.Transactional;

public interface AssignToContractRepository extends JpaRepository<User, Integer> {

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO CONTRACT_MASTER (Contract_id,invoice_to_id,consignee_id,shipment_mode_id,delivery_condition_id,product_desc_id,work_id,bank_details_id,regd_office_id,it_pan_no,\r\n"
			+ "	Export,bid,H_SType,ServiceCode,SType,SCode,HType,HCode,Taxinx,ARENo,LotNo,ContainterNo,EPCGNo,CalculationType,[Percent],xptxt,rvscharg,tax,ntax,\r\n"
			+ "	advfrightrec,created_by, created_date) VALUES (:Contract_id,:invoice_to_id,:consignee_id,:shipment_mode_id,:delivery_condition_id,:product_desc_id,:work_id,:bank_details_id,:regd_office_id,:it_pan_no,\r\n"
			+ ":Export,:bid,:H_SType,:ServiceCode,:SType,:SCode,:HType,:HCode,:Taxinx,:ARENo,:LotNo,:ContainterNo,:EPCGNo,:CalculationType,:Percent,:xptxt,:rvscharg,:tax,:ntax,\r\n"
			+ "	:advfrightrec,:created_by,GETDATE())", nativeQuery = true)
	int createContract(int Contract_id, int invoice_to_id, int consignee_id, int shipment_mode_id,
			int delivery_condition_id, int product_desc_id, int work_id, int bank_details_id, int regd_office_id,
			int it_pan_no, String Export, int bid, int H_SType, String ServiceCode, String SType, String SCode,
			String HType, String HCode, int Taxinx, String ARENo, String LotNo, String ContainterNo, String EPCGNo,
			String CalculationType, String Percent, String xptxt, int rvscharg, int tax, int ntax, int advfrightrec,
			String created_by);

	@Transactional
	@Modifying
	@Query(value = "UPDATE CONTRACT_MASTER SET Contract_id = :Contract_id,invoice_to_id = :invoice_to_id,consignee_id = :consignee_id,shipment_mode_id = :shipment_mode_id,delivery_condition_id = :delivery_condition_id,product_desc_id = :product_desc_id,work_id = :work_id,bank_details_id = :bank_details_id,regd_office_id = :regd_office_id,it_pan_no =:it_pan_no,\r\n"
			+ "	Export = :Export,bid = :bid,H_SType = :H_SType,ServiceCode = :ServiceCode,SType = :SType,SCode = :SCode,HType = :HType,HCode = :HCode,Taxinx = :Taxinx,ARENo = :ARENo,LotNo = :LotNo,ContainterNo = :ContainterNo,EPCGNo = :EPCGNo,CalculationType = :CalculationType,[Percent] = :Percent,xptxt = :xptxt,rvscharg = :rvscharg,tax = :tax,ntax =:ntax,\r\n"
			+ "	advfrightrec = :advfrightrec,modified_by = :modified_by,modified_date = GETDATE() where con_slno = :con_slno", nativeQuery = true)
	int updateContract(int Contract_id, int invoice_to_id, int consignee_id, int shipment_mode_id,
			int delivery_condition_id, int product_desc_id, int work_id, int bank_details_id, int regd_office_id,
			int it_pan_no, String Export, int bid, int H_SType, String ServiceCode, String SType, String SCode,
			String HType, String HCode, int Taxinx, String ARENo, String LotNo, String ContainterNo, String EPCGNo,
			String CalculationType, String Percent, String xptxt, int rvscharg, int tax, int ntax, int advfrightrec,
			String modified_by, int con_slno);

	@Transactional
	@Modifying
	@Query(value = "update CONTRACT_MASTER SET is_delete = 1, modified_by = :modified_by, modified_date = GETDATE() where con_slno =:con_slno", nativeQuery = true)
	int updateContractDelete(String modified_by, String con_slno);

	@Query(value = "select work_id,workorder_no,factory_id from WORKORDER_MASTER where is_delete = 0", nativeQuery = true)
	List<WorkOrderMasterInterface> getWorkOrderList();

	@Query(value = "select account_id,bank_name,factory_id from BANK_MASTER where is_delete = 0", nativeQuery = true)
	List<BankMasterInterface> getBankNameList();

	@Query(value = "select servicecode_id,service_type,service_code,factory_id from SERVICECODE_MASTER where is_delete = 0", nativeQuery = true)
	List<ServiceCodeMasterInterface> getServiceCode();

	@Query(value = "   SELECT \r\n"
			+ "    bu.business_unit_id,\r\n"
			+ "    bu.business_unit_name,\r\n"
			+ "    bu.gst_number,\r\n"
			+ "    bu.bu_code,\r\n"
			+ "    bu.location,\r\n"
			+ "    st.state_name\r\n"
			+ "	,org.org_name as orgname\r\n"
			+ "FROM business_units bu\r\n"
			+ "INNER JOIN STATE_MASTER st \r\n"
			+ "    ON bu.state_id = st.id\r\n"
			+ "	inner join ORGANIZATION_MASTER org on bu.org_id =org.org_id\r\n"
			+ "WHERE bu.is_delete = 0;", nativeQuery = true)
	List<BusinessUnitInterface> getBusinessUnitList();

	@Query(value = "select id,address, is_invoice,is_consignee,factory_id from INVOICE_CONSIGNEE_ADDRESS_MASTER where is_delete = 0", nativeQuery = true)
	List<InvoiceConsigneeAddressInterface> getInvoiceConsinee();

	@Query(value = "select si_id, shipment_mode,delivery_condition,factory_id from SHIPMENT_DELIVERY_CONDITION where is_delete = 0", nativeQuery = true)
	List<ShipmentDeliveryConditionInterfaces> getShipmentDeliveryCondition();

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO CONTRACT_MASTER_HISTORY (con_slno,contract_id,bid,invoice_type_calculation,percentage_value,invoice_to_id,consignee_id,shipment_mode_id,delivery_condition_id,product_desc_id,bank_details_id,work_id,regd_office_id,s_code,h_code,export,tax_ex_inc,taxable,non_taxable,tax_payable,freight_advance_recovery,area_no,lot_no,containter_no,epcgno,export_title_text,created_by,created_date,modified_by,modified_date,action,transaction_date, contract_name, milestone_id) "
			+ " select con_slno,contract_id,bid,invoice_type_calculation,percentage_value,invoice_to_id,consignee_id,shipment_mode_id,delivery_condition_id,product_desc_id,bank_details_id,work_id,regd_office_id,s_code,h_code,export,tax_ex_inc,taxable,non_taxable,tax_payable,freight_advance_recovery,area_no,lot_no,containter_no,epcgno,export_title_text,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE(),contract_name, milestone_id from CONTRACT_MASTER where con_slno = :con_slno ", nativeQuery = true)
	void insertContracrtHistory(int con_slno, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO CONTRACT_MASTER_HISTORY (con_slno,contract_id,bid,invoice_type_calculation,percentage_value,invoice_to_id,consignee_id,shipment_mode_id,delivery_condition_id,product_desc_id,bank_details_id,work_id,regd_office_id,s_code,h_code,export,tax_ex_inc,taxable,non_taxable,tax_payable,freight_advance_recovery,area_no,lot_no,containter_no,epcgno,export_title_text,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,contract_name, milestone_id) "
			+ " select con_slno,contract_id,bid,invoice_type_calculation,percentage_value,invoice_to_id,consignee_id,shipment_mode_id,delivery_condition_id,product_desc_id,bank_details_id,work_id,regd_office_id,s_code,h_code,export,tax_ex_inc,taxable,non_taxable,tax_payable,freight_advance_recovery,area_no,lot_no,containter_no,epcgno,export_title_text,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE(),contract_name, milestone_id from CONTRACT_MASTER where con_slno = :con_slno ", nativeQuery = true)
	void updateContracrHistory(String con_slno, String modified_by);

	@Query(value = "select  tax_id, tax_name, tax_per from TAX_MASTER where is_delete = 0 AND enddate >= CAST(GETDATE() AS DATE)", nativeQuery = true)
	List<TaxMasterInterface> getTaxMaster();

	@Query(value = "select cm.*,bu.business_unit_name,bu.bu_code from CONTRACT_MASTER cm\r\n"
			+ " inner join business_units bu on bu.business_unit_id = cm.bid \r\n"
			+ " where cm.is_delete = 0", nativeQuery = true)
	List<AssignToContract> getContractList();
	
	@Query(value = "select contract_id, descr,cname from Contracts", nativeQuery = true)
	List<ContractG2Interfaces> getContractListG2();

	@Query(value = "SELECT contract_id, descr FROM Contracts WHERE contract_id NOT IN (SELECT contract_id FROM CONTRACT_MASTER)", nativeQuery = true)
	List<ContractG2Interfaces> getContractListUnique();

	@Query(value = "select distinct cm.*,\r\n"
			+ "icam.address as invoice_to_id_value, icam1.address as consignee_id_value,\r\n"
			+ "shd.shipment_mode as shipment_mode_id_value, shdd.delivery_condition as delivery_condition_id_value,\r\n"
			+ "bm.bank_name as bank_details_id_value,wm.workorder_no as work_id_value, om.org_name as reg_office_id_value,\r\n"
			+ "sm.service_code as s_code_value, sms.service_code as h_code_value,\r\n"
			+ "tm.tax_id,tm.tax_name,tm.tax_per,apni.tax_total as taxable_amount, apni.net_total as nontaxable_amount, apni.total from CONTRACT_MASTER cm\r\n"
			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam on icam.id = cm.consignee_id\r\n"
			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam1 on icam1.id = cm.invoice_to_id\r\n"
			+ "inner join SHIPMENT_DELIVERY_CONDITION shd on shd.si_id = cm.shipment_mode_id\r\n"
			+ "inner join SHIPMENT_DELIVERY_CONDITION shdd on  shdd.si_id = cm.delivery_condition_id\r\n"
			+ "inner join WORKORDER_MASTER wm on wm.work_id = cm.work_id\r\n"
			+ "inner join BANK_MASTER bm on bm.account_id = cm.bank_details_id\r\n"
			+ "inner join SERVICECODE_MASTER sms on sms.servicecode_id = cm.h_code\r\n"
			+ "inner join SERVICECODE_MASTER sm on sm.servicecode_id = cm.s_code\r\n"
			+ "inner join organization_master om on om.org_id = cm.regd_office_id\r\n"
			+ "inner join CONTRACT_ASSIGN_TAX cat on cat.contract_id = cm.contract_id\r\n"
			+ "inner join TAX_MASTER tm on tm.tax_id = cat.tax_id\r\n"
			+ "left join OPENING_BALANCE apn on apn.con_id = cm.contract_id\r\n"
			+ "left join OPENING_BALANCE_ITEM apni on apni.pn_id = apn.pn_id\r\n"
			+ " where cm.con_slno = :con_slno and cm.factory_id = :factory_id", nativeQuery = true)
	List<AssignToContract> searchContractById(String con_slno, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO CONTRACT_ASSIGN_TAX(contract_id,tax_id,factory_id,created_by,created_date) values (:contract_id,:tax_id,:factory_id,:created_by,GETDATE())", nativeQuery = true)
	void insertTaxContract(int contract_id, int tax_id, String factory_id, String created_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO CONTRACT_MASTER (contract_id,bid,invoice_type_calculation,percentage_value,invoice_to_id,consignee_id,shipment_mode_id,delivery_condition_id,product_desc_id,bank_details_id,work_id,regd_office_id,s_code,h_code,export,tax_ex_inc,taxable,non_taxable,tax_payable,freight_advance_recovery,area_no,lot_no,containter_no,epcgno,export_title_text,created_by,created_date,contract_name,factory_id) VALUES "
			+ " (:contract_id,:bid,:invoice_type_calculation,:percentage_value,:invoice_to_id,:consignee_id,:shipment_mode_id,:delivery_condition_id,:product_desc_id,:bank_details_id,:work_id,:regd_office_id,:s_code,:h_code,:export,:tax_ex_inc,:taxable,:non_taxable,:tax_payable,:freight_advance_recovery,:area_no,:lot_no,:containter_no,:epcgno,:export_title_text,:created_by,GETDATE(),:contract_name,:factory_id )", nativeQuery = true)
	int createContractInfo(int contract_id, int bid, String invoice_type_calculation, String percentage_value,
			int invoice_to_id, int consignee_id, int shipment_mode_id, int delivery_condition_id,
			String product_desc_id, int bank_details_id, int work_id, int regd_office_id, Integer s_code, Integer h_code,
			String export, String tax_ex_inc, String taxable, String non_taxable, String tax_payable,
			String freight_advance_recovery, String area_no, String lot_no, String containter_no, String epcgno,
			String export_title_text, String created_by, String contract_name, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO CONTRACT_MASTER (contract_id,bid,invoice_type_calculation,percentage_value,invoice_to_id,consignee_id,shipment_mode_id,delivery_condition_id,product_desc_id,bank_details_id,work_id,regd_office_id,s_code,h_code,export,tax_ex_inc,taxable,non_taxable,tax_payable,freight_advance_recovery,area_no,lot_no,containter_no,epcgno,export_title_text,created_by,created_date,contract_name,factory_id) VALUES "
			+ " (:contract_id,:bid,:invoice_type_calculation,:percentage_value,:invoice_to_id,:consignee_id,:shipment_mode_id,:delivery_condition_id,:product_desc_id,:bank_details_id,:work_id,:regd_office_id,:s_code,:h_code,:export,:tax_ex_inc,:taxable,:non_taxable,:tax_payable,:freight_advance_recovery,:area_no,:lot_no,:containter_no,:epcgno,:export_title_text,:created_by,GETDATE(),:contract_name,:factory_id )", nativeQuery = true)
	int createContractInfoNew(int contract_id, int bid, String invoice_type_calculation, String percentage_value,
			int invoice_to_id, int consignee_id, int shipment_mode_id, int delivery_condition_id,
			String product_desc_id, int bank_details_id, int work_id, int regd_office_id, int s_code, int h_code,
			String export, String tax_ex_inc, String taxable, String non_taxable, String tax_payable,
			String freight_advance_recovery, String area_no, String lot_no, String containter_no, String epcgno,
			String export_title_text, String created_by, String contract_name, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE CONTRACT_MASTER SET "
	        + "contract_id = :contract_id, bid = :bid, invoice_type_calculation = :invoice_type_calculation, "
	        + "percentage_value = :percentage_value, invoice_to_id = :invoice_to_id, consignee_id = :consignee_id, "
	        + "shipment_mode_id = :shipment_mode_id, delivery_condition_id = :delivery_condition_id, "
	        + "product_desc_id = :product_desc_id, bank_details_id = :bank_details_id, work_id = :work_id, "
	        + "regd_office_id = :regd_office_id, s_code = :s_code, h_code = :h_code, export = :export, "
	        + "tax_ex_inc = :tax_ex_inc, taxable = :taxable, non_taxable = :non_taxable, "
	        + "tax_payable = :tax_payable, freight_advance_recovery = :freight_advance_recovery, "
	        + "area_no = :area_no, lot_no = :lot_no, containter_no = :containter_no, epcgno = :epcgno, "
	        + "export_title_text = :export_title_text, modified_by = :modified_by, modified_date = GETDATE(), "
	        + "contract_name = :contract_name, factory_id = :factory_id "
	        + "WHERE con_slno = :con_slno "
	        
	        // 🔥 ADD THIS BLOCK
	        + "AND ( "
	        + "ISNULL(contract_id,0) <> ISNULL(:contract_id,0) OR "
	        + "ISNULL(bid,0) <> ISNULL(:bid,0) OR "
	        + "ISNULL(invoice_type_calculation,'') <> ISNULL(:invoice_type_calculation,'') OR "
	        + "ISNULL(percentage_value,'') <> ISNULL(:percentage_value,'') OR "
	        + "ISNULL(invoice_to_id,0) <> ISNULL(:invoice_to_id,0) OR "
	        + "ISNULL(consignee_id,0) <> ISNULL(:consignee_id,0) OR "
	        + "ISNULL(shipment_mode_id,0) <> ISNULL(:shipment_mode_id,0) OR "
	        + "ISNULL(delivery_condition_id,0) <> ISNULL(:delivery_condition_id,0) OR "
	        + "ISNULL(product_desc_id,'') <> ISNULL(:product_desc_id,'') OR "
	        + "ISNULL(bank_details_id,0) <> ISNULL(:bank_details_id,0) OR "
	        + "ISNULL(work_id,0) <> ISNULL(:work_id,0) OR "
	        + "ISNULL(regd_office_id,0) <> ISNULL(:regd_office_id,0) OR "
	        + "ISNULL(s_code,0) <> ISNULL(:s_code,0) OR "
	        + "ISNULL(h_code,0) <> ISNULL(:h_code,0) OR "
	        + "ISNULL(export,'') <> ISNULL(:export,'') OR "
	        + "ISNULL(tax_ex_inc,'') <> ISNULL(:tax_ex_inc,'') OR "
	        + "ISNULL(taxable,'') <> ISNULL(:taxable,'') OR "
	        + "ISNULL(non_taxable,'') <> ISNULL(:non_taxable,'') OR "
	        + "ISNULL(tax_payable,'') <> ISNULL(:tax_payable,'') OR "
	        + "ISNULL(freight_advance_recovery,'') <> ISNULL(:freight_advance_recovery,'') OR "
	        + "ISNULL(area_no,'') <> ISNULL(:area_no,'') OR "
	        + "ISNULL(lot_no,'') <> ISNULL(:lot_no,'') OR "
	        + "ISNULL(containter_no,'') <> ISNULL(:containter_no,'') OR "
	        + "ISNULL(epcgno,'') <> ISNULL(:epcgno,'') OR "
	        + "ISNULL(export_title_text,'') <> ISNULL(:export_title_text,'') OR "
	        + "ISNULL(contract_name,'') <> ISNULL(:contract_name,'') OR "
	        + "ISNULL(factory_id,'') <> ISNULL(:factory_id,'') "
	        + ")",
	        nativeQuery = true)
		int updateContractInfo(int contract_id, int bid, String invoice_type_calculation, String percentage_value,
			int invoice_to_id, int consignee_id, int shipment_mode_id, int delivery_condition_id,
			String product_desc_id, int bank_details_id, int work_id, int regd_office_id, Integer s_code, Integer h_code,
			String export, String tax_ex_inc, String taxable, String non_taxable, String tax_payable,
			String freight_advance_recovery, String area_no, String lot_no, String containter_no, String epcgno,
			String export_title_text, String modified_by, String contract_name, String factory_id, int con_slno);

	@Transactional
	@Modifying
	@Query(value = "delete from CONTRACT_ASSIGN_TAX where id = :id", nativeQuery = true)
	void deleteTaxContract(int id);

	@Transactional
	@Modifying
	@Query(value = "delete from CONTRACT_ASSIGN_TAX where tax_id = :id and contract_id = :contract_id", nativeQuery = true)
	void deleteTaxContract(int id, int contract_id);

	@Query(value = "select tload_id,loadno from Tra_Loads where contract_id = :contract_id", nativeQuery = true)
	List<LoadsG2List> getTraLoadsG2(int contract_id);

	@Query(value = "select distinct cm.*,\r\n"
			+ "icam.address as invoice_to_id_value, icam1.address as consignee_id_value,\r\n"
			+ "shd.shipment_mode as shipment_mode_id_value, shdd.delivery_condition as delivery_condition_id_value,\r\n"
			+ "bm.bank_name as bank_details_id_value,wm.workorder_no as work_id_value, om.org_name as reg_office_id_value,\r\n"
			+ "sm.service_code as s_code_value, sms.service_code as h_code_value,\r\n"
			+ "tm.tax_id,tm.tax_name,tm.tax_per,apni.tax_total as taxable_amount, apni.net_total as nontaxable_amount, apni.total from CONTRACT_MASTER cm\r\n"
			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam on icam.id = cm.consignee_id\r\n"
			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam1 on icam1.id = cm.invoice_to_id\r\n"
			+ "inner join SHIPMENT_DELIVERY_CONDITION shd on shd.si_id = cm.shipment_mode_id\r\n"
			+ "inner join SHIPMENT_DELIVERY_CONDITION shdd on  shdd.si_id = cm.delivery_condition_id\r\n"
			+ "inner join WORKORDER_MASTER wm on wm.work_id = cm.work_id\r\n"
			+ "inner join BANK_MASTER bm on bm.account_id = cm.bank_details_id\r\n"
			+ "inner join SERVICECODE_MASTER sms on sms.servicecode_id = cm.h_code\r\n"
			+ "inner join SERVICECODE_MASTER sm on sm.servicecode_id = cm.s_code\r\n"
			+ "inner join organization_master om on om.org_id = cm.regd_office_id\r\n"
			+ "inner join CONTRACT_ASSIGN_TAX cat on cat.contract_id = cm.contract_id\r\n"
			+ "inner join TAX_MASTER tm on tm.tax_id = cat.tax_id\r\n"
			+ "left join OPENING_BALANCE apn on apn.con_id = cm.contract_id\r\n"
			+ "left join OPENING_BALANCE_ITEM apni on apni.pn_id = apn.pn_id\r\n"
			+ " where cm.contract_id = :contract_id and cm.factory_id =:factory_id", nativeQuery = true)
	List<AssignToContract> searchContract(String contract_id, String factory_id);
	
	@Query(value =
	        "SELECT DISTINCT cm.*, " +
	        		"ISNULL(icam.name_of_add,'') + " +
	        		"CASE " +
	        		"WHEN ISNULL(icam.add1,'') <> '' " +
	        		"THEN CHAR(13) + CHAR(10) + icam.add1 " +
	        		"ELSE '' " +
	        		"END + " +
	        		"CASE " +
	        		"WHEN ISNULL(icam.add2,'') <> '' " +
	        		"THEN CHAR(13) + CHAR(10) + icam.add2 " +
	        		"ELSE '' " +
	        		"END AS invoice_to_id_value, " +

	        		"ISNULL(icam1.name_of_add,'') + " +
	        		"CASE " +
	        		"WHEN ISNULL(icam1.add1,'') <> '' " +
	        		"THEN CHAR(13) + CHAR(10) + icam1.add1 " +
	        		"ELSE '' " +
	        		"END + " +
	        		"CASE " +
	        		"WHEN ISNULL(icam1.add2,'') <> '' " +
	        		"THEN CHAR(13) + CHAR(10) + icam1.add2 " +
	        		"ELSE '' " +
	        		"END AS consignee_id_value, " +

	        "shd.shipment_mode AS shipment_mode_id_value, " +
	        "shdd.delivery_condition AS delivery_condition_id_value, " +

	        "bm.bank_name AS bank_details_id_value, " +
	        "wm.workorder_no AS work_id_value, " +
	        "om.org_name AS reg_office_id_value, " +

	        "sm.service_code AS s_code_value, " +
	        "sms.service_code AS h_code_value, " +

	        "bu.business_unit_name, " +
	        "bu.gst_number BusinessgstNumber, " +
			"bu.pan_number as BusinesspanNumber, " +
			"bu.state_id as BusinessStateName, " +
	        "org.org_name, " +
	        "etm.export_title, " +

	        // =========================
	        // CONSIGNEE DETAILS
	        // =========================
	        "icam1.gst_no AS consigneeGstNo, " +
	        "icam1.pan_no AS consigneePanNo, " +
	        "icam1.district AS consigneeDistrict, " +
	        "icam1.pin_no AS consigneePinNo, " +

	        "stm.state_id AS consigneeStateCode, " +
	        "stm.state_name AS consigneeStateName, " +
	        "cum.country_name AS consigneeCountryName, " +

	        // =========================
	        // INVOICE DETAILS
	        // =========================
	        "icam.gst_no AS invoiceGstNo, " +
	        "icam.pan_no AS invoicePanNo, " +
	        "icam.district AS invoiceDistrict, " +
	        "icam.pin_no AS invoicePinNo, " +

	        "stm1.state_id AS invoiceStateCode, " +
	        "stm1.state_name AS invoiceStateName, " +
	        "cum1.country_name AS invoiceCountryName, " +

	        // =========================
	        // BANK DETAILS
	        // =========================
	        "bm.city AS bankcityname, " +
	        "stmbm.state_code AS bankstatecode, " +
	        "stmbm.state_name AS bankstatename, " +
	        "cmbm.country_code AS bankcountrycode, " +
	        "cmbm.country_name AS bankcountryname, " +

	        "bm.bank_name, " +
	        "bm.account_number, " +
	        "bm.branch_address, " +
	        "bm.branch_code, " +
	        "bm.ifsc_code, " +
	        "bm.swift_code, " +

	        "wm.work_date, " +
	        "imm.supply_place " +

	        "FROM CONTRACT_MASTER cm " +

	        "INNER JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER icam " +
	        "ON icam.id = cm.invoice_to_id " +

	        "INNER JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER icam1 " +
	        "ON icam1.id = cm.consignee_id " +

	        "INNER JOIN SHIPMENT_DELIVERY_CONDITION shd " +
	        "ON shd.si_id = cm.shipment_mode_id " +

	        "INNER JOIN SHIPMENT_DELIVERY_CONDITION shdd " +
	        "ON shdd.si_id = cm.delivery_condition_id " +

	        "INNER JOIN WORKORDER_MASTER wm " +
	        "ON wm.work_id = cm.work_id " +

	        "INNER JOIN BANK_MASTER bm " +
	        "ON bm.account_id = cm.bank_details_id " +

	        "LEFT JOIN SERVICECODE_MASTER sms " +
	        "ON sms.servicecode_id = cm.h_code " +

	        "LEFT JOIN SERVICECODE_MASTER sm " +
	        "ON sm.servicecode_id = cm.s_code " +

	        "INNER JOIN ORGANIZATION_MASTER om " +
	        "ON om.org_id = cm.regd_office_id " +

	        "INNER JOIN BUSINESS_UNITS bu " +
	        "ON bu.business_unit_id = cm.bid " +

	        "INNER JOIN STATE_MASTER stm " +
	        "ON stm.state_id = icam1.state_id " +

	        "INNER JOIN STATE_MASTER stm1 " +
	        "ON stm1.state_id = icam.state_id " +

	        "INNER JOIN STATE_MASTER stmbm " +
	        "ON stmbm.id = bm.state_id " +

	        "INNER JOIN COUNTRY_MASTER cmbm " +
	        "ON cmbm.id = bm.country_id " +

	        "INNER JOIN COUNTRY_MASTER cum " +
	        "ON cum.id = icam1.country_id " +

	        "INNER JOIN COUNTRY_MASTER cum1 " +
	        "ON cum1.id = icam.country_id " +

	        "INNER JOIN ORGANIZATION_MASTER org " +
	        "ON org.org_id = bu.org_id " +

	        "LEFT JOIN INVOICE_MASTER imm " +
	        "ON imm.contract_id = cm.contract_id " +

	        "LEFT JOIN EXPORT_TITLE_MASTER etm " +
	        "ON etm.id = cm.export_title_text " +

	        "WHERE CAST(cm.contract_id AS VARCHAR(20)) = :contract_id " +
	        "AND (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' " +
	        "LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%')",

	        nativeQuery = true)

	List<AssignToContract> searchContractNew(
	        String contract_id,
	        String factory_id);

	@Query(value = "select distinct cm.*, \r\n"
			+ "CONCAT( \r\n"
			+ "ISNULL(icam.name_of_add + ', ', ''), \r\n"
			+ "ISNULL(icam.add1 + ', ', ''), \r\n"
			+ "ISNULL(icam.add2 + ', ', ''), \r\n"
			+ "ISNULL(icam.city + ', ', ''), \r\n"
			+ "ISNULL(icam.district + ', ', ''), \r\n"
			+ "ISNULL(CAST(icam.pin_no AS VARCHAR), '') \r\n"
			+ ") as invoice_to_id_value, \r\n"

			+ "CONCAT( \r\n"
			+ "ISNULL(icam1.name_of_add + ', ', ''), \r\n"
			+ "ISNULL(icam1.add1 + ', ', ''), \r\n"
			+ "ISNULL(icam1.add2 + ', ', ''), \r\n"
			+ "ISNULL(icam1.city + ', ', ''), \r\n"
			+ "ISNULL(icam1.district + ', ', ''), \r\n"
			+ "ISNULL(CAST(icam1.pin_no AS VARCHAR), '') \r\n"
			+ ") as consignee_id_value, \r\n"

			+ "shd.shipment_mode as shipment_mode_id_value, \r\n"
			+ "shdd.delivery_condition as delivery_condition_id_value, \r\n"
			+ "bm.bank_name as bank_details_id_value, \r\n"
			+ "wm.workorder_no as work_id_value, \r\n"
			+ "om.org_name as reg_office_id_value, \r\n"
			+ "sm.service_code as s_code_value, \r\n"
			+ "sms.service_code as h_code_value, \r\n"

			+ "CONCAT( \r\n"
			+ "ISNULL(icam.name_of_add + ', ', ''), \r\n"
			+ "ISNULL(icam.add1 + ', ', ''), \r\n"
			+ "ISNULL(icam.add2 + ', ', ''), \r\n"
			+ "ISNULL(icam.city + ', ', ''), \r\n"
			+ "ISNULL(icam.district + ', ', ''), \r\n"
			+ "ISNULL(CAST(icam.pin_no AS VARCHAR), '') \r\n"
			+ ") AS consigneeAddress, \r\n"

			+ "CONCAT( \r\n"
			+ "ISNULL(icam1.name_of_add + ', ', ''), \r\n"
			+ "ISNULL(icam1.add1 + ', ', ''), \r\n"
			+ "ISNULL(icam1.add2 + ', ', ''), \r\n"
			+ "ISNULL(icam1.city + ', ', ''), \r\n"
			+ "ISNULL(icam1.district + ', ', ''), \r\n"
			+ "ISNULL(CAST(icam1.pin_no AS VARCHAR), '') \r\n"
			+ ") AS invoiceAddress, \r\n"

			+ "bu.business_unit_name as businessUnitName, \r\n"
			+ "etm.export_title \r\n"

			+ "from CONTRACT_MASTER cm \r\n"

			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam "
			+ "on icam.id = cm.consignee_id \r\n"

			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam1 "
			+ "on icam1.id = cm.invoice_to_id \r\n"

			+ "inner join SHIPMENT_DELIVERY_CONDITION shd "
			+ "on shd.si_id = cm.shipment_mode_id \r\n"

			+ "inner join SHIPMENT_DELIVERY_CONDITION shdd "
			+ "on shdd.si_id = cm.delivery_condition_id \r\n"

			+ "inner join WORKORDER_MASTER wm "
			+ "on wm.work_id = cm.work_id \r\n"

			+ "inner join BANK_MASTER bm "
			+ "on bm.account_id = cm.bank_details_id \r\n"

			+ "left join SERVICECODE_MASTER sms "
			+ "on sms.servicecode_id = cm.h_code \r\n"

			+ "left join SERVICECODE_MASTER sm "
			+ "on sm.servicecode_id = cm.s_code \r\n"

			+ "inner join organization_master om "
			+ "on om.org_id = cm.regd_office_id \r\n"

			+ "inner join BUSINESS_UNITS bu "
			+ "on bu.business_unit_id = cm.bid \r\n"

			+ "left join EXPORT_TITLE_MASTER etm "
			+ "on etm.id = cm.export_title_text \r\n"

			+ "where cm.con_slno = :con_slno",
			nativeQuery = true)

List<AssignToContract> searchContractById(String con_slno);
	
	@Query(value = "select distinct cm.*,\r\n"
			+ "icam.address as invoice_to_id_value, icam1.address as consignee_id_value,\r\n"
			+ "shd.shipment_mode as shipment_mode_id_value, shdd.delivery_condition as delivery_condition_id_value,\r\n"
			+ "bm.bank_name as bank_details_id_value,wm.workorder_no as work_id_value, om.org_name as reg_office_id_value,\r\n"
			+ "sm.service_code as s_code_value, sms.service_code as h_code_value,\r\n"
			+ "tm.tax_id,tm.tax_name,tm.tax_per,apni.tax_total as taxable_amount, apni.net_total as nontaxable_amount, apni.total,\r\n"
			+ "icam.address AS consigneeAddress,icam.district AS consigneeDistrict,icam.pin_no AS consigneePinNo,\r\n"
			+ "icam.pan_no AS consigneePanNo,icam.gst_no AS consigneeGstNo,smm.state_name AS consigneeStateName,\r\n"
			+ "smm.state_code AS consigneeStateCode,cmm.country_name AS consigneeCountryName,\r\n"
			+ "icam1.address AS invoiceAddress,icam1.district AS invoiceDistrict,icam1.pin_no AS invoicePinNo,\r\n"
			+ "icam1.pan_no AS invoicePanNo,icam1.gst_no AS invoiceGstNo,smm1.state_name AS invoiceStateName,\r\n"
			+ "smm1.state_code AS invoiceStateCode,cmm1.country_name AS invoiceCountryName,\r\n"
			+ " bu.business_unit_name as businessUnitName, bu.gst_number as businessgstNumber,\r\n"
			+ "bu.location as businessLocation,bu.bu_code as businessBuCode, sbu.state_code businessStateCode,\r\n"
			+ "sbu.state_name businessStateName, sbu.state_id as businessStateId from CONTRACT_MASTER cm\r\n"
			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam on icam.id = cm.consignee_id\r\n"
			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam1 on icam1.id = cm.invoice_to_id\r\n"
			+ "inner join SHIPMENT_DELIVERY_CONDITION shd on shd.si_id = cm.shipment_mode_id\r\n"
			+ "inner join SHIPMENT_DELIVERY_CONDITION shdd on  shdd.si_id = cm.delivery_condition_id\r\n"
			+ "inner join WORKORDER_MASTER wm on wm.work_id = cm.work_id\r\n"
			+ "inner join BANK_MASTER bm on bm.account_id = cm.bank_details_id\r\n"
			+ "inner join SERVICECODE_MASTER sms on sms.servicecode_id = cm.h_code\r\n"
			+ "inner join SERVICECODE_MASTER sm on sm.servicecode_id = cm.s_code\r\n"
			+ "inner join organization_master om on om.org_id = cm.regd_office_id\r\n"
			+ "inner join CONTRACT_ASSIGN_TAX cat on cat.contract_id = cm.contract_id\r\n"
			+ "inner join TAX_MASTER tm on tm.tax_id = cat.tax_id\r\n"
			+ "left join BUSINESS_UNITS bu on bu.business_unit_id = cm.bid\r\n"
			+ "left join OPENING_BALANCE apn on apn.pn_id = (SELECT MAX(pn_id) FROM OPENING_BALANCE WHERE con_id = cm.contract_id )"
			+ "LEFT JOIN OPENING_BALANCE_ITEM apni ON apni.pn_id = apn.pn_id\r\n"
			+ "left join STATE_MASTER smm on smm.id = icam.state_id\r\n"
			+ "left join COUNTRY_MASTER cmm on cmm.id = icam.country_id\r\n"
			+ "left join STATE_MASTER smm1 on smm1.id = icam1.state_id\r\n"
			+ "left join COUNTRY_MASTER cmm1 on cmm1.id = icam1.country_id\r\n"
			+ "left join STATE_MASTER sbu on sbu.id = bu.state_id\r\n"
			+ " where cm.contract_id = :contract_id", nativeQuery = true)
	List<AssignToContract> searchContractinInvoice(String contract_id);

//	@Query(value =  "select cm.contract_id,cm.contract_name,cm.percentage_value,mm.milestone_code,mm.milestone_name,cm.milestone_id from CONTRACT_MASTER cm\r\n"
//			+ "inner join MILESTONE_MASTER mm on mm.milestone_id = cm.milestone_id where cm.is_delete = 0", nativeQuery = true)
//	List<ContractListFromContractInterfaces> getContractListFromContract();

	@Query(value = "select cm.contract_id,cm.contract_name,cm.factory_id from CONTRACT_MASTER cm\r\n"
			+ " where cm.is_delete = 0", nativeQuery = true)
	List<ContractListFromContractInterfaces> getContractListFromContract();

	@Transactional
	@Modifying
	@Query(value = "delete from CONTRACT_ASSIGN_TAX where contract_id = :contract_id", nativeQuery = true)
	void deleteContractIdInAssignToContract(int contract_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO MILESTONE_ASSGIN_CONTRACT_MASTER (milestone_id,contract_id,created_by,created_date,factory_id) VALUES (:milestone_id,:contract_id,:created_by,GETDATE(),:factory_id)", nativeQuery = true)
	int assignMileStoneToContract(String milestone_id, String contract_id, String created_by, String factory_id);

	@Query(value = "select ma.id, mm.milestone_id, CONCAT(mm.milestone_code, ' - ', mm.milestone_name) AS milestone_name, ma.factory_id from MILESTONE_ASSGIN_CONTRACT_MASTER ma\r\n"
			+ "inner join MILESTONE_MASTER mm on mm.milestone_id = ma.milestone_id\r\n"
			+ " where  ma.contract_id = :contract_id", nativeQuery = true)
	List<MileStoneAssignedContractListInterfaces> listContractMaster(int contract_id);

	@Query(value = "select count(*) from MILESTONE_ASSGIN_CONTRACT_MASTER where milestone_id = :milestone_id and contract_id = :contract_id", nativeQuery = true)
	int checkDuplicateMilestone(String milestone_id, String contract_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE CONTRACT_MASTER SET is_locked = :locked where contract_id = :contact_id and factory_id = :factory_id", nativeQuery = true)
	void updateISReleaseInContractMaster(long contact_id, String locked, String factory_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where con_slno = :con_slno and is_locked = 1", nativeQuery = true)
	int checkContractIdisLocked(int con_slno);

	@Transactional
	@Modifying
	@Query(value = "UPDATE CONTRACT_ASSIGN_TAX SET modified_by = :modified_by,modified_date = GETDATE() WHERE tax_id = :tax_id AND contract_id = :contract_id;", nativeQuery = true)
	int updateTaxContract(int tax_id, int contract_id, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO CONTRACT_ASSIGN_TAX(contract_id,tax_id,factory_id,created_by,created_date) values (:contract_id,:tax_id,:factory_id,:created_by,GETDATE())", nativeQuery = true)
	int insertTaxContract(int contract_id, List<String> tax_id, int factory_id, String created_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO CONTRACT_MASTER (contract_id,bid,invoice_type_calculation,percentage_value,invoice_to_id,consignee_id,shipment_mode_id,delivery_condition_id,product_desc_id,bank_details_id,work_id,regd_office_id,s_code,h_code,export,tax_ex_inc,taxable,non_taxable,tax_payable,freight_advance_recovery,area_no,lot_no,containter_no,epcgno,export_title_text,created_by,created_date,contract_name,factory_id) VALUES "
			+ " (:contract_id,:bid,:invoice_type_calculation,:percentage_value,:invoice_to_id,:consignee_id,:shipment_mode_id,:delivery_condition_id,:product_desc_id,:bank_details_id,:work_id,:regd_office_id,:s_code,:h_code,:export,:tax_ex_inc,:taxable,:non_taxable,:tax_payable,:freight_advance_recovery,:area_no,:lot_no,:containter_no,:epcgno,:export_title_text,:created_by,GETDATE(),:contract_name,:factory_id )", nativeQuery = true)
	int createContractInfoNew(int contract_id, int bid, String invoice_type_calculation, String percentage_value,
			int invoice_to_id, int consignee_id, int shipment_mode_id, int delivery_condition_id,
			String product_desc_id, int bank_details_id, int work_id, int regd_office_id, int s_code, int h_code,
			String export, String tax_ex_inc, String taxable, String non_taxable, String tax_payable,
			String freight_advance_recovery, String area_no, String lot_no, String containter_no, String epcgno,
			String export_title_text, String created_by, String contract_name, int factory_id);

	@Query(value = "select contract_id from CONTRACT_MASTER where con_slno =:con_slno", nativeQuery = true)
	int getContractorIdforConSlno(String con_slno);

	@Query(value = "select distinct tax_id from CONTRACT_ASSIGN_TAX where contract_id = :contract_id", nativeQuery = true)
	List<Integer> getTaxListFromContractAssignTax(int contract_id);

	/*
	 * @Query(value =
	 * "select tm.tax_id,tm.tax_name,tm.tax_per from CONTRACT_MASTER cm  \r\n" +
	 * "inner join CONTRACT_ASSIGN_TAX cat on cat.contract_id = cm.contract_id  \r\n"
	 * + "inner join TAX_MASTER tm on tm.tax_id = cat.tax_id    \r\n" +
	 * "where cm.contract_id = :contract_id and cm.factory_id = :factory_id",
	 * nativeQuery = true) List<TaxMasterInterface> searchTaxId(String contract_id,
	 * String factory_id);
	 */
	
	@Query(value = "SELECT "
	        + "tm.tax_id, "
	        + "tm.tax_name, "
	        + "tm.tax_per, "
	        + "qm.grand_total, "
	        + "qm.freight, "
	        + "SUM(qim.pices) AS total_pices, "
	        + "SUM(qim.qty) AS total_qty, "
	        + "SUM(qim.per_kgs) AS total_kgs, "
	        + "MAX(cm.percentage_value) AS percentage_value, "
	        + "(qm.grand_total + qm.freight) AS basic_value, "
	        + "CASE "
	        + "    WHEN LOWER(cm.taxable) = 'yes' THEN "
	        + "        CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2)) "
	        + "    ELSE 0.0 "
	        + "END AS taxable, "
	        + "CASE "
	        + "    WHEN LOWER(cm.taxable) = 'no' THEN "
	        + "        CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2)) "
	        + "    ELSE 0.0 "
	        + "END AS non_taxable, "
	        + "CASE "
	        + "    WHEN LOWER(cm.taxable) = 'no' THEN "
	        + "        CAST(ROUND((qm.grand_total + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) "
	        + "    ELSE "
	        + "        ABS(CAST(ROUND((CAST((qm.grand_total + qm.freight) - (qm.grand_total * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2))) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2))) "
	        + "END AS optd, "
	        + "ABS(CAST((qm.grand_total + qm.freight) - (qm.grand_total * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2))) AS optc, "
	        + "( "
	        + "    ABS(CAST((qm.grand_total + qm.freight) - (qm.grand_total * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2))) "
	        + "    + "
	        + "    CASE "
	        + "        WHEN LOWER(cm.taxable) = 'no' THEN "
	        + "            CAST(ROUND((qm.grand_total + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) "
	        + "        ELSE "
	        + "            ABS(CAST(ROUND((CAST((qm.grand_total + qm.freight) - (qm.grand_total * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2))) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2))) "
	        + "    END "
	        + ") AS optc_p, "
	        + "CAST(ROUND((qm.grand_total + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) AS tax_value, "
	        + "CAST(ROUND((qm.grand_total + qm.freight) + ((qm.grand_total + qm.freight) * (tm.tax_per / 100)), 2) AS DECIMAL(10, 2)) AS total_after_tax, "
	        + "CASE "
	        + "    WHEN LOWER(cm.taxable) = 'no' THEN "
	        + "        CAST(ROUND((qm.grand_total + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) "
	        + "    ELSE "
	        + "        ABS(CAST(ROUND((CAST((qm.grand_total + qm.freight) - (qm.grand_total * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2))) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2))) "
	        + "END AS ptc, "
	        + "CASE "
	        + "    WHEN LOWER(cm.taxable) = 'no' THEN "
	        + "        CAST(ROUND((qm.grand_total + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) "
	        + "    ELSE "
	        + "        ABS(CAST(ROUND((CAST((qm.grand_total + qm.freight) - (qm.grand_total * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2))) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2))) "
	        + "END AS ptd, "
	        + "CASE "
	        + "    WHEN LOWER(cm.taxable) = 'no' THEN 0.0 "
	        + "    WHEN CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2)) > 0.00 THEN "
	        + "        CAST((CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2))) * (tm.tax_per / 100) AS DECIMAL(10, 2)) "
	        + "    ELSE 0.0 "
	        + "END AS adv_tax, "
	        + "CONCAT(dbo.NumberToRupeesWords(CAST(ROUND((qm.grand_total + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2))), ' Only') AS tax_value_in_words "
	        + "FROM CONTRACT_MASTER cm "
	        + "INNER JOIN CONTRACT_ASSIGN_TAX cat ON cat.contract_id = cm.contract_id "
	        + "INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id "
	        + "INNER JOIN QSPACKING_MASTER qm ON qm.con_id = :contract_id AND qm.load_id = :load_id AND qm.factory_id = :factory_id "
	        + "INNER JOIN QSPACKING_ITEM_MASTER qim ON qim.pn_id = qm.pn_id "
	        + "WHERE cm.contract_id = :contract_id "
	        + "AND (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%') "
	        + "AND qm.Cancel = 0 "
	        + "GROUP BY "
	        + "tm.tax_id, tm.tax_name, tm.tax_per, qm.grand_total, qm.freight, cm.taxable",
	        nativeQuery = true)
	List<TaxMasterInterface> searchTaxId(String contract_id, String factory_id, String load_id);
	
	
	@Query(value = "SELECT\r\n"
			+ "    tm.tax_id,\r\n"
			+ "    tm.tax_name,\r\n"
			+ "    tm.tax_per,\r\n"
			+ "    qm.grand_total,\r\n"
			+ "    qm.freight,\r\n"
			+ "    SUM(qim.pices) AS total_pices,\r\n"
			+ "    SUM(qim.qty) AS total_qty,\r\n"
			+ "	SUM(qim.per_kgs) As total_kgs,\r\n"
			+ "    MAX(cm.percentage_value) AS percentage_value,\r\n"
			+ "    (qm.grand_total + qm.freight) AS basic_value,\r\n"
			+ "    CASE \r\n"
			+ "        WHEN cm.taxable = 'yes' THEN \r\n"
			+ "            CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2))\r\n"
			+ "        ELSE 0.0 \r\n"
			+ "    END AS taxable,\r\n"
			+ "    CASE \r\n"
			+ "        WHEN cm.taxable = 'no' THEN \r\n"
			+ "            CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2))\r\n"
			+ "        ELSE 0.0 \r\n"
			+ "    END AS non_taxable,\r\n"
			+ "    SUM(CAST(ROUND((qm.grand_total + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(18,0))) OVER() AS optd,\r\n"
			+ "    CAST((qm.grand_total + qm.freight) - ((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2)) AS optc,\r\n"
			+ "    CAST(ROUND((qm.grand_total + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) AS tax_value,\r\n"
			+ "    CAST(ROUND((qm.grand_total + qm.freight) + ((qm.grand_total + qm.freight) * (tm.tax_per / 100)), 2) AS DECIMAL(10, 2)) AS total_after_tax,\r\n"
			+ "    CAST(ROUND((CAST((qm.grand_total + qm.freight) - ((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2))) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) AS ptc,\r\n"
			+ "    CAST(ROUND((CAST((qm.grand_total + qm.freight) - ((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2))) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) AS ptd,\r\n"
			+ "    CASE \r\n"
			+ "        WHEN cm.taxable = 'no' THEN \r\n"
			+ "            0.0\r\n"
			+ "        WHEN CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2)) > 0.00 THEN \r\n"
			+ "            CAST((CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2))) * (tm.tax_per / 100) AS DECIMAL(10, 2))\r\n"
			+ "        ELSE 0.0 \r\n"
			+ "    END AS adv_tax,\r\n"
			+ "    CONCAT(dbo.NumberToRupeesWords(CAST(ROUND((qm.grand_total + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2))), ' Only') AS tax_value_in_words\r\n"
			+ "FROM\r\n"
			+ "    CONTRACT_MASTER cm\r\n"
			+ "INNER JOIN\r\n"
			+ "    CONTRACT_ASSIGN_TAX cat \r\n"
			+ "        ON cat.contract_id = cm.contract_id\r\n"
			+ "INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id\r\n"
			+ "INNER JOIN QSPACKING_MASTER qm ON qm.con_id = :contract_id AND qm.load_id = :load_id   and qm.factory_id=:factory_id \r\n"
			+ "INNER JOIN QSPACKING_ITEM_MASTER qim on qim.pn_id = qm.pn_id\r\n"
			+ "WHERE\r\n"
			+ "    cm.contract_id = :contract_id \r\n"
			+ "    AND  (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%') and qm.Cancel =1\r\n"
			+ "GROUP BY\r\n"
			+ "    tm.tax_id,\r\n"
			+ "    tm.tax_name,\r\n"
			+ "    tm.tax_per,\r\n"
			+ "    qm.grand_total,\r\n"
			+ "    qm.freight,\r\n"
			+ "    cm.taxable", nativeQuery = true)
	List<TaxMasterInterface> searchTaxIdcancel(String contract_id, String factory_id,String load_id);
	@Query(value = "SELECT " +
	        "    tm.tax_id, " +
	        "    tm.tax_name, " +
	        "    tm.tax_per, " +
	        "    qcpm.grand_total, " +
	        "    SUM(qcim.pices) AS total_pices, " +
	        "    SUM(qcim.qty) AS total_qty, " +
	        "    SUM(qcim.per_kgs) AS total_kgs, " +
	        "    MAX(cm.percentage_value) AS percentage_value, " +
	        "    (qcpm.grand_total + 0) AS basic_value, " +
	        "    CASE  " +
	        "        WHEN cm.taxable = 'yes' THEN  " +
	        "            CAST((qcpm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2)) " +
	        "        ELSE 0.0  " +
	        "    END AS taxable, " +
	        "    CASE  " +
	        "        WHEN cm.taxable = 'no' THEN  " +
	        "            CAST((qcpm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2)) " +
	        "        ELSE 0.0  " +
	        "    END AS non_taxable, " +
	        "    CAST((qcpm.grand_total + 0) - ((qcpm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2)) AS optd, " +
	        "    CAST((qcpm.grand_total + 0) - ((qcpm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2)) AS optc, " +
	        "    CAST(ROUND((qcpm.grand_total + 0) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) AS tax_value, " +
	        "    CAST(ROUND((qcpm.grand_total +0) + ((qcpm.grand_total + 0) * (tm.tax_per / 100)), 2) AS DECIMAL(10, 2)) AS total_after_tax, " +
	        "    CAST(ROUND((CAST((qcpm.grand_total + 0) - ((qcpm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2))) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) AS ptc, " +
	        "    CAST(ROUND((CAST((qcpm.grand_total + 0) - ((qcpm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2))) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) AS ptd, " +
	        "    CASE  " +
	        "        WHEN cm.taxable = 'no' THEN  " +
	        "            0.0 " +
	        "        WHEN CAST((qcpm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2)) > 0.00 THEN  " +
	        "            CAST((CAST((qcpm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2))) * (tm.tax_per / 100) AS DECIMAL(10, 2)) " +
	        "        ELSE 0.0  " +
	        "    END AS adv_tax, " +
	        "    CONCAT(dbo.NumberToRupeesWords(CAST(ROUND((qcpm.grand_total + 0) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2))), ' Only') AS tax_value_in_words " +
	        "FROM  " +
	        "    CONTRACT_MASTER cm  " +
	        "INNER JOIN  " +
	        "    CONTRACT_ASSIGN_TAX cat  " +
	        "        ON cat.contract_id = cm.contract_id " +
	        "INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id " +
	        "INNER JOIN QSCHALLAN_PACKINGNOTE_MASTER qcpm ON qcpm.contract_id = :contract_id AND qcpm.load_id = :load_id " +
	        "INNER JOIN QSCHALLAN_PACKINGNOTEITEM_MASTER qcim ON qcim.pn_id = qcpm.pn_id " +
	        "WHERE  " +
	        "    cm.contract_id = :contract_id  " +
	        "    AND (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%') " +
	        "GROUP BY  " +
	        "    tm.tax_id, " +
	        "    tm.tax_name, " +
	        "    tm.tax_per, " +
	        "    qcpm.grand_total, " +
	        "    cm.taxable", 
	        nativeQuery = true)
	List<TaxMasterInterface> searchdlyTaxId(String contract_id, String factory_id, String load_id);
	
	@Query(value =
		    "SELECT \r\n" +
		    "    tm.tax_id, \r\n" +
		    "    tm.tax_name, \r\n" +
		    "    tm.tax_per, \r\n" +
		    "    qam.grand_total, \r\n" +
		    "    SUM(qaim.pices) AS total_pices, \r\n" +
		    "    SUM(qaim.qty) AS total_qty, \r\n" +
		    "    SUM(qaim.per_kgs) AS total_kgs, \r\n" +
		    "    MAX(cm.percentage_value) AS percentage_value, \r\n" +
		    "    qam.grand_total AS basic_value, \r\n" +

		    "    CASE \r\n" +
		    "        WHEN cm.taxable = 'yes' THEN \r\n" +
		    "            CAST(qam.grand_total * (MAX(cm.percentage_value) / 100.0) AS DECIMAL(10,2)) \r\n" +
		    "        ELSE 0.00 \r\n" +
		    "    END AS taxable, \r\n" +

		    "    CASE \r\n" +
		    "        WHEN cm.taxable = 'no' THEN \r\n" +
		    "            CAST(qam.grand_total * (MAX(cm.percentage_value) / 100.0) AS DECIMAL(10,2)) \r\n" +
		    "        ELSE 0.00 \r\n" +
		    "    END AS non_taxable, \r\n" +

		    "    ABS(CAST(ROUND( \r\n" +
		    "        (CAST(qam.grand_total - (qam.grand_total * (CAST(MAX(cm.percentage_value) AS DECIMAL(10,2)) / 100)) AS DECIMAL(10,2))) \r\n" +
		    "        * (tm.tax_per / 100), 0) AS DECIMAL(10,2))) AS optd, \r\n" +

		    "    ( \r\n" +
		    "        ABS(CAST(qam.grand_total - (qam.grand_total * (CAST(MAX(cm.percentage_value) AS DECIMAL(10,2)) / 100)) AS DECIMAL(10,2))) \r\n" +
		    "        + \r\n" +
		    "        ABS(CAST(ROUND( \r\n" +
		    "            (CAST(qam.grand_total - (qam.grand_total * (CAST(MAX(cm.percentage_value) AS DECIMAL(10,2)) / 100)) AS DECIMAL(10,2))) \r\n" +
		    "            * (tm.tax_per / 100), 0) AS DECIMAL(10,2))) \r\n" +
		    "    ) AS optc, \r\n" +

		    "    CAST(ROUND(qam.grand_total * (tm.tax_per / 100.0), 0) AS DECIMAL(10,2)) AS tax_value, \r\n" +

		    "    CAST(ROUND(qam.grand_total + (qam.grand_total * (tm.tax_per / 100.0)), 2) AS DECIMAL(10,2)) AS total_after_tax, \r\n" +

		    "    ABS(CAST(ROUND((qam.grand_total - (qam.grand_total * (MAX(cm.percentage_value) / 100.0))) * (tm.tax_per / 100.0), 0) AS DECIMAL(10,2))) AS ptc, \r\n" +

		    "    ABS(CAST(ROUND((qam.grand_total - (qam.grand_total * (MAX(cm.percentage_value) / 100.0))) * (tm.tax_per / 100.0), 0) AS DECIMAL(10,2))) AS ptd, \r\n" +

		    "    CASE \r\n" +
		    "        WHEN cm.taxable = 'no' THEN 0.00 \r\n" +
		    "        WHEN qam.grand_total * (MAX(cm.percentage_value) / 100.0) > 0 THEN \r\n" +
		    "            CAST((qam.grand_total * (MAX(cm.percentage_value) / 100.0)) * (tm.tax_per / 100.0) AS DECIMAL(10,2)) \r\n" +
		    "        ELSE 0.00 \r\n" +
		    "    END AS adv_tax, \r\n" +

		    "    CONCAT(dbo.NumberToRupeesWords(CAST(ROUND(qam.grand_total * (tm.tax_per / 100.0), 0) AS DECIMAL(10,2))), ' Only') AS tax_value_in_words \r\n" +

		    "FROM CONTRACT_MASTER cm \r\n" +
		    "INNER JOIN CONTRACT_ASSIGN_TAX cat ON cat.contract_id = cm.contract_id \r\n" +
		    "INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id \r\n" +

		    "INNER JOIN ( \r\n" +
		    "    SELECT pn.contract_id, pn.pn_id, pn.grand_total \r\n" +
		    "    FROM QSADVANCE_PACKINGNOTE_MASTER pn \r\n" +
		    "    WHERE pn.pn_id = :pn_id \r\n" +
		    "    AND pn.is_delete = 0 \r\n" +
		    ") qam ON qam.contract_id = cm.contract_id \r\n" +

		    "INNER JOIN QSADVANCE_PACKINGNOTEITEM_MASTER qaim \r\n" +
		    "    ON qaim.pn_id = qam.pn_id \r\n" +

		    "WHERE cm.contract_id = :contract_id \r\n" +
		    "  AND (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',') \r\n" +
		    "      LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%' \r\n" +

		    "GROUP BY \r\n" +
		    "    tm.tax_id, \r\n" +
		    "    tm.tax_name, \r\n" +
		    "    tm.tax_per, \r\n" +
		    "    cm.taxable, \r\n" +
		    "    qam.grand_total",
		    nativeQuery = true)
		List<TaxMasterInterface> searchadvTaxId(String contract_id, String factory_id, String pn_id);
	
	@Query(value = "SELECT\r\n"
	        + "    tm.tax_id,\r\n"
	        + "    tm.tax_name,\r\n"
	        + "    tm.tax_per,\r\n"
	        + "    qam.grand_total,\r\n"
	        + "    SUM(qaim.pices) AS total_pices,\r\n"
	        + "    SUM(qaim.qty) AS total_qty,\r\n"
	        + "    SUM(qaim.per_kgs) AS total_kgs,\r\n"
	        + "    MAX(cm.percentage_value) AS percentage_value,\r\n"
	        + "    (qam.grand_total + 0) AS basic_value,\r\n"
	        + "    CASE \r\n"
	        + "        WHEN cm.taxable = 'yes' THEN \r\n"
	        + "            CAST((qam.grand_total) * (MAX(cm.percentage_value) / 100.0) AS DECIMAL(10, 2))\r\n"
	        + "        ELSE 0.0 \r\n"
	        + "    END AS taxable,\r\n"
	        + "    CASE \r\n"
	        + "        WHEN cm.taxable = 'no' THEN \r\n"
	        + "            CAST((qam.grand_total) * (MAX(cm.percentage_value) / 100.0) AS DECIMAL(10, 2))\r\n"
	        + "        ELSE 0.0 \r\n"
	        + "    END AS non_taxable,\r\n"
	        + "    ABS(CAST(SUM(ROUND(qam.grand_total * (tm.tax_per / 100.0), 0)) OVER ()AS DECIMAL(10,2))) AS optd,\r\n"
	        + "    ABS(CAST((qam.grand_total + 0)- (qam.grand_total * (MAX(cm.percentage_value) / 100.0))AS DECIMAL(10, 2))) AS optc,\r\n"
	        + "    CAST(ROUND((qam.grand_total + 0) * (tm.tax_per / 100.0), 0) AS DECIMAL(10, 2)) AS tax_value,\r\n"
	        + "    CAST(ROUND((qam.grand_total + 0) + ((qam.grand_total + 0) * (tm.tax_per / 100.0)), 2) AS DECIMAL(10, 2)) AS total_after_tax,\r\n"
	        + "    ABS(CAST(ROUND(((qam.grand_total + 0)- (qam.grand_total * (MAX(cm.percentage_value) / 100.0)))* (tm.tax_per / 100.0),0)AS DECIMAL(10, 2))) AS ptc,\r\n"
	        + "    ABS(CAST(ROUND(((qam.grand_total + 0)- (qam.grand_total * (MAX(cm.percentage_value) / 100.0)))* (tm.tax_per / 100.0),0)AS DECIMAL(10, 2))) AS ptd,\r\n"
	        + "    CASE \r\n"
	        + "        WHEN cm.taxable = 'no' THEN 0.0\r\n"
	        + "        WHEN CAST((qam.grand_total) * (MAX(cm.percentage_value) / 100.0) AS DECIMAL(10, 2)) > 0.00 THEN \r\n"
	        + "            CAST(((qam.grand_total) * (MAX(cm.percentage_value) / 100.0)) * (tm.tax_per / 100.0) AS DECIMAL(10, 2))\r\n"
	        + "        ELSE 0.0 \r\n"
	        + "    END AS adv_tax,\r\n"
	        + "    CONCAT(dbo.NumberToRupeesWords(CAST(ROUND((qam.grand_total + 0) * (tm.tax_per / 100.0), 0) AS DECIMAL(10, 2))), ' Only') AS tax_value_in_words\r\n"
	        + "FROM\r\n"
	        + "    CONTRACT_MASTER cm\r\n"
	        + "INNER JOIN CONTRACT_ASSIGN_TAX cat ON cat.contract_id = cm.contract_id\r\n"
	        + "INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id\r\n"
	        + "INNER JOIN QSCHALLAN_PACKINGNOTE_MASTER qam ON qam.contract_id = :contract_id AND qam.load_id = :load_id\r\n"
	        + "INNER JOIN QSCHALLAN_PACKINGNOTEITEM_MASTER qaim ON qaim.pn_id = qam.pn_id\r\n"
	        + "WHERE\r\n"
	        + "    cm.contract_id = :contract_id\r\n"
	        + "    AND (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%')\r\n"
	        + "GROUP BY\r\n"
	        + "    tm.tax_id,\r\n"
	        + "    tm.tax_name,\r\n"
	        + "    tm.tax_per,\r\n"
	        + "    qam.grand_total,\r\n"
	        + "    cm.taxable", nativeQuery = true)
	List<TaxMasterInterface> searchDeliveryTaxId(String contract_id, String factory_id, String load_id);
	
	@Query(value = "SELECT\r\n"
			+ "			   tm.tax_id,\r\n"
			+ "			   tm.tax_name,\r\n"
			+ "			   tm.tax_per,\r\n"
			+ "			   qm.totalpn_amount,\r\n"
			+ "			   qm.freight,\r\n"
			+ "			   SUM(qim.quantities) AS total_qty,\r\n"
			+ "				SUM(qim.kgs) As total_kgs,\r\n"
			+ "			   MAX(cm.percentage_value) AS percentage_value,\r\n"
			+ "			   (qm.totalpn_amount + qm.freight) AS basic_value,\r\n"
			+ "			   CASE \r\n"
			+ "			       WHEN cm.taxable = 'yes' THEN \r\n"
			+ "			           CAST((qm.totalpn_amount) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2))\r\n"
			+ "			       ELSE 0.0 \r\n"
			+ "			   END AS taxable,\r\n"
			+ "			   CASE \r\n"
			+ "			       WHEN cm.taxable = 'no' THEN \r\n"
			+ "			           CAST((qm.totalpn_amount) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2))\r\n"
			+ "			       ELSE 0.0 \r\n"
			+ "			   END AS non_taxable,\r\n"
			+ "CASE "
			+ "    WHEN LOWER(cm.taxable) = 'no' THEN "
			+ "        CAST(ROUND((qm.totalpn_amount + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) "
			+ "    ELSE "
			+ "        ABS(CAST(ROUND((CAST((qm.totalpn_amount + qm.freight) - "
			+ "        (qm.totalpn_amount * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) "
			+ "        AS DECIMAL(10, 2))) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2))) "
			+ "END AS optd, "
			+ "ABS(CAST(ROUND((qm.totalpn_amount + qm.freight) - "
			+ "(qm.totalpn_amount * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)), "
			+ "2) AS DECIMAL(10, 2))) AS optc, "
			+ "( "
			+ "    ABS(CAST(ROUND((qm.totalpn_amount + qm.freight) - "
			+ "    (qm.totalpn_amount * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)), "
			+ "    2) AS DECIMAL(10, 2))) "
			+ "    + "
			+ "    CASE "
			+ "        WHEN LOWER(cm.taxable) = 'no' THEN "
			+ "            CAST(ROUND((qm.totalpn_amount + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) "
			+ "        ELSE "
			+ "            ABS(CAST(ROUND((CAST((qm.totalpn_amount + qm.freight) - "
			+ "            (qm.totalpn_amount * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) "
			+ "            AS DECIMAL(10, 2))) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2))) "
			+ "    END "
			+ ") AS optc_dp, "
	        + "CAST(ROUND((qm.totalpn_amount + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) AS tax_value, "
	        + "CAST(ROUND((qm.totalpn_amount + qm.freight) + ((qm.totalpn_amount + qm.freight) * (tm.tax_per / 100)), 2) AS DECIMAL(10, 2)) AS total_after_tax, "
	        + "CASE "
	        + "    WHEN LOWER(cm.taxable) = 'no' THEN "
	        + "        CAST(ROUND((qm.totalpn_amount + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) "
	        + "    ELSE "
	        + "        ABS(CAST(ROUND((CAST((qm.totalpn_amount + qm.freight) - (qm.totalpn_amount * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2))) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2))) "
	        + "END AS ptc, "
	        + "CASE "
	        + "    WHEN LOWER(cm.taxable) = 'no' THEN "
	        + "        CAST(ROUND((qm.totalpn_amount + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) "
	        + "    ELSE "
	        + "        ABS(CAST(ROUND((CAST((qm.totalpn_amount + qm.freight) - (qm.totalpn_amount * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2))) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2))) "
	        + "END AS ptd, "		
			+ "			   CAST(ROUND((qm.totalpn_amount + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) AS tax_value,\r\n"
			+ "			   CAST(ROUND((qm.totalpn_amount + qm.freight) + ((qm.totalpn_amount + qm.freight) * (tm.tax_per / 100)), 2) AS DECIMAL(10, 2)) AS total_after_tax,\r\n"
			+ "			   CASE \r\n"
			+ "			       WHEN cm.taxable = 'no' THEN \r\n"
			+ "			           0.0\r\n"
			+ "			       WHEN CAST((qm.totalpn_amount) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2)) > 0.00 THEN \r\n"
			+ "			           CAST((CAST((qm.totalpn_amount) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2))) * (tm.tax_per / 100) AS DECIMAL(10, 2))\r\n"
			+ "			       ELSE 0.0 \r\n"
			+ "			   END AS adv_tax,\r\n"
			+ "			   CONCAT(dbo.NumberToRupeesWords(CAST(ROUND((qm.totalpn_amount + qm.freight) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2))), ' Only') AS tax_value_in_words\r\n"
			+ "			FROM\r\n"
			+ "			   CONTRACT_MASTER cm\r\n"
			+ "			INNER JOIN\r\n"
			+ "			   CONTRACT_ASSIGN_TAX cat \r\n"
			+ "			       ON cat.contract_id = cm.contract_id\r\n"
			+ "			INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id\r\n"
			+ "			INNER JOIN DEBITCREDIT_PACKINGNOTE qm ON qm.con_id = :contract_id AND qm.dcPn_no = :DcPn_no\r\n"
			+ "			INNER JOIN DEBITCREDIT_PACKINGNOTE_ITEMS qim on qim.dc_pn_id = qm.dc_pn_id\r\n"
			+ "			WHERE\r\n"
			+ "			   cm.contract_id = :contract_id \r\n"
			+ "			   AND  (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%')\r\n"
			+ "			GROUP BY\r\n"
			+ "			   tm.tax_id,\r\n"
			+ "			   tm.tax_name,\r\n"
			+ "			   tm.tax_per,\r\n"
			+ "			   qm.totalpn_amount,\r\n"
			+ "			   qm.freight,\r\n"
			+ "			   cm.taxable", nativeQuery = true)
	List<TaxMasterInterface> searchdbtcrdTaxId(String contract_id, String factory_id, String DcPn_no);
	
	
	@Query(value = "SELECT\r\n"
	        + "    tm.tax_id,\r\n"
	        + "    tm.tax_name,\r\n"
	        + "    tm.tax_per,\r\n"
	        + "    qam.grand_total,\r\n"
	        + "    0 AS zero_value,\r\n"
	        + "    SUM(qaim.pices) AS total_pices,\r\n"
	        + "    SUM(qaim.qty) AS total_qty,\r\n"
	        + "    SUM(qaim.per_kgs) AS total_kgs,\r\n"
	        + "    MAX(cm.percentage_value) AS percentage_value,\r\n"
	        + "    qam.grand_total AS basic_value,\r\n"
	        + "    CASE \r\n"
	        + "        WHEN cm.taxable = 'yes' THEN \r\n"
	        + "            CAST(qam.grand_total * (MAX(cm.percentage_value) / 100.0) AS DECIMAL(10, 2))\r\n"
	        + "        ELSE 0.0 \r\n"
	        + "    END AS taxable,\r\n"
	        + "    CASE \r\n"
	        + "        WHEN cm.taxable = 'no' THEN \r\n"
	        + "            CAST(qam.grand_total * (MAX(cm.percentage_value) / 100.0) AS DECIMAL(10, 2))\r\n"
	        + "        ELSE 0.0 \r\n"
	        + "    END AS non_taxable,\r\n"
	        + "    CAST(qam.grand_total - (qam.grand_total * (MAX(cm.percentage_value) / 100.0)) AS DECIMAL(10, 2)) AS optd,\r\n"
	        + "    CAST(qam.grand_total - (qam.grand_total * (MAX(cm.percentage_value) / 100.0)) AS DECIMAL(10, 2)) AS optc,\r\n"
	        + "    CAST(ROUND(qam.grand_total * (tm.tax_per / 100.0), 0) AS DECIMAL(10, 2)) AS tax_value,\r\n"
	        + "    CAST(ROUND(qam.grand_total + (qam.grand_total * (tm.tax_per / 100.0)), 2) AS DECIMAL(10, 2)) AS total_after_tax,\r\n"
	        + "    CAST(ROUND((qam.grand_total - (qam.grand_total * (MAX(cm.percentage_value) / 100.0))) * (tm.tax_per / 100.0), 0) AS DECIMAL(10, 2)) AS ptc,\r\n"
	        + "    CAST(ROUND((qam.grand_total - (qam.grand_total * (MAX(cm.percentage_value) / 100.0))) * (tm.tax_per / 100.0), 0) AS DECIMAL(10, 2)) AS ptd,\r\n"
	        + "    CASE \r\n"
	        + "        WHEN cm.taxable = 'no' THEN 0.0\r\n"
	        + "        WHEN qam.grand_total * (MAX(cm.percentage_value) / 100.0) > 0 THEN \r\n"
	        + "            CAST((qam.grand_total * (MAX(cm.percentage_value) / 100.0)) * (tm.tax_per / 100.0) AS DECIMAL(10, 2))\r\n"
	        + "        ELSE 0.0 \r\n"
	        + "    END AS adv_tax,\r\n"
	        + "    CONCAT(dbo.NumberToRupeesWords(CAST(ROUND(qam.grand_total * (tm.tax_per / 100.0), 0) AS DECIMAL(10, 2))), ' Only') AS tax_value_in_words\r\n"
	        + "FROM CONTRACT_MASTER cm\r\n"
	        + "INNER JOIN CONTRACT_ASSIGN_TAX cat ON cat.contract_id = cm.contract_id\r\n"
	        + "INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id\r\n"
	        + "INNER JOIN (\r\n"
	        + "    SELECT pn.contract_id, pn.pn_id, pn.grand_total\r\n"
	        + "    FROM QSADVANCE_PACKINGNOTE_MASTER pn\r\n"
	        + "    WHERE pn.pn_id = :pn_id\r\n"
	        + "      AND pn.is_delete = 0\r\n"
	        + ") qam ON qam.contract_id = cm.contract_id\r\n"
	        + "INNER JOIN QSADVANCE_PACKINGNOTEITEM_MASTER qaim ON qaim.pn_id = qam.pn_id\r\n"
	        + "WHERE cm.contract_id = :contract_id\r\n"
	        + "  AND cm.factory_id = :factory_id\r\n"
	        + "GROUP BY tm.tax_id, tm.tax_name, tm.tax_per, cm.taxable, qam.grand_total",
	        nativeQuery = true)
	List<TaxMasterInterface> searchadvanceTaxId(String contract_id, String factory_id, String pn_id);
	
	@Query(value = "WITH BaseValue AS (\r\n"
			+ "    SELECT \r\n"
			+ "        SUM(qm.grand_total + qm.freight) AS base_value\r\n"
			+ "    FROM \r\n"
			+ "        QSPACKING_MASTER qm\r\n"
			+ "    WHERE \r\n"
			+ "        qm.con_id = :contract_id\r\n"
			+ "        AND qm.load_id = :load_id\r\n"
			+ "        AND qm.Cancel = 0\r\n"
			+ "),\r\n"
			+ "TotalAfterTax AS (\r\n"
			+ "    SELECT \r\n"
			+ "        tm.tax_id,\r\n"
			+ "        tm.tax_name,\r\n"
			+ "        tm.tax_per,\r\n"
			+ "        qm.grand_total,\r\n"
			+ "        qm.freight,\r\n"
			+ "        CAST(((qm.grand_total + qm.freight) * (tm.tax_per / 100)) AS DECIMAL(10,2)) AS tax_value\r\n"
			+ "    FROM CONTRACT_MASTER cm\r\n"
			+ "    INNER JOIN CONTRACT_ASSIGN_TAX cat \r\n"
			+ "        ON cat.contract_id = cm.contract_id\r\n"
			+ "    INNER JOIN TAX_MASTER tm \r\n"
			+ "        ON tm.tax_id = cat.tax_id\r\n"
			+ "    INNER JOIN QSPACKING_MASTER qm \r\n"
			+ "        ON qm.con_id = :contract_id\r\n"
			+ "        AND qm.load_id = :load_id\r\n"
			+ "    WHERE\r\n"
			+ "        cm.contract_id = :contract_id\r\n"
			+ "        AND (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%')\r\n"
			+ "        AND qm.Cancel = 0\r\n"
			+ ")\r\n"
			+ "SELECT \r\n"
			+ "    CAST(ROUND((SELECT base_value FROM BaseValue),0) AS DECIMAL(10,2)) +\r\n"
			+ "    CAST(ROUND(SUM(tax_value),0) AS DECIMAL(10,2)) AS final_value\r\n"
			+ "FROM TotalAfterTax",
			nativeQuery = true)
	String getTotalValue(String contract_id, String factory_id, String load_id);
	
	@Query(value = "WITH BaseValue AS ( \r\n"
		    + "    SELECT (qcpm.grand_total + 0) AS base_value \r\n"
		    + "    FROM QSCHALLAN_PACKINGNOTE_MASTER qcpm \r\n"
		    + "    WHERE qcpm.contract_id = :contract_id \r\n"
		    + "      AND qcpm.load_id = :load_id \r\n"
		    + "), \r\n"
		    + "TotalAfterTax AS ( \r\n"
		    + "    SELECT \r\n"
		    + "        tm.tax_id, \r\n"
		    + "        tm.tax_name, \r\n"
		    + "        tm.tax_per, \r\n"
		    + "        qcpm.grand_total, \r\n"
		    + "        CAST(((qcpm.grand_total + 0) * (tm.tax_per / 100)) AS DECIMAL(10,2)) AS tax_value \r\n"
		    + "    FROM CONTRACT_MASTER cm \r\n"
		    + "    INNER JOIN CONTRACT_ASSIGN_TAX cat ON cat.contract_id = cm.contract_id \r\n"
		    + "    INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id \r\n"
		    + "    INNER JOIN QSCHALLAN_PACKINGNOTE_MASTER qcpm \r\n"
		    + "        ON qcpm.contract_id = :contract_id \r\n"
		    + "       AND qcpm.load_id = :load_id \r\n"
		    + "    WHERE cm.contract_id = :contract_id \r\n"
		    + "      AND (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%') \r\n"
		    + ") \r\n"
		    + "SELECT \r\n"
		    + "    CAST(ROUND((SELECT base_value FROM BaseValue), 0) AS DECIMAL(10,2)) + \r\n"
		    + "    CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10,2)) AS final_value \r\n"
		    + "FROM TotalAfterTax",
		    nativeQuery = true)
	String getdlyTotalValue(String contract_id, String factory_id, String load_id);
	
	@Query(value = "WITH BaseValue AS ( \r\n"
	        + "    SELECT  \r\n"
	        + "        CAST(qam.grand_total AS DECIMAL(10, 2)) AS base_value, \r\n"
	        + "        qam.contract_id \r\n"
	        + "    FROM QSADVANCE_PACKINGNOTE_MASTER qam \r\n"
	        + "    WHERE qam.pn_id = :pn_id \r\n"
	        + "), \r\n"
	        + "AvailableTaxes AS ( \r\n"
	        + "    SELECT  \r\n"
	        + "        tm.tax_id, \r\n"
	        + "        tm.tax_name, \r\n"
	        + "        tm.tax_per, \r\n"
	        + "        bv.base_value \r\n"
	        + "    FROM BaseValue bv \r\n"
	        + "    INNER JOIN CONTRACT_MASTER cm \r\n"
	        + "        ON cm.contract_id = bv.contract_id \r\n"
	        + "    INNER JOIN CONTRACT_ASSIGN_TAX cat \r\n"
	        + "        ON cat.contract_id = cm.contract_id \r\n"
	        + "    INNER JOIN TAX_MASTER tm \r\n"
	        + "        ON tm.tax_id = cat.tax_id \r\n"
	        + "    WHERE (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' \r\n"
	        + "           LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%') \r\n"
	        + "), \r\n"
	        + "TotalAfterTax AS ( \r\n"
	        + "    SELECT  \r\n"
	        + "        tax_id, \r\n"
	        + "        tax_name, \r\n"
	        + "        tax_per, \r\n"
	        + "        base_value, \r\n"
	        + "        CAST((base_value * (tax_per / 100.0)) AS DECIMAL(10, 2)) AS tax_value \r\n"
	        + "    FROM AvailableTaxes \r\n"
	        + ") \r\n"
	        + "SELECT \r\n"
	        + "    CAST(ROUND(b.base_value, 0) AS DECIMAL(10, 2)) + \r\n"
	        + "    CAST(ROUND(ISNULL(SUM(t.tax_value), 0), 0) AS DECIMAL(10, 2)) AS final_value \r\n"
	        + "FROM BaseValue b \r\n"
	        + "LEFT JOIN TotalAfterTax t ON 1 = 1 \r\n"
	        + "GROUP BY b.base_value",
	        nativeQuery = true)
	String getadvTotalValue(String pn_id, String factory_id);
	
	@Query(value = "WITH BaseValue AS (\r\n"
			+ "		  SELECT \r\n"
			+ "		      (qm.totalpn_amount + qm.freight) AS base_value\r\n"
			+ "		  FROM \r\n"
			+ "		      DEBITCREDIT_PACKINGNOTE qm\r\n"
			+ "		  WHERE \r\n"
			+ "		      qm.con_id = :contract_id\r\n"
			+ "		      AND qm.dcPn_no = :DcPn_no\r\n"
			+ "		),\r\n"
			+ "		TotalAfterTax AS (   \r\n"
			+ "		  SELECT \r\n"
			+ "		      tm.tax_id,\r\n"
			+ "		      tm.tax_name,\r\n"
			+ "		      tm.tax_per, \r\n"
			+ "		      qm.totalpn_amount,\r\n"
			+ "		      qm.freight,\r\n"
			+ "		      -- Calculate the tax value based on grand_total + freight\r\n"
			+ "		      CAST(((qm.totalpn_amount + qm.freight) * (tm.tax_per / 100)) AS DECIMAL(10, 2)) AS tax_value\r\n"
			+ "		  FROM\r\n"
			+ "		      CONTRACT_MASTER cm          \r\n"
			+ "		  INNER JOIN\r\n"
			+ "		      CONTRACT_ASSIGN_TAX cat \r\n"
			+ "		          ON cat.contract_id = cm.contract_id          \r\n"
			+ "		  INNER JOIN\r\n"
			+ "		      TAX_MASTER tm \r\n"
			+ "		          ON tm.tax_id = cat.tax_id       \r\n"
			+ "		  INNER JOIN\r\n"
			+ "		      DEBITCREDIT_PACKINGNOTE qm \r\n"
			+ "		          ON qm.con_id = :contract_id\r\n"
			+ "		          AND qm.dcPn_no = :DcPn_no\r\n"
			+ "		  WHERE\r\n"
			+ "		      cm.contract_id =  :contract_id    \r\n"
			+ "		      AND (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%')\r\n"
			+ "		)\r\n"
			+ "		-- Final SELECT: Combine base_value and total tax (grand_total_after_tax) into one column\r\n"
			+ "		SELECT \r\n"
			+ "		  CAST(ROUND((SELECT base_value FROM BaseValue), 0) AS DECIMAL(10, 2)) + \r\n"
			+ "		  CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS final_value\r\n"
			+ "		FROM TotalAfterTax;", nativeQuery = true)
	String getdbtcrdTotalValue(String contract_id, String factory_id, String DcPn_no);
	
	@Query(value = "WITH BaseValue AS ( SELECT (qm.grand_total + qm.freight) AS base_value, qm.grand_total as totalamount,\r\n"
			+ "					cc.percentage_value as openbal FROM QSPACKING_MASTER qm\r\n"
			+ "				inner join CONTRACT_MASTER cc on cc.contract_id = qm.con_id\r\n"
			+ "			    WHERE qm.con_id = :contract_id AND qm.load_id = :load_id  and qm.factory_id=:factory_id  and qm.Cancel=0\r\n"
			+ "			),\r\n"
			+ "			TotalAfterTax AS (   \r\n"
			+ "			    SELECT tm.tax_id, tm.tax_name, tm.tax_per, qm.grand_total, qm.freight,\r\n"
			+ "			        CAST(((qm.grand_total + qm.freight) * (tm.tax_per / 100)) AS DECIMAL(10, 2)) AS tax_value\r\n"
			+ "			    FROM CONTRACT_MASTER cm          \r\n"
			+ "			    INNER JOIN CONTRACT_ASSIGN_TAX cat ON cat.contract_id = cm.contract_id          \r\n"
			+ "			    INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id       \r\n"
			+ "			    INNER JOIN QSPACKING_MASTER qm ON qm.con_id = :contract_id AND qm.load_id = :load_id  and qm.factory_id=:factory_id     \r\n"
			+ "			    WHERE cm.contract_id = :contract_id AND (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%')  and qm.Cancel=0\r\n"
			+ "			)\r\n"
			+ "			SELECT \r\n"
			+ "			    CAST(ROUND((SELECT base_value FROM BaseValue), 0) AS DECIMAL(10, 2)) + \r\n"
			+ "			    CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS final_value,\r\n"
			+ "				CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS Tax_Final_total,\r\n"
			+ "				CAST(ROUND(\r\n"
			+ "				(CAST(ROUND((SELECT totalamount FROM BaseValue), 0) AS DECIMAL(10, 2)) * \r\n"
			+ "				(CAST(ROUND((SELECT openbal FROM BaseValue), 0) AS DECIMAL(10, 2)) / 100)), 0) AS DECIMAL(10, 2)) AS taxable_final_total\r\n"
			+ "			FROM TotalAfterTax;", nativeQuery = true)
	List<String> getTotalValueList(String contract_id, String factory_id, String load_id);
	
	@Query(value = "WITH BaseValue AS ( SELECT (qm.grand_total + qm.freight) AS base_value, qm.grand_total as totalamount,\r\n"
			+ "					cc.percentage_value as openbal FROM QSPACKING_MASTER qm\r\n"
			+ "				inner join CONTRACT_MASTER cc on cc.contract_id = qm.con_id\r\n"
			+ "			    WHERE qm.con_id = :contract_id AND qm.load_id = :load_id  and qm.factory_id = :factory_id and qm.Cancel=1\r\n"
			+ "			),\r\n"
			+ "			TotalAfterTax AS (   \r\n"
			+ "			    SELECT tm.tax_id, tm.tax_name, tm.tax_per, qm.grand_total, qm.freight,\r\n"
			+ "			        CAST(((qm.grand_total + qm.freight) * (tm.tax_per / 100)) AS DECIMAL(10, 2)) AS tax_value\r\n"
			+ "			    FROM CONTRACT_MASTER cm          \r\n"
			+ "			    INNER JOIN CONTRACT_ASSIGN_TAX cat ON cat.contract_id = cm.contract_id          \r\n"
			+ "			    INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id       \r\n"
			+ "			    INNER JOIN QSPACKING_MASTER qm ON qm.con_id = :contract_id AND qm.load_id = :load_id    and qm.factory_id = :factory_id \r\n"
			+ "			    WHERE cm.contract_id = :contract_id AND (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%')  and qm.Cancel=1\r\n"
			+ "			)\r\n"
			+ "			SELECT \r\n"
			+ "			    CAST(ROUND((SELECT base_value FROM BaseValue), 0) AS DECIMAL(10, 2)) + \r\n"
			+ "			    CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS final_value,\r\n"
			+ "				CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS Tax_Final_total,\r\n"
			+ "				CAST(ROUND(\r\n"
			+ "				(CAST(ROUND((SELECT totalamount FROM BaseValue), 0) AS DECIMAL(10, 2)) * \r\n"
			+ "				(CAST(ROUND((SELECT openbal FROM BaseValue), 0) AS DECIMAL(10, 2)) / 100)), 0) AS DECIMAL(10, 2)) AS taxable_final_total\r\n"
			+ "			FROM TotalAfterTax;", nativeQuery = true)
	List<String> getTotalValueListcancel(String contract_id, String factory_id, String load_id);
	
	@Query(value = "WITH BaseValue AS ( SELECT (qm.grand_total +0) AS base_value, qm.grand_total as totalamount,\r\n"
			+ "					cc.percentage_value as openbal FROM QSCHALLAN_PACKINGNOTE_MASTER qm\r\n"
			+ "				inner join CONTRACT_MASTER cc on cc.contract_id = qm.contract_id\r\n"
			+ "			    WHERE qm.contract_id = :contract_id AND qm.load_id = :load_id\r\n"
			+ "			),\r\n"
			+ "			TotalAfterTax AS (   \r\n"
			+ "			    SELECT tm.tax_id, tm.tax_name, tm.tax_per, qm.grand_total,\r\n"
			+ "			        CAST(((qm.grand_total + 0) * (tm.tax_per / 100)) AS DECIMAL(10, 2)) AS tax_value\r\n"
			+ "			    FROM CONTRACT_MASTER cm          \r\n"
			+ "			    INNER JOIN CONTRACT_ASSIGN_TAX cat ON cat.contract_id = cm.contract_id          \r\n"
			+ "			    INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id       \r\n"
			+ "			    INNER JOIN QSCHALLAN_PACKINGNOTE_MASTER qm ON qm.contract_id = :contract_id AND qm.load_id = :load_id      \r\n"
			+ "			    WHERE cm.contract_id = :contract_id AND (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%')\r\n"
			+ "			)\r\n"
			+ "			SELECT \r\n"
			+ "			    CAST(ROUND((SELECT base_value FROM BaseValue), 0) AS DECIMAL(10, 2)) + \r\n"
			+ "			    CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS final_value,\r\n"
			+ "				CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS Tax_Final_total,\r\n"
			+ "				CAST(ROUND(\r\n"
			+ "				(CAST(ROUND((SELECT totalamount FROM BaseValue), 0) AS DECIMAL(10, 2)) * \r\n"
			+ "				(CAST(ROUND((SELECT openbal FROM BaseValue), 0) AS DECIMAL(10, 2)) / 100)), 0) AS DECIMAL(10, 2)) AS taxable_final_total\r\n"
			+ "			FROM TotalAfterTax;", nativeQuery = true)
	List<String> getdlyTotalValueList(String contract_id, String factory_id, String load_id);
	
	@Query(value = "WITH BaseValue AS ("
	        + "    SELECT "
	        + "        (qam.grand_total + 0) AS base_value, "
	        + "        qam.grand_total AS totalamount, "
	        + "        cc.percentage_value AS openbal "
	        + "    FROM "
	        + "        QSADVANCE_PACKINGNOTE_MASTER qam "
	        + "    INNER JOIN "
	        + "        CONTRACT_MASTER cc ON cc.contract_id = qam.contract_id "
	        + "    WHERE "
	        + "        qam.contract_id = :contract_id AND qam.load_id = :load_id "
	        + "), "
	        + "TotalAfterTax AS ("
	        + "    SELECT "
	        + "        tm.tax_id, "
	        + "        tm.tax_name, "
	        + "        tm.tax_per, "
	        + "        qam.grand_total, "
	        + "        CAST(((qam.grand_total + 0) * (tm.tax_per / 100.0)) AS DECIMAL(10, 2)) AS tax_value "
	        + "    FROM "
	        + "        CONTRACT_MASTER cm "
	        + "    INNER JOIN "
	        + "        CONTRACT_ASSIGN_TAX cat ON cat.contract_id = cm.contract_id "
	        + "    INNER JOIN "
	        + "        TAX_MASTER tm ON tm.tax_id = cat.tax_id "
	        + "    INNER JOIN "
	        + "        QSADVANCE_PACKINGNOTE_MASTER qam ON qam.contract_id = :contract_id "
	        + "    WHERE "
	        + "        cm.contract_id = :contract_id "
	        + "        AND qam.load_id = :load_id "
	        + "        AND (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%') "
	        + ") "
	        + "SELECT "
	        + "    CAST(ROUND((SELECT base_value FROM BaseValue), 0) AS DECIMAL(10, 2)) + "
	        + "    CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS final_value, "
	        + "    CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS Tax_Final_total, "
	        + "    CAST(ROUND( "
	        + "        (CAST(ROUND((SELECT totalamount FROM BaseValue), 0) AS DECIMAL(10, 2)) * "
	        + "         (CAST(ROUND((SELECT openbal FROM BaseValue), 0) AS DECIMAL(10, 2)) / 100.0)) "
	        + "    , 0) AS DECIMAL(10, 2)) AS taxable_final_total "
	        + "FROM "
	        + "    TotalAfterTax;", nativeQuery = true)
	List<String> getadvTotalValueList(String contract_id, String factory_id, String load_id);
	
	@Query(value = " WITH BaseValue AS ( SELECT (qm.totalpn_amount + qm.freight) AS base_value, qm.totalpn_amount as totalamount,\r\n"
			+ "							cc.percentage_value as openbal FROM DEBITCREDIT_PACKINGNOTE qm\r\n"
			+ "						inner join CONTRACT_MASTER cc on cc.contract_id = qm.con_id\r\n"
			+ "					    WHERE qm.con_id = :contract_id AND qm.dcPn_no = :load_id\r\n"
			+ "					),\r\n"
			+ "					TotalAfterTax AS (   \r\n"
			+ "					    SELECT tm.tax_id, tm.tax_name, tm.tax_per, qm.totalpn_amount, qm.freight,\r\n"
			+ "					        CAST(((qm.totalpn_amount + qm.freight) * (tm.tax_per / 100)) AS DECIMAL(10, 2)) AS tax_value\r\n"
			+ "					    FROM CONTRACT_MASTER cm          \r\n"
			+ "					    INNER JOIN CONTRACT_ASSIGN_TAX cat ON cat.contract_id = cm.contract_id          \r\n"
			+ "					    INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id       \r\n"
			+ "					    INNER JOIN DEBITCREDIT_PACKINGNOTE qm ON qm.con_id = :contract_id AND qm.dcPn_no = :load_id      \r\n"
			+ "					    WHERE cm.contract_id = :contract_id AND  (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%')\r\n"
			+ "					)\r\n"
			+ "					SELECT \r\n"
			+ "					    CAST(ROUND((SELECT base_value FROM BaseValue), 0) AS DECIMAL(10, 2)) + \r\n"
			+ "					    CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS final_value,\r\n"
			+ "						CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS Tax_Final_total,\r\n"
			+ "						CAST(ROUND(\r\n"
			+ "						(CAST(ROUND((SELECT totalamount FROM BaseValue), 0) AS DECIMAL(10, 2)) * \r\n"
			+ "						(CAST(ROUND((SELECT openbal FROM BaseValue), 0) AS DECIMAL(10, 2)) / 100)), 0) AS DECIMAL(10, 2)) AS taxable_final_total\r\n"
			+ "					FROM TotalAfterTax;", nativeQuery = true)
	List<String> getdbtcrdTotalValueList(String contract_id, String factory_id, String load_id);

	@Query(value=" select concat(dbo.NumberToRupeesWords(:grandTotalValue),' Only')", nativeQuery = true)
	String getgrandTotalInWordsQuery(String grandTotalValue);

	@Transactional
	@Modifying
	@Query(value="UPDATE MILESTONE_ASSGIN_CONTRACT_MASTER SET milestone_id =:milestone_id,contract_id=:contract_id,modified_by=:modified_by,modified_date=GETDATE() where id = :id", nativeQuery = true)
	int updateMileStoneToContract(String milestone_id, String contract_id, String modified_by, String id);

	@Query(value="select macm.id,mm.milestone_id, CONCAT(mm.milestone_code, ' - ', mm.milestone_name) AS milestone_name from MILESTONE_ASSGIN_CONTRACT_MASTER macm\r\n"
			+ "inner join MILESTONE_MASTER mm on mm.milestone_id = macm.milestone_id where macm.id = :id", nativeQuery = true)
	MileStoneAssignedContractListInterfaces searchMileStoneToContract(String id);

	@Query(value="select count(*) from QSPACKING_MASTER where con_id = :contract_id  and milestone_id = :milestone_id", nativeQuery = true)
	int MilestoneUsedinQSPackingCombineOfContractorAndMilestoneId(String contract_id, String milestone_id);

	@Query(value="select count(*) from CONTRACT_ASSIGN_TAX where contract_id = :contract_id and tax_id = :taxValue ", nativeQuery = true)
	int checkTaxExitForContractor(int contract_id, Integer taxValue);
	
	@Query(value = "SELECT\r\n"
			+ "			    tm.tax_id,\r\n"
			+ "			    tm.tax_name,\r\n"
			+ "			    tm.tax_per,\r\n"
			+ "			    qm.grand_total,\r\n"
			+ "			    SUM(qim.qty) AS total_qty,\r\n"
			+ "				SUM(qim.per_kgs) As total_kgs,\r\n"
			+ "			    MAX(cm.percentage_value) AS percentage_value,\r\n"
			+ "			    (qm.grand_total) AS basic_value,\r\n"
			+ "			    CASE \r\n"
			+ "			        WHEN cm.taxable = 'yes' THEN \r\n"
			+ "			            CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2))\r\n"
			+ "			        ELSE 0.0 \r\n"
			+ "			    END AS taxable,\r\n"
			+ "			    CASE \r\n"
			+ "			        WHEN cm.taxable = 'no' THEN \r\n"
			+ "			            CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2))\r\n"
			+ "			        ELSE 0.0 \r\n"
			+ "			    END AS non_taxable,\r\n"
			+ "			    SUM(CAST(ROUND((qm.grand_total) * (tm.tax_per / 100), 0) AS DECIMAL(18,0))) OVER() AS optd,\r\n"
			+ "			    ABS(CAST((qm.grand_total)- (qm.grand_total * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100))AS DECIMAL(10, 2))) AS optc,\r\n"
			+ "			    CAST(ROUND((qm.grand_total) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) AS tax_value,\r\n"
			+ "			    CAST(ROUND((qm.grand_total) + ((qm.grand_total) * (tm.tax_per / 100)), 2) AS DECIMAL(10, 2)) AS total_after_tax,\r\n"
			+ "			    ABS(CAST(ROUND((CAST((qm.grand_total)- (qm.grand_total * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100))AS DECIMAL(10, 2))) * (tm.tax_per / 100),0)AS DECIMAL(10, 2))) AS ptc,\r\n"
			+ "			    ABS(CAST(ROUND((CAST((qm.grand_total) - (qm.grand_total * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100))AS DECIMAL(10, 2))) * (tm.tax_per / 100),0)AS DECIMAL(10, 2))) AS ptd,\r\n"
			+ "			    CASE \r\n"
			+ "			        WHEN cm.taxable = 'no' THEN \r\n"
			+ "			            0.0\r\n"
			+ "			        WHEN CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2)) > 0.00 THEN \r\n"
			+ "			            CAST((CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2))) * (tm.tax_per / 100) AS DECIMAL(10, 2))\r\n"
			+ "			        ELSE 0.0 \r\n"
			+ "			    END AS adv_tax,\r\n"
			+ "			    CONCAT(dbo.NumberToRupeesWords(CAST(ROUND((qm.grand_total) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2))), ' Only') AS tax_value_in_words\r\n"
			+ "			FROM\r\n"
			+ "			    CONTRACT_MASTER cm\r\n"
			+ "			INNER JOIN\r\n"
			+ "			    CONTRACT_ASSIGN_TAX cat \r\n"
			+ "			        ON cat.contract_id = cm.contract_id\r\n"
			+ "			INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id\r\n"
			+ "			INNER JOIN CONSOLIDATED_PACKING_NOTE qm ON qm.con_id = :contract_id AND qm.load_id = :load_id\r\n"
			+ "			INNER JOIN CONSOLIDATED_PACKING_ITEMS qim on qim.Conpn_id = qm.Conpn_id\r\n"
			+ "			WHERE\r\n"
			+ "			    cm.contract_id = :contract_id \r\n"
			+ "			    AND  (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%') and qm.Cancel =0\r\n"
			+ "			GROUP BY\r\n"
			+ "			    tm.tax_id,\r\n"
			+ "			    tm.tax_name,\r\n"
			+ "			    tm.tax_per,\r\n"
			+ "			    qm.grand_total,\r\n"
			+ "			    cm.taxable\r\n"
			+ "	", nativeQuery = true)
	List<TaxMasterInterface> searchconsolidatedTaxId(String contract_id, String factory_id,String load_id);
	
	@Query(value = "WITH BaseValue AS (\r\n"
			+ "			    SELECT \r\n"
			+ "			        (qm.grand_total) AS base_value\r\n"
			+ "			    FROM \r\n"
			+ "			        CONSOLIDATED_PACKING_NOTE qm\r\n"
			+ "			    WHERE \r\n"
			+ "			        qm.con_id = :contract_id\r\n"
			+ "			        AND qm.load_id = :load_id  and qm.Cancel =0 \r\n"
			+ "			),\r\n"
			+ "			TotalAfterTax AS (   \r\n"
			+ "			    SELECT \r\n"
			+ "			        tm.tax_id,\r\n"
			+ "			        tm.tax_name,\r\n"
			+ "			        tm.tax_per, \r\n"
			+ "			        qm.grand_total,\r\n"
			+ "			        \r\n"
			+ "			        -- Calculate the tax value based on grand_total\r\n"
			+ "			        CAST(((qm.grand_total) * (tm.tax_per / 100)) AS DECIMAL(10, 2)) AS tax_value\r\n"
			+ "			    FROM\r\n"
			+ "			        CONTRACT_MASTER cm          \r\n"
			+ "			    INNER JOIN\r\n"
			+ "			        CONTRACT_ASSIGN_TAX cat \r\n"
			+ "			            ON cat.contract_id = cm.contract_id          \r\n"
			+ "			    INNER JOIN\r\n"
			+ "			        TAX_MASTER tm \r\n"
			+ "			            ON tm.tax_id = cat.tax_id       \r\n"
			+ "			    INNER JOIN\r\n"
			+ "			        CONSOLIDATED_PACKING_NOTE qm \r\n"
			+ "			            ON qm.con_id = :contract_id\r\n"
			+ "			            AND qm.load_id = :load_id      \r\n"
			+ "			    WHERE\r\n"
			+ "			        cm.contract_id = :contract_id      \r\n"
			+ "			        AND  (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%') and qm.Cancel =0\r\n"
			+ "			)\r\n"
			+ "			-- Final SELECT: Combine base_value and total tax (grand_total_after_tax) into one column\r\n"
			+ "			SELECT \r\n"
			+ "			    CAST(ROUND((SELECT base_value FROM BaseValue), 0) AS DECIMAL(10, 2)) + \r\n"
			+ "			    CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS final_value\r\n"
			+ "			FROM TotalAfterTax;", nativeQuery = true)
	String getconsolidatedTotalValue(String contract_id, String factory_id, String load_id);
	
	
	@Query(value = "WITH BaseValue AS ( SELECT (qm.grand_total) AS base_value, qm.grand_total as totalamount,\r\n"
			+ "								cc.percentage_value as openbal FROM CONSOLIDATED_PACKING_NOTE qm\r\n"
			+ "							inner join CONTRACT_MASTER cc on cc.contract_id = qm.con_id\r\n"
			+ "						    WHERE qm.con_id = :contract_id AND qm.load_id = :load_id  and qm.Cancel=0\r\n"
			+ "						),\r\n"
			+ "						TotalAfterTax AS (   \r\n"
			+ "						    SELECT tm.tax_id, tm.tax_name, tm.tax_per, qm.grand_total,\r\n"
			+ "						        CAST(((qm.grand_total) * (tm.tax_per / 100)) AS DECIMAL(10, 2)) AS tax_value\r\n"
			+ "						    FROM CONTRACT_MASTER cm          \r\n"
			+ "						    INNER JOIN CONTRACT_ASSIGN_TAX cat ON cat.contract_id = cm.contract_id          \r\n"
			+ "						    INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id       \r\n"
			+ "						    INNER JOIN CONSOLIDATED_PACKING_NOTE qm ON qm.con_id = :contract_id AND qm.load_id = :load_id      \r\n"
			+ "						    WHERE cm.contract_id = :contract_id AND (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%')  and qm.Cancel=0\r\n"
			+ "						)\r\n"
			+ "						SELECT \r\n"
			+ "						    CAST(ROUND((SELECT base_value FROM BaseValue), 0) AS DECIMAL(10, 2)) + \r\n"
			+ "						    CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS final_value,\r\n"
			+ "							CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS Tax_Final_total,\r\n"
			+ "							CAST(ROUND(\r\n"
			+ "							(CAST(ROUND((SELECT totalamount FROM BaseValue), 0) AS DECIMAL(10, 2)) * \r\n"
			+ "							(CAST(ROUND((SELECT openbal FROM BaseValue), 0) AS DECIMAL(10, 2)) / 100)), 0) AS DECIMAL(10, 2)) AS taxable_final_total\r\n"
			+ "						FROM TotalAfterTax;", nativeQuery = true)
	List<String> getTotalconsolidatedValueList(String contract_id, String factory_id, String load_id);
	
	@Query(value = "SELECT\r\n"
			+ "			    tm.tax_id,\r\n"
			+ "			    tm.tax_name,\r\n"
			+ "			    tm.tax_per,\r\n"
			+ "			    qm.grand_total,\r\n"
			+ "			    SUM(qim.qty) AS total_qty,\r\n"
			+ "				SUM(qim.per_kgs) As total_kgs,\r\n"
			+ "			    MAX(cm.percentage_value) AS percentage_value,\r\n"
			+ "			    (qm.grand_total) AS basic_value,\r\n"
			+ "			    CASE \r\n"
			+ "			        WHEN cm.taxable = 'yes' THEN \r\n"
			+ "			            CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2))\r\n"
			+ "			        ELSE 0.0 \r\n"
			+ "			    END AS taxable,\r\n"
			+ "			    CASE \r\n"
			+ "			        WHEN cm.taxable = 'no' THEN \r\n"
			+ "			            CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2))\r\n"
			+ "			        ELSE 0.0 \r\n"
			+ "			    END AS non_taxable,\r\n"
			+ "			    SUM(CAST(ROUND((qm.grand_total) * (tm.tax_per / 100), 0) AS DECIMAL(18,0))) OVER() AS optd,\r\n"
			+ "			    CAST((qm.grand_total) - ((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2)) AS optc,\r\n"
			+ "			    CAST(ROUND((qm.grand_total) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) AS tax_value,\r\n"
			+ "			    CAST(ROUND((qm.grand_total) + ((qm.grand_total) * (tm.tax_per / 100)), 2) AS DECIMAL(10, 2)) AS total_after_tax,\r\n"
			+ "			    CAST(ROUND((CAST((qm.grand_total) - ((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2))) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) AS ptc,\r\n"
			+ "			    CAST(ROUND((CAST((qm.grand_total) - ((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100)) AS DECIMAL(10, 2))) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2)) AS ptd,\r\n"
			+ "			    CASE \r\n"
			+ "			        WHEN cm.taxable = 'no' THEN \r\n"
			+ "			            0.0\r\n"
			+ "			        WHEN CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2)) > 0.00 THEN \r\n"
			+ "			            CAST((CAST((qm.grand_total) * (CAST(MAX(cm.percentage_value) AS DECIMAL(10, 2)) / 100) AS DECIMAL(10, 2))) * (tm.tax_per / 100) AS DECIMAL(10, 2))\r\n"
			+ "			        ELSE 0.0 \r\n"
			+ "			    END AS adv_tax,\r\n"
			+ "			    CONCAT(dbo.NumberToRupeesWords(CAST(ROUND((qm.grand_total) * (tm.tax_per / 100), 0) AS DECIMAL(10, 2))), ' Only') AS tax_value_in_words\r\n"
			+ "			FROM\r\n"
			+ "			    CONTRACT_MASTER cm\r\n"
			+ "			INNER JOIN\r\n"
			+ "			    CONTRACT_ASSIGN_TAX cat \r\n"
			+ "			        ON cat.contract_id = cm.contract_id\r\n"
			+ "			INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id\r\n"
			+ "			INNER JOIN CONSOLIDATED_PACKING_NOTE qm ON qm.con_id = :contract_id AND qm.load_id = :load_id\r\n"
			+ "			INNER JOIN CONSOLIDATED_PACKING_ITEMS qim on qim.Conpn_id = qm.Conpn_id\r\n"
			+ "			WHERE\r\n"
			+ "			    cm.contract_id = :contract_id \r\n"
			+ "			    AND  (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%') and qm.Cancel =1\r\n"
			+ "			GROUP BY\r\n"
			+ "			    tm.tax_id,\r\n"
			+ "			    tm.tax_name,\r\n"
			+ "			    tm.tax_per,\r\n"
			+ "			    qm.grand_total,\r\n"
			+ "			    cm.taxable", nativeQuery = true)
	List<TaxMasterInterface> searchconsolidatedTaxIdcancel(String contract_id, String factory_id,String load_id);
	
	@Query(value = "WITH BaseValue AS ( SELECT (qm.grand_total) AS base_value, qm.grand_total as totalamount,\r\n"
			+ "							cc.percentage_value as openbal FROM CONSOLIDATED_PACKING_NOTE qm\r\n"
			+ "						inner join CONTRACT_MASTER cc on cc.contract_id = qm.con_id\r\n"
			+ "					    WHERE qm.con_id = :contract_id AND qm.load_id = :load_id  and qm.Cancel=1\r\n"
			+ "					),\r\n"
			+ "					TotalAfterTax AS (   \r\n"
			+ "					    SELECT tm.tax_id, tm.tax_name, tm.tax_per, qm.grand_total,\r\n"
			+ "					        CAST(((qm.grand_total) * (tm.tax_per / 100)) AS DECIMAL(10, 2)) AS tax_value\r\n"
			+ "					    FROM CONTRACT_MASTER cm          \r\n"
			+ "					    INNER JOIN CONTRACT_ASSIGN_TAX cat ON cat.contract_id = cm.contract_id          \r\n"
			+ "					    INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id       \r\n"
			+ "					    INNER JOIN CONSOLIDATED_PACKING_NOTE qm ON qm.con_id = :contract_id AND qm.load_id = :load_id      \r\n"
			+ "					    WHERE cm.contract_id = :contract_id AND (',' + CAST(cm.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%')  and qm.Cancel=1\r\n"
			+ "					)\r\n"
			+ "					SELECT \r\n"
			+ "					    CAST(ROUND((SELECT base_value FROM BaseValue), 0) AS DECIMAL(10, 2)) + \r\n"
			+ "					    CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS final_value,\r\n"
			+ "						CAST(ROUND(SUM(tax_value), 0) AS DECIMAL(10, 2)) AS Tax_Final_total,\r\n"
			+ "						CAST(ROUND(\r\n"
			+ "						(CAST(ROUND((SELECT totalamount FROM BaseValue), 0) AS DECIMAL(10, 2)) * \r\n"
			+ "						(CAST(ROUND((SELECT openbal FROM BaseValue), 0) AS DECIMAL(10, 2)) / 100)), 0) AS DECIMAL(10, 2)) AS taxable_final_total\r\n"
			+ "					FROM TotalAfterTax", nativeQuery = true)
	List<String> getconsolidatedTotalValueListcancel(String contract_id, String factory_id, String load_id);
}
