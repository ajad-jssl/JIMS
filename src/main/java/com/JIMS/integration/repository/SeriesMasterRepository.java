package com.JIMS.integration.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.controller.AdvanceStatesInterface;
import com.JIMS.integration.controller.ChallanSeriesInterface;
import com.JIMS.integration.controller.DebitCreditInterface;
import com.JIMS.integration.controller.DeliverySeriesInterface;
import com.JIMS.integration.controller.PaymentSeriesInterface;
import com.JIMS.integration.controller.SelfInvoiceSeriesInterface;
import com.JIMS.integration.entity.User;
import com.JIMS.integration.interfaces.AdvanceSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.ChallanSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.DebitCreditSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.DeliveryChallanSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.GSTSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.GstStatesInterface;
import com.JIMS.integration.interfaces.PaymentSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.SelfInvoiceSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.SeriesMasterList;

import jakarta.transaction.Transactional;

public interface SeriesMasterRepository extends JpaRepository<User, Integer> {

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_gst, created_by, created_date, status) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:user_id, GETDATE(), :status)", nativeQuery = true)
	int insertGST(String state_id, String invoice_series, String start_date, String end_date, String user_id,
			String status);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_advance, created_by, created_date, status,factory_id) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:user_id, GETDATE(), :status, :factory_id)", nativeQuery = true)
	int insertAdvance(String state_id, String invoice_series, String start_date, String end_date, String user_id,
			String status, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_debitcredit, created_by, created_date,debitcredit_type, status,factory_id ) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:user_id, GETDATE(), :debitcredit_type , :status,:factory_id)", nativeQuery = true)
	int insertDebitCredit(String state_id, String invoice_series, String start_date, String end_date, String user_id,
			String debitcredit_type, String status, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_challan , created_by, created_date, status,factory_id) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:user_id, GETDATE(), :status, :factory_id)", nativeQuery = true)
	int insertChallan(String state_id, String invoice_series, String start_date, String end_date, String user_id,
			String status, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_paymentvoucher, created_by, created_date, status,factory_id) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:user_id, GETDATE(), :status,:factory_id)", nativeQuery = true)
	int insertPaymentVoucher(String state_id, String invoice_series, String start_date, String end_date, String user_id,
			String status, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_selfinvoice, created_by, created_date, status,factory_id) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:user_id, GETDATE(), :status, :factory_id)", nativeQuery = true)
	int insertSelfInvoice(String state_id, String invoice_series, String start_date, String end_date, String user_id,
			String status, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_deliverychallan, created_by, created_date, status,factory_id) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:user_id, GETDATE(), :status, :factory_id)", nativeQuery = true)
	int insertDeliveryChallan(String state_id, String invoice_series, String start_date, String end_date,
			String user_id, String status, String factory_id);

	// UPDATE QUERY
	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET state_id = :state_id, invoice_series = :invoice_series, start_date = :start_date, end_date = :end_date, is_gst = 1, modified_by =:user_id, modified_date = GETDATE() WHERE series_id = :series_id", nativeQuery = true)
	int updateGST(String state_id, String invoice_series, String start_date, String end_date, String user_id,
			String series_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET state_id = :state_id, invoice_series = :invoice_series, start_date = :start_date, end_date = :end_date, is_advance = 1, modified_by =:user_id, modified_date = GETDATE() WHERE series_id = :series_id", nativeQuery = true)
	int updateAdvance(String state_id, String invoice_series, String start_date, String end_date, String user_id,
			String series_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET state_id = :state_id, invoice_series = :invoice_series, start_date = :start_date, end_date = :end_date, is_debitcredit = 1, modified_by =:user_id, modified_date = GETDATE() WHERE series_id = :series_id", nativeQuery = true)
	int updateDebitCredit(String state_id, String invoice_series, String start_date, String end_date, String user_id,
			String series_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET state_id = :state_id, invoice_series = :invoice_series, start_date = :start_date, end_date = :end_date, is_challan = 1, modified_by =:user_id, modified_date = GETDATE() WHERE series_id = :series_id", nativeQuery = true)
	int updateChallan(String state_id, String invoice_series, String start_date, String end_date, String user_id,
			String series_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET state_id = :state_id, invoice_series = :invoice_series, start_date = :start_date, end_date = :end_date, is_paymentvoucher = 1, modified_by =:user_id, modified_date = GETDATE() WHERE series_id = :series_id", nativeQuery = true)
	int updatePaymentVoucher(String state_id, String invoice_series, String start_date, String end_date, String user_id,
			String series_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET state_id = :state_id, invoice_series = :invoice_series, start_date = :start_date, end_date = :end_date, is_selfinvoice = 1, modified_by =:user_id, modified_date = GETDATE() WHERE series_id = :series_id", nativeQuery = true)
	int updateSelfInvoice(String state_id, String invoice_series, String start_date, String end_date, String user_id,
			String series_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET state_id = :state_id, invoice_series = :invoice_series, start_date = :start_date, end_date = :end_date, is_deliverychallan = 1, modified_by =:user_id, modified_date = GETDATE() WHERE series_id = :series_id", nativeQuery = true)
	int updateDeliveryChallan(String state_id, String invoice_series, String start_date, String end_date,
			String user_id, String series_id);

	// DELETE

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET is_delete = 1, modified_by = :user_id, modified_date = GETDATE() "
			+ "WHERE series_no = :series_no AND is_gst = 1", nativeQuery = true)
	int deleteGSTSeries(String series_no, String user_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET is_delete = 1, modified_by = :user_id, modified_date = GETDATE() "
			+ "WHERE series_no = :series_no AND is_advance = 1", nativeQuery = true)
	int deleteAdvanceSeries(String series_no, String user_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET is_delete = 1, modified_by = :user_id, modified_date = GETDATE() "
			+ "WHERE series_no = :series_no AND is_debitCredit = 1", nativeQuery = true)
	int deleteDebitCreditSeries(String series_no, String user_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET is_delete = 1, modified_by = :user_id, modified_date = GETDATE() "
			+ "WHERE series_no = :series_no AND is_challan = 1", nativeQuery = true)
	int deleteChallanSeries(String series_no, String user_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET is_delete = 1, modified_by = :user_id, modified_date = GETDATE() "
			+ "WHERE series_no = :series_no AND is_paymentVoucher = 1", nativeQuery = true)
	int deletePaymentVoucherSeries(String series_no, String user_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET is_delete = 1, modified_by = :user_id, modified_date = GETDATE() "
			+ "WHERE series_no = :series_no AND is_selfInvoice = 1", nativeQuery = true)
	int deleteSelfInvoiceSeries(String series_no, String user_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET is_delete = 1, modified_by = :user_id, modified_date = GETDATE() "
			+ "WHERE series_no = :series_no AND is_deliveryChallan = 1", nativeQuery = true)
	int deleteDeliveryChallanSeries(String series_no, String user_id);

	// SEARCH
	// Search for GST Series Master by ID
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE series_id = :series_id AND is_gst = 1", nativeQuery = true)
	GSTSeriesMasterInterfaces searchGSTSeriesById(String series_id);
	
	
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_gst = 1",nativeQuery = true)
	List<GSTSeriesMasterInterfaces> getAllSeriesByGst();
	
	
	
	

	// Search for Advance Series Master by ID
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE series_id = :series_id AND is_advance = 1", nativeQuery = true)
	AdvanceSeriesMasterInterfaces searchAdvanceSeriesById(String series_id);

	// Search for Debit Credit Series Master by ID
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE series_id = :series_id AND is_debitcredit = 1", nativeQuery = true)
	DebitCreditSeriesMasterInterfaces searchDebitCreditSeriesById(String series_id);

	// Search for Challan Series Master by ID
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE series_id = :series_id AND is_challan = 1", nativeQuery = true)
	ChallanSeriesMasterInterfaces searchChallanSeriesById(String series_id);

	// Search for Payment Series Master by ID
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE series_id = :series_id AND is_paymentvoucher = 1", nativeQuery = true)
	PaymentSeriesMasterInterfaces searchPaymentSeriesById(String series_id);

	// Search for Self Invoice Series Master by ID
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE series_id = :series_id AND is_selfinvoice = 1", nativeQuery = true)
	SelfInvoiceSeriesMasterInterfaces searchSelfInvoiceSeriesById(String series_id);

	// Search for Delivery Challan Series Master by ID
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE series_id = :series_id AND is_deliverychallan = 1", nativeQuery = true)
	DeliveryChallanSeriesMasterInterfaces searchDeliveryChallanSeriesById(String series_id);

	// List all GST Series Masters
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_gst = 1", nativeQuery = true)
	List<GSTSeriesMasterInterfaces> listAllGSTSeries();

	// List all Advance Series Masters
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_advance = 1", nativeQuery = true)
	List<AdvanceSeriesMasterInterfaces> listAllAdvanceSeries();

	// List all Debit Credit Series Masters
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_debitcredit = 1", nativeQuery = true)
	List<DebitCreditSeriesMasterInterfaces> listAllDebitCreditSeries();

	// List all Challan Series Masters
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_challan = 1", nativeQuery = true)
	List<ChallanSeriesMasterInterfaces> listAllChallanSeries();

	// List all Payment Series Masters
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_paymentvoucher = 1", nativeQuery = true)
	List<PaymentSeriesMasterInterfaces> listAllPaymentSeries();

	// List all Self Invoice Series Masters
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_selfinvoice = 1", nativeQuery = true)
	List<SelfInvoiceSeriesMasterInterfaces> listAllSelfInvoiceSeries();

	// List all Delivery Challan Series Masters
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_deliverychallan = 1", nativeQuery = true)
	List<DeliveryChallanSeriesMasterInterfaces> listAllDeliveryChallanSeries();

	/*
	 * @Query(value
	 * ="Select * from SERIES_MASTER where is_gst = 1 and is_advance =1 and is_debitcredit = 1"
	 * +
	 * " and is_challan=1 and is_paymentVoucher = 1 and is_selfinvoice = 1 and is_deliveryChallan = 1 and is_delete = 0"
	 * , nativeQuery = true) List<SeriesMasterList> valList();
	 */

	@Query(value = "select sm.*, concat(ssm.state_code, '- ' , ssm.state_name,'(',ssm.state_id,')') as state_name  from SERIES_MASTER sm inner join STATE_MASTER ssm on ssm.id = sm.state_id where sm.is_delete = 0 and sm.factory_id = :factory_id", nativeQuery = true)
	List<SeriesMasterList> valList(String factory_id);

//	@Query(value ="SELECT [bu_id],s.id as state_id, (s.[state_name] +'( '+ u.state_code +' )' +' - '+ [state_gstno]) as code FROM [GST_MASTER] as u inner join STATE_MASTER as s on s.state_code = u.state_code", nativeQuery = true)
//	List<SeriesMasterList> typeList();

	@Query(value = "select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_gst = 1)", nativeQuery = true)
	List<SeriesMasterList> typeList();

	@Query(value = "select id, state_id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_gst = 1  and status = 'Open' and is_delete = 0  and factory_id = :factory_id)", nativeQuery = true)
	List<SeriesMasterList> typeListGST(String factory_id);

	@Query(value = "SELECT * from STATE_MASTER", nativeQuery = true)
	List<SeriesMasterList> typeSateList();

	@Query(value = "select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_advance = 1  and status = 'Open' and is_delete = 0  and factory_id = :factory_id)", nativeQuery = true)
	List<SeriesMasterList> typeListADVANCE(String factory_id);

	@Query(value = "select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_debitCredit = 1  and status = 'Open' and is_delete = 0 and factory_id = :factory_id)", nativeQuery = true)
	List<SeriesMasterList> typeListDEBITCREDIT(String factory_id);

	@Query(value = "select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_challan = 1  and status = 'Open' and is_delete = 0  and factory_id = :factory_id)", nativeQuery = true)
	List<SeriesMasterList> typeListCHALLAN(String factory_id);

	@Query(value = "select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_paymentVoucher = 1  and status = 'Open' and is_delete = 0  and factory_id = :factory_id)", nativeQuery = true)
	List<SeriesMasterList> typeListPAYMENT(String factory_id);

	@Query(value = "select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_selfInvoice = 1  and status = 'Open' and is_delete = 0  and factory_id = :factory_id)", nativeQuery = true)
	List<SeriesMasterList> typeListSELFINVOICE(String factory_id);

	@Query(value = "select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_deliveryChallan = 1  and status = 'Open' and is_delete = 0  and factory_id = :factory_id)", nativeQuery = true)
	List<SeriesMasterList> typeListDELIVERYCHALAN(String factory_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_gst, created_by, created_date, status) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:created_by, GETDATE(), 'OPEN')", nativeQuery = true)
	int addGstMasterNew(String state_id, String invoice_series, String start_date, String end_date, String created_by);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_advance, created_by, created_date, status) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:created_by, GETDATE(), 'OPEN')", nativeQuery = true)
	int addAdvanceMasterNew(String state_id, String invoice_series, String start_date, String end_date,
			String created_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_debitcredit, created_by, created_date,debitcredit_type, status) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:created_by, GETDATE(), :debitcredit_type , 'OPEN')", nativeQuery = true)
	int addDebitCreditMasterNew(String state_id, String invoice_series, String start_date, String end_date,
			String debitcredit_type, String created_by);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_challan , created_by, created_date, status) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:created_by, GETDATE(), 'OPEN')", nativeQuery = true)
	int addChallanMasterNew(String state_id, String invoice_series, String start_date, String end_date,
			String created_by);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_paymentvoucher, created_by, created_date, status) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:created_by, GETDATE(), 'OPEN')", nativeQuery = true)
	int addPaymentVoucherNew(String state_id, String invoice_series, String start_date, String end_date,
			String created_by);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_selfinvoice, created_by, created_date, status) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:created_by, GETDATE(), 'OPEN')", nativeQuery = true)
	int addSelfInvoiceNew(String state_id, String invoice_series, String start_date, String end_date, String created_by);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_deliverychallan, created_by, created_date, status) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:created_by, GETDATE(), 'OPEN')", nativeQuery = true)
	int addDeliveryChallan(String state_id, String invoice_series, String start_date, String end_date,
			String created_by);

	@Query(value = "SELECT sem.*, sm.state_name FROM SERIES_MASTER sem\r\n"
			+ "inner join STATE_MASTER sm on sm.id = sem.state_id \r\n"
			+ " WHERE sem.is_gst = 1", nativeQuery = true)
	List<GSTSeriesMasterInterfaces> getGstMasterListNew();

	@Query(value = "SELECT sem.*, sm.state_name FROM SERIES_MASTER sem\r\n"
			+ "inner join STATE_MASTER sm on sm.id = sem.state_id \r\n"
			+ " WHERE sem.is_advance = 1", nativeQuery = true)
	List<AdvanceSeriesMasterInterfaces> getAdvanceMasterListNew();

	@Query(value = "SELECT sem.*, sm.state_name FROM SERIES_MASTER sem\r\n"
			+ "inner join STATE_MASTER sm on sm.id = sem.state_id \r\n"
			+ " WHERE sem.is_debitCredit = 1", nativeQuery = true)
	List<DebitCreditSeriesMasterInterfaces> getDebitCreditListNew();

	@Query(value = "SELECT sem.*, sm.state_name FROM SERIES_MASTER sem\r\n"
			+ "inner join STATE_MASTER sm on sm.id = sem.state_id \r\n"
			+ " WHERE sem.is_challan = 1", nativeQuery = true)
	List<ChallanSeriesMasterInterfaces> getChallansMasterListNew();

	@Query(value = "SELECT sem.*, sm.state_name FROM SERIES_MASTER sem\r\n"
			+ "inner join STATE_MASTER sm on sm.id = sem.state_id \r\n"
			+ " WHERE sem.is_paymentVoucher = 1", nativeQuery = true)
	List<PaymentSeriesMasterInterfaces> getPaymentVoucherMasterListNew();

	@Query(value = "SELECT sem.*, sm.state_name FROM SERIES_MASTER sem\r\n"
			+ "inner join STATE_MASTER sm on sm.id = sem.state_id \r\n"
			+ " WHERE sem.is_selfInvoice = 1", nativeQuery = true)
	List<SelfInvoiceSeriesMasterInterfaces> getSelfInvoiceListNew();

	@Query(value = "SELECT sem.*, sm.state_name FROM SERIES_MASTER sem\r\n"
			+ "inner join STATE_MASTER sm on sm.id = sem.state_id \r\n"
			+ " WHERE sem.is_deliveryChallan = 1", nativeQuery = true)
	List<DeliveryChallanSeriesMasterInterfaces> getDeliveryChallanListNew();

	@Query(value = "SELECT sem.*, sm.state_name FROM SERIES_MASTER sem\r\n"
			+ "inner join STATE_MASTER sm on sm.id = sem.state_id \r\n"
			+ " WHERE sem.series_no = :series_id", nativeQuery = true)
	GSTSeriesMasterInterfaces getGstMasterBasedOnId(String series_id);

	@Query(value = "SELECT sem.*, sm.state_name FROM SERIES_MASTER sem\r\n"
			+ "inner join STATE_MASTER sm on sm.id = sem.state_id \r\n"
			+ " WHERE sem.series_no = :series_id", nativeQuery = true)
	AdvanceSeriesMasterInterfaces getAdvanceMasterBaedOnId(String series_id);

	@Query(value = "SELECT sem.*, sm.state_name FROM SERIES_MASTER sem\r\n"
			+ "inner join STATE_MASTER sm on sm.id = sem.state_id \r\n"
			+ " WHERE sem.series_no = :series_id", nativeQuery = true)
	DebitCreditSeriesMasterInterfaces getDebitCreditBasedOnId(String series_id);

	@Query(value = "SELECT sem.*, sm.state_name FROM SERIES_MASTER sem\r\n"
			+ "inner join STATE_MASTER sm on sm.id = sem.state_id \r\n"
			+ " WHERE sem.series_no = :series_id", nativeQuery = true)
	ChallanSeriesMasterInterfaces getChallansMasterBasedOnId(String series_id);

	@Query(value = "SELECT sem.*, sm.state_name FROM SERIES_MASTER sem\r\n"
			+ "inner join STATE_MASTER sm on sm.id = sem.state_id \r\n"
			+ " WHERE sem.series_no = :series_id", nativeQuery = true)
	PaymentSeriesMasterInterfaces getPaymentVoucherMasterBasedOnId(String series_id);

	@Query(value = "SELECT sem.*, sm.state_name FROM SERIES_MASTER sem\r\n"
			+ "inner join STATE_MASTER sm on sm.id = sem.state_id \r\n"
			+ " WHERE sem.series_no = :series_id", nativeQuery = true)
	SelfInvoiceSeriesMasterInterfaces getSelfInvoiceBasedOnId(String series_id);

	@Query(value = "SELECT sem.*, sm.state_name FROM SERIES_MASTER sem\r\n" + "inner join STATE_MASTER"
			+ " sm on sm.id = sem.state_id \r\n" + " WHERE sem.series_no = :series_id", nativeQuery = true)
	DeliveryChallanSeriesMasterInterfaces getDeliveryChallanBasedOnId(String series_id);

	@Query(value = "SELECT DISTINCT sem.state_id, sm.location as state_name,sm.state_id as state_id_code from SERIES_MASTER sem "
			+ "INNER JOIN BUSINESS_UNITS sm on sm.bu_code = sem.state_id "
			+ "WHERE sem.is_gst = 1 and sem.is_delete = 0 ", nativeQuery = true)
	List<GstStatesInterface> getGstStatesOnly();
	
	@Query(value = "SELECT DISTINCT sem.state_id, sm.location as state_name from SERIES_MASTER sem "
			+ "INNER JOIN BUSINESS_UNITS sm on sm.bu_code = sem.state_id "
			+ "WHERE sem.is_advance = 1 and sem.is_delete = 0 ", nativeQuery = true)
	List<AdvanceStatesInterface> getAdvanceStates();

	@Query(value = "select sm.id as state_id, sm.state_code + '-' + sm.state_name + '(' + CAST(sm.state_id AS VARCHAR) + ')' as state_name,sm.state_id as state_ids from STATE_MASTER sm where sm.id not in "
			+ "(select state_id from  SERIES_MASTER where status = null)", nativeQuery = true)
	List<GstStatesInterface> getClosedStatusStatesList();
	
	

	
	
	
	
	
	
	@Query(value = "select sm.id as state_id, sm.state_code + '-' + sm.state_name + '(' + CAST(sm.state_id AS VARCHAR) + ')' as state_name,sm.state_id as state_ids from STATE_MASTER sm where sm.id not in "
			+ "(select state_id from  SERIES_MASTER where status = null)", nativeQuery = true)
	List<AdvanceStatesInterface> getClosedStatusAdvanceStatesList();
	
	@Query(value = "SELECT Distinct sem.series_no, sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.location as state_name from SERIES_MASTER sem\r\n"
			+ "INNER JOIN BUSINESS_UNITS sm on sm.bu_code= sem.state_id\r\n"
			+ "WHERE sem.state_id = :state_id and  sem.is_gst = 1 and sem.is_delete = 0 ", nativeQuery = true)
	List<GstStatesInterface> getGstStateInfoBasedOnId(String state_id);
	
	
	
