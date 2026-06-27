package com.JIMS.integration.controller;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.JIMS.integration.entity.Invoice;
import com.JIMS.integration.entity.otherinvoice;
import com.JIMS.integration.entity.ConsolidatedInvoiceItem;
import com.JIMS.integration.entity.Consolidated_invoice;
import com.JIMS.integration.interfaces.AssignToContract;
import com.JIMS.integration.interfaces.InvoiceMasterInterface;
import com.JIMS.integration.interfaces.InvoiceTypeInterfaces;
import com.JIMS.integration.interfaces.ListAssignMilesonetoContractors;
import com.JIMS.integration.interfaces.Listotherloadsinterface;
import com.JIMS.integration.interfaces.OtherInvoiceInterface;
import com.JIMS.integration.interfaces.QSPacking_QSPackingItem_LIST_INTERFACES;
import com.JIMS.integration.interfaces.ServiceCodeMasterInterface;
import com.JIMS.integration.interfaces.TaxMasterInterface;
import com.JIMS.integration.interfaces.consolidatedQSPacking_Interfaces;
import com.JIMS.integration.interfaces.othersPackNoteIteminterface;
import com.JIMS.integration.interfaces.othersPackNoteinterface;
import com.JIMS.integration.interfaces.othersPackingItem_LIST_INTERFACES;
import com.JIMS.integration.repository.AssignToContractRepository;
import com.JIMS.integration.repository.ConsolidatedInvoiceItemRepository;
import com.JIMS.integration.repository.ConsolidatedinvoiceRepository;
import com.JIMS.integration.repository.InvoiceRepository;
import com.JIMS.integration.repository.QSPackingRepository;
import com.JIMS.integration.repository.otherinvoice_repository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class InvoiceController {
	Logger logger = LogManager.getLogger(InvoiceController.class);

	@Autowired
	public InvoiceRepository invoiceRepository;
	
	@Autowired
	public ConsolidatedinvoiceRepository consolidatedinvoiceRepository;
	
	@Autowired
	public otherinvoice_repository otherinvoiceRepository;
	
	@Autowired
	public QSPackingRepository qsPackingRepository;
	@Autowired
	public AssignToContractRepository assignToContractRepository;

	@Value("${file.upload-dir}")
	private String uploadDir;

	public String getUploadDir() {
		return uploadDir;
	}
	@GetMapping("/invoicemaster/invoice_hsn_servicenew")
	public @ResponseBody Map<String, Object> serachQSPackingItemIdNew(@RequestParam String stype,
			@RequestParam String hcode, @RequestParam String pn_id, @RequestParam String modified_by) {
		logger.info("EXECUTING METHOD :: serachQSPackingItemIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			int value = invoiceRepository.updateQSPAckingItem(stype, hcode, pn_id, modified_by);
			response.put("message", (value > 0) ? "Success" : "failure");
			response.put("status", (value > 0) ? "yes" : "no");
			response.put("action", "Search_Record_In_QSPACKINGITEMMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR serachQSPackingItemIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: serachQSPackingItemIdNew");
		return response;
	}

	@GetMapping("/invoicemaster/listloadidnew")
	public @ResponseBody Map<String, Object> serachListLoadIdNew(@RequestParam int con_id,
			@RequestParam int factory_id) {
		logger.info("EXECUTING METHOD :: serachListLoadIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<Map<Integer, String>> value = invoiceRepository.getLoadIdQsPacking(con_id, factory_id);
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "ListLoadId_Record");
			response.put("data", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR serachListLoadIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: serachListLoadIdNew");
		return response;
	}

	@GetMapping("/invoicemaster/invoicetypelist/steelornonsteel/new")
	public @ResponseBody Map<String, Object> listScrapTypeNew() {
		logger.info("EXECUTING METHOD :: listScrapTypeNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<String> value = null;
		try {
			value = invoiceRepository.listInvoiceMaster();
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER");
			response.put("invoice_types", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listScrapTypeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listScrapTypeNew");
		return response;
	}

	@GetMapping("/invoicemaster/listservicecodenew")
	public @ResponseBody Map<String, Object> listServiceCodeNew(@RequestParam String factory_id) {
		logger.info("EXECUTING METHOD :: listServiceCodeNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<ServiceCodeMasterInterface> value = null;
		try {
			value = invoiceRepository.listServiceCode(factory_id);
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER_SERVICECODE");
			response.put("list_service_codes", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listServiceCodeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listServiceCodeNew");
		return response;
	}

	@GetMapping("/invoicemaster/listhsncodenew")
	public @ResponseBody Map<String, Object> listHSNCodeNew(@RequestParam String factory_id) {
		logger.info("EXECUTING METHOD :: listHSNCodeNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<ServiceCodeMasterInterface> value = null;
		try {
			value = invoiceRepository.listHSNCode(factory_id);
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER_HSNCODE");
			response.put("list_hsn_code", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listHSNCodeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listHSNCodeNew");
		return response;
	}

	@GetMapping("/invoice/listbgtypenew")
	public @ResponseBody Map<String, Object> listBGTypeNew() {
		logger.info("EXECUTING METHOD :: listBGTypeNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, String>> value = null;
		try {
			value = invoiceRepository.bgTypeList();
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER_BGTYPE");
			response.put("BG_LIST", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listBGTypeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listBGTypeNew");
		return response;
	}

	@GetMapping("/invoicemaster/listinvoicetypenew") // INVOICE_TYPE table 10 hard code value
	public @ResponseBody Map<String, Object> listInvoice_typeNew() {
		logger.info("EXECUTING METHOD :: listInvoice_typeNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<InvoiceTypeInterfaces> value = null;
		try {
			value = invoiceRepository.listInvoiceType();
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_TYPE_LIST");
			response.put("INVOICE_TYPE_LIST", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listInvoice_typeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listInvoice_typeNew");
		return response;
	}

	@PostMapping("/invoice/addnew")
	public @ResponseBody Map<String, Object> createInvoiceNew(@RequestParam int contract_id,
			@RequestParam String load_id, @RequestParam String invoice_type, @RequestParam String con_slno,
			@RequestParam String created_by, @RequestParam(required = false) String product_desc,
			@RequestParam(required = false) String remarks, @RequestParam(required = false) String date_of_notification,
			@RequestParam(required = false) String date_val, @RequestParam(required = false) String bg_type,
			@RequestParam(required = false) String date_of_issue, @RequestParam(required = false) String reference_no,
			@RequestParam(required = false) String lc_number, @RequestParam(required = false) String supply_place,
			@RequestParam(required = false) String s_t_exempted, @RequestParam(required = false) String lr_docketno,
			@RequestParam String pn_id, @RequestParam(required = false) String bg_no,
			@RequestParam(required = false) String date_of_expiry, @RequestParam(required = false) String date_of_ref,
			@RequestParam(required = false) String lc_issue_date, @RequestParam String contract_name,
			@RequestParam int factory_id, @RequestParam(required = false) List<String> service_code_id,
			@RequestParam(required = false) List<String> hsn_code_id, @RequestParam String inc_type,
			@RequestParam(required = false) List<String> slno) {
		logger.info("EXECUTING METHOD :: createInvoiceNew");
		Map<String, Object> response = new HashMap<String, Object>();
		Invoice invoiceObj = null;
		try {
			Invoice invoice = new Invoice();
			invoice.setContract_slno(con_slno);
			invoice.setContract_id(contract_id);
			invoice.setLoad_id(load_id);
			invoice.setInvoice_type(invoice_type);
			invoice.setProductDesc(product_desc);
			invoice.setRemarks(remarks);
			invoice.setDateOfNotification(date_of_notification);
			invoice.setDateVal(date_val);
			invoice.setBgType(bg_type);
			invoice.setDateOfIssue(date_of_issue);
			invoice.setReferenceNo(reference_no);
			invoice.setLcNumber(lc_number);
			invoice.setSupplyPlace(supply_place);
			invoice.setStExempted(s_t_exempted);
			invoice.setLrDocketNo(lr_docketno);
			invoice.setBgNo(bg_no);
			invoice.setDateOfExpiry(date_of_expiry);
			invoice.setDateOfRef(date_of_ref);
			invoice.setLcIssueDate(lc_issue_date);
			invoice.setCreatedBy(created_by);
			invoice.setContract_name(contract_name);
			LocalDateTime time = LocalDateTime.now();
			invoice.setCreatedDate(time);
			invoice.setFactory_id(factory_id);
			invoice.setPn_id(Integer.parseInt(pn_id));
			logger.info("EXECUTING METHOD :: BEFORE adding INVOICE");
			invoiceObj = invoiceRepository.save(invoice);
			logger.info("EXECUTING METHOD :: AFTER adding INVOICE");
			logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING IN INVOICE");
			invoiceRepository.addPackingNoteItemInsertTypeIdbasedonPnId(inc_type, created_by, pn_id);
			invoiceRepository.addPackingNoteInsertTypeIdbasedonPnId(created_by, pn_id);
			logger.info("EXECUTING METHOD :: AFTER UPDATING QSPACKING IN INVOICE");
			response.put("action", "INVOICE_ADD");
			response.put("message", (invoiceObj != null) ? "Success" : "failure");
			response.put("status", (invoiceObj != null) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR  createInvoiceNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createInvoiceNew");
		return response;
	}
	
	@PostMapping("/invoice/addnewdly")
	public @ResponseBody Map<String, Object> createdlyInvoiceNew(@RequestParam int contract_id,
			@RequestParam String load_id, @RequestParam String invoice_type, @RequestParam String con_slno,
			@RequestParam String created_by, @RequestParam(required = false) String product_desc,
			@RequestParam(required = false) String remarks, @RequestParam(required = false) String date_of_notification,
			@RequestParam(required = false) String date_val, @RequestParam(required = false) String bg_type,
			@RequestParam(required = false) String date_of_issue, @RequestParam(required = false) String reference_no,
			@RequestParam(required = false) String lc_number, @RequestParam(required = false) String supply_place,
			@RequestParam(required = false) String s_t_exempted, @RequestParam(required = false) String lr_docketno,
			@RequestParam String pn_id, @RequestParam(required = false) String bg_no,
			@RequestParam(required = false) String date_of_expiry, @RequestParam(required = false) String date_of_ref,
			@RequestParam(required = false) String lc_issue_date, @RequestParam String contract_name,
			@RequestParam int factory_id, @RequestParam(required = false) List<String> service_code_id,
			@RequestParam(required = false) List<String> hsn_code_id, @RequestParam String type_id,
			@RequestParam(required = false) List<String> slno,@RequestParam int lot_id) {
		logger.info("EXECUTING METHOD :: createInvoiceNew");
		Map<String, Object> response = new HashMap<String, Object>();
		Invoice invoiceObj = null;
		try {
			Invoice invoice = new Invoice();
			invoice.setContract_slno(con_slno);
			invoice.setContract_id(contract_id);
			invoice.setLoad_id(load_id);
			invoice.setInvoice_type(invoice_type);
			invoice.setProductDesc(product_desc);
			invoice.setRemarks(remarks);
			invoice.setDateOfNotification(date_of_notification);
			invoice.setDateVal(date_val);
			invoice.setBgType(bg_type);
			invoice.setDateOfIssue(date_of_issue);
			invoice.setReferenceNo(reference_no);
			invoice.setLcNumber(lc_number);
			invoice.setSupplyPlace(supply_place);
			invoice.setStExempted(s_t_exempted);
			invoice.setLrDocketNo(lr_docketno);
			invoice.setBgNo(bg_no);
			invoice.setDateOfExpiry(date_of_expiry);
			invoice.setDateOfRef(date_of_ref);
			invoice.setLcIssueDate(lc_issue_date);
			invoice.setCreatedBy(created_by);
			invoice.setContract_name(contract_name);
			LocalDateTime time = LocalDateTime.now();
			invoice.setCreatedDate(time);
			invoice.setFactory_id(factory_id);
			invoice.setPn_id(Integer.parseInt(pn_id));
			invoice.setLot_id(lot_id);
			logger.info("EXECUTING METHOD :: BEFORE adding INVOICE");
			invoiceObj = invoiceRepository.save(invoice);
			logger.info("EXECUTING METHOD :: AFTER adding INVOICE");
			logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING IN INVOICE");
			invoiceRepository.adddlyPackingNoteInsertTypeIdbasedonPnId(type_id, created_by, pn_id);
			logger.info("EXECUTING METHOD :: AFTER UPDATING QSPACKING IN INVOICE");
			response.put("action", "INVOICE_ADD");
			response.put("message", (invoiceObj != null) ? "Success" : "failure");
			response.put("status", (invoiceObj != null) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR  createInvoiceNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createInvoiceNew");
		return response;
	}
	
	@PostMapping("/invoice/advnew")
	public @ResponseBody Map<String, Object> createadvInvoiceNew(@RequestParam int contract_id,
			@RequestParam String load_id, @RequestParam String invoice_type, @RequestParam String con_slno,
			@RequestParam String created_by, @RequestParam(required = false) String product_desc,
			@RequestParam(required = false) String remarks, @RequestParam(required = false) String date_of_notification,
			@RequestParam(required = false) String date_val, @RequestParam(required = false) String bg_type,
			@RequestParam(required = false) String date_of_issue, @RequestParam(required = false) String reference_no,
			@RequestParam(required = false) String lc_number, @RequestParam(required = false) String supply_place,
			@RequestParam(required = false) String s_t_exempted, @RequestParam(required = false) String lr_docketno,
			@RequestParam String pn_id, @RequestParam(required = false) String bg_no,
			@RequestParam(required = false) String date_of_expiry, @RequestParam(required = false) String date_of_ref,
			@RequestParam(required = false) String lc_issue_date, @RequestParam String contract_name,
			@RequestParam int factory_id, @RequestParam(required = false) List<String> service_code_id,
			@RequestParam(required = false) List<String> hsn_code_id,
			@RequestParam(required = false) List<String> slno) {
		logger.info("EXECUTING METHOD :: createInvoiceNew");
		Map<String, Object> response = new HashMap<String, Object>();
		Invoice invoiceObj = null;
		try {
			Invoice invoice = new Invoice();
			invoice.setContract_slno(con_slno);
			invoice.setContract_id(contract_id);
			invoice.setLoad_id(load_id);
			invoice.setInvoice_type(invoice_type);
			invoice.setProductDesc(product_desc);
			invoice.setRemarks(remarks);
			invoice.setDateOfNotification(date_of_notification);
			invoice.setDateVal(date_val);
			invoice.setBgType(bg_type);
			invoice.setDateOfIssue(date_of_issue);
			invoice.setReferenceNo(reference_no);
			invoice.setLcNumber(lc_number);
			invoice.setSupplyPlace(supply_place);
			invoice.setStExempted(s_t_exempted);
			invoice.setLrDocketNo(lr_docketno);
			invoice.setBgNo(bg_no);
			invoice.setDateOfExpiry(date_of_expiry);
			invoice.setDateOfRef(date_of_ref);
			invoice.setLcIssueDate(lc_issue_date);
			invoice.setCreatedBy(created_by);
			invoice.setContract_name(contract_name);
			LocalDateTime time = LocalDateTime.now();
			invoice.setCreatedDate(time);
			invoice.setFactory_id(factory_id);
			invoice.setPn_id(Integer.parseInt(pn_id));
			logger.info("EXECUTING METHOD :: BEFORE adding INVOICE");
			invoiceObj = invoiceRepository.save(invoice);
			logger.info("EXECUTING METHOD :: AFTER adding INVOICE");
			logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING IN INVOICE");
			invoiceRepository.addadvPackingNoteItemInsertTypeIdbasedonPnId(created_by, pn_id);
			logger.info("EXECUTING METHOD :: AFTER UPDATING QSPACKING IN INVOICE");
			response.put("action", "INVOICE_ADD");
			response.put("message", (invoiceObj != null) ? "Success" : "failure");
			response.put("status", (invoiceObj != null) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR  createInvoiceNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createInvoiceNew");
		return response;
	}

	@PostMapping("/invoice/updatenew")
	public @ResponseBody Map<String, Object> updateInvoiceNew(@RequestParam Integer id,
			@RequestParam String modified_by, @RequestParam String product_desc, @RequestParam String remarks,
			@RequestParam(required = false) String date_of_notification, @RequestParam(required = false) String date_val, @RequestParam String pn_id,
			@RequestParam(required = false) String bg_type, @RequestParam(required = false) String date_of_issue, @RequestParam(required = false) String reference_no,
			@RequestParam(required = false) String lc_number, @RequestParam(required = false) String supply_place, @RequestParam(required = false) String s_t_exempted,
			@RequestParam(required = false) String lr_docketno, @RequestParam(required = false) String bg_no, @RequestParam(required = false) String date_of_expiry,
			@RequestParam(required = false) String date_of_ref, @RequestParam(required = false) String lc_issue_date,
			@RequestParam(required = false) List<String> service_code_id,
			@RequestParam(required = false) List<String> hsn_code_id, @RequestParam(required = false) String inc_type,//invoice_type_id for type_remarks
			@RequestParam(required = false) List<String> slno) {
		logger.info("EXECUTING METHOD :: updateInvoiceNew ");
		Map<String, Object> response = new HashMap<String, Object>();
		int valueCount = 0;
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE");
			valueCount = invoiceRepository.updateInvoiceEntryInfo(product_desc, remarks, date_of_notification, date_val,
					bg_type, date_of_issue, reference_no, lc_number, supply_place, lr_docketno, bg_no, date_of_expiry,
					date_of_ref, lc_issue_date, modified_by, s_t_exempted, id);
			logger.info("EXECUTING METHOD :: AFTER UPDATING INVOICE");
			logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING IN INVOICE");
			invoiceRepository.addPackingNoteItemInsertTypeIdbasedonPnId(inc_type, modified_by, pn_id);
			logger.info("EXECUTING METHOD :: AFTER UPDATING QSPACKING IN INVOICE");
			response.put("action", "INVOICE_UPDATE");
			response.put("message", (valueCount > 0) ? "Success" : "No fields changed");
			response.put("status", (valueCount > 0) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateInvoiceNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateInvoiceNew ");
		return response;
	}
	
	@PostMapping("/invoice/updatdlyenew")
	public @ResponseBody Map<String, Object> updatedlyInvoiceNew(@RequestParam Integer id,
			@RequestParam String modified_by, @RequestParam String product_desc, @RequestParam String remarks,
			@RequestParam(required = false) String date_of_notification, @RequestParam(required = false) String date_val, @RequestParam String pn_id,
			@RequestParam(required = false) String bg_type, @RequestParam(required = false) String date_of_issue, @RequestParam(required = false) String reference_no,
			@RequestParam(required = false) String lc_number, @RequestParam(required = false) String supply_place, @RequestParam(required = false) String s_t_exempted,
			@RequestParam(required = false) String lr_docketno, @RequestParam(required = false) String bg_no, @RequestParam(required = false) String date_of_expiry,
			@RequestParam(required = false) String date_of_ref, @RequestParam(required = false) String lc_issue_date,
			@RequestParam(required = false) List<String> service_code_id,
			@RequestParam(required = false) List<String> hsn_code_id, @RequestParam(required = false) String type_id,//invoice_type_id for type_remarks
			@RequestParam(required = false) List<String> slno) {
		logger.info("EXECUTING METHOD :: updateInvoiceNew ");
		Map<String, Object> response = new HashMap<String, Object>();
		int valueCount = 0;
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE");
			valueCount = invoiceRepository.updateInvoiceEntryInfo(product_desc, remarks, date_of_notification, date_val,
					bg_type, date_of_issue, reference_no, lc_number, supply_place, lr_docketno, bg_no, date_of_expiry,
					date_of_ref, lc_issue_date, modified_by, s_t_exempted, id);
			logger.info("EXECUTING METHOD :: AFTER UPDATING INVOICE");
			logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING IN INVOICE");
			invoiceRepository.adddlyPackingNoteInsertTypeIdbasedonPnId(type_id, modified_by, pn_id);
			logger.info("EXECUTING METHOD :: AFTER UPDATING QSPACKING IN INVOICE");
			response.put("action", "INVOICE_UPDATE");
			response.put("message", (valueCount > 0) ? "Success" : "Not Updated");
			response.put("status", (valueCount > 0) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateInvoiceNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateInvoiceNew ");
		return response;
	}
	
	@PostMapping("/invoice/updateadvnew")
	public @ResponseBody Map<String, Object> updateadvInvoiceNew(@RequestParam Integer id,
			@RequestParam String modified_by, @RequestParam String product_desc, @RequestParam String remarks,
			@RequestParam(required = false) String date_of_notification, @RequestParam(required = false) String date_val, @RequestParam String pn_id,
			@RequestParam(required = false) String bg_type, @RequestParam(required = false) String date_of_issue, @RequestParam(required = false) String reference_no,
			@RequestParam(required = false) String lc_number, @RequestParam(required = false) String supply_place, @RequestParam(required = false) String s_t_exempted,
			@RequestParam(required = false) String lr_docketno, @RequestParam(required = false) String bg_no, @RequestParam(required = false) String date_of_expiry,
			@RequestParam(required = false) String date_of_ref, @RequestParam(required = false) String lc_issue_date,
			@RequestParam(required = false) List<String> service_code_id,
			@RequestParam(required = false) List<String> hsn_code_id, 
			@RequestParam(required = false) List<String> slno) {
		logger.info("EXECUTING METHOD :: updateInvoiceNew ");
		Map<String, Object> response = new HashMap<String, Object>();
		int valueCount = 0;
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE");
			valueCount = invoiceRepository.updateInvoiceEntryInfo(product_desc, remarks, date_of_notification, date_val,
					bg_type, date_of_issue, reference_no, lc_number, supply_place, lr_docketno, bg_no, date_of_expiry,
					date_of_ref, lc_issue_date, modified_by, s_t_exempted, id);
			logger.info("EXECUTING METHOD :: AFTER UPDATING INVOICE");
			logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING IN INVOICE");
			invoiceRepository.addadvPackingNoteItemInsertTypeIdbasedonPnId(modified_by, pn_id);
			logger.info("EXECUTING METHOD :: AFTER UPDATING QSPACKING IN INVOICE");
			response.put("action", "INVOICE_UPDATE");
			response.put("message", (valueCount > 0) ? "Success" : "Not Updated");
			response.put("status", (valueCount > 0) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateInvoiceNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateInvoiceNew ");
		return response;
	}

	@GetMapping("/invoice/listnewdly")
	public @ResponseBody Map<String, Object> listInvoiceInformationNew(@RequestParam String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: listInvoiceInformationNew");
		List<InvoiceMasterInterface> invoiceMasterInterfaces = null;
		try {
			logger.info("EXECUTING METHOD :: BEFORE LISTING INVOICE");
			invoiceMasterInterfaces = invoiceRepository.listdlyInvoiceMasterInfo(factory_id);
			logger.info("EXECUTING METHOD :: AFTER LISTING INVOICE");
			response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER");
			response.put("Data", invoiceMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listInvoiceInformationNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTIED METHOD :: listInvoiceInformationNew");
		return response;
	}
	
	
	
	@GetMapping("/invoice/listnewdlypaged")
	public @ResponseBody Map<String, Object> listInvoiceInformationNewPaged(
	        @RequestParam String factory_id,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(required = false) String search) {

	    Map<String, Object> response = new HashMap<>();
	    logger.info("EXECUTING METHOD :: listInvoiceInformationNewPaged");

	    try {
	        Pageable pageable = PageRequest.of(page, size);

	        Page<InvoiceMasterInterface> pageResult =
	                invoiceRepository.listdlyInvoiceMasterInfoPaged(factory_id, search, pageable);

	        logger.info("TOTAL RECORDS :: " + pageResult.getTotalElements());

	        response.put("message",     pageResult.hasContent() ? "Success" : "failure");
	        response.put("status",      pageResult.hasContent() ? "yes" : "no");
	        response.put("action",      "List_Record_In_INVOICE_MASTER");
	        response.put("Data",        pageResult.getContent());
	        response.put("totalItems",  pageResult.getTotalElements());
	        response.put("currentPage", pageResult.getNumber());
	        response.put("totalPages",  pageResult.getTotalPages());

	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("ERROR IN listInvoiceInformationNewPaged :: " + e.getMessage());
	        response.put("message", "Error occurred");
	        response.put("status",  "error");
	    }

	    logger.info("EXECUTED METHOD :: listInvoiceInformationNewPaged");
	    return response;
	}
	
	
	@GetMapping("/invoice/advlistnew")
	public @ResponseBody Map<String, Object> listadvInvoiceInformationNew(@RequestParam String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: listInvoiceInformationNew");
		List<InvoiceMasterInterface> invoiceMasterInterfaces = null;
		try {
			logger.info("EXECUTING METHOD :: BEFORE LISTING INVOICE");
			invoiceMasterInterfaces = invoiceRepository.listadvInvoiceMasterInfo(factory_id);
			logger.info("EXECUTING METHOD :: AFTER LISTING INVOICE");
			response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER");
			response.put("Data", invoiceMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listInvoiceInformationNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTIED METHOD :: listInvoiceInformationNew");
		return response;
	}

	
	@GetMapping("/invoice/advlistnewss")
	public @ResponseBody Map<String, Object> listadvInvoiceInformationNewss(
	        @RequestParam String factory_id,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(required = false) String search){

	    Map<String, Object> response = new HashMap<>();
	    logger.info("EXECUTING METHOD :: listInvoiceInformationNew");

	    try {

	        Pageable pageable = PageRequest.of(
	                page,
	                size,
	                Sort.by("created_date").descending()
	        );

	        Page<InvoiceMasterInterface> invoicePage =
	                invoiceRepository.listadvInvoiceMasterInfoss(factory_id, search, pageable);
	        response.put("message", "Success");
	        response.put("status", "yes");
	        response.put("action", "List_Record_In_INVOICE_MASTER");

	        response.put("data", invoicePage.getContent());
	        response.put("currentPage", invoicePage.getNumber());
	        response.put("totalItems", invoicePage.getTotalElements());
	        response.put("totalPages", invoicePage.getTotalPages());

	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("ERROR IN listInvoiceInformationNew :: " + e.getMessage());
	        response.put("message", "failure");
	        response.put("status", "no");
	    }

	    logger.info("EXECUTED METHOD :: listInvoiceInformationNew");
	    return response;
	}
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/invoice/verificationlist/invoicenumbernullnew")
	public @ResponseBody Map<String, Object> listVerificationPendingListNew(@RequestParam String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<InvoiceMasterInterface> invoiceMasterInterfaces = null;
		try {
			invoiceMasterInterfaces = invoiceRepository.listInvoiceVerificationInfo();
			response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER");
			response.put("Data", invoiceMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	
	  @GetMapping("/invoice/getcontractqspackingdetailsnew") public @ResponseBody
	  Map<String, Object> listGetContractQspackingNew(@RequestParam String
	  contract_id,
	  
	  @RequestParam String load_id, @RequestParam String factory_id) {
	  logger.info("EXECUTING METHOD :: listGetContractQspackingNew"); Map<String,
	  Object> response = new HashMap<String, Object>(); List<AssignToContract>
	  assignToContract = null; List<TaxMasterInterface> range = null;
	  List<QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
	  List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
	  HashMap<String, String> ls = new HashMap<String, String>(); try { String
	  pn_id = qsPackingRepository.getPnIdBasedOnContractandLoad_id(contract_id,
	  load_id, factory_id); qsPackingInterfaces =
	  qsPackingRepository.searchQSPackingByIdnew(pn_id, factory_id);
	  assignToContract = assignToContractRepository.searchContractNew(contract_id,
	  factory_id); range = assignToContractRepository.searchTaxId(contract_id,
	  factory_id, load_id); String grandTotalValue =
	  assignToContractRepository.getTotalValue(contract_id, factory_id, load_id);
	  ls.put("Grand_Total_tax", grandTotalValue); lsv.add(ls);
	  response.put("message", (assignToContract.size() > 0 &&
	  qsPackingInterfaces.size() > 0 && range.size() > 0) ? "Success" : "failure");
	  response.put("status", (assignToContract.size() > 0 &&
	  qsPackingInterfaces.size() > 0 && range.size() > 0) ? "yes" : "no");
	  response.put("action", "List_Record_In_INVOICE_MASTER");
	  response.put("Contractor_Data", assignToContract);
	  response.put("QsPacking_Data", qsPackingInterfaces);
	  response.put("Grand_Total_value", lsv);
	  
	  response.put("tax", range); } catch (Exception e) { e.printStackTrace();
	  logger.error("ERROR IN THE METHOD FOR listGetContractQspackingNew ::   -> " +
	  e.getMessage()); }
	  logger.info("EXECUTED METHOD :: listGetContractQspackingNew"); return
	  response; }
	  
	  @GetMapping("/invoice/getcontractdeliverychallanqspackingdetailsnew") public @ResponseBody
	  Map<String, Object> listGetContractdlyQspackingNew(@RequestParam String
	  contract_id,
	  
	  @RequestParam String load_id, @RequestParam String factory_id) {
	  logger.info("EXECUTING METHOD :: listGetContractQspackingNew"); Map<String,
	  Object> response = new HashMap<String, Object>(); List<AssignToContract>
	  assignToContract = null; List<TaxMasterInterface> range = null;
	  List<QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
	  List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
	  HashMap<String, String> ls = new HashMap<String, String>(); try { String
	  pn_id = qsPackingRepository.getdlyPnIdBasedOnContractandLoad_id(contract_id,
	  load_id, factory_id); qsPackingInterfaces =
	  qsPackingRepository.searchdlyQSPackingByIdnew(pn_id, factory_id);
	  assignToContract = assignToContractRepository.searchContractNew(contract_id,
	  factory_id); range = assignToContractRepository.searchdlyTaxId(contract_id,
				factory_id, load_id); 
		 String grandTotalValue =
		 assignToContractRepository.getdlyTotalValue(contract_id, factory_id,
		 load_id); ls.put("Grand_Total_tax", grandTotalValue); lsv.add(ls);
		/*
		 * List<Object[]> results =
		 * assignToContractRepository.getdlyTotalValue(contract_id, factory_id,
		 * load_id);
		 * 
		 * if (!results.isEmpty()) {
		 * 
		 * Object[] row = results.get(0); // only one row expected
		 * 
		 * HashMap<String, String> ls1 = new HashMap<>();
		 * 
		 * ls1.put("Base_Value", String.valueOf(row[0]));
		 * ls1.put("Total_Tax_Percentage", String.valueOf(row[1]));
		 * ls1.put("Total_Tax_Value", String.valueOf(row[2]));
		 * ls1.put("Grand_Total_Tax", String.valueOf(row[3]));
		 * 
		 * lsv.add(ls1); // ✅ works too }
		 */
	  lsv.add(ls);
	  response.put("message", (assignToContract.size() > 0 &&
	  qsPackingInterfaces.size() > 0 && range.size() > 0) ? "Success" : "failure");
	  response.put("status", (assignToContract.size() > 0 &&
	  qsPackingInterfaces.size() > 0 && range.size() > 0) ? "yes" : "no");
	  response.put("action", "List_Record_In_INVOICE_MASTER");
	  response.put("Contractor_Data", assignToContract);
	  response.put("QsPacking_Data", qsPackingInterfaces);
	  response.put("Grand_Total_value", lsv);
	  
	  response.put("tax", range); } catch (Exception e) { e.printStackTrace();
	  logger.error("ERROR IN THE METHOD FOR listGetContractQspackingNew ::   -> " +
	  e.getMessage()); }
	  logger.info("EXECUTED METHOD :: listGetContractQspackingNew"); return
	  response; }
	 
	
	@GetMapping("/invoice/getcontractqsadvancepackingdetailsnew")
	public @ResponseBody Map<String, Object> listGetContractQsadvancepackingNew(@RequestParam String contract_id,@RequestParam String load_id,
			@RequestParam String factory_id) {
		logger.info("EXECUTING METHOD :: listGetContractQsadvancepackingNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<AssignToContract> assignToContract = null;
		List<TaxMasterInterface> range = null;
		List<QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
		List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> ls = new HashMap<String, String>();
		try {
			String pn_id = qsPackingRepository.getadvPnIdBasedOnContract_id(contract_id, load_id);
			qsPackingInterfaces = qsPackingRepository.searchQSadvPackingByIdnew(pn_id, factory_id);
			assignToContract = assignToContractRepository.searchContractNew(contract_id, factory_id);
			range = assignToContractRepository.searchadvanceTaxId(contract_id, factory_id, pn_id);
			String grandTotalValue = assignToContractRepository.getadvTotalValue(pn_id, factory_id);
			ls.put("Grand_Total_tax", grandTotalValue);
			lsv.add(ls);
			response.put("message",
					(assignToContract.size() > 0 && qsPackingInterfaces.size() > 0 && range.size() > 0) ? "Success"
							: "failure");
			response.put("status",
					(assignToContract.size() > 0 && qsPackingInterfaces.size() > 0 && range.size() > 0) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER");
			response.put("Contractor_Data", assignToContract);
			response.put("QsPacking_Data", qsPackingInterfaces);
			response.put("Grand_Total_value", lsv);

			response.put("tax", range);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listGetContractQsadvancepackingNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listGetContractQsadvancepackingNew");
		return response;
	}

	 
//	@GetMapping("/invoice/searchidnew")
//	public @ResponseBody Map<String, Object> listSearchByIdNew(@RequestParam String id) {
//		logger.info("EXECUTING METHOD :: listSearchByIdNew");
//		Map<String, Object> response = new HashMap<String, Object>();
//		InvoiceMasterInterface invoiceMasterInterfaces = null;
//		List<AssignToContract> assignToContract = null;
//		List<TaxMasterInterface> range = null;
//		List<QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
//		List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
//		HashMap<String, String> ls = new HashMap<String, String>();
//		try {
//			invoiceMasterInterfaces = invoiceRepository.listSearchById(id);
//			String load_id = invoiceMasterInterfaces.getLoad_id();
//			String contract_id = invoiceMasterInterfaces.getContract_id();
//			String factory_id = invoiceMasterInterfaces.getFactory_id();
//			String pn_id = qsPackingRepository.getPnIdBasedOnContract_id(contract_id, load_id);
//			qsPackingInterfaces = qsPackingRepository.searchQSPackingByIdnew(pn_id, factory_id);
//			assignToContract = assignToContractRepository.searchContractNew(contract_id, factory_id);
//			range = assignToContractRepository.searchTaxId(contract_id, factory_id, load_id);
//			List<String> grandTot = assignToContractRepository.getTotalValueList(contract_id, factory_id, load_id);
//			String dataVal = grandTot.get(0);
//			String[] grandTotal_Value = dataVal.split(",");
//			String grandTotalValue = grandTotal_Value[0];
//			String tax_Final_total = grandTotal_Value[1];
//			String taxable_final_total = grandTotal_Value[2];
//			String grandTotalInWords = assignToContractRepository.getgrandTotalInWordsQuery(grandTotalValue);
//			float total_tax_adv = 0.0f;
//			float total_taxable = 0.0f;
//			float optd = 0.0f;
//			float ptc = 0.0f;
//			for (TaxMasterInterface tx : range) {
//				total_tax_adv = total_tax_adv + Float.parseFloat(tx.getAdv_tax());
//				total_taxable = Float.parseFloat(tx.getTaxable());
//				optd = Float.parseFloat(tx.getOptc());
//				ptc = ptc + Float.parseFloat(tx.getPtc());
//			}
//			total_tax_adv = total_tax_adv + total_taxable;
//			ls.put("Grand_Total_tax", grandTotalValue);
//			ls.put("GrandTotalInWords", grandTotalInWords);
//			ls.put("Tax_Final_total", String.valueOf(ptc));
//			ls.put("PBC_Final_total", String.valueOf(ptc + optd));
//			ls.put("Tax_with_basic_final_total", taxable_final_total);
//			ls.put("Total_adv_tax_Taxable", String.valueOf(total_tax_adv));
//			lsv.add(ls);
//			response.put("Grand_Total_value", lsv);
//			response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
//			response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
//			response.put("action", "List_Record_In_INVOICE_MASTER");
//			response.put("Invoice_Data", invoiceMasterInterfaces);
//			response.put("Contractor_Data", assignToContract);
//			response.put("QsPacking_Data", qsPackingInterfaces);
//			response.put("tax", range);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("ERROR IN THE METHOD FOR listSearchByIdNew ::   -> " + e.getMessage());
//		}
//		logger.info("EXECUTED METHOD :: listSearchByIdNew");
//		return response;
//	}
	 
	
	//New api changed by ajad 14-02-2026

	@GetMapping("/invoice/searchidnew")
	public @ResponseBody Map<String, Object> listSearchByIdNew(@RequestParam String id) {
		logger.info("EXECUTING METHOD :: listSearchByIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		InvoiceMasterInterface invoiceMasterInterfaces = null;
		List<AssignToContract> assignToContract = null;
		List<TaxMasterInterface> range = null;
		List<QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
		List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> ls = new HashMap<String, String>();
		try {
			invoiceMasterInterfaces = invoiceRepository.listSearchById(id);
			String load_id = invoiceMasterInterfaces.getLoad_id();
			String contract_id = invoiceMasterInterfaces.getContract_id();
			String factory_id = invoiceMasterInterfaces.getFactory_id();
			String pn_id = qsPackingRepository.getPnIdBasedOnContract_id(contract_id, load_id,factory_id);
			qsPackingInterfaces = qsPackingRepository.searchQSPackingByIdnew(pn_id, factory_id);
			assignToContract = assignToContractRepository.searchContractNew(contract_id, factory_id);
			range = assignToContractRepository.searchTaxId(contract_id, factory_id, load_id);
			List<String> grandTot = assignToContractRepository.getTotalValueList(contract_id, factory_id, load_id);
			String dataVal = grandTot.get(0);
			String[] grandTotal_Value = dataVal.split(",");
			String grandTotalValue = grandTotal_Value[0];
			String tax_Final_total = grandTotal_Value[1];
			String taxable_final_total = grandTotal_Value[2];
			String grandTotalInWords = assignToContractRepository.getgrandTotalInWordsQuery(grandTotalValue);
			float total_tax_adv = 0.0f;
			float total_taxable = 0.0f;
			float optd = 0.0f;
			float ptc = 0.0f;
			for (TaxMasterInterface tx : range) {
				total_tax_adv = total_tax_adv + Float.parseFloat(tx.getAdv_tax());
				total_taxable = Float.parseFloat(tx.getTaxable());
				optd = Float.parseFloat(tx.getOptc());
				ptc = ptc + Float.parseFloat(tx.getPtc());
			}
			total_tax_adv = total_tax_adv + total_taxable;
			ls.put("Grand_Total_tax", grandTotalValue);
			ls.put("GrandTotalInWords", grandTotalInWords);
			ls.put("Tax_Final_total", String.valueOf(ptc));
			ls.put("PBC_Final_total", String.valueOf(ptc + optd));
			ls.put("Tax_with_basic_final_total", taxable_final_total); //
			ls.put("Total_adv_tax_Taxable", String.valueOf(total_tax_adv));
			ls.put("Total_adv_tax_Taxable", String.format("%.2f", total_tax_adv));
			lsv.add(ls);
			response.put("Grand_Total_value", lsv);
			response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER");
			response.put("Invoice_Data", invoiceMasterInterfaces);
			response.put("Contractor_Data", assignToContract);
			response.put("QsPacking_Data", qsPackingInterfaces);
			response.put("tax", range);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listSearchByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listSearchByIdNew");
		return response;
	}

	
	@GetMapping("/invoice/searchidcancel")
	public @ResponseBody Map<String, Object> listSearchByIdcancel(@RequestParam String id) {
		logger.info("EXECUTING METHOD :: listSearchByIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		InvoiceMasterInterface invoiceMasterInterfaces = null;
		List<AssignToContract> assignToContract = null;
		List<TaxMasterInterface> range = null;
		List<QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
		List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> ls = new HashMap<String, String>();
		try {
			invoiceMasterInterfaces = invoiceRepository.listSearchById(id);
			String load_id = invoiceMasterInterfaces.getLoad_id();
			String contract_id = invoiceMasterInterfaces.getContract_id();
			String factory_id = invoiceMasterInterfaces.getFactory_id();
			String pn_id = qsPackingRepository.getPnIdBasedOncontractcancel(contract_id, load_id,factory_id);
			qsPackingInterfaces = qsPackingRepository.searchQSPackingByIdnew(pn_id, factory_id);
			assignToContract = assignToContractRepository.searchContractNew(contract_id, factory_id);
			range = assignToContractRepository.searchTaxIdcancel(contract_id, factory_id, load_id);		
			List<String> grandTot = assignToContractRepository.getTotalValueListcancel(contract_id, factory_id, load_id);
			String dataVal = grandTot.get(0);
			String[] grandTotal_Value = dataVal.split(",");
			String grandTotalValue = grandTotal_Value[0];
			String tax_Final_total = grandTotal_Value[1];
			String taxable_final_total = grandTotal_Value[2];
			String grandTotalInWords = assignToContractRepository.getgrandTotalInWordsQuery(grandTotalValue);
			float total_tax_adv = 0.0f;
			float total_taxable = 0.0f;
			float optd = 0.0f;
			float ptc = 0.0f;
			for(TaxMasterInterface tx : range) {
				total_tax_adv = total_tax_adv+Float.parseFloat(tx.getAdv_tax());
				total_taxable = Float.parseFloat(tx.getTaxable());
				optd = Float.parseFloat(tx.getOptc());
				ptc = ptc + Float.parseFloat(tx.getPtc());
			}
			total_tax_adv = total_tax_adv + total_taxable;
			ls.put("Grand_Total_tax", grandTotalValue);
			ls.put("GrandTotalInWords", grandTotalInWords);
			ls.put("Tax_Final_total", String.valueOf(ptc));
			ls.put("PBC_Final_total", String.valueOf(ptc+optd));
			ls.put("Tax_with_basic_final_total", taxable_final_total);
			ls.put("Total_adv_tax_Taxable", String.valueOf(total_tax_adv));
			lsv.add(ls);
			response.put("Grand_Total_value", lsv);
			response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER");
			response.put("Invoice_Data", invoiceMasterInterfaces);
			response.put("Contractor_Data", assignToContract);
			response.put("QsPacking_Data", qsPackingInterfaces);
			response.put("tax", range);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listSearchByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listSearchByIdNew");
		return response;
	}
	
	@GetMapping("/invoice/searchdlyidnew")
	public @ResponseBody Map<String, Object> listSearchdlyByIdNew(@RequestParam String id) {
		logger.info("EXECUTING METHOD :: listSearchByIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		InvoiceMasterInterface invoiceMasterInterfaces = null;
		List<AssignToContract> assignToContract = null;
		List<TaxMasterInterface> range = null;
		List<QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
		List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> ls = new HashMap<String, String>();
		try {
			invoiceMasterInterfaces = invoiceRepository.listSearchById(id);
			String load_id = invoiceMasterInterfaces.getLoad_id();
			String contract_id = invoiceMasterInterfaces.getContract_id();
			String factory_id = invoiceMasterInterfaces.getFactory_id();
			String pn_id = qsPackingRepository.getdlyPnIdBasedOnContract_id(contract_id, load_id);
			qsPackingInterfaces = qsPackingRepository.searchdlyQSPackingByIdnew(pn_id, factory_id);
			assignToContract = assignToContractRepository.searchContractNew(contract_id, factory_id);
			range = assignToContractRepository.searchDeliveryTaxId(contract_id, factory_id, load_id);		
			List<String> grandTot = assignToContractRepository.getdlyTotalValueList(contract_id, factory_id, load_id);
			String dataVal = grandTot.get(0);
			String[] grandTotal_Value = dataVal.split(",");
			String grandTotalValue = grandTotal_Value[0];
			String tax_Final_total = grandTotal_Value[1];
			String taxable_final_total = grandTotal_Value[2];
			String grandTotalInWords = assignToContractRepository.getgrandTotalInWordsQuery(grandTotalValue);
			float total_tax_adv = 0.0f;
			float total_taxable = 0.0f;
			float optd = 0.0f;
			float ptc = 0.0f;
			for(TaxMasterInterface tx : range) {
				total_tax_adv = total_tax_adv+Float.parseFloat(tx.getAdv_tax());
				total_taxable = Float.parseFloat(tx.getTaxable());
				optd = Float.parseFloat(tx.getOptc());
				ptc = ptc + Float.parseFloat(tx.getPtc());
			}
			total_tax_adv = total_tax_adv + total_taxable;
			ls.put("Grand_Total_tax", grandTotalValue);
			ls.put("GrandTotalInWords", grandTotalInWords);
			ls.put("Tax_Final_total", String.valueOf(ptc));
			ls.put("PBC_Final_total", String.valueOf(ptc+optd));
			ls.put("Tax_with_basic_final_total", taxable_final_total);
			ls.put("Total_adv_tax_Taxable", String.valueOf(total_tax_adv));
			lsv.add(ls);
			response.put("Grand_Total_value", lsv);
			response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER");
			response.put("Invoice_Data", invoiceMasterInterfaces);
			response.put("Contractor_Data", assignToContract);
			response.put("QsPacking_Data", qsPackingInterfaces);
			response.put("tax", range);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listSearchByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listSearchByIdNew");
		return response;
	}
	
	@GetMapping("/invoice/searchadvidnew")
	public @ResponseBody Map<String, Object> listSearchadvByIdNew(@RequestParam String id) {
		logger.info("EXECUTING METHOD :: listSearchByIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		InvoiceMasterInterface invoiceMasterInterfaces = null;
		List<AssignToContract> assignToContract = null;
		List<TaxMasterInterface> range = null;
		List<QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
		List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> ls = new HashMap<String, String>();
		try {
			invoiceMasterInterfaces = invoiceRepository.listSearchById(id);
			String load_id = invoiceMasterInterfaces.getLoad_id();
			String contract_id = invoiceMasterInterfaces.getContract_id();
			String factory_id = invoiceMasterInterfaces.getFactory_id();
			String pn_id = qsPackingRepository.getadvPnIdBasedOnContract_id(contract_id,load_id);
			qsPackingInterfaces = qsPackingRepository.searchQSadvPackingByIdnew(pn_id, factory_id);
			assignToContract = assignToContractRepository.searchContractNew(contract_id, factory_id);
			range = assignToContractRepository.searchadvTaxId(contract_id, factory_id, pn_id);		
			List<String> grandTot = assignToContractRepository.getadvTotalValueList(contract_id, factory_id,load_id);
			String dataVal = grandTot.get(0);
			String[] grandTotal_Value = dataVal.split(",");
			String grandTotalValue = grandTotal_Value[0];
			String tax_Final_total = grandTotal_Value[1];
			String taxable_final_total = grandTotal_Value[2];
			String grandTotalInWords = assignToContractRepository.getgrandTotalInWordsQuery(grandTotalValue);
			float total_tax_adv = 0.0f;
			float total_taxable = 0.0f;
			float optd = 0.0f;
			float ptc = 0.0f;
			for(TaxMasterInterface tx : range) {
				total_tax_adv = total_tax_adv+Float.parseFloat(tx.getAdv_tax());
				total_taxable = Float.parseFloat(tx.getTaxable());
				optd = Float.parseFloat(tx.getOptc());
				ptc = ptc + Float.parseFloat(tx.getPtc());
			}
			total_tax_adv = total_tax_adv + total_taxable;
			ls.put("Grand_Total_tax", grandTotalValue);
			ls.put("GrandTotalInWords", grandTotalInWords);
			ls.put("Tax_Final_total", String.valueOf(ptc));
			ls.put("PBC_Final_total", String.valueOf(ptc+optd));
			ls.put("Tax_with_basic_final_total", taxable_final_total);
			ls.put("Total_adv_tax_Taxable", String.valueOf(total_tax_adv));
			lsv.add(ls);
			response.put("Grand_Total_value", lsv);
			response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER");
			response.put("Invoice_Data", invoiceMasterInterfaces);
			response.put("Contractor_Data", assignToContract);
			response.put("QsPacking_Data", qsPackingInterfaces);
			response.put("tax", range);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listSearchByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listSearchByIdNew");
		return response;
	}

	// Rejected Function
	@PostMapping("/invoice/deletebyIdsnew")
	public @ResponseBody Map<String, Object> listDeleteByIdNew(@RequestParam String id,
			@RequestParam String rejected_by, @RequestParam int pn_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: listDeleteByIdNew");
		try {
			invoiceRepository.updateRejectedById(rejected_by, id);
			invoiceRepository.insertIntoInvoiceHistoryisDelete(id, rejected_by);
			int count = qsPackingRepository.invoiceQSDelete(pn_id);
			int count1 = qsPackingRepository.invoiceQSItemDelete(pn_id);
			qsPackingRepository.updateIsDeleteQSPackingMasterHistoryRecord(rejected_by, String.valueOf(pn_id));
			qsPackingRepository.IsDeleteQSPackingItemMasterHistoryRecord(rejected_by, String.valueOf(pn_id));
			// >>>>>> delete the data from INVOICE_MASTER, QSPACKING_MASTER,
			// QSPACKING_ITEM_MASTER
			// invoiceRepository.listDeleteById(id);
			invoiceRepository.deleteInvoiceMasterBasedOnId(id);
			qsPackingRepository.deleteQSPackingBasedOnPnId(String.valueOf(pn_id));
			qsPackingRepository.deleteQSPackingItemBasedOnPnId(String.valueOf(pn_id));
			response.put("message", (count > 0 && count1 > 0) ? "Success" : "failure");
			response.put("status", (count > 0 && count1 > 0) ? "yes" : "no");
			response.put("action", "DELETE_Record_In_INVOICE_MASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listDeleteByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listDeleteByIdNew");
		return response;
	}

	@PostMapping("/invoice/releasebyidnew")
	public @ResponseBody Map<String, Object> releaseByIdNew(@RequestParam String id, @RequestParam String factory_id,
			@RequestParam String released_by, @RequestParam int pn_id) {
		logger.info("EXECUTING METHOD :: releaseByIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<Float> total_balance;
		Float total_bal = 0.0f;
		float avl_bal = 0.0f;
		try {
			int count = invoiceRepository.UpdateReleaseById(id, released_by);
			long contact_id = invoiceRepository.getContractIdFromInvoiceMaster(Integer.parseInt(id), factory_id);
			//OPENING BALANCE SUBTRACTION 
			String pn_idvalue = invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contact_id);
			total_balance = invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);
			total_bal = total_balance.orElse((float) 0);
			String open_tax_adv = invoiceRepository.getRecoveryAmountfromInvoice(id);
			String open_non_tax_adv = invoiceRepository.getOpenNonTaxAdvFromInvoice(id);
			total_bal = total_balance.orElse((float) 0);
			
			if( total_bal < 0 ) {
				response.put("message", "Please Create the Opening Balance");
				return response;
			}
			float taxValue = invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contact_id);
			float adjustmentAmount;

			if (Float.parseFloat(open_tax_adv) > 0) {

			    float value = Float.parseFloat(open_tax_adv) * (taxValue / 100);

			    adjustmentAmount = value + Float.parseFloat(open_tax_adv);

			} else {

			    adjustmentAmount = Float.parseFloat(open_non_tax_adv);
			}

			if (total_bal >= adjustmentAmount) {

			    avl_bal = total_bal + adjustmentAmount;

			} else {

//			    response.put("message", "Insufficient Balance Please check!");
//			    return response;
			}
			invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(pn_idvalue, avl_bal, released_by);
			qsPackingRepository.updateisLockinQSPacking(pn_id, "0");
			qsPackingRepository.updateisLockinQSPackingItem(pn_id, "0");
			qsPackingRepository.updateisLockinadvQSPacking(pn_id, "0");
			qsPackingRepository.updateisLockinadvQSPackingItem(pn_id, "0");
			qsPackingRepository.updateisLockindlyQSPacking(pn_id, "0");
			qsPackingRepository.updateisLockindlyQSPackingItem(pn_id, "0");
			assignToContractRepository.updateISReleaseInContractMaster(contact_id, "0", factory_id);
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "RELEASED_Record_In_INVOICE_MASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR releaseByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: releaseByIdNew");
		return response;
	}

	
	@PostMapping("/invoice/advreleasebyidnew")
	public @ResponseBody Map<String, Object> advreleaseByIdNew(@RequestParam String id, @RequestParam String factory_id,
			@RequestParam String released_by, @RequestParam int pn_id) {
		logger.info("EXECUTING METHOD :: releaseByIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<Float> total_balance;
		Float total_bal = 0.0f;
		float avl_bal = 0.0f;
		try {
			int count = invoiceRepository.UpdateReleaseById(id, released_by);
			long contact_id = invoiceRepository.getContractIdFromInvoiceMaster(Integer.parseInt(id), factory_id);
			//OPENING BALANCE SUBTRACTION 
			String pn_idvalue = invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contact_id);
			total_balance = invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);
			total_bal = total_balance.orElse((float) 0);
			String open_tax_adv = invoiceRepository.getRecoveryAmountfromInvoice(id);
			float taxValue = invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contact_id);
//			float value = (open_tax_adv *( taxValue / 100));
//			avl_bal = (total_bal + open_tax_adv +value);
//			invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(pn_idvalue, avl_bal, released_by);
			qsPackingRepository.updateisLockinQSPacking(pn_id, "0");
			qsPackingRepository.updateisLockinQSPackingItem(pn_id, "0");
			qsPackingRepository.updateisLockinadvQSPacking(pn_id, "0");
			qsPackingRepository.updateisLockinadvQSPackingItem(pn_id, "0");
			qsPackingRepository.updateisLockindlyQSPacking(pn_id, "0");
			qsPackingRepository.updateisLockindlyQSPackingItem(pn_id, "0");
			assignToContractRepository.updateISReleaseInContractMaster(contact_id, "0", factory_id);
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "RELEASED_Record_In_INVOICE_MASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR releaseByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: releaseByIdNew");
		return response;
	}
	@PostMapping("/invoice/verificationnew")
	private @ResponseBody Map<String, Object> invoiceVerficationNew(@RequestParam String verified_by,
			@RequestParam String gst_remarks,
			@RequestParam(required = false) String non_tax_adv, @RequestParam(required = false) String tax_adv,
			@RequestParam(required = false) String qs_packing_item_slno, @RequestParam String total,
			@RequestParam String payable_by_customer, @RequestParam String payable_to_dept,
			@RequestParam String open_tax_adv, @RequestParam String open_non_tax_adv,
			@RequestParam(required = false) String recovery_amt, @RequestParam String factory_id, @RequestParam int id,
			@RequestParam int pn_id, @RequestParam List<String> tax_id, @RequestParam List<String> tax_per,
			@RequestParam List<String> tax_value, @RequestParam List<String> adv_tax,
			@RequestParam List<String> tax_payable_by_customer, @RequestParam List<String> tax_payable_to_dept,
			@RequestParam(required = false) List<String> t_adv) {
		logger.info("EXECUTING METHOD :: invoiceVerficationNew");
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		Optional<Float> total_balance;
		float total_bal = 0;
		float avl_bal = 0;
		String newSeriesNumber = null;
		try {
			long contact_id = invoiceRepository.getContractIdFromInvoiceMaster(id, factory_id);
			Boolean  isRelease = invoiceRepository.getIsReleaseFromInvoiceMaster(id, factory_id);
			System.out.println("isRelease value = " + isRelease);
			Optional<Long> optionalSeriesNumber = invoiceRepository.getSeriesNumberbasedOnId(id);
			long serieNumber = optionalSeriesNumber.orElse((long) 0);
			if (serieNumber < 1) {
				response.put("message", "Please Generate Series Number");
				return response;
			}
			String seriesNumberInString = String.valueOf(serieNumber);
			StringBuilder sbc = new StringBuilder();
			sbc.append(seriesNumberInString.substring(0, Math.min(5, seriesNumberInString.length())));
			seriesNumberInString = sbc.toString();
			Optional<Long> optionalInvoiceId = invoiceRepository.getInvoiceNumber(seriesNumberInString);
			long invoiceNumber = optionalInvoiceId.orElse((long) 0);
			if (optionalInvoiceId.isPresent()) {
				// if invoice_no already generate and it's release and coming for approve once again.
				String num = String.valueOf(invoiceNumber);
				int size = num.lastIndexOf("0000");
				String partAfterZero = null;
				if (size != -1) {
					partAfterZero = num.substring(size + 4);
					int a = Integer.parseInt(partAfterZero);
					a = a + 1;
					partAfterZero = String.valueOf(a);
				}
				Long incrementedValue = Long.parseLong(partAfterZero);
				newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
				//OPENING BALANCE SUBTRACTION 
				String pn_idvalue = invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contact_id);
				total_balance = invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);
				total_bal = total_balance.orElse((float) 0);
				
				if( total_bal < 0 ) {
					response.put("message", "Please Create the Opening Balance");
					return response;
				}
				float taxValue = invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contact_id);
				float adjustmentAmount;

				if (Float.parseFloat(open_tax_adv) > 0) {

				    float value = Float.parseFloat(open_tax_adv) * (taxValue / 100);

				    adjustmentAmount = value + Float.parseFloat(open_tax_adv);

				} else {

				    adjustmentAmount = Float.parseFloat(open_non_tax_adv);
				}

				if (total_bal >= adjustmentAmount) {

				    avl_bal = total_bal - adjustmentAmount;

				} else {

				    response.put("message", "Insufficient Balance Please check!");
				    return response;
				}
				logger.info("EXECUTING METHOD :: BEFORE UPDATING OPENING BALANCE");
				invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(pn_idvalue, avl_bal, verified_by);
				logger.info("EXECUTED METHOD :: AFTER UPDATING OPENING BALANCE");
				logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE VERIFICATION ");
				if (isRelease) {
					count = invoiceRepository.updateInvoiceVerificationDetails_is_release(non_tax_adv, tax_adv, total,
							payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks, verified_by,
							id);
				}
				else
				{
					count = invoiceRepository.updateInvoiceVerificationDetails(non_tax_adv, tax_adv, total,
							payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks, verified_by,
							newSeriesNumber, id);
				}
				logger.info("EXECUTED METHOD :: AFTER UPDATING INVOICE VERIFICATION ");
				logger.info("EXECUTING METHOD :: BEFORE UPDATING CONTRACT MASTER LOCKING ");
				assignToContractRepository.updateISReleaseInContractMaster(contact_id, "1", factory_id);
				logger.info("EXECUTED METHOD :: AFTER UPDATING CONTRACT MASTER LOCKING ");
				logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING MASTER LOCKING ");
				qsPackingRepository.updateisLockinQSPacking(pn_id, "1");
				logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING MASTER LOCKING ");
				logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING ITEM MASTER LOCKING ");
				qsPackingRepository.updateisLockinQSPackingItem(pn_id, "1");
				logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING ITEM MASTER LOCKING ");
				int sizeRange = tax_id.size();
				logger.info("EXECUTING METHOD :: BEFORE ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
				for (int i = 0; i < sizeRange; i++) {
					invoiceRepository.insertINVOICE_TAXENTRY_DETAILSNew(tax_id.get(i), tax_per.get(i), tax_value.get(i),
							adv_tax.get(i), tax_payable_by_customer.get(i), tax_payable_to_dept.get(i),String.valueOf(id), verified_by);
				}
				logger.info("EXECUTED METHOD :: AFTER ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
			} else {
					String num = String.valueOf(serieNumber);
					if (num != null && !num.equals("0")) {
						int size = num.lastIndexOf("0000");
						String partAfterZero = null;
						if (size != -1) {
							partAfterZero = num.substring(size + 4);
							int a = Integer.parseInt(partAfterZero);
							a = a + 1;
							partAfterZero = String.valueOf(a);
						}
						Long incrementedValue = Long.parseLong(partAfterZero);
						newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
						//OPENING BALANCE SUBTRACTION 
						String pn_idvalue = invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contact_id);
						total_balance = invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);
						total_bal = total_balance.orElse((float) 0);
						
						if( total_bal < 0 ) {
							response.put("message", "Please Create the Opening Balance");
							return response;
						}
						float taxValue = invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contact_id);
						float adjustmentAmount;

						if (Float.parseFloat(open_tax_adv) > 0) {

						    float value = Float.parseFloat(open_tax_adv) * (taxValue / 100);

						    adjustmentAmount = value + Float.parseFloat(open_tax_adv);

						} else {

						    adjustmentAmount = Float.parseFloat(open_non_tax_adv);
						}

						if (total_bal >= adjustmentAmount) {

						    avl_bal = total_bal - adjustmentAmount;

						} else {

						    response.put("message", "Insufficient Balance Please check!");
						    return response;
						}
						logger.info("EXECUTING METHOD :: BEFORE UPDATING OPENING BALANCE FIRST TIME");
						invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(pn_idvalue, avl_bal, verified_by);
						logger.info("EXECUTED METHOD :: AFTER UPDATING OPENING BALANCE FIRST TIME");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE VERIFICATION FIRST TIME");
						if (isRelease) {
							count = invoiceRepository.updateInvoiceVerificationDetails_is_release(non_tax_adv, tax_adv, total,
									payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks,
									verified_by, id);
						}
						else
						{
							count = invoiceRepository.updateInvoiceVerificationDetails(non_tax_adv, tax_adv, total,
									payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks,
									verified_by, newSeriesNumber, id);
						}
						logger.info("EXECUTED METHOD :: AFTER UPDATING INVOICE VERIFICATION FIRST TIME");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING CONTRACT MASTER LOCKING FIRST TIME");
						assignToContractRepository.updateISReleaseInContractMaster(contact_id, "1", factory_id);
						logger.info("EXECUTED METHOD :: AFTER UPDATING CONTRACT MASTER LOCKING FIRST TIME");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING MASTER LOCKING FIRST TIME");
						qsPackingRepository.updateisLockinQSPacking(pn_id, "1");
						logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING MASTER LOCKING FIRST TIME");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING ITEM MASTER LOCKING FIRST TIME");
						qsPackingRepository.updateisLockinQSPackingItem(pn_id, "1");
						logger.info("EXECUTING METHOD :: AFTER UPDATING QSPACKING MASTER ITEM LOCKING FIRST TIME");
						int sizeRange = tax_id.size();
						logger.info("EXECUTED METHOD :: BEFORE ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
						for (int i = 0; i < sizeRange; i++) {
							invoiceRepository.insertINVOICE_TAXENTRY_DETAILSNew(tax_id.get(i), tax_per.get(i),
									tax_value.get(i), adv_tax.get(i), tax_payable_by_customer.get(i),
									tax_payable_to_dept.get(i), String.valueOf(id), verified_by);
						}
						logger.info("EXECUTED METHOD :: AFTER ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
					} else {
						response.put("message", "Please Generate Series Number");
						return response;
					}
			}
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "VERIFICATION_Record_In_INVOICE_MASTER");
			response.put("Invoice_NO", newSeriesNumber);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR releaseByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: invoiceVerficationNew");
		return response;
	}
	
	@PostMapping("/invoice/verificationdlynew")
	private @ResponseBody Map<String, Object> invoicedlyVerficationNew(@RequestParam String verified_by,
			@RequestParam String gst_remarks,
			@RequestParam(required = false) String non_tax_adv, @RequestParam(required = false) String tax_adv,
			@RequestParam(required = false) String qs_packing_item_slno, @RequestParam String total,
			@RequestParam String payable_by_customer, @RequestParam String payable_to_dept,
			@RequestParam String open_tax_adv, @RequestParam String open_non_tax_adv,
			@RequestParam(required = false) String recovery_amt, @RequestParam String factory_id, @RequestParam int id,
			@RequestParam int pn_id, @RequestParam List<String> tax_id, @RequestParam List<String> tax_per,
			@RequestParam List<String> tax_value, @RequestParam List<String> adv_tax,
			@RequestParam List<String> tax_payable_by_customer, @RequestParam List<String> tax_payable_to_dept,
			@RequestParam(required = false) List<String> t_adv) {
		logger.info("EXECUTING METHOD :: invoiceVerficationNew");
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		Optional<Float> total_balance;
		float total_bal = 0;
		float avl_bal = 0;
		String newSeriesNumber = null;
		try {
			long contact_id = invoiceRepository.getContractIdFromInvoiceMaster(id, factory_id);
			Boolean  isRelease = invoiceRepository.getIsReleaseFromInvoiceMaster(id, factory_id);
			Optional<Long> optionalSeriesNumber = invoiceRepository.getdlySeriesNumberbasedOnId(id);
			long serieNumber = optionalSeriesNumber.orElse((long) 0);
			if (serieNumber < 1) {
				response.put("message", "Please Generate Series Number");
				return response;
			}
			String seriesNumberInString = String.valueOf(serieNumber);
			StringBuilder sbc = new StringBuilder();
			sbc.append(seriesNumberInString.substring(0, Math.min(5, seriesNumberInString.length())));
			seriesNumberInString = sbc.toString();
			Optional<Long> optionalInvoiceId = invoiceRepository.getInvoiceNumber(seriesNumberInString);
			long invoiceNumber = optionalInvoiceId.orElse((long) 0);
			if (optionalInvoiceId.isPresent()) {
				// if invoice_no already generate and it's release and coming for approve once again.
				String num = String.valueOf(invoiceNumber);
				int size = num.lastIndexOf("0000");
				String partAfterZero = null;
				if (size != -1) {
					partAfterZero = num.substring(size + 4);
					int a = Integer.parseInt(partAfterZero);
					a = a + 1;
					partAfterZero = String.valueOf(a);
				}
				Long incrementedValue = Long.parseLong(partAfterZero);
				newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
				//OPENING BALANCE SUBTRACTION 
				String pn_idvalue = invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contact_id);
				total_balance = invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);
				total_bal = total_balance.orElse((float) 0);
				if(total_bal < 0 ) {
					response.put("message", "Please Create the Opening Balance");
					return response;
				}
				float taxValue = invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contact_id);
				float value = (Float.parseFloat(open_tax_adv)) *( taxValue / 100);
				if(total_bal >= (value + Float.parseFloat(open_tax_adv))) {
					avl_bal = (total_bal - (value + Float.parseFloat(open_tax_adv)));
				}else {
					response.put("message","Insufficient Balance Please check!");
					return response;
				}
				logger.info("EXECUTING METHOD :: BEFORE UPDATING OPENING BALANCE");
//				invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(pn_idvalue, avl_bal, verified_by);
				logger.info("EXECUTED METHOD :: AFTER UPDATING OPENING BALANCE");
				logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE VERIFICATION ");
				if (isRelease) {
				count = invoiceRepository.updateInvoiceVerificationDetails_is_release(non_tax_adv, tax_adv, total,
						payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks, verified_by,
						id);
				}
				else
				{
					count = invoiceRepository.updateInvoiceVerificationDetails(non_tax_adv, tax_adv, total,
							payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks, verified_by,
							newSeriesNumber, id);
				}
				logger.info("EXECUTED METHOD :: AFTER UPDATING INVOICE VERIFICATION ");
				logger.info("EXECUTING METHOD :: BEFORE UPDATING CONTRACT MASTER LOCKING ");
				assignToContractRepository.updateISReleaseInContractMaster(contact_id, "1", factory_id);
				logger.info("EXECUTED METHOD :: AFTER UPDATING CONTRACT MASTER LOCKING ");
				logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING MASTER LOCKING ");
				qsPackingRepository.updateisLockindlyQSPacking(pn_id, "1");
				logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING MASTER LOCKING ");
				logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING ITEM MASTER LOCKING ");
				qsPackingRepository.updateisLockindlyQSPackingItem(pn_id, "1");
				logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING ITEM MASTER LOCKING ");
				int sizeRange = tax_id.size();
				logger.info("EXECUTING METHOD :: BEFORE ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
				for (int i = 0; i < sizeRange; i++) {
					invoiceRepository.insertINVOICE_TAXENTRY_DETAILSNew(tax_id.get(i), tax_per.get(i), tax_value.get(i),
							adv_tax.get(i), tax_payable_by_customer.get(i), tax_payable_to_dept.get(i),String.valueOf(id), verified_by);
				}
				logger.info("EXECUTED METHOD :: AFTER ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
			} else {
					String num = String.valueOf(serieNumber);
					if (num != null && !num.equals("0")) {
						int size = num.lastIndexOf("0000");
						String partAfterZero = null;
						if (size != -1) {
							partAfterZero = num.substring(size + 4);
							int a = Integer.parseInt(partAfterZero);
							a = a + 1;
							partAfterZero = String.valueOf(a);
						}
						Long incrementedValue = Long.parseLong(partAfterZero);
						newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
						//OPENING BALANCE SUBTRACTION 
						String pn_idvalue = invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contact_id);
						total_balance = invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);
						total_bal = total_balance.orElse((float) 0);
						if(total_bal < 0 ) {
							response.put("message", "Please Create the Opening Balance");
							return response;
						}
						float taxValue = invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contact_id);
						float value = (Float.parseFloat(open_tax_adv)) *( taxValue / 100);
						if(total_bal >= (value + Float.parseFloat(open_tax_adv))) {
							avl_bal = (total_bal - (value + Float.parseFloat(open_tax_adv)));
						}else {
							response.put("message","Insufficient Balance Please check!");
							return response;
						}
						logger.info("EXECUTING METHOD :: BEFORE UPDATING OPENING BALANCE FIRST TIME");
//						invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(pn_idvalue, avl_bal, verified_by);
						logger.info("EXECUTED METHOD :: AFTER UPDATING OPENING BALANCE FIRST TIME");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE VERIFICATION FIRST TIME");
						if (isRelease) {
							count = invoiceRepository.updateInvoiceVerificationDetails_is_release(non_tax_adv, tax_adv, total,
									payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks, verified_by,
									id);
							}
							else
							{
								count = invoiceRepository.updateInvoiceVerificationDetails(non_tax_adv, tax_adv, total,
										payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks, verified_by,
										newSeriesNumber, id);
							}
						logger.info("EXECUTED METHOD :: AFTER UPDATING INVOICE VERIFICATION FIRST TIME");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING CONTRACT MASTER LOCKING FIRST TIME");
						assignToContractRepository.updateISReleaseInContractMaster(contact_id, "1", factory_id);
						logger.info("EXECUTED METHOD :: AFTER UPDATING CONTRACT MASTER LOCKING FIRST TIME");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING MASTER LOCKING FIRST TIME");
						qsPackingRepository.updateisLockinQSPacking(pn_id, "1");
						logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING MASTER LOCKING FIRST TIME");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING ITEM MASTER LOCKING FIRST TIME");
						qsPackingRepository.updateisLockinQSPackingItem(pn_id, "1");
						logger.info("EXECUTING METHOD :: AFTER UPDATING QSPACKING MASTER ITEM LOCKING FIRST TIME");
						int sizeRange = tax_id.size();
						logger.info("EXECUTED METHOD :: BEFORE ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
						for (int i = 0; i < sizeRange; i++) {
							invoiceRepository.insertINVOICE_TAXENTRY_DETAILSNew(tax_id.get(i), tax_per.get(i),
									tax_value.get(i), adv_tax.get(i), tax_payable_by_customer.get(i),
									tax_payable_to_dept.get(i), String.valueOf(id), verified_by);
						}
						logger.info("EXECUTED METHOD :: AFTER ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
					} else {
						response.put("message", "Please Generate Series Number");
						return response;
					}
			}
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "VERIFICATION_Record_In_INVOICE_MASTER");
			response.put("Invoice_NO", newSeriesNumber);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR releaseByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: invoiceVerficationNew");
		return response;
	}
	
	
	@PostMapping("/invoice/advverificationnew")
	private @ResponseBody Map<String, Object> advinvoiceVerficationNew(@RequestParam String verified_by,
			@RequestParam String gst_remarks,
			@RequestParam(required = false) String non_tax_adv, @RequestParam(required = false) String tax_adv,
			@RequestParam(required = false) String qs_packing_item_slno, @RequestParam String total,
			@RequestParam String payable_by_customer, @RequestParam String payable_to_dept,
			@RequestParam String open_tax_adv, @RequestParam String open_non_tax_adv,
			@RequestParam(required = false) String recovery_amt, @RequestParam String factory_id, @RequestParam int id,
			@RequestParam int pn_id, @RequestParam List<String> tax_id, @RequestParam List<String> tax_per,
			@RequestParam List<String> tax_value, @RequestParam List<String> adv_tax,
			@RequestParam List<String> tax_payable_by_customer, @RequestParam List<String> tax_payable_to_dept,
			@RequestParam(required = false) List<String> t_adv) {
		logger.info("EXECUTING METHOD :: invoiceVerficationNew");
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		Optional<Float> total_balance;
		float total_bal = 0;
		float avl_bal = 0;
		String newSeriesNumber = null;
		try {
			long contact_id = invoiceRepository.getContractIdFromInvoiceMaster(id, factory_id);
			Boolean  isRelease = invoiceRepository.getIsReleaseFromInvoiceMaster(id, factory_id);
			System.out.println("isRelease value = " + isRelease);
			//long isRelease = invoiceRepository.getContractIdFromInvoiceMaster(id, factory_id);
			Optional<Long> optionalSeriesNumber = invoiceRepository.getadvSeriesNumberbasedOnId(id);
			long serieNumber = optionalSeriesNumber.orElse((long) 0);
			if (serieNumber < 1) {
				response.put("message", "Please Generate Series Number");
				return response;
			}
			String seriesNumberInString = String.valueOf(serieNumber);
			StringBuilder sbc = new StringBuilder();
			sbc.append(seriesNumberInString.substring(0, Math.min(5, seriesNumberInString.length())));
			seriesNumberInString = sbc.toString();
			Optional<Long> optionalInvoiceId = invoiceRepository.getInvoiceNumber(seriesNumberInString);
			long invoiceNumber = optionalInvoiceId.orElse((long) 0);
			if (optionalInvoiceId.isPresent()) {
				// if invoice_no already generate and it's release and coming for approve once again.
				String num = String.valueOf(invoiceNumber);
				int size = num.lastIndexOf("0000");
				String partAfterZero = null;
				if (size != -1) {
					partAfterZero = num.substring(size + 4);
					int a = Integer.parseInt(partAfterZero);
					a = a + 1;
					partAfterZero = String.valueOf(a);
				}
				Long incrementedValue = Long.parseLong(partAfterZero);
				newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
				//OPENING BALANCE SUBTRACTION 
				String pn_idvalue = invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contact_id);
				total_balance = invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);
				total_bal = total_balance.orElse((float) 0);
//				if(total_bal < 0 ) {
//					response.put("message", "Please Create the Opening Balance");
//					return response;
//				}
				float taxValue = invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contact_id);
				float value = (Float.parseFloat(open_tax_adv)) *( taxValue / 100);
				if(total_bal >= (value + Float.parseFloat(open_tax_adv))) {
					avl_bal = (total_bal - (value + Float.parseFloat(open_tax_adv)));
				}else {
					response.put("message","Insufficient Balance Please check!");
					return response;
				}
				String open_tax_adv1="0";
				String open_non_tax_adv1="0";
//				logger.info("EXECUTING METHOD :: BEFORE UPDATING OPENING BALANCE");
//				invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(pn_idvalue, avl_bal, verified_by);
//				logger.info("EXECUTED METHOD :: AFTER UPDATING OPENING BALANCE");
				logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE VERIFICATION ");
				if (isRelease) {
					count = invoiceRepository.updateInvoiceVerificationDetails_is_release(non_tax_adv, tax_adv, total,
							payable_by_customer, payable_to_dept, open_tax_adv1, open_non_tax_adv1, gst_remarks, verified_by,
							id);
				}
				else
				{
				count = invoiceRepository.updateInvoiceVerificationDetails(non_tax_adv, tax_adv, total,
						payable_by_customer, payable_to_dept, open_tax_adv1, open_non_tax_adv1, gst_remarks, verified_by,
						newSeriesNumber, id);
				}
			   
				logger.info("EXECUTED METHOD :: AFTER UPDATING INVOICE VERIFICATION ");
				logger.info("EXECUTING METHOD :: BEFORE UPDATING CONTRACT MASTER LOCKING ");
				assignToContractRepository.updateISReleaseInContractMaster(contact_id, "1", factory_id);
				logger.info("EXECUTED METHOD :: AFTER UPDATING CONTRACT MASTER LOCKING ");
				logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING MASTER LOCKING ");
				qsPackingRepository.updateisLockinadvQSPacking(pn_id, "1");
				logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING MASTER LOCKING ");
				logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING ITEM MASTER LOCKING ");
				qsPackingRepository.updateisLockinadvQSPackingItem(pn_id, "1");
				logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING ITEM MASTER LOCKING ");
				int sizeRange = tax_id.size();
				logger.info("EXECUTING METHOD :: BEFORE ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
				for (int i = 0; i < sizeRange; i++) {
					invoiceRepository.insertINVOICE_TAXENTRY_DETAILSNew(tax_id.get(i), tax_per.get(i), tax_value.get(i),
							adv_tax.get(i), tax_payable_by_customer.get(i), tax_payable_to_dept.get(i),String.valueOf(id), verified_by);
				}
				logger.info("EXECUTED METHOD :: AFTER ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
			} else {
					String num = String.valueOf(serieNumber);
					if (num != null && !num.equals("0")) {
						int size = num.lastIndexOf("0000");
						String partAfterZero = null;
						if (size != -1) {
							partAfterZero = num.substring(size + 4);
							int a = Integer.parseInt(partAfterZero);
							a = a + 1;
							partAfterZero = String.valueOf(a);
						}
						Long incrementedValue = Long.parseLong(partAfterZero);
						newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
						//OPENING BALANCE SUBTRACTION 
						String pn_idvalue = invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contact_id);
						total_balance = invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);
						total_bal = total_balance.orElse((float) 0);
						if(total_bal < 0 ) {
							response.put("message", "Please Create the Opening Balance");
							return response;
						}
						float taxValue = invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contact_id);
						float value = (Float.parseFloat(open_tax_adv)) *( taxValue / 100);
						if(total_bal >= (value + Float.parseFloat(open_tax_adv))) {
							avl_bal = (total_bal - (value + Float.parseFloat(open_tax_adv)));
						}else {
							response.put("message","Insufficient Balance Please check!");
							return response;
						}
						logger.info("EXECUTING METHOD :: BEFORE UPDATING OPENING BALANCE FIRST TIME");
//						invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(pn_idvalue, avl_bal, verified_by);
						logger.info("EXECUTED METHOD :: AFTER UPDATING OPENING BALANCE FIRST TIME");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE VERIFICATION FIRST TIME");
						if (isRelease) {
							count = invoiceRepository.updateInvoiceVerificationDetails_is_release(non_tax_adv, tax_adv, total,
									payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks, verified_by,
									id);
						}
						else
						{
						count = invoiceRepository.updateInvoiceVerificationDetails(non_tax_adv, tax_adv, total,
								payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks, verified_by,
								newSeriesNumber, id);
						}
						logger.info("EXECUTED METHOD :: AFTER UPDATING INVOICE VERIFICATION FIRST TIME");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING CONTRACT MASTER LOCKING FIRST TIME");
						assignToContractRepository.updateISReleaseInContractMaster(contact_id, "1", factory_id);
						logger.info("EXECUTED METHOD :: AFTER UPDATING CONTRACT MASTER LOCKING FIRST TIME");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING MASTER LOCKING FIRST TIME");
						qsPackingRepository.updateisLockinadvQSPacking(pn_id, "1");
						logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING MASTER LOCKING FIRST TIME");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING ITEM MASTER LOCKING FIRST TIME");
						qsPackingRepository.updateisLockinadvQSPackingItem(pn_id, "1");
						logger.info("EXECUTING METHOD :: AFTER UPDATING QSPACKING MASTER ITEM LOCKING FIRST TIME");
						int sizeRange = tax_id.size();
						logger.info("EXECUTED METHOD :: BEFORE ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
						for (int i = 0; i < sizeRange; i++) {
							invoiceRepository.insertINVOICE_TAXENTRY_DETAILSNew(tax_id.get(i), tax_per.get(i),
									tax_value.get(i), adv_tax.get(i), tax_payable_by_customer.get(i),
									tax_payable_to_dept.get(i), String.valueOf(id), verified_by);
						}
						logger.info("EXECUTED METHOD :: AFTER ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
					} else {
						response.put("message", "Please Generate Series Number");
						return response;
					}
			}
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "VERIFICATION_Record_In_INVOICE_MASTER");
			response.put("Invoice_NO", newSeriesNumber);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR releaseByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: invoiceVerficationNew");
		return response;
	}
	
	@GetMapping("/getlistcontractorsfromqspackingnew")
	public @ResponseBody Map<String, Object> getlistContractorfromQsPacking(@RequestParam int factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: getlistContractorfromQsPacking");
		try {
			List<ListAssignMilesonetoContractors> count = qsPackingRepository.getlistContractorfromQs(factory_id);
			response.put("action", "List Contractor assigned QSPACKING");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("DATA", count);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getlistContractorfromQsPacking ::   -> " + e.getMessage());
		}
		logger.info("EXECUTIED METHOD :: getlistContractorfromQsPacking");
		return response;
	}
	
	@GetMapping("/getlistcontractorsfromqsadvpackingnew")
	public @ResponseBody Map<String, Object> getlistContractorfromQsadvPacking(@RequestParam int factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: getlistContractorfromQsPacking");
		try {
			List<ListAssignMilesonetoContractors> count = qsPackingRepository.getlistContractorfromQsadv(factory_id);
			response.put("action", "List Contractor assigned QSPACKING");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("DATA", count);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getlistContractorfromQsPacking ::   -> " + e.getMessage());
		}
		logger.info("EXECUTIED METHOD :: getlistContractorfromQsPacking");
		return response;
	}
	
	@GetMapping("/getlistcontractorsfromqschallanpackingnew")
	public @ResponseBody Map<String, Object> getlistcontractorsfromqschallanpackingnew(@RequestParam int factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: getlistContractorfromQsPacking");
		try {
			List<ListAssignMilesonetoContractors> count = qsPackingRepository.getlistContractorfromQschallan(factory_id);
			response.put("action", "List Contractor assigned QSPACKING");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("DATA", count);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getlistContractorfromQsPacking ::   -> " + e.getMessage());
		}
		logger.info("EXECUTIED METHOD :: getlistContractorfromQsPacking");
		return response;
	}

	@GetMapping("/getlistloadidforthatcontractorsfromqspackingnew")
	public @ResponseBody Map<String, Object> getlistloadfromContractorfromQsPacking(@RequestParam int factory_id,
			@RequestParam String con_id) {
		logger.info("EXECUTING METHOD :: getlistContractorfromQsPacking");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<ListAssignMilesonetoContractors> count = qsPackingRepository.getlistloadContractorfromQs(factory_id,con_id);
			response.put("action", "List loads assigned contractors");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("DATA", count);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getlistContractorfromQsPacking ::   -> " + e.getMessage());
		}
		logger.info("EXECUTIED METHOD :: getlistContractorfromQsPacking");
		return response;
	}
	
	@GetMapping("/getlistloadidforthatcontractorsfromqsadvpackingnew")
	public @ResponseBody Map<String, Object> getlistloadfromContractorfromQsadvPacking(@RequestParam int factory_id,
			@RequestParam String contract_id) {
		logger.info("EXECUTING METHOD :: getlistContractorfromQsPacking");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<ListAssignMilesonetoContractors> count = qsPackingRepository.getadvlistloadContractorfromQs(factory_id,contract_id);
			response.put("action", "List loads assigned contractors");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("DATA", count);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getlistContractorfromQsPacking ::   -> " + e.getMessage());
		}
		logger.info("EXECUTIED METHOD :: getlistContractorfromQsPacking");
		return response;
	}
	
	@GetMapping("/getlistloadidforthatcontractorsfromqsdeliverypackingnew")
	public @ResponseBody Map<String, Object> getlistloadfromContractorfromQsdlyPacking(@RequestParam int factory_id,
			@RequestParam String contract_id) {
		logger.info("EXECUTING METHOD :: getlistContractorfromQsPacking");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<ListAssignMilesonetoContractors> count = qsPackingRepository.getdlylistloadContractorfromQs(factory_id,contract_id);
			response.put("action", "List loads assigned contractors");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("DATA", count);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getlistContractorfromQsPacking ::   -> " + e.getMessage());
		}
		logger.info("EXECUTIED METHOD :: getlistContractorfromQsPacking");
		return response;
	}

	@GetMapping("/invoicemaster/invoice_hsn_service")
	public @ResponseBody Map<String, Object> serachQSPackingItemId(String stype, String hcode, String pn_id,
			String modified_by) {
		logger.info("EXECUTING METHOD :: serachQSPackingItemId");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			int value = invoiceRepository.updateQSPAckingItem(stype, hcode, pn_id, modified_by);
			response.put("message", (value > 0) ? "Success" : "failure");
			response.put("status", (value > 0) ? "yes" : "no");
			response.put("action", "Search_Record_In_QSPACKINGITEMMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR serachQSPackingItemId ::   -> " + e.getMessage());
		}
		logger.info("EXECUTIED METHOD :: serachQSPackingItemId");
		return response;
	}

	@GetMapping("/invoicemaster/invoicetypelist")
	public @ResponseBody Map<String, Object> listScrapType() {
		logger.info("EXECUTING METHOD :: listScrapType");
		Map<String, Object> response = new HashMap<String, Object>();
		List<String> value = null;
		try {
			value = invoiceRepository.listInvoiceMaster();
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER");
			response.put("UOM_LIST", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listScrapType ::   -> " + e.getMessage());
		}
		logger.info("EXECUTIED METHOD :: listScrapType");
		return response;
	}

	
	@GetMapping("/invoice/listbgtype")
	public @ResponseBody Map<String, Object> listBGType() {
		logger.info("EXECUTING METHOD :: listBGType");
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, String>> value = null;
		try {
			value = invoiceRepository.bgTypeList();
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER_BGTYPE");
			response.put("BG_LIST", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listBGType ::   -> " + e.getMessage());
		}
		logger.info("EXECUTIED METHOD :: listBGType");
		return response;
	}

	@GetMapping("/invoicemaster/listinvoicetype")
	public @ResponseBody Map<String, Object> listInvoice_type() {
		logger.info("EXECUTING METHOD :: listInvoice_type");
		Map<String, Object> response = new HashMap<String, Object>();
		List<InvoiceTypeInterfaces> value = null;
		try {
			value = invoiceRepository.listInvoiceType();
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_TYPE_LIST");
			response.put("INVOICE_TYPE_LIST", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listInvoice_type ::   -> " + e.getMessage());
		}
		logger.info("EXECUTIED METHOD :: listInvoice_type");
		return response;
	}

	
	@GetMapping("/invoice/verificationlist")
	public @ResponseBody Map<String, Object> listVerificationPendingList() {
		logger.info("EXECUTING METHOD :: listVerificationPendingList");
		Map<String, Object> response = new HashMap<String, Object>();
		List<InvoiceMasterInterface> invoiceMasterInterfaces = null;
		try {
			invoiceMasterInterfaces = invoiceRepository.listInvoiceVerificationInfo();
			response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER");
			response.put("Data", invoiceMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listVerificationPendingList ::   -> " + e.getMessage());
		}
		logger.info("EXECUTIED METHOD :: listVerificationPendingList");
		return response;
	}
	
	@PostMapping("/Otherspacking/addothersPackingNote")
	public @ResponseBody Map<String, Object> addothersPackingNote(@RequestParam String freight, @RequestParam String file_path, @RequestParam String factory_id,
			@RequestParam List<String> uom_id, @RequestParam List<String> type_id, @RequestParam List<String> quantity, @RequestParam List<String> kgs,
			@RequestParam List<String> unit_price, @RequestParam List<String> total,@RequestParam String buyer_ref,@RequestParam String otherstype_id,
			@RequestParam List<String> remarkstype_id, @RequestParam String totalpn_amount,@RequestParam Date date,@RequestParam String Transporter_name,@RequestParam String vechile_no,
			@RequestParam("file") MultipartFile file, @RequestParam String created_by) {

		Map<String, Object> addothersPackingNotemap = new HashMap<String, Object>();
		String uniqueFileName = null;
		int insertPackingNoteItemrec = 0;
		String newothersLoad = null;
		long maxSize = 10 * 1024 * 1024;
		int size = quantity.size();
		if (size != kgs.size() || size != unit_price.size() || size != total.size() || size != uom_id.size()
				|| size != type_id.size()) {
			addothersPackingNotemap.put("message", "All input lists must have the same size!");
			return addothersPackingNotemap;
		}
		try {

			String fileName = file.getOriginalFilename();
			if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
				addothersPackingNotemap.put("message", "Only PDF files are allowed.");
				return addothersPackingNotemap;
			}

			if (file.getSize() > maxSize) {
				addothersPackingNotemap.put("message", "File size exceeds the maximum limit of 10MB.");
				addothersPackingNotemap.put("status", "no");
				return addothersPackingNotemap;
			}

			// uniqueFileName = file.getOriginalFilename();
			uniqueFileName = UUID.randomUUID() + "_@$@_" + file.getOriginalFilename();
			File directory = new File(uploadDir);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			File serverFile = new File(directory, uniqueFileName);
			if (serverFile.exists()) {
				addothersPackingNotemap.put("message", "File already exists! Please provide another file.");
				return addothersPackingNotemap;
			}

			// Save the file to the server
			file.transferTo(serverFile);

			String lastothersLoad = invoiceRepository.findLastotherLoad();

			if (lastothersLoad != null) {
				int numericPart = Integer.parseInt(lastothersLoad.replace("OTH" + "-", ""));
				int digitLength = lastothersLoad.split("-")[1].length();
				numericPart++;
			
					newothersLoad = String.format("OTH-%0" + digitLength + "d", numericPart);
			
			} else {
		
					"OTH".equals("OTH"); 
					newothersLoad = "OTH-00001"; // Default for CDT
			}

			System.out.println("Generated newothersLoad: " + newothersLoad);

			int insertToPackingNoterec = invoiceRepository.insertIntothersPackingNote(newothersLoad,
					freight, buyer_ref,  otherstype_id, Transporter_name, vechile_no, date, uniqueFileName, factory_id, created_by, totalpn_amount);

			String other_pn_id = invoiceRepository.getothers_pnidFromPackingNote(newothersLoad);
			insertPackingNoteItemrec = 0;
			logger.info("EXECUTING METHOD :: BEFORE CREATE otherpacking ITEM");
			for (int i = 0; i < size; i++) {
				  logger.info("INSERT INDEX = " + i);
				insertPackingNoteItemrec = invoiceRepository.insertothersPackingNoteItemDetails(other_pn_id,
						newothersLoad, uom_id.get(i), quantity.get(i), type_id.get(i), kgs.get(i), unit_price.get(i), total.get(i),
						"1", created_by);

			}

			addothersPackingNotemap.put("message",
					(insertToPackingNoterec > 0 && insertPackingNoteItemrec > 0) ? "Success"
							: " Scrap Debit/Credit Packing note not created");
			addothersPackingNotemap.put("status", (insertToPackingNoterec > 0 && insertPackingNoteItemrec > 0) ? "yes" : "no");
			addothersPackingNotemap.put("action", "CreateScrapDebit/CreditPackingNote");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return addothersPackingNotemap;

	}
	
	@GetMapping("/Otherspacking/getAllothersPackingNoteDetails")
	public @ResponseBody Map<String, Object> getAllSteelPackingNotes(@RequestParam String factory_id) {

		Map<String, Object> allSteelDebitCreditPackingNotemap = new HashMap<>();

		try {
			List<othersPackNoteinterface> packingNoteInfoList = invoiceRepository
					.getAllothersPackingNoteDetails(factory_id);
			List<String> packingNoteNumbers = invoiceRepository.getAllPackingNoteNumberForothers(factory_id);

			Map<String, List<othersPackNoteIteminterface>> itemMap = new HashMap<>();
			if(packingNoteNumbers.isEmpty())
			{
				return allSteelDebitCreditPackingNotemap;
			}
			
			for (String pnNo : packingNoteNumbers) {
				List<othersPackNoteIteminterface> items = invoiceRepository.getothersPackingNoteItemsDetails(pnNo);
				if (items != null && !items.isEmpty()) {
					itemMap.put(pnNo, items);
				}
			}
		

			List<Map<String, Object>> resultList = new ArrayList<>();
			for (othersPackNoteinterface note : packingNoteInfoList) {
				Map<String, Object> noteMap = new HashMap<>();
				noteMap.put("con_name", note.getConId());
				noteMap.put("oth_pn_id", note.getOtherPnId());
				noteMap.put("oth_Pn_no", note.getOtherPnNo());
				noteMap.put("Transporter_name", note.getTransporterName());
				noteMap.put("freight", note.getFreight());
				noteMap.put("totalpn_amount", note.getTotalPnAmount());
				noteMap.put("filepath", note.getFilepath());
				noteMap.put("INVGEN", note.getinvgen());

				List<othersPackNoteIteminterface> items = itemMap.getOrDefault(note.getOtherPnNo(),
						new ArrayList<>());
				noteMap.put("DebitCreditPackingNoteItems", items);

				resultList.add(noteMap);
			}

			allSteelDebitCreditPackingNotemap.put("action", "AllSteelPackingNoteDetails");

			if (!resultList.isEmpty()) {
				allSteelDebitCreditPackingNotemap.put("message", "Success");
				allSteelDebitCreditPackingNotemap.put("status", "yes");
				allSteelDebitCreditPackingNotemap.put("SteelDebitCreditPackingNoteInfo", resultList);
			} else {
				allSteelDebitCreditPackingNotemap.put("message", "SteelDebitCreditPackingNoteDetailsNotFound");
				allSteelDebitCreditPackingNotemap.put("status", "no");
			}

		} catch (Exception e) {
			e.printStackTrace();
			allSteelDebitCreditPackingNotemap.put("message", "Error occurred while fetching packing note details.");
			allSteelDebitCreditPackingNotemap.put("status", "error");
		}

		return allSteelDebitCreditPackingNotemap;
	}
	
	
	
	
	@GetMapping("/Otherspacking/getAllothersPackingNoteDetailsPaged")
	public @ResponseBody Map<String, Object> getAllothersPackingNotesPaged(
	        @RequestParam String factory_id,
	        @RequestParam(defaultValue = "") String search,   // ✅ add search
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    Map<String, Object> response = new HashMap<>();
	    logger.info("EXECUTING METHOD :: getAllothersPackingNotesPaged");

	    try {
	        //  Step 1 - Get paginated notes
	        Pageable pageable = PageRequest.of(page, size);
	        Page<othersPackNoteinterface> pageResult =
	                invoiceRepository.getAllothersPackingNoteDetailsPaged(
	                        factory_id,
	                        search,      //  pass search
	                        pageable
	                );
	        logger.info("TOTAL RECORDS :: " + pageResult.getTotalElements());

	        //  Step 2 - Extract pn numbers from current page
	        List<String> pnNos = pageResult.getContent()
	                .stream()
	                .map(othersPackNoteinterface::getOtherPnNo)
	                .filter(pn -> pn != null && !pn.isEmpty())
	                .collect(Collectors.toList());

	        //  Step 3 - Single batch query for all items
	        Map<String, List<othersPackNoteIteminterface>> itemMap = new HashMap<>();
	        if (!pnNos.isEmpty()) {
	            List<othersPackNoteIteminterface> allItems =
	                    invoiceRepository.getothersPackingNoteItemsDetailsBatch(pnNos);

	            itemMap = allItems.stream()
	                    .collect(Collectors.groupingBy(
	                            othersPackNoteIteminterface::getOtherPnNo));
	        }

	        //  Step 4 - Build result list
	        List<Map<String, Object>> resultList = new ArrayList<>();
	        for (othersPackNoteinterface note : pageResult.getContent()) {
	            Map<String, Object> noteMap = new HashMap<>();
	            noteMap.put("con_name",         note.getConId());
	            noteMap.put("oth_pn_id",        note.getOtherPnId());
	            noteMap.put("oth_Pn_no",        note.getOtherPnNo());
	            noteMap.put("Transporter_name", note.getTransporterName());
	            noteMap.put("freight",          note.getFreight());
	            noteMap.put("totalpn_amount",   note.getTotalPnAmount());
	            noteMap.put("filepath",         note.getFilepath());
	            noteMap.put("INVGEN",           note.getinvgen());
	            noteMap.put("DebitCreditPackingNoteItems",
	                    itemMap.getOrDefault(note.getOtherPnNo(), new ArrayList<>()));
	            resultList.add(noteMap);
	        }

	        //  Step 5 - Build response
	        response.put("action",  "AllSteelPackingNoteDetails");
	        response.put("message", resultList.isEmpty() ? "SteelDebitCreditPackingNoteDetailsNotFound" : "Success");
	        response.put("status",  resultList.isEmpty() ? "no" : "yes");
	        response.put("SteelDebitCreditPackingNoteInfo", resultList);
	        response.put("totalItems",   pageResult.getTotalElements());
	        response.put("currentPage",  pageResult.getNumber());
	        response.put("totalPages",   pageResult.getTotalPages());

	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("ERROR in getAllothersPackingNotesPaged :: " + e.getMessage());
	        response.put("message", "Error occurred while fetching packing note details.");
	        response.put("status",  "error");
	    }

	    logger.info("EXECUTED METHOD :: getAllothersPackingNotesPaged");
	    return response;
	}

	@GetMapping("/othersinvoice/getLoadListForTheContractor")
	public @ResponseBody Map<String, Object> getscraplistloadfromContractorfromQsPacking(@RequestParam int factory_id
			) {
		//logger.info("EXECUTING METHOD :: getlistContractorfromQsPacking");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<Listotherloadsinterface> count = invoiceRepository.getothersloadContractorfromQs(factory_id);
			response.put("action", "List loads assigned others");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("DATA", count);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error("ERROR IN THE METHOD FOR getlistContractorfromQsPacking ::   -> " + e.getMessage());
		}
		//logger.info("EXECUTIED METHOD :: getlistContractorfromQsPacking");
		return response;
	}
	
	  @GetMapping("/invoice/getotherspackingdetailsnew") public @ResponseBody
	  Map<String, Object> listGetotherspackingNew( @RequestParam String load_id, @RequestParam String factory_id) {
	  logger.info("EXECUTING METHOD :: listGetContractQspackingNew"); Map<String,
	  Object> response = new HashMap<String, Object>(); 
	  List<othersPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
	  List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
	  HashMap<String, String> ls = new HashMap<String, String>(); try { String
	  pn_id = qsPackingRepository.getPnIdBasedOnothersLoad_id( load_id, factory_id); qsPackingInterfaces =
	  qsPackingRepository.searchothersPackingByIdnew(pn_id, factory_id);
	  response.put("QsPacking_Data", qsPackingInterfaces);
	  response.put("Grand_Total_value", lsv);
	   } catch (Exception e) { e.printStackTrace();
	  logger.error("ERROR IN THE METHOD FOR listGetContractQspackingNew ::   -> " +
	  e.getMessage()); }
	  logger.info("EXECUTED METHOD :: listGetContractQspackingNew"); return
	  response; 
	  }

	  @PostMapping("/invoice/addnewothers")
	  public @ResponseBody Map<String, Object> createothersInvoiceNew(
	          @RequestParam String con_id,
	          @RequestParam(required = false)  String invoice_no,
	          @RequestParam String load_id,
	          @RequestParam Double grand_total,
	          @RequestParam String product_desc,
	          @RequestParam(required = false) String remarks,
	          @RequestParam(required = false) String export,
	          @RequestParam(required = false) String date_of_notification,
	          @RequestParam(required = false) String date_val,
	          @RequestParam(required = false) String bg_type,
	          @RequestParam(required = false) String date_of_issue,
	          @RequestParam(required = false) String reference_no,
	          @RequestParam(required = false) String lc_number,
	          @RequestParam(required = false) String supply_place,
	          @RequestParam(required = false) String s_t_exempted,
	          @RequestParam(required = false) String lr_docketno,
	          @RequestParam(required = false) String bg_no,
	          @RequestParam(required = false) String date_of_expiry,
	          @RequestParam(required = false) String date_of_ref,
	          @RequestParam(required = false) String lc_issue_date,
	          @RequestParam String created_by,
	          @RequestParam(required = false) String created_date,
	          @RequestParam(required = false) String modified_by,
	          @RequestParam(required = false) String modified_date,
	          @RequestParam(required = false) Boolean is_delete,
	          @RequestParam(required = false) Boolean is_release,
	          @RequestParam(required = false) Integer factory_id,
	          @RequestParam(required = false) Boolean is_verified,
	          @RequestParam(required = false) String verified_by,
	          @RequestParam(required = false) String verified_status,
	          @RequestParam(required = false) String payable_to_dept,
	          @RequestParam(required = false) String payable_by_customer,
	          @RequestParam(required = false) String released_by,
	          @RequestParam(required = false) String released_datetime,
	          @RequestParam(required = false) String gst_remarks,
	          @RequestParam(required = false) Double tax1,
	          @RequestParam(required = false) Double tax1_per,
	          @RequestParam(required = false) Double tax1_value,
	          @RequestParam(required = false) Double tax2,
	          @RequestParam(required = false) Double tax2_per,
	          @RequestParam(required = false) Double tax2_value,
	          @RequestParam(required = false) Double tax3,
	          @RequestParam(required = false) Double tax3_per,
	          @RequestParam(required = false) Double tax3_value,
	          @RequestParam(required = false) Double total_tax,
	          @RequestParam(required = false) Double net_amount,
	          @RequestParam(required = false) String invoice_remarks_id,
	          @RequestParam(required = false) Integer business_unit_id,
	          @RequestParam(required = false) Integer invoice_to_id,
	          @RequestParam(required = false) String prod_desc,
	          @RequestParam(required = false) Integer ship_mode_id,
	          @RequestParam(required = false) Integer workorder_id,
	          @RequestParam(required = false) String area_number,
	          @RequestParam(required = false) String lot_number,
	          @RequestParam(required = false) String container_name,
	          @RequestParam(required = false) String epcg_license,
	          @RequestParam(required = false) String export_title_text_id,
	          @RequestParam(required = false) String tax_onpayable_rev,
	          @RequestParam(required = false) Double percentage,
	          @RequestParam(required = false) Integer consignee_id,
	          @RequestParam(required = false) Integer bank_id,
	          @RequestParam(required = false) Integer deliverycondition_id,
	          @RequestParam(required = false) Integer registered_office_id,
	          @RequestParam(required = false) Integer servicecode_id,
	          @RequestParam(required = false) Integer hsncode_id
	       
	  ) {
	      logger.info("EXECUTING METHOD :: createothersInvoiceNew");
	      Map<String, Object> response = new HashMap<>();
	      otherinvoice invoiceObj = null;

	      try {
	    	  otherinvoice invoice = new otherinvoice();
	    	  invoice.setCon_id(con_id);
	          invoice.setInvoice_no(invoice_no);
	          invoice.setLoad_id(load_id);
	          invoice.setGrand_total(grand_total);
	          invoice.setProduct_desc(product_desc);
	          invoice.setRemarks(remarks);
	          invoice.setDate_of_notification(date_of_notification);
	          invoice.setDate_val(date_val);
	          invoice.setBg_type(bg_type);
	          invoice.setDate_of_issue(date_of_issue);
	          invoice.setReference_no(reference_no);
	          invoice.setLc_number(lc_number);
	          invoice.setSupply_place(supply_place);
	          invoice.setS_t_exempted(s_t_exempted);
	          invoice.setLr_docketno(lr_docketno);
	          invoice.setBg_no(bg_no);
	          invoice.setDate_of_expiry(date_of_expiry);
	          invoice.setDate_of_ref(date_of_ref);
	          invoice.setLc_issue_date(lc_issue_date);
	          invoice.setCreated_by(created_by);
	          invoice.setFactory_id(factory_id);
	          invoice.setGst_remarks(gst_remarks);
	          invoice.setTax1(tax1);
	          invoice.setTax1_per(tax1_per);
	          invoice.setTax1_value(tax1_value);
	          invoice.setTax2(tax2);
	          invoice.setTax2_per(tax2_per);
	          invoice.setTax2_value(tax2_value);
	          invoice.setTax3(tax3);
	          invoice.setTax3_per(tax3_per);
	          invoice.setTax3_value(tax3_value);
	          invoice.setTotal_tax(total_tax);
	          invoice.setNet_amount(net_amount);
	          invoice.setBusiness_Unit_id(business_unit_id);
	          invoice.setInvoice_to_id(invoice_to_id);
	          invoice.setProdDesc(prod_desc);
	          invoice.setShip_mode_id(ship_mode_id);
	          invoice.setWorkorder_id(workorder_id);
	          invoice.setArea_number(area_number);
	          invoice.setLot_number(lot_number);
	          invoice.setContainer_name(container_name);
	          invoice.setEPCG_license(epcg_license);
	          invoice.setExport_title_text_id(export_title_text_id);
	          invoice.setTaxOnpayableRev(tax_onpayable_rev);
	          invoice.setPercentage(percentage);
	          invoice.setConsignee_id(consignee_id);
	          invoice.setBank_id(bank_id);
	          invoice.setDeliverycondition_id(deliverycondition_id);
	          invoice.setRegistered_office_id(registered_office_id);
	          invoice.setServicecode_id(servicecode_id);
	          invoice.setHSNcode_id(hsncode_id);
	          invoice.setIs_release(false);
	          invoice.setExport(export);
	          invoice.setInvoice_remarks_id(invoice_remarks_id);
	          invoice.setCreated_date(LocalDateTime.now());

	          logger.info("BEFORE saving INVOICE");
			  invoiceObj = otherinvoiceRepository.save(invoice); 
	          logger.info("AFTER saving INVOICE");

	          response.put("action", "INVOICE_ADD");
	          response.put("message", (invoiceObj != null) ? "Success" : "Failure");
	          response.put("status", (invoiceObj != null) ? "yes" : "no");

	      } catch (Exception e) {
	          e.printStackTrace();
	          logger.error("ERROR IN THE METHOD createDlyInvoiceNew :: -> " + e.getMessage());
	          response.put("action", "INVOICE_ADD");
	          response.put("message", e.getMessage());
	          response.put("status", "error");
	      }

	      logger.info("EXECUTED METHOD :: createDlyInvoiceNew");
	      return response;
	  }
	  
	  @GetMapping("/invoice/listnew")
		public @ResponseBody Map<String, Object> listInvoiInformationNew(@RequestParam String factory_id) {
			Map<String, Object> response = new HashMap<String, Object>();
			logger.info("EXECUTING METHOD :: listInvoiceInformationNew");
			List<InvoiceMasterInterface> invoiceMasterInterfaces = null;
			try {
				logger.info("EXECUTING METHOD :: BEFORE LISTING INVOICE");
				invoiceMasterInterfaces = invoiceRepository.listInvoiceMasterInfo(factory_id);
				logger.info("EXECUTING METHOD :: AFTER LISTING INVOICE");
				response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
				response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
				response.put("action", "List_Record_In_INVOICE_MASTER");
				response.put("Data", invoiceMasterInterfaces);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR IN THE METHOD FOR listInvoiceInformationNew ::   -> " + e.getMessage());
			}
			logger.info("EXECUTIED METHOD :: listInvoiceInformationNew");
			return response;
		}
	  
	  
	  @GetMapping("/invoice/listnewpaged")
	  public @ResponseBody Map<String, Object> listInvoiInformationNewPaged(
	          @RequestParam String factory_id,
	          @RequestParam(required = false) String search,
	          @RequestParam(defaultValue = "0") int page,
	          @RequestParam(defaultValue = "10") int size) {

	      Map<String, Object> response = new HashMap<>();
	      logger.info("EXECUTING METHOD :: listInvoiInformationNewPaged");

	      try {
	          Pageable pageable = PageRequest.of(page, size);

	          Page<InvoiceMasterInterface> pageResult =
	                  invoiceRepository.listInvoiceMasterInfoPaged(factory_id, search, pageable);

	          logger.info("TOTAL RECORDS :: " + pageResult.getTotalElements());

	          response.put("message",     pageResult.hasContent() ? "Success" : "failure");
	          response.put("status",      pageResult.hasContent() ? "yes" : "no");
	          response.put("action",      "List_Record_In_INVOICE_MASTER");
	          response.put("Data",        pageResult.getContent());
	          response.put("totalItems",  pageResult.getTotalElements());
	          response.put("currentPage", pageResult.getNumber());
	          response.put("totalPages",  pageResult.getTotalPages());

	      } catch (Exception e) {
	          e.printStackTrace();
	          logger.error("ERROR IN listInvoiInformationNewPaged :: " + e.getMessage());
	          response.put("message", "Error occurred");
	          response.put("status",  "error");
	      }

	      logger.info("EXECUTED METHOD :: listInvoiInformationNewPaged");
	      return response;
	  }
	  

		@GetMapping("/invoice/otherslistnew")
		public @ResponseBody Map<String, Object> listothersInvoiceInformationNew(@RequestParam String factory_id) {
			Map<String, Object> response = new HashMap<String, Object>();
			logger.info("EXECUTING METHOD :: listInvoiceInformationNew");
			List<OtherInvoiceInterface> invoiceMasterInterfaces = null;
			try {
				logger.info("EXECUTING METHOD :: BEFORE LISTING INVOICE");
				invoiceMasterInterfaces = invoiceRepository.listothersInvoiceMasterInfo(factory_id);
				logger.info("EXECUTING METHOD :: AFTER LISTING INVOICE");
				response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
				response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
				response.put("action", "List_Record_In_INVOICE_MASTER");
				response.put("Data", invoiceMasterInterfaces);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR IN THE METHOD FOR listothersInvoiceInformationNew ::   -> " + e.getMessage());
			}
			logger.info("EXECUTIED METHOD :: listothersInvoiceInformationNew");
			return response;
		}
		
		
		
		
		@GetMapping("/invoice/otherslistnewpaged")
		public @ResponseBody Map<String, Object> listOthersInvoiceInformationNewPaged(
		        @RequestParam String factory_id,
		        @RequestParam(defaultValue = "") String search, 
		        @RequestParam(defaultValue = "0") int page,
		        @RequestParam(defaultValue = "10") int size) {

		    Map<String, Object> response = new HashMap<>();
		    logger.info("EXECUTING METHOD :: listOthersInvoiceInformationNewPaged");

		    try {
		        Pageable pageable = PageRequest.of(page, size);

		        Page<OtherInvoiceInterface> pageResult =
		                invoiceRepository.listOthersInvoiceMasterInfoPaged(factory_id, search, pageable);


		        logger.info("TOTAL RECORDS :: " + pageResult.getTotalElements());

		        response.put("message",     pageResult.hasContent() ? "Success" : "failure");
		        response.put("status",      pageResult.hasContent() ? "yes" : "no");
		        response.put("action",      "List_Record_In_OTHERS_INVOICE_MASTER");
		        response.put("Data",        pageResult.getContent());
		        response.put("totalItems",  pageResult.getTotalElements());
		        response.put("currentPage", pageResult.getNumber());
		        response.put("totalPages",  pageResult.getTotalPages());

		    } catch (Exception e) {
		        e.printStackTrace();
		        logger.error("ERROR IN listOthersInvoiceInformationNewPaged :: " + e.getMessage());
		        response.put("message", "Error occurred");
		        response.put("status",  "error");
		    }

		    logger.info("EXECUTED METHOD :: listOthersInvoiceInformationNewPaged");
		    return response;
		}
		
		@GetMapping("/invoice/searchothersidnew")
		public @ResponseBody Map<String, Object> listSearchothersByIdNew(@RequestParam String id) {
			logger.info("EXECUTING METHOD :: listSearchByIdNew");
			Map<String, Object> response = new HashMap<String, Object>();
			List<OtherInvoiceInterface> assignToContract = null;
			OtherInvoiceInterface invoiceMasterInterfaces = null;
			List<othersPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
			List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> ls = new HashMap<String, String>();
			try {
				invoiceMasterInterfaces = invoiceRepository.listothersSearchById(id);
				String load_id = invoiceMasterInterfaces.getLoad_id();
				String factory_id = invoiceMasterInterfaces.getFactory_id();
				String pn_id = qsPackingRepository.getothersPnIdBasedOnContract_id(load_id);
				qsPackingInterfaces = qsPackingRepository.searchothersPackingByIdnew(pn_id, factory_id);
				assignToContract = invoiceRepository.searchothersContractNew(load_id, factory_id);
//				String totalamount = assignToContract.getGrand_total();
				String grandTotalValue = invoiceMasterInterfaces.getGrand_total();
				String totalTax = invoiceMasterInterfaces.getTax1_value();
				String totalTax2 = invoiceMasterInterfaces.getTax2_value();
				String totalTax3 = invoiceMasterInterfaces.getTax3_value();
//				String grandTotalValue = assignToContract;
				String grandtaxInWords = assignToContractRepository.getgrandTotalInWordsQuery(totalTax);
				String grandtax2InWords = assignToContractRepository.getgrandTotalInWordsQuery(totalTax2);
				String grandtax3InWords = assignToContractRepository.getgrandTotalInWordsQuery(totalTax3);
				String grandTotalInWords = assignToContractRepository.getgrandTotalInWordsQuery(grandTotalValue);
				lsv.add(ls);
				ls.put("GrandtaxInWords", grandtaxInWords);
				ls.put("Grandtax2InWords", grandtax2InWords);
				ls.put("Grandtax3InWords", grandtax3InWords);
				ls.put("GrandTotalInWords", grandTotalInWords);
				response.put("Grand_Total_value", lsv);
				response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
				response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
				response.put("action", "List_Record_In_INVOICE_MASTER");
				response.put("Invoice_Data", assignToContract);
				response.put("QsPacking_Data", qsPackingInterfaces);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR IN THE METHOD FOR listSearchByIdNew ::   -> " + e.getMessage());
			}
			logger.info("EXECUTED METHOD :: listSearchByIdNew");
			return response;
		}
		
	
		@PostMapping("/invoice/otherupdateadvnew")
		public @ResponseBody Map<String, Object> otherupdateadvInvoiceNew(@RequestParam Integer id,
				@RequestParam String modified_by, @RequestParam String product_desc, @RequestParam String remarks,  @RequestParam Double grand_total,
				@RequestParam(required = false) String date_of_notification, @RequestParam(required = false) String date_val, @RequestParam String pn_id,
				@RequestParam(required = false) String bg_type, @RequestParam(required = false) String date_of_issue, @RequestParam(required = false) String reference_no,
				@RequestParam(required = false) String lc_number, @RequestParam(required = false) String supply_place, @RequestParam(required = false) String s_t_exempted,
				@RequestParam(required = false) String lr_docketno, @RequestParam(required = false) String bg_no, @RequestParam(required = false) String date_of_expiry,
				@RequestParam(required = false) String date_of_ref, @RequestParam(required = false) String lc_issue_date,@RequestParam(required = false) Double tax1,
		          @RequestParam(required = false) Double tax1_per,
		          @RequestParam(required = false) Double tax1_value,
		          @RequestParam(required = false) Double tax2,
		          @RequestParam(required = false) Double tax2_per,
		          @RequestParam(required = false) Double tax2_value,
		          @RequestParam(required = false) Double tax3,
		          @RequestParam(required = false) Double tax3_per,
		          @RequestParam(required = false) Double tax3_value,
		          @RequestParam(required = false) Double total_tax,
				@RequestParam(required = false) List<String> service_code_id,
				@RequestParam(required = false) List<String> hsn_code_id, 
				@RequestParam(required = false) List<String> slno) {
			logger.info("EXECUTING METHOD :: updateInvoiceNew ");
			Map<String, Object> response = new HashMap<String, Object>();
			int valueCount = 0;
			try {
				logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE");
				valueCount = invoiceRepository.otherupdateInvoiceEntryInfo(product_desc, remarks, date_of_notification, date_val, tax1, tax1_per, tax1_value, tax2, tax2_per, tax2_value, tax3, tax3_per, tax3_value, grand_total, 
						bg_type, date_of_issue, reference_no, lc_number, supply_place, lr_docketno, bg_no, date_of_expiry,
						date_of_ref, lc_issue_date, modified_by, s_t_exempted, id);
				logger.info("EXECUTING METHOD :: AFTER UPDATING INVOICE");
				response.put("action", "INVOICE_UPDATE");
				response.put("message", (valueCount > 0) ? "Success" : "No fields changed.");
				response.put("status", (valueCount > 0) ? "yes" : "no");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR IN THE METHOD FOR updateInvoiceNew ::   -> " + e.getMessage());
			}
			logger.info("EXECUTED METHOD :: updateInvoiceNew ");
			return response;
		}
		
		@PostMapping("/invoice/othersverificationnew")
		private @ResponseBody Map<String, Object> othersinvoiceVerficationNew(@RequestParam String verified_by,
				@RequestParam String gst_remarks,
				@RequestParam(required = false) String non_tax_adv, @RequestParam(required = false) String tax_adv,
				@RequestParam(required = false) String qs_packing_item_slno,
				@RequestParam(required = false) String recovery_amt, @RequestParam String factory_id, @RequestParam int id,
				@RequestParam int pn_id,
				@RequestParam(required = false) List<String> t_adv) {
			logger.info("EXECUTING METHOD :: invoiceVerficationNew");
			Map<String, Object> response = new HashMap<String, Object>();
			int count = 0;
			String newSeriesNumber = null;
			try {
				Boolean  isRelease = invoiceRepository.getIsReleaseFromInvoiceMasterothers(id, factory_id);
				Optional<Long> optionalSeriesNumber = invoiceRepository.getSeriesNumberbasedOnIdothers(id);
				long serieNumber = optionalSeriesNumber.orElse((long) 0);
				if (serieNumber < 1) {
					response.put("message", "Please Generate Series Number");
					return response;
				}
				String seriesNumberInString = String.valueOf(serieNumber);
				StringBuilder sbc = new StringBuilder();
				sbc.append(seriesNumberInString.substring(0, Math.min(5, seriesNumberInString.length())));
				seriesNumberInString = sbc.toString();
				Optional<Long> optionalInvoiceId = invoiceRepository.getInvoiceNumber(seriesNumberInString);
				long invoiceNumber = optionalInvoiceId.orElse((long) 0);
				if (optionalInvoiceId.isPresent()) {
					// if invoice_no already generate and it's release and coming for approve once again.
					String num = String.valueOf(invoiceNumber);
					int size = num.lastIndexOf("0000");
					String partAfterZero = null;
					if (size != -1) {
						partAfterZero = num.substring(size + 4);
						int a = Integer.parseInt(partAfterZero);
						a = a + 1;
						partAfterZero = String.valueOf(a);
					}
					Long incrementedValue = Long.parseLong(partAfterZero);
					newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
				
					if (isRelease) {
						count = invoiceRepository.updateothersInvoiceVerificationDetails_is_release(gst_remarks, verified_by,
								id);
						}
						else
						{
							count = invoiceRepository.updateothersInvoiceVerificationDetails(gst_remarks, verified_by,
									newSeriesNumber, id);
						}
					
					logger.info("EXECUTED METHOD :: AFTER UPDATING INVOICE VERIFICATION ");
					logger.info("EXECUTING METHOD :: BEFORE UPDATING CONTRACT MASTER LOCKING ");
					qsPackingRepository.updateisLockinothersQSPacking(pn_id, "1");
					logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING MASTER LOCKING ");
					logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING ITEM MASTER LOCKING ");
					
				} else {
						String num = String.valueOf(serieNumber);
						if (num != null && !num.equals("0")) {
							int size = num.lastIndexOf("0000");
							String partAfterZero = null;
							if (size != -1) {
								partAfterZero = num.substring(size + 4);
								int a = Integer.parseInt(partAfterZero);
								a = a + 1;
								partAfterZero = String.valueOf(a);
							}
							Long incrementedValue = Long.parseLong(partAfterZero);
							newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
							if (isRelease) {
								count = invoiceRepository.updateothersInvoiceVerificationDetails_is_release(gst_remarks, verified_by,
										id);
								}
								else
								{
									count = invoiceRepository.updateothersInvoiceVerificationDetails(gst_remarks, verified_by,
											newSeriesNumber, id);
								}
							logger.info("EXECUTED METHOD :: AFTER UPDATING INVOICE VERIFICATION FIRST TIME");
							logger.info("EXECUTING METHOD :: BEFORE UPDATING QS MASTER LOCKING FIRST TIME");
							qsPackingRepository.updateisLockinothersQSPacking(pn_id, "1");
							
						} else {
							response.put("message", "Please Generate Series Number");
							return response;
						}
				}
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "VERIFICATION_Record_In_INVOICE_MASTER");
				response.put("Invoice_NO", newSeriesNumber);

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR IN THE METHOD FOR releaseByIdNew ::   -> " + e.getMessage());
			}
			logger.info("EXECUTED METHOD :: invoiceVerficationNew");
			return response;
		}
		

		@PostMapping("/invoice/othersreleasebyidnew")
		public @ResponseBody Map<String, Object> othersreleaseByIdNew(@RequestParam String id, @RequestParam String factory_id,
				@RequestParam String released_by, @RequestParam int pn_id) {
			logger.info("EXECUTING METHOD :: releaseByIdNew");
			Map<String, Object> response = new HashMap<String, Object>();
			try {
				int count = invoiceRepository.othersUpdateReleaseById(id, released_by);
				qsPackingRepository.othersupdateisLockinQSPacking(pn_id, "0");
			
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "RELEASED_Record_In_INVOICE_MASTER");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR IN THE METHOD FOR releaseByIdNew ::   -> " + e.getMessage());
			}
			logger.info("EXECUTED METHOD :: releaseByIdNew");
			return response;
		}
		
		@PostMapping("/othersinvoice/cancelbyidnew")
		public @ResponseBody Map<String, Object> cancelOthersInvoiceById(
		        @RequestParam String id,
		        @RequestParam String cancelled_by,
		        @RequestParam String other_pn_id
		   ) {

		    Map<String, Object> response = new HashMap<>();
			qsPackingRepository.updateothersQSPackingCancelByPnId(other_pn_id);
		    int count = invoiceRepository.updateOthersInvoiceCancelById(id, cancelled_by);

		    response.put("message", count > 0 ? "Success" : "Failure");
		    response.put("status", count > 0 ? "yes" : "no");
		    response.put("action", "CANCEL_OTHERS_INVOICE");

		    return response;
		}

		@PostMapping("/invoice/cancelbyidnew")
		public @ResponseBody Map<String, Object> cancelByIdNew(
		        @RequestParam String id,
		        @RequestParam String factory_id,
		        @RequestParam String cancelled_by,
		        @RequestParam String pn_id
		       ) {

		    logger.info("EXECUTING METHOD :: cancelByIdNew");

		    Map<String, Object> response = new HashMap<>();
		    Optional<Float> total_balance;
		    Float total_bal = 0.0f;
		    float avl_bal = 0.0f;

		    try {
		        /* 1️⃣ Mark invoice as CANCELLED */
		    	int packingCount = qsPackingRepository.updateQSPackingCancelByPnId(pn_id);
		        int count = invoiceRepository.updateCancelById(id, cancelled_by);
		        if (count > 0) {

		            /* 2️⃣ Fetch contract */
		            long contract_id =
		                    invoiceRepository.getContractIdFromInvoiceMaster(
		                            Integer.parseInt(id), factory_id
		                    );
		        
		            /* 3️⃣ Opening balance reversal */
		            String pn_idvalue =
		                    invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contract_id);

		            total_balance =
		                    invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);

		            total_bal = total_balance.orElse(0.0f);

		            String open_tax_adv =
		                    invoiceRepository.getRecoveryAmountfromInvoice(id);
		            String open_non_tax_adv = invoiceRepository.getOpenNonTaxAdvFromInvoice(id);
					
					if( total_bal < 0 ) {
						response.put("message", "Please Create the Opening Balance");
						return response;
					}
					float taxValue = invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contract_id);
					float adjustmentAmount;

					if (Float.parseFloat(open_tax_adv) > 0) {

					    float value = Float.parseFloat(open_tax_adv) * (taxValue / 100);

					    adjustmentAmount = value + Float.parseFloat(open_tax_adv);

					} else {

					    adjustmentAmount = Float.parseFloat(open_non_tax_adv);
					}

					if (total_bal >= adjustmentAmount) {

					    avl_bal = total_bal + adjustmentAmount;

					} else {

//					    response.put("message", "Insufficient Balance Please check!");
//					    return response;
					}
		        	
		            invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(
		                    pn_idvalue, avl_bal, cancelled_by
		            );
		          

		            /* 5️⃣ Mark contract as released = NO */
		            assignToContractRepository.updateISReleaseInContractMaster(
		                    contract_id, "1", factory_id
		            );
		        }

		        response.put("message", count > 0 ? "Success" : "failure");
		        response.put("status", count > 0 ? "yes" : "no");
		        response.put("action", "CANCELLED_Record_In_INVOICE_MASTER");

		    } catch (Exception e) {
		        e.printStackTrace();
		        logger.error("ERROR IN cancelByIdNew :: " + e.getMessage());
		    }

		    logger.info("EXECUTED METHOD :: cancelByIdNew");
		    return response;
		}

		@PostMapping("/invoice/challancancelbyidnew")
		public @ResponseBody Map<String, Object> challancancelByIdNew(
		        @RequestParam String id,
		        @RequestParam String factory_id,
		        @RequestParam String cancelled_by,
		        @RequestParam String pn_id
		       ) {

		    logger.info("EXECUTING METHOD :: cancelByIdNew");

		    Map<String, Object> response = new HashMap<>();
		    Optional<Float> total_balance;
		    Float total_bal = 0.0f;
		    float avl_bal = 0.0f;

		    try {
		        /* 1️⃣ Mark invoice as CANCELLED */
		    	int challanpackingCount = qsPackingRepository.updatechallanQSPackingCancelByPnId(pn_id);
		        int count = invoiceRepository.updateCancelById(id, cancelled_by);

		        if (count > 0) {

		            /* 2️⃣ Fetch contract */
		            long contract_id =
		                    invoiceRepository.getContractIdFromInvoiceMaster(
		                            Integer.parseInt(id), factory_id
		                    );

		            /* 3️⃣ Opening balance reversal */
		            String pn_idvalue =
		                    invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contract_id);

		            total_balance =
		                    invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);

		            total_bal = total_balance.orElse(0.0f);

//		            String open_tax_adv =
//		                    invoiceRepository.getRecoveryAmountfromInvoice(id);

		            float taxValue =
		                    invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contract_id);

//		            float tax_amount = open_tax_adv * (taxValue / 100);
//
//		            /* 🔁 Reverse calculation (subtract instead of add) */
//		            avl_bal = total_bal - (open_tax_adv + tax_amount);
//		        	float value = (open_tax_adv *( taxValue / 100));
//		        	avl_bal = (total_bal + open_tax_adv +value);

//		            invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(
//		                    pn_idvalue, avl_bal, cancelled_by
//		            );
		          

		            /* 5️⃣ Mark contract as released = NO */
		            assignToContractRepository.updateISReleaseInContractMaster(
		                    contract_id, "1", factory_id
		            );
		        }

		        response.put("message", count > 0 ? "Success" : "failure");
		        response.put("status", count > 0 ? "yes" : "no");
		        response.put("action", "CANCELLED_Record_In_INVOICE_MASTER");

		    } catch (Exception e) {
		        e.printStackTrace();
		        logger.error("ERROR IN cancelByIdNew :: " + e.getMessage());
		    }

		    logger.info("EXECUTED METHOD :: cancelByIdNew");
		    return response;
		}
		
		@PostMapping("/invoice/advancecancelbyidnew")
		public @ResponseBody Map<String, Object> advancecancelByIdNew(
		        @RequestParam String id,
		        @RequestParam String factory_id,
		        @RequestParam String cancelled_by,
		        @RequestParam String pn_id
		       ) {

		    logger.info("EXECUTING METHOD :: cancelByIdNew");

		    Map<String, Object> response = new HashMap<>();
		    Optional<Float> total_balance;
		    Float total_bal = 0.0f;
		    float avl_bal = 0.0f;

		    try {
		        /* 1️⃣ Mark invoice as CANCELLED */
		    	int advpackingCount = qsPackingRepository.updateadvQSPackingCancelByPnId(pn_id);
		        int count = invoiceRepository.updateCancelById(id, cancelled_by);

		        if (count > 0) {

		            /* 2️⃣ Fetch contract */
		            long contract_id =
		                    invoiceRepository.getContractIdFromInvoiceMaster(
		                            Integer.parseInt(id), factory_id
		                    );

		            /* 3️⃣ Opening balance reversal */
		            String pn_idvalue =
		                    invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contract_id);

		            total_balance =
		                    invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);

		            total_bal = total_balance.orElse(0.0f);

//		            float open_tax_adv =
//		                    invoiceRepository.getRecoveryAmountfromInvoice(id);

		            float taxValue =
		                    invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contract_id);

//		            float tax_amount = open_tax_adv * (taxValue / 100);
//
//		            /* 🔁 Reverse calculation (subtract instead of add) */
//		            avl_bal = total_bal - (open_tax_adv + tax_amount);
//		        	float value = (open_tax_adv *( taxValue / 100));
//		        	avl_bal = (total_bal + open_tax_adv +value);
//		            invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(
//		                    pn_idvalue, avl_bal, cancelled_by
//		            );
		          

		            /* 5️⃣ Mark contract as released = NO */
		            assignToContractRepository.updateISReleaseInContractMaster(
		                    contract_id, "1", factory_id
		            );
		        }

		        response.put("message", count > 0 ? "Success" : "failure");
		        response.put("status", count > 0 ? "yes" : "no");
		        response.put("action", "CANCELLED_Record_In_INVOICE_MASTER");

		    } catch (Exception e) {
		        e.printStackTrace();
		        logger.error("ERROR IN cancelByIdNew :: " + e.getMessage());
		    }

		    logger.info("EXECUTED METHOD :: cancelByIdNew");
		    return response;
		}
		
		
		// ✅ Endpoint 1 - Get distinct contracts
		@GetMapping("/getlistcontractorsfromconsolidatedpackingnew")
		public @ResponseBody Map<String, Object> getlistContractorfromconsolidatedPacking(
		        @RequestParam int factory_id) {

		    Map<String, Object> response = new HashMap<>();
		    logger.info("EXECUTING METHOD :: getlistContractorfromconsolidatedPacking");

		    try {
		        List<ListAssignMilesonetoContractors> list =
		                qsPackingRepository.getlistContractorfromconsolidated(factory_id);

		        response.put("action",  "List Contractor assigned consolidatedPACKING");
		        response.put("message", list.size() > 0 ? "Success" : "failed");
		        response.put("status",  list.size() > 0 ? "yes"     : "no");
		        response.put("DATA",    list);

		    } catch (Exception e) {
		        e.printStackTrace();
		        logger.error("ERROR IN getlistContractorfromconsolidatedPacking :: " + e.getMessage());
		        response.put("message", "Error occurred");
		        response.put("status",  "error");
		    }

		    logger.info("EXECUTED METHOD :: getlistContractorfromconsolidatedPacking");
		    return response;
		}
		
		@GetMapping("/consolidatedinvoice/getloadsforcontract")   // ✅ new unique URL
		public @ResponseBody Map<String, Object> getLoadsForContractUnlocked(
		        @RequestParam int factory_id,
		        @RequestParam String con_id) {

		    Map<String, Object> response = new HashMap<>();
		    logger.info("EXECUTING METHOD :: getLoadsForContractUnlocked");

		    try {
		        List<ListAssignMilesonetoContractors> list =
		                qsPackingRepository.getlistLoadIdsForContractUnlocked(factory_id, con_id);

		        response.put("message", list.size() > 0 ? "Success" : "failed");
		        response.put("status",  list.size() > 0 ? "yes"     : "no");
		        response.put("DATA",    list);

		    } catch (Exception e) {
		        e.printStackTrace();
		        logger.error("ERROR IN getLoadsForContractUnlocked :: " + e.getMessage());
		        response.put("message", "Error occurred");
		        response.put("status",  "error");
		    }

		    logger.info("EXECUTED METHOD :: getLoadsForContractUnlocked");
		    return response;
		}


		
		
		@GetMapping("/getlistloadidforthatcontractorsfromconsolidatedpackingnew")
		public @ResponseBody Map<String, Object> getlistloadfromContractorfromconsolidatedPacking(@RequestParam int factory_id,
				@RequestParam String con_id) {
			logger.info("EXECUTING METHOD :: getlistContractorfromconsolidatedPacking");
			Map<String, Object> response = new HashMap<String, Object>();
			try {
				List<ListAssignMilesonetoContractors> count = qsPackingRepository.getlistloadContractorfromconsolidated(factory_id,con_id);
				response.put("action", "List loads assigned contractors");
				response.put("message", (count.size() > 0) ? "Success" : "failed");
				response.put("status", (count.size() > 0) ? "yes" : "no");
				response.put("DATA", count);
				return response;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR IN THE METHOD FOR getlistContractorfromconsolidatedPacking ::   -> " + e.getMessage());
			}
			logger.info("EXECUTIED METHOD :: getlistContractorfromconsolidatedPacking");
			return response;
		}
		
		 @GetMapping("/invoice/getconsolidatedpackingdetailsnew") public @ResponseBody
		  Map<String, Object> listGetContractconsolidatedpackingNew(@RequestParam String
		  contract_id,
		  @RequestParam String load_id, @RequestParam String factory_id) {
		  logger.info("EXECUTING METHOD :: listGetContractconsolidatedpackingNew"); Map<String,
		  Object> response = new HashMap<String, Object>(); List<AssignToContract>
		  assignToContract = null; List<TaxMasterInterface> range = null;
		  List<consolidatedQSPacking_Interfaces> qsPackingInterfaces = null;
		  List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
		  HashMap<String, String> ls = new HashMap<String, String>(); try { String
		  pn_id = qsPackingRepository.getconsolidatedPnIdBasedOnContractandLoad_id(
		  load_id, factory_id); qsPackingInterfaces =
		  qsPackingRepository.searchconsolidatedPackingByIdnew(pn_id, factory_id);
		  assignToContract = assignToContractRepository.searchContractNew(contract_id,
		  factory_id); range = assignToContractRepository.searchconsolidatedTaxId(contract_id,
		  factory_id, load_id); String grandTotalValue =
		  assignToContractRepository.getconsolidatedTotalValue(contract_id, factory_id, load_id);
		  ls.put("Grand_Total_tax", grandTotalValue); lsv.add(ls);
		  response.put("message", (assignToContract.size() > 0 &&
		  qsPackingInterfaces.size() > 0 && range.size() > 0) ? "Success" : "failure");
		  response.put("status", (assignToContract.size() > 0 &&
		  qsPackingInterfaces.size() > 0 && range.size() > 0) ? "yes" : "no");
		  response.put("action", "List_Record_In_consolidated_INVOICE_MASTER");
		  response.put("Contractor_Data", assignToContract);
		  response.put("QsPacking_Data", qsPackingInterfaces);
		  response.put("Grand_Total_value", lsv);
		  
		  response.put("tax", range); } catch (Exception e) { e.printStackTrace();
		  logger.error("ERROR IN THE METHOD FOR listGetContractconsolidatedpackingNew ::   -> " +
		  e.getMessage()); }
		  logger.info("EXECUTED METHOD :: listGetContractconsolidatedpackingNew"); return
		  response; }
		
		 @PostMapping("/invoice/addconsolidatednew")
			public @ResponseBody Map<String, Object> createconsolidatedInvoiceNew(@RequestParam int contract_id,
					@RequestParam String load_id, @RequestParam String invoice_type, @RequestParam String con_slno,
					@RequestParam String created_by, @RequestParam(required = false) String product_desc,
					@RequestParam(required = false) String remarks, @RequestParam(required = false) String date_of_notification,
					@RequestParam(required = false) String date_val, @RequestParam(required = false) String bg_type,
					@RequestParam(required = false) String date_of_issue, @RequestParam(required = false) String reference_no,
					@RequestParam(required = false) String lc_number, @RequestParam(required = false) String supply_place,
					@RequestParam(required = false) String s_t_exempted, @RequestParam(required = false) String lr_docketno,
					@RequestParam String pn_id, @RequestParam(required = false) String bg_no,
					@RequestParam(required = false) String date_of_expiry, @RequestParam(required = false) String date_of_ref,
					@RequestParam(required = false) String lc_issue_date, @RequestParam String contract_name,
					@RequestParam int factory_id, @RequestParam(required = false) List<String> service_code_id,
					@RequestParam(required = false) List<String> hsn_code_id, @RequestParam String inc_type,
					@RequestParam(required = false) List<String> slno) {
				logger.info("EXECUTING METHOD :: createInvoiceNew");
				Map<String, Object> response = new HashMap<String, Object>();
				Consolidated_invoice invoiceObj = null;
				try {
					Consolidated_invoice invoice = new Consolidated_invoice();
					invoice.setContract_slno(con_slno);
					invoice.setContract_id(contract_id);
					invoice.setLoad_id(load_id);
					invoice.setInvoice_type(invoice_type);
					invoice.setProductDesc(product_desc);
					invoice.setRemarks(remarks);
					invoice.setDateOfNotification(date_of_notification);
					invoice.setDateVal(date_val);
					invoice.setBgType(bg_type);
					invoice.setDateOfIssue(date_of_issue);
					invoice.setReferenceNo(reference_no);
					invoice.setLcNumber(lc_number);
					invoice.setSupplyPlace(supply_place);
					invoice.setStExempted(s_t_exempted);
					invoice.setLrDocketNo(lr_docketno);
					invoice.setBgNo(bg_no);
					invoice.setDateOfExpiry(date_of_expiry);
					invoice.setDateOfRef(date_of_ref);
					invoice.setLcIssueDate(lc_issue_date);
					invoice.setCreatedBy(created_by);
					invoice.setContract_name(contract_name);
					LocalDateTime time = LocalDateTime.now();
					invoice.setCreatedDate(time);
					invoice.setFactory_id(factory_id);
					invoice.setPn_id(Integer.parseInt(pn_id));
					logger.info("EXECUTING METHOD :: BEFORE adding INVOICE");
					invoiceObj = consolidatedinvoiceRepository.save(invoice);
					logger.info("EXECUTING METHOD :: AFTER adding INVOICE");
					logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING IN INVOICE");
					invoiceRepository.addconsolidatedPackingNoteItemInsertTypeIdbasedonPnId(inc_type, created_by, pn_id);
					invoiceRepository.addconsolidatedPackingNoteInsertTypeIdbasedonPnId(created_by, pn_id);
					logger.info("EXECUTING METHOD :: AFTER UPDATING QSPACKING IN INVOICE");
					response.put("action", "INVOICE_ADD");
					response.put("message", (invoiceObj != null) ? "Success" : "failure");
					response.put("status", (invoiceObj != null) ? "yes" : "no");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("ERROR IN THE METHOD FOR  createInvoiceNew ::   -> " + e.getMessage());
				}
				logger.info("EXECUTED METHOD :: createInvoiceNew");
				return response;
			}
		 
		  @GetMapping("/consolidatedinvoice/listnew")
			public @ResponseBody Map<String, Object> listInvoiInformationconsolidatedNew(@RequestParam String factory_id) {
				Map<String, Object> response = new HashMap<String, Object>();
				logger.info("EXECUTING METHOD :: listInvoiceInformationNew");
				List<InvoiceMasterInterface> invoiceMasterInterfaces = null;
				try {
					logger.info("EXECUTING METHOD :: BEFORE LISTING INVOICE");
					invoiceMasterInterfaces = invoiceRepository.listInvoiceMasterconsolidatedInfo(factory_id);
					logger.info("EXECUTING METHOD :: AFTER LISTING INVOICE");
					response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
					response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
					response.put("action", "List_Record_In_INVOICE_MASTER");
					response.put("Data", invoiceMasterInterfaces);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("ERROR IN THE METHOD FOR listInvoiceInformationNew ::   -> " + e.getMessage());
				}
				logger.info("EXECUTIED METHOD :: listInvoiceInformationNew");
				return response;
			}
		  
		  
		  
		  @GetMapping("/consolidatedinvoice/listnewpaged")
		  public @ResponseBody Map<String, Object> listInvoiInformationConsolidatedNewPaged(
		          @RequestParam String factory_id,
		          @RequestParam(defaultValue = "") String search,
		          @RequestParam(defaultValue = "0") int page,
		          @RequestParam(defaultValue = "10") int size) {

		      Map<String, Object> response = new HashMap<>();
		      logger.info("EXECUTING METHOD :: listInvoiInformationConsolidatedNewPaged");

		      try {

		          Pageable pageable = PageRequest.of(page, size);

		          Page<InvoiceMasterInterface> pageResult =
		        	        invoiceRepository.listInvoiceMasterConsolidatedInfoPaged(factory_id, search, pageable);

		          response.put("message", pageResult.hasContent() ? "Success" : "failure");
		          response.put("status", pageResult.hasContent() ? "yes" : "no");
		          response.put("Data", pageResult.getContent());
		          response.put("totalItems", pageResult.getTotalElements());
		          response.put("currentPage", pageResult.getNumber());
		          response.put("totalPages", pageResult.getTotalPages());

		      } catch (Exception e) {
		          e.printStackTrace();
		          response.put("message", "Error occurred");
		          response.put("status", "error");
		      }

		      return response;
		  }
		  
		  
		  
		  
		  
		  
		  @GetMapping("/invoice/searchconsolidatedidnew")
			public @ResponseBody Map<String, Object> listSearchconsolidatedByIdNew(@RequestParam String id) {
				logger.info("EXECUTING METHOD :: listSearchByIdNew");
				Map<String, Object> response = new HashMap<String, Object>();
				InvoiceMasterInterface invoiceMasterInterfaces = null;
				List<AssignToContract> assignToContract = null;
				List<TaxMasterInterface> range = null;
				List<consolidatedQSPacking_Interfaces> qsPackingInterfaces = null;
				List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> ls = new HashMap<String, String>();
				try {
					invoiceMasterInterfaces = invoiceRepository.listconsolidatedSearchById(id);
					String load_id = invoiceMasterInterfaces.getLoad_id();
					String contract_id = invoiceMasterInterfaces.getContract_id();
					String factory_id = invoiceMasterInterfaces.getFactory_id();
					String pn_id = qsPackingRepository.getconsolidatedPnIdBasedOnContractandLoad_id(load_id, factory_id);
					qsPackingInterfaces = qsPackingRepository.searchconsolidatedPackingByIdnew(pn_id, factory_id);
					assignToContract = assignToContractRepository.searchContractNew(contract_id, factory_id);
					range = assignToContractRepository.searchconsolidatedTaxId(contract_id, factory_id, load_id);
					List<String> grandTot = assignToContractRepository.getTotalconsolidatedValueList(contract_id, factory_id, load_id);
					String dataVal = grandTot.get(0);
					String[] grandTotal_Value = dataVal.split(",");
					String grandTotalValue = grandTotal_Value[0];
					String tax_Final_total = grandTotal_Value[1];
					String taxable_final_total = grandTotal_Value[2];
					String grandTotalInWords = assignToContractRepository.getgrandTotalInWordsQuery(grandTotalValue);
					float total_tax_adv = 0.0f;
					float total_taxable = 0.0f;
					float optd = 0.0f;
					float ptc = 0.0f;
					for (TaxMasterInterface tx : range) {
						total_tax_adv = total_tax_adv + Float.parseFloat(tx.getAdv_tax());
						total_taxable = Float.parseFloat(tx.getTaxable());
						optd = Float.parseFloat(tx.getOptc());
						ptc = ptc + Float.parseFloat(tx.getPtc());
					}
					total_tax_adv = total_tax_adv + total_taxable;
					ls.put("Grand_Total_tax", grandTotalValue);
					ls.put("GrandTotalInWords", grandTotalInWords);
					ls.put("Tax_Final_total", String.valueOf(ptc));
					ls.put("PBC_Final_total", String.valueOf(ptc + optd));
					ls.put("Tax_with_basic_final_total", taxable_final_total); //
					ls.put("Total_adv_tax_Taxable", String.valueOf(total_tax_adv));
					ls.put("Total_adv_tax_Taxable", String.format("%.2f", total_tax_adv));
					lsv.add(ls);
					response.put("Grand_Total_value", lsv);
					response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
					response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
					response.put("action", "List_Record_In_INVOICE_MASTER");
					response.put("Invoice_Data", invoiceMasterInterfaces);
					response.put("Contractor_Data", assignToContract);
					response.put("QsPacking_Data", qsPackingInterfaces);
					response.put("tax", range);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("ERROR IN THE METHOD FOR listSearchByIdNew ::   -> " + e.getMessage());
				}
				logger.info("EXECUTED METHOD :: listSearchByIdNew");
				return response;
			}
		  
			@PostMapping("/invoice/consolidatedupdatenew")
			public @ResponseBody Map<String, Object> updateconsolidatedInvoiceNew(@RequestParam Integer id,
					@RequestParam String modified_by, @RequestParam String product_desc, @RequestParam String remarks,
					@RequestParam(required = false) String date_of_notification, @RequestParam(required = false) String date_val, @RequestParam String pn_id,
					@RequestParam(required = false) String bg_type, @RequestParam(required = false) String date_of_issue, @RequestParam(required = false) String reference_no,
					@RequestParam(required = false) String lc_number, @RequestParam(required = false) String supply_place, @RequestParam(required = false) String s_t_exempted,
					@RequestParam(required = false) String lr_docketno, @RequestParam(required = false) String bg_no, @RequestParam(required = false) String date_of_expiry,
					@RequestParam(required = false) String date_of_ref, @RequestParam(required = false) String lc_issue_date,
					@RequestParam(required = false) List<String> service_code_id,
					@RequestParam(required = false) List<String> hsn_code_id, @RequestParam(required = false) String inc_type,//invoice_type_id for type_remarks
					@RequestParam(required = false) List<String> slno) {
				logger.info("EXECUTING METHOD :: updateInvoiceNew ");
				Map<String, Object> response = new HashMap<String, Object>();
				int valueCount = 0;
				try {
					logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE");
					valueCount = invoiceRepository.updateconsolidatedInvoiceEntryInfo(product_desc, remarks, date_of_notification, date_val,
							bg_type, date_of_issue, reference_no, lc_number, supply_place, lr_docketno, bg_no, date_of_expiry,
							date_of_ref, lc_issue_date, modified_by, s_t_exempted, id);
					logger.info("EXECUTING METHOD :: AFTER UPDATING INVOICE");
					logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING IN INVOICE");
					invoiceRepository.updatePackingNoteItemconsolidated(inc_type, modified_by, pn_id);
					logger.info("EXECUTING METHOD :: AFTER UPDATING consolidatedPACKING IN INVOICE");
					response.put("action", "INVOICE_UPDATE");
					response.put("message", (valueCount > 0) ? "Success" : "No fields changed");
					response.put("status", (valueCount > 0) ? "yes" : "no");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("ERROR IN THE METHOD FOR updateconsolidatedInvoiceNew ::   -> " + e.getMessage());
				}
				logger.info("EXECUTED METHOD :: updateconsolidatedInvoiceNew ");
				return response;
			}
			
			@PostMapping("/invoice/consolidatedverificationnew")
			private @ResponseBody Map<String, Object> invoiceconsolidatedVerficationNew(@RequestParam String verified_by,
					@RequestParam String gst_remarks,
					@RequestParam(required = false) String non_tax_adv, @RequestParam(required = false) String tax_adv,
					@RequestParam(required = false) String qs_packing_item_slno, @RequestParam String total,
					@RequestParam String payable_by_customer, @RequestParam String payable_to_dept,
					@RequestParam String open_tax_adv, @RequestParam String open_non_tax_adv,
					@RequestParam(required = false) String recovery_amt, @RequestParam String factory_id, @RequestParam int id,
					@RequestParam int pn_id, @RequestParam List<String> tax_id, @RequestParam List<String> tax_per,
					@RequestParam List<String> tax_value, @RequestParam List<String> adv_tax,
					@RequestParam List<String> tax_payable_by_customer, @RequestParam List<String> tax_payable_to_dept,
					@RequestParam(required = false) List<String> t_adv) {
				logger.info("EXECUTING METHOD :: invoiceVerficationNew");
				Map<String, Object> response = new HashMap<String, Object>();
				int count = 0;
				Optional<Float> total_balance;
				float total_bal = 0;
				float avl_bal = 0;
				String newSeriesNumber = null;
				try {
					long contact_id = invoiceRepository.getconsolidatedContractIdFromInvoiceMaster(id, factory_id);
					Boolean  isRelease = invoiceRepository.getIsReleaseconsolidatedFromInvoiceMaster(id, factory_id);
					System.out.println("isRelease value =------------------------------------------ " + isRelease);
					Optional<Long> optionalSeriesNumber = invoiceRepository.getconsolidatedSeriesNumberbasedOnId(id);
					long serieNumber = optionalSeriesNumber.orElse((long) 0);
					if (serieNumber < 1) {
						response.put("message", "Please Generate Series Number");
						return response;
					}
					String seriesNumberInString = String.valueOf(serieNumber);
					StringBuilder sbc = new StringBuilder();
					sbc.append(seriesNumberInString.substring(0, Math.min(5, seriesNumberInString.length())));
					seriesNumberInString = sbc.toString();
					Optional<Long> optionalInvoiceId = invoiceRepository.getInvoiceNumber(seriesNumberInString);
					long invoiceNumber = optionalInvoiceId.orElse((long) 0);
					if (optionalInvoiceId.isPresent()) {
						// if invoice_no already generate and it's release and coming for approve once again.
						String num = String.valueOf(invoiceNumber);
						int size = num.lastIndexOf("0000");
						String partAfterZero = null;
						if (size != -1) {
							partAfterZero = num.substring(size + 4);
							int a = Integer.parseInt(partAfterZero);
							a = a + 1;
							partAfterZero = String.valueOf(a);
						}
						Long incrementedValue = Long.parseLong(partAfterZero);
						newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
						//OPENING BALANCE SUBTRACTION 
						String pn_idvalue = invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contact_id);
						total_balance = invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);
						total_bal = total_balance.orElse((float) 0);
						if(total_bal < 0 ) {
							response.put("message", "Please Create the Opening Balance");
							return response;
						}
						float taxValue = invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contact_id);
						float value = (Float.parseFloat(open_tax_adv)) *( taxValue / 100);
						if(total_bal >= (value + Float.parseFloat(open_tax_adv))) {
							avl_bal = (total_bal - (value + Float.parseFloat(open_tax_adv)));
						}else {
							response.put("message","Insufficient Balance Please check!");
							return response;
						}
						logger.info("EXECUTING METHOD :: BEFORE UPDATING OPENING BALANCE");
						invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(pn_idvalue, avl_bal, verified_by);
						logger.info("EXECUTED METHOD :: AFTER UPDATING OPENING BALANCE");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE VERIFICATION ");
						if (isRelease) {
							count = invoiceRepository.updateconsolidatedInvoiceVerificationDetails_is_release(non_tax_adv, tax_adv, total,
									payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks, verified_by,
									id);
						}
						else
						{
							count = invoiceRepository.updateconsolidatedInvoiceVerificationDetails(non_tax_adv, tax_adv, total,
									payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks, verified_by,
									newSeriesNumber, id);
						}
						logger.info("EXECUTED METHOD :: AFTER UPDATING INVOICE VERIFICATION ");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING CONTRACT MASTER LOCKING ");
						assignToContractRepository.updateISReleaseInContractMaster(contact_id, "1", factory_id);
						logger.info("EXECUTED METHOD :: AFTER UPDATING CONTRACT MASTER LOCKING ");
						logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING MASTER LOCKING ");
						qsPackingRepository.updateisLockinconsolidatedPacking(pn_id, "1");
						logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING MASTER LOCKING ");
					
						int sizeRange = tax_id.size();
						logger.info("EXECUTING METHOD :: BEFORE ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
						for (int i = 0; i < sizeRange; i++) {
							invoiceRepository.insertINVOICE_TAXENTRY_DETAILSNew(tax_id.get(i), tax_per.get(i), tax_value.get(i),
									adv_tax.get(i), tax_payable_by_customer.get(i), tax_payable_to_dept.get(i),String.valueOf(id), verified_by);
						}
						logger.info("EXECUTED METHOD :: AFTER ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
					} else {
							String num = String.valueOf(serieNumber);
							if (num != null && !num.equals("0")) {
								int size = num.lastIndexOf("0000");
								String partAfterZero = null;
								if (size != -1) {
									partAfterZero = num.substring(size + 4);
									int a = Integer.parseInt(partAfterZero);
									a = a + 1;
									partAfterZero = String.valueOf(a);
								}
								Long incrementedValue = Long.parseLong(partAfterZero);
								newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
								//OPENING BALANCE SUBTRACTION 
								String pn_idvalue = invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contact_id);
								total_balance = invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);
								total_bal = total_balance.orElse((float) 0);
								if(total_bal == 0 || total_bal < 0 ) {
									response.put("message", "Please Create the Opening Balance");
									return response;
								}
								float taxValue = invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contact_id);
								float value = (Float.parseFloat(open_tax_adv)) *( taxValue / 100);
								if(total_bal >= (value + Float.parseFloat(open_tax_adv))) {
									avl_bal = (total_bal - (value + Float.parseFloat(open_tax_adv)));
								}else {
									response.put("message","Insufficient Balance Please check!");
									return response;
								}
								logger.info("EXECUTING METHOD :: BEFORE UPDATING OPENING BALANCE FIRST TIME");
								invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(pn_idvalue, avl_bal, verified_by);
								logger.info("EXECUTED METHOD :: AFTER UPDATING OPENING BALANCE FIRST TIME");
								logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE VERIFICATION FIRST TIME");
								if (isRelease) {
									count = invoiceRepository.updateconsolidatedInvoiceVerificationDetails_is_release(non_tax_adv, tax_adv, total,
											payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks,
											verified_by, id);
								}
								else
								{
									count = invoiceRepository.updateconsolidatedInvoiceVerificationDetails(non_tax_adv, tax_adv, total,
											payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks,
											verified_by, newSeriesNumber, id);
								}
								logger.info("EXECUTED METHOD :: AFTER UPDATING INVOICE VERIFICATION FIRST TIME");
								logger.info("EXECUTING METHOD :: BEFORE UPDATING CONTRACT MASTER LOCKING FIRST TIME");
								assignToContractRepository.updateISReleaseInContractMaster(contact_id, "1", factory_id);
								logger.info("EXECUTED METHOD :: AFTER UPDATING CONTRACT MASTER LOCKING FIRST TIME");
								logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING MASTER LOCKING FIRST TIME");
								qsPackingRepository.updateisLockinconsolidatedPacking(pn_id, "1");
								logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING MASTER LOCKING FIRST TIME");
								int sizeRange = tax_id.size();
								logger.info("EXECUTED METHOD :: BEFORE ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
								for (int i = 0; i < sizeRange; i++) {
									invoiceRepository.insertINVOICE_TAXENTRY_DETAILSNew(tax_id.get(i), tax_per.get(i),
											tax_value.get(i), adv_tax.get(i), tax_payable_by_customer.get(i),
											tax_payable_to_dept.get(i), String.valueOf(id), verified_by);
								}
								logger.info("EXECUTED METHOD :: AFTER ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
							} else {
								response.put("message", "Please Generate Series Number");
								return response;
							}
					}
					response.put("message", (count > 0) ? "Success" : "failure");
					response.put("status", (count > 0) ? "yes" : "no");
					response.put("action", "consolidatedVERIFICATION_Record_In_INVOICE_MASTER");
					response.put("Invoice_NO", newSeriesNumber);

				} catch (Exception e) {
					e.printStackTrace();
					logger.error("ERROR IN THE METHOD FOR releaseByIdNew ::   -> " + e.getMessage());
				}
				logger.info("EXECUTED METHOD :: consolidatedinvoiceVerficationNew");
				return response;
			}
			
			@PostMapping("/invoice/consolidatedreleasebyidnew")
			public @ResponseBody Map<String, Object> consolidatedreleaseByIdNew(@RequestParam String id, @RequestParam String factory_id,
					@RequestParam String released_by, @RequestParam int pn_id) {
				logger.info("EXECUTING METHOD :: releaseByIdNew");
				Map<String, Object> response = new HashMap<String, Object>();
				Optional<Float> total_balance;
				Float total_bal = 0.0f;
				float avl_bal = 0.0f;
				try {
					int count = invoiceRepository.consolidatedUpdateReleaseById(id, released_by);
					long contact_id = invoiceRepository.getconsolidatedContractIdFromInvoiceMaster(Integer.parseInt(id), factory_id);
					//OPENING BALANCE SUBTRACTION 
					String pn_idvalue = invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contact_id);
					total_balance = invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);
					total_bal = total_balance.orElse((float) 0);
					float open_tax_adv = invoiceRepository.getconsolidatedRecoveryAmountfromInvoice(id);
					float taxValue = invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contact_id);
					float value = (open_tax_adv *( taxValue / 100));
					avl_bal = (total_bal + open_tax_adv +value);
					invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(pn_idvalue, avl_bal, released_by);
					qsPackingRepository.updateisLockinconsolidatedPacking(pn_id, "0");
					assignToContractRepository.updateISReleaseInContractMaster(contact_id, "0", factory_id);
					response.put("message", (count > 0) ? "Success" : "failure");
					response.put("status", (count > 0) ? "yes" : "no");
					response.put("action", "RELEASED_Record_In_INVOICE_MASTER");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("ERROR IN THE METHOD FOR releaseByIdNew ::   -> " + e.getMessage());
				}
				logger.info("EXECUTED METHOD :: releaseByIdNew");
				return response;
			}
			
			@Autowired
			private ConsolidatedInvoiceItemRepository consolidatedInvoiceItemRepository;
			
			@PostMapping("/invoice/cancelconsolidatedbyidnew")
			public @ResponseBody Map<String, Object> cancelconsolidatedByIdNew(
			        @RequestParam String id,
			        @RequestParam String factory_id,
			        @RequestParam String cancelled_by,
			        @RequestParam String pn_id,@RequestParam String load_id
			       ) {

			    logger.info("EXECUTING METHOD :: cancelByIdNew");
			    
			    logger.info("The load_id is: {}", load_id);

			    Map<String, Object> response = new HashMap<>();
			    Optional<Float> total_balance;
			    Float total_bal = 0.0f;
			    float avl_bal = 0.0f;

			    try {
			        /* 1️ Mark invoice as CANCELLED */
			    	int packingCount = qsPackingRepository.updateconsolidatedQSPackingCancelByPnId(pn_id);
			        int count = invoiceRepository.updateconsolidatedCancelById(id, cancelled_by);
			        if (count > 0) {

			            /* 2️ Fetch contract */
			            long contract_id =
			                    invoiceRepository.getconsolidatedContractIdFromInvoiceMaster(
			                            Integer.parseInt(id), factory_id
			                    );
			        
			            /* 3️ Opening balance reversal */
			            String pn_idvalue =
			                    invoiceRepository.getLastInseretedOpeningBalancePrimaryId(contract_id);

			            total_balance =
			                    invoiceRepository.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);

			            total_bal = total_balance.orElse(0.0f);

			            float open_tax_adv =
			                    invoiceRepository.getconsolidatedRecoveryAmountfromInvoice(id);

			            float taxValue =
			                    invoiceRepository.getTaxPercentageDetailsAssignedToContractor(contract_id);
			        	float value = (open_tax_adv *( taxValue / 100));
			        	avl_bal = (total_bal + open_tax_adv +value);
			            invoiceRepository.reduceOpeningBalanceBasedOnOpenTaxable(
			                    pn_idvalue, avl_bal, cancelled_by
			            );
			          

			            /* 5️ Mark contract as released = NO */
			            assignToContractRepository.updateISReleaseInContractMaster(
			                    contract_id, "1", factory_id
			            );
			            
//			            Update the Cancel in the invoice_mapping
			          
			            
			            
			            
			            qsPackingRepository.cancelConsolidatedInvoiceItemsByLoadId(
			                    load_id,      
			                    cancelled_by
			            );
			            logger.info("CANCELLED consolidated_invoice_items for load_id :: " + load_id);
			            
			            
			        }

			        response.put("message", count > 0 ? "Success" : "failure");
			        response.put("status", count > 0 ? "yes" : "no");
			        response.put("action", "CANCELLED_Record_In_INVOICE_MASTER");

			    } catch (Exception e) {
			        e.printStackTrace();
			        logger.error("ERROR IN cancelByIdNew :: " + e.getMessage());
			    }

			    logger.info("EXECUTED METHOD :: cancelByIdNew");
			    return response;
			}

			@GetMapping("/invoice/searchconsolidatedidcancel")
			public @ResponseBody Map<String, Object> listconsolidatedSearchByIdcancel(@RequestParam String id) {
				logger.info("EXECUTING METHOD :: listSearchByIdNew");
				Map<String, Object> response = new HashMap<String, Object>();
				InvoiceMasterInterface invoiceMasterInterfaces = null;
				List<AssignToContract> assignToContract = null;
				List<TaxMasterInterface> range = null;
				List<consolidatedQSPacking_Interfaces> qsPackingInterfaces = null;
				List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> ls = new HashMap<String, String>();
				try {
					invoiceMasterInterfaces = invoiceRepository.listconsolidatedSearchById(id);
					String load_id = invoiceMasterInterfaces.getLoad_id();
					String contract_id = invoiceMasterInterfaces.getContract_id();
					String factory_id = invoiceMasterInterfaces.getFactory_id();
					String pn_id = qsPackingRepository.getconsolidatedPnIdBasedOnContractandLoad_id(load_id, factory_id);
					qsPackingInterfaces = qsPackingRepository.searchconsolidatedPackingByIdnew(pn_id, factory_id);
					assignToContract = assignToContractRepository.searchContractNew(contract_id, factory_id);
					range = assignToContractRepository.searchconsolidatedTaxIdcancel(contract_id, factory_id, load_id);		
					List<String> grandTot = assignToContractRepository.getconsolidatedTotalValueListcancel(contract_id, factory_id, load_id);
					String dataVal = grandTot.get(0);
					String[] grandTotal_Value = dataVal.split(",");
					String grandTotalValue = grandTotal_Value[0];
					String tax_Final_total = grandTotal_Value[1];
					String taxable_final_total = grandTotal_Value[2];
					String grandTotalInWords = assignToContractRepository.getgrandTotalInWordsQuery(grandTotalValue);
					float total_tax_adv = 0.0f;
					float total_taxable = 0.0f;
					float optd = 0.0f;
					float ptc = 0.0f;
					for(TaxMasterInterface tx : range) {
						total_tax_adv = total_tax_adv+Float.parseFloat(tx.getAdv_tax());
						total_taxable = Float.parseFloat(tx.getTaxable());
						optd = Float.parseFloat(tx.getOptc());
						ptc = ptc + Float.parseFloat(tx.getPtc());
					}
					total_tax_adv = total_tax_adv + total_taxable;
					ls.put("Grand_Total_tax", grandTotalValue);
					ls.put("GrandTotalInWords", grandTotalInWords);
					ls.put("Tax_Final_total", String.valueOf(ptc));
					ls.put("PBC_Final_total", String.valueOf(ptc+optd));
					ls.put("Tax_with_basic_final_total", taxable_final_total);
					ls.put("Total_adv_tax_Taxable", String.valueOf(total_tax_adv));
					lsv.add(ls);
					response.put("Grand_Total_value", lsv);
					response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
					response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
					response.put("action", "List_Record_In_INVOICE_MASTER");
					response.put("Invoice_Data", invoiceMasterInterfaces);
					response.put("Contractor_Data", assignToContract);
					response.put("QsPacking_Data", qsPackingInterfaces);
					response.put("tax", range);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("ERROR IN THE METHOD FOR listSearchByIdNew ::   -> " + e.getMessage());
				}
				logger.info("EXECUTED METHOD :: listSearchByIdNew");
				return response;
			}
			
			
}