//	@Query(value = "SELECT top(1) sem.series_no, sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.state_name from SERIES_MASTER sem\r\n"
//			+ "			INNER JOIN STATE_MASTER sm on sm.id = sem.state_id and  sem.is_gst = 1 and sem.is_delete = 0 order by series_no desc", nativeQuery = true)
//GstStatesInterface getLatestGstStatus();
	
	
	
	
	
	
	
	
	
	@Query(value = " SELECT top(1) sem.series_no, sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.state_name from SERIES_MASTER sem\r\n"
			+ "			INNER JOIN STATE_MASTER sm on sm.id = sem.state_id and  sem.is_gst = 1 and sem.is_delete = 0  AND sem.state_id = :stateId order by series_no desc", nativeQuery = true)
		GstStatesInterface getLatestGstStatusByState(@Param("stateId")  String stateId);
	
	
	
	@Query(value = 
		    "SELECT TOP (1) " +
		    " sem.series_no, " +
		    " sem.invoice_series, " +
		    " sem.state_id, " +
		    " sem.start_date, " +
		    " sem.end_date, " +
		    " CASE " +
		    "   WHEN GETDATE() BETWEEN sem.start_date AND sem.end_date " +
		    "   THEN 'OPEN' ELSE 'CLOSE' " +
		    " END AS gst_status, " +
		    " sm.state_name " +
		    "FROM SERIES_MASTER sem " +
		    "INNER JOIN STATE_MASTER sm " +
		    "   ON sm.id = sem.state_id " +
		    "  AND sem.is_advance = 1 " +
		    "  AND sem.is_delete = 0 " +
		    "  AND sem.state_id = :stateId " +
		    "ORDER BY sem.series_no DESC",
		    nativeQuery = true)
		AdvanceStatesInterface getLatestAdvanceStatusByState(
		        @Param("stateId") String stateId);

	
	
	
	
	
	
	
	@Query(value = "SELECT Distinct sem.series_no, sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.location as state_name from SERIES_MASTER sem\r\n"
			+ "INNER JOIN BUSINESS_UNITS sm on sm.state_id = sem.state_id and  sem.is_gst = 1 and sem.is_delete = 0"
			 , nativeQuery = true)
	List<GstStatesInterface> getGstStateInfoBasedOnIdS();
	
	@Query(value = "SELECT Distinct sem.series_no , sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.location as state_name from SERIES_MASTER sem\r\n"
			+ "INNER JOIN BUSINESS_UNITS sm on sm.bu_code = sem.state_id\r\n"
			+ "WHERE sem.state_id = :state_id and  sem.is_advance = 1 and sem.is_delete = 0 ", nativeQuery = true)
	List<AdvanceStatesInterface> getAdvanceStateInfoBasedOnId(String state_id);
	
	
	
	
	@Query(value="SELECT Distinct sem.series_no , sem.invoice_series, sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.location as state_name from SERIES_MASTER sem\r\n"
			+ "			 INNER JOIN BUSINESS_UNITS sm on sm.state_id = sem.state_id \r\n"
			+ "			   and   sem.is_advance = 1 and sem.is_delete = 0 ", nativeQuery = true)
	List<AdvanceStatesInterface> getAdvanceStateInfoBasedOnIds();
	

	@Query(value = "SELECT DISTINCT sem.state_id, sm.location as state_name from SERIES_MASTER sem "
			+ "INNER JOIN BUSINESS_UNITS sm on sm.state_id = sem.state_id "
			+ "WHERE sem.is_debitCredit = 1 and sem.is_delete = 0 ", nativeQuery = true)
	    List<DebitCreditInterface> getAllDebitCredit();

	@Query(value = "select sm.id as state_id, sm.state_code + '-' + sm.state_name + '(' + CAST(sm.state_id AS VARCHAR) + ')' as state_name,sm.state_id as state_ids from STATE_MASTER sm where sm.id not in "
			+ "(select state_id from  SERIES_MASTER where status = null)", nativeQuery = true)
	    List<DebitCreditInterface> getClosedDebitCreditStatusList();

	@Query(value = "SELECT Distinct sem.series_no, sem.invoice_series, sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.location as state_name, sem.debitcredit_type as debitcredittype from SERIES_MASTER sem\r\n"
			+ "INNER JOIN BUSINESS_UNITS sm on sm.state_id = sem.state_id\r\n"
			+ "WHERE sem.state_id = :state_id and  sem.is_debitCredit = 1 and sem.is_delete = 0 ", nativeQuery = true)
	    List<DebitCreditInterface> getDebitCreditById(String state_id);
	
	
	@Query(value = "SELECT Distinct sem.series_no, sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.location as state_name, sem.debitcredit_type as debitcredittype from SERIES_MASTER sem\r\n"
			+ "			     INNER JOIN BUSINESS_UNITS sm on sm.state_id = sem.state_id\r\n"
			+ "			      and  sem.is_debitCredit = 1 and sem.is_delete = 0",nativeQuery = true)
	 List<DebitCreditInterface> getDebitCreditByIdS();
	

	@Query(value = "SELECT DISTINCT sem.state_id, sm.state_name from SERIES_MASTER sem "
			+ "INNER JOIN STATE_MASTER sm on sm.id = sem.state_id "
			+ "WHERE sem.is_challan = 1 and sem.is_delete = 0 ", nativeQuery = true)
    List<ChallanSeriesInterface> getAllChallanSeries();

	@Query(value = "select sm.id as state_id, sm.state_code + '-' + sm.state_name + '(' + CAST(sm.state_id AS VARCHAR) + ')' as state_name,sm.state_id as state_ids from STATE_MASTER sm where sm.id not in "
			+ "(select state_id from  SERIES_MASTER where status = null)", nativeQuery = true)
    List<ChallanSeriesInterface> getClosedChallanSeries();

	@Query(value = "SELECT sem.series_no, sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.state_name from SERIES_MASTER sem\r\n"
			+ "INNER JOIN STATE_MASTER sm on sm.id = sem.state_id\r\n"
			+ "WHERE sem.state_id = :state_id and  sem.is_challan = 1 and sem.is_delete = 0 ", nativeQuery = true)
    List<ChallanSeriesInterface> getChallanSeriesById(String state_id);
	
	
	@Query(value="SELECT sem.series_no, sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.state_name from SERIES_MASTER sem \r\n"
			+ "			     INNER JOIN STATE_MASTER sm on sm.state_id = sem.state_id\r\n"
			+ "			    and  sem.is_challan = 1 and sem.is_delete = 0",nativeQuery = true)
	 List<ChallanSeriesInterface> getChallanSeriesByIdS();
		

	
	
	
	
    
	@Query(value = "SELECT DISTINCT sem.state_id, sm.state_name from SERIES_MASTER sem "
			+ "INNER JOIN STATE_MASTER sm on sm.id = sem.state_id "
			+ "WHERE sem.is_paymentVoucher = 1 and sem.is_delete = 0 ", nativeQuery = true)
    List<PaymentSeriesInterface> getAllPaymentSeries();

	@Query(value = "select sm.id as state_id, sm.state_code + '-' + sm.state_name + '(' + CAST(sm.state_id AS VARCHAR) + ')' as state_name ,sm.state_id as state_ids from STATE_MASTER sm where sm.id not in "
			+ "(select state_id from  SERIES_MASTER where status = null)", nativeQuery = true)
    List<PaymentSeriesInterface> getClosedPaymentSeries();

	@Query(value = "SELECT sem.series_no, sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.state_name from SERIES_MASTER sem\r\n"
			+ "INNER JOIN STATE_MASTER sm on sm.id = sem.state_id\r\n"
			+ "WHERE sem.state_id = :state_id and  sem.is_paymentVoucher = 1 and sem.is_delete = 0 ", nativeQuery = true)
    List<PaymentSeriesInterface> getPaymentSeriesById(String state_id);
	
	
	
	@Query(value = "  SELECT sem.series_no, sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.state_name from SERIES_MASTER sem      \r\n"
			+ "			       INNER JOIN STATE_MASTER sm on sm.state_id = sem.state_id      \r\n"
			+ "			     and  sem.is_paymentVoucher = 1 and sem.is_delete = 0 ", nativeQuery = true)
	 List<PaymentSeriesInterface> getPaymentSeriesByIdS();
	
	@Query(value = "SELECT DISTINCT sem.state_id, sm.state_name from SERIES_MASTER sem "
			+ "INNER JOIN STATE_MASTER sm on sm.id = sem.state_id "
			+ "WHERE sem.is_selfInvoice = 1 and sem.is_delete = 0 ", nativeQuery = true)
	List<SelfInvoiceSeriesInterface> getAllSelfInvoiceSeries();

	@Query(value = "select sm.id as state_id, sm.state_code + '-' + sm.state_name + '(' + CAST(sm.state_id AS VARCHAR) + ')' as state_name,sm.state_id as state_ids from STATE_MASTER sm where sm.id not in "
			+ "(select state_id from  SERIES_MASTER where status = null)", nativeQuery = true)
	List<SelfInvoiceSeriesInterface> getClosedSelfInvoiceSeries();

	@Query(value = "SELECT sem.series_no, sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.state_name from SERIES_MASTER sem\r\n"
			+ "INNER JOIN STATE_MASTER sm on sm.id = sem.state_id\r\n"
			+ "WHERE sem.state_id = :state_id and  sem.is_selfInvoice = 1 and sem.is_delete = 0 ", nativeQuery = true)
	List<SelfInvoiceSeriesInterface> getSelfInvoiceSeriesById(String state_id);
	
	
	@Query(value = "SELECT sem.series_no, sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.state_name from SERIES_MASTER sem      \r\n"
			+ "			       INNER JOIN STATE_MASTER sm on sm.state_id = sem.state_id      \r\n"
			+ "			       and  sem.is_selfInvoice = 1 and sem.is_delete = 0 ",nativeQuery = true)
	List<SelfInvoiceSeriesInterface> getSelfInvoiceSeriesByIdS();
	
	
	
	
	
	
	@Query(value = "SELECT DISTINCT sem.state_id, sm.location as state_name from SERIES_MASTER sem "
			+ "INNER JOIN BUSINESS_UNITS sm on sm.bu_code = sem.state_id "
			+ "WHERE sem.is_deliveryChallan = 1 and sem.is_delete = 0 ", nativeQuery = true)
	List<DeliverySeriesInterface> getAllDeliverySeries();

	@Query(value = "select sm.id as state_id, sm.state_code + '-' + sm.state_name + '(' + CAST(sm.state_id AS VARCHAR) + ')' as state_name,sm.state_id as state_ids from STATE_MASTER sm where sm.id not in "
			+ "(select state_id from  SERIES_MASTER where status = null)", nativeQuery = true)
	List<DeliverySeriesInterface> getClosedDeliverySeries();

	@Query(value = "SELECT Distinct sem.series_no, sem.invoice_series, sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.location as state_name from SERIES_MASTER sem\r\n"
			+ "INNER JOIN BUSINESS_UNITS sm on sm.bu_code = sem.state_id\r\n"
			+ "WHERE sem.state_id = :state_id and  sem.is_deliveryChallan = 1 and sem.is_delete = 0 ", nativeQuery = true)
	List<DeliverySeriesInterface> getDeliverySeriesById(String state_id);
	
	
	@Query(value = " SELECT Distinct sem.series_no, sem.invoice_series, sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.location as state_name from SERIES_MASTER sem     \r\n"
			+ "	INNER JOIN BUSINESS_UNITS sm on sm.state_id = sem.state_id \r\n"
			+ "	and  sem.is_deliveryChallan = 1 and sem.is_delete = 0",nativeQuery = true)
	List<DeliverySeriesInterface> getDeliverySeriesByIdS();
	
	
	
	
	
	
	@Query(value = """
		    SELECT TOP(1)
		        sem.series_no,
		        sem.invoice_series,
		        sem.state_id,
		        sem.start_date,
		        sem.end_date,
		        CASE
		            WHEN GETDATE() BETWEEN sem.start_date AND sem.end_date
		            THEN 'OPEN' ELSE 'CLOSE'
		        END AS gst_status,
		        sm.state_name,
		        sem.debitcredit_type
		    FROM SERIES_MASTER sem
		    INNER JOIN STATE_MASTER sm ON sm.id = sem.state_id
		    WHERE sem.is_debitCredit = 1
		      AND sem.is_delete = 0
		      AND sem.state_id = :stateId
		      AND sem.debitcredit_type = :type
		    ORDER BY sem.series_no DESC
		""", nativeQuery = true)
	DebitCreditInterface getLatestDebitCreditStatusByStateAndType(
		        @Param("stateId") String stateId,
		        @Param("type") String type
		);
	
	
	@Query(value = """
		    SELECT TOP(1)
		        sem.series_no,
		        sem.invoice_series,
		        sem.state_id,
		        sem.start_date,
		        sem.end_date,
		        CASE
		            WHEN GETDATE() BETWEEN sem.start_date AND sem.end_date
		            THEN 'OPEN'
		            ELSE 'CLOSE'
		        END AS gst_status,
		        sm.state_name
		    FROM SERIES_MASTER sem
		    INNER JOIN STATE_MASTER sm ON sm.id = sem.state_id
		    WHERE sem.is_challan = 1
		      AND sem.is_delete = 0
		      AND sem.state_id = :state_id
		    ORDER BY sem.series_no DESC
		""", nativeQuery = true)
	ChallanSeriesInterface getLatestChallanStatusByState(
		        @Param("state_id") String state_id);
	
	
	
	
	@Query(value=" SELECT top(1) sem.series_no, sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.state_name from SERIES_MASTER sem\r\n"
			+ "			       			INNER JOIN STATE_MASTER sm on sm.id = sem.state_id and  sem.is_paymentVoucher = 1 and sem.is_delete = 0  AND sem.state_id = :state_id order by series_no desc",nativeQuery = true)
	PaymentSeriesInterface getLatestPaymentVoucherStatusByState( @Param("state_id") String state_id);
	
	
	
	
	@Query(value = "SELECT top(1) sem.series_no, sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.state_name from SERIES_MASTER sem\r\n"
			+ "			       			INNER JOIN STATE_MASTER sm on sm.id = sem.state_id and  sem.is_selfInvoice = 1 and sem.is_delete = 0  AND sem.state_id =:state_id order by series_no desc",nativeQuery = true)
	SelfInvoiceSeriesInterface getLatestInVoiceStatusByState( @Param("state_id") String state_id);

	
	@Query(value = "\r\n"
			+ "				 SELECT top(1) sem.series_no, sem.invoice_series,  sem.state_id, sem.start_date, sem.end_date, case when GETDATE() between sem.start_date and sem.end_date then 'OPEN' else 'CLOSE' end as gst_status,sm.state_name from SERIES_MASTER sem\r\n"
			+ "			       			INNER JOIN STATE_MASTER sm on sm.id = sem.state_id and  sem.is_deliveryChallan = 1 and sem.is_delete = 0  AND sem.state_id =:state_id order by series_no desc",nativeQuery = true)
	DeliverySeriesInterface getLatestDeliveryStatusByState(@Param("state_id") String state_id);
	
	
}
