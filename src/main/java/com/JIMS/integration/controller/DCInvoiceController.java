//package com.suveechi.integration.controller;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.suveechi.integration.interfaces.AssignToContract;
//import com.suveechi.integration.interfaces.InvoiceMasterInterface;
//import com.suveechi.integration.interfaces.QSPacking_QSPackingItem_LIST_INTERFACES;
//import com.suveechi.integration.repository.AssignToContractRepository;
//import com.suveechi.integration.repository.InvoiceRepository;
//import com.suveechi.integration.repository.QSPackingRepository;
//
//@CrossOrigin
//@RestController
//@RequestMapping("/jssl")
//public class DCInvoiceController {
//
//	@Autowired
//	public InvoiceRepository invoiceRepository;
//	@Autowired
//	public QSPackingRepository qsPackingRepository;
//	@Autowired
//	public AssignToContractRepository assignToContractRepository;
//
//	@GetMapping("/invoicemasterdc/listloadid/{con_id}/{factory_id}")
//	public @ResponseBody Map<String, Object> serachListLoadId(@PathVariable int con_id, @PathVariable int factory_id,
//			@PathVariable String dcType) {
//		Map<String, Object> response = new HashMap<String, Object>();
//		try {
//			List<Map<Integer, String>> value = invoiceRepository.getLoadIdQsPacking(con_id, factory_id);
//			response.put("message", (value != null) ? "Success" : "failure");
//			response.put("status", (value != null) ? "yes" : "no");
//			response.put("action", "ListLoadId_Record");
//			response.put("data", value);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	@PostMapping("/invoicedc/add")
//	public @ResponseBody Map<String, Object> createInvoice(@RequestBody Map<String, String> data) {
//		Map<String, Object> response = new HashMap<String, Object>();
//		Map<String, String> val = null;
//		int valCount = 0;
//		int count = 0;
//		List<Map<String, String>> obj = null;
//		try {
//			if (data != null && !data.isEmpty()) {
//				val = data;
//				String invoice_type = val.get("invoice_type");
//				int contract_id = Integer.parseInt(val.get("contract_id"));
//				String dc_type = val.get("dc_type");
//				String load = val.get("load");
//				String con_slno = val.get("contract_slno"); // add in table
//				String product_desc = val.get("product_desc");
//				String remarks = val.get("remarks");
//				String date_of_notification = val.get("date_of_notification");
//				String date_val = val.get("date_val");
//				String transporter = val.get("transporter");
//				String vechile_no = val.get("vechile_no");
//				String st_exmepted_under = val.get("st_exmepted_under");
//				String lr_docketno = val.get("lr_docketno");
//				String buyer_reference_no = val.get("buyer_reference_no");
//				String weighbridge_slip_no = val.get("weighbridge_slip_no");
//				String s_t_exempted = val.get("s_t_exempted");
//				//String transporter_name = val.get("transporter_name");
//				String grn_no = val.get("grn_no");
//				String grn_date = val.get("grn_date");
//				String invoice_challan_no = val.get("invoice_challan_no");
//				String lc_issue_date = val.get("lc_issue_date");
//				String contract_name = val.get("contract_name");
//				int factory_id = val.containsKey("factory_id") ? Integer.parseInt(val.get("factory_id")) : 0;
//				String created_by = val.get("created_by");
//			}
//
//			response.put("action", "DC_INVOICE_ADD");
//			response.put("message", (count > 0) ? "Success" : "failure");
//			response.put("status", (count > 0) ? "yes" : "no");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	@PostMapping("/invoicedc/update")
//	public @ResponseBody Map<String, Object> updateInvoice(@RequestBody Map<String, Object> data) {
//		Map<String, Object> response = new HashMap<String, Object>();
//		Map<String, String> val = null;
//		int valueCount = 0;
//		List<Map<String, String>> obj = null;
//		try {
//			val = (Map<String, String>) data.get("val");
//			int valCount = 0;
//			int count = 0;
//			// Extract the 'obj' part, which is a list of maps
//			obj = (List<Map<String, String>>) data.get("obj");
//			int id = Integer.parseInt(val.get("id"));
//			// String invoice_type = val.get("invoice_type");
//			String modified_by = val.get("modified_by");
//			String product_desc = val.get("product_desc");
//			String remarks = val.get("remarks");
//			String date_of_notification = val.get("date_of_notification");
//			String date_val = val.get("date_val");
//			String bg_type = val.get("bg_type");
//			String date_of_issue = val.get("date_of_issue");
//			String reference_no = val.get("reference_no");
//			String lc_number = val.get("lc_number");
//			String supply_place = val.get("supply_place");
//			String s_t_exempted = val.get("s_t_exempted");
//			String lr_docketno = val.get("lr_docketno");
//			String bg_no = val.get("bg_no");
//			String date_of_expiry = val.get("date_of_expiry");
//			String date_of_ref = val.get("date_of_ref");
//			String lc_issue_date = val.get("lc_issue_date");
//
//			valueCount = invoiceRepository.updateInvoiceEntryInfo(product_desc, remarks, date_of_notification, date_val,
//					bg_type, date_of_issue, reference_no, lc_number, supply_place, lr_docketno, bg_no, date_of_expiry,
//					date_of_ref, lc_issue_date, modified_by, s_t_exempted, id);
//			invoiceRepository.insertIntoInvoiceHistory(val.get(id), modified_by);
//			invoiceRepository.insertInvoiceHistory(modified_by, id);
//			for (Map<String, String> item : obj) {
//				String service_code_id = item.get("service_code_id");
//				String hsn_code_id = item.get("hsn_code_id");
//				String invoice_type_id = item.get("invoice_type_id");
//				String slno = item.get("slno");
//				qsPackingRepository.updateQSPackingItemMasterHistoryRecordSlno(modified_by, slno);
//				invoiceRepository.addPackingNoteItems(service_code_id, hsn_code_id, invoice_type_id, modified_by, slno);
//			}
//			response.put("action", "INVOICE_UPDATE");
//			response.put("message", (valueCount > 0) ? "Success" : "failure");
//			response.put("status", (valueCount > 0) ? "yes" : "no");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	@GetMapping("/invoicedc/list/{factory_id}")
//	public @ResponseBody Map<String, Object> listInvoiceInformation(@PathVariable String factory_id) {
//		Map<String, Object> response = new HashMap<String, Object>();
//		List<InvoiceMasterInterface> invoiceMasterInterfaces = null;
//		try {
//			invoiceMasterInterfaces = invoiceRepository.listInvoiceMasterInfo(factory_id);
//			response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
//			response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
//			response.put("action", "List_Record_In_INVOICE_MASTER");
//			response.put("Data", invoiceMasterInterfaces);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	@GetMapping("/invoicedc/verificationlist")
//	public @ResponseBody Map<String, Object> listVerificationPendingList() {
//		Map<String, Object> response = new HashMap<String, Object>();
//		List<InvoiceMasterInterface> invoiceMasterInterfaces = null;
//		try {
//			invoiceMasterInterfaces = invoiceRepository.listInvoiceVerificationInfo();
//			response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
//			response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
//			response.put("action", "List_Record_In_INVOICE_MASTER");
//			response.put("Data", invoiceMasterInterfaces);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	@GetMapping("/invoicedc/getcontractqspackingdetails/{contract_id}/{load_id}/{factory_id}") // check the logic from
//																								// why it required and
//																								// test
//	public @ResponseBody Map<String, Object> listGetContractQspacking(@PathVariable String contract_id,
//			@PathVariable String load_id, @PathVariable String factory_id) {
//		Map<String, Object> response = new HashMap<String, Object>();
//		List<AssignToContract> assignToContract = null;
//		List<QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
//		try {
//			String pn_id = qsPackingRepository.getPnIdBasedOnContractandLoad_id(contract_id, load_id, factory_id);
//			qsPackingInterfaces = qsPackingRepository.searchQSPackingById(pn_id);
//			assignToContract = assignToContractRepository.searchContract(contract_id, factory_id);
//			response.put("message", (assignToContract != null) ? "Success" : "failure");
//			response.put("status", (assignToContract != null) ? "yes" : "no");
//			response.put("action", "List_Record_In_INVOICE_MASTER");
//			response.put("Contractor_Data", assignToContract);
//			response.put("QsPacking_Data", qsPackingInterfaces);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	@GetMapping("/invoicedc/searchid/{id}/{contract_id}")
//	public @ResponseBody Map<String, Object> listSearchById(@PathVariable String id, @PathVariable String contract_id) {
//		Map<String, Object> response = new HashMap<String, Object>();
//		InvoiceMasterInterface invoiceMasterInterfaces = null;
//		List<AssignToContract> assignToContract = null;
//		List<QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
//		try {
//			invoiceMasterInterfaces = invoiceRepository.listSearchById(id);
//			String load_id = invoiceMasterInterfaces.getLoad_id();
//			String pn_id = qsPackingRepository.getPnIdBasedOnContract_id(contract_id, load_id);
//			qsPackingInterfaces = qsPackingRepository.searchQSPackingById(pn_id);
//			assignToContract = assignToContractRepository.searchContractinInvoice(contract_id);
//			response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
//			response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
//			response.put("action", "List_Record_In_INVOICE_MASTER");
//			response.put("Invoice_Data", invoiceMasterInterfaces);
//			response.put("Contractor_Data", assignToContract);
//			response.put("QsPacking_Data", qsPackingInterfaces);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	@PostMapping("/invoicedc/deletebyIds")
//	public @ResponseBody Map<String, Object> listDeleteById(@RequestBody Map<String, String> val) {
//		Map<String, Object> response = new HashMap<String, Object>();
//		try {
//			String id = val.get("id");
//			String slno = val.get("slno");
//			String rejected_by = val.get("rejected_by");
//
//			Optional<Integer> pn_value = qsPackingRepository.getPn_id(slno);
//			Integer pn_id1 = pn_value.get();
//			int pn_id = pn_id1.intValue();
//			invoiceRepository.updateRejectedById(rejected_by, id);
//			invoiceRepository.insertIntoInvoiceHistoryisDelete(id, rejected_by);
//			int count = qsPackingRepository.invoiceQSDelete(pn_id);
//			int count1 = qsPackingRepository.invoiceQSItemDelete(pn_id);
//			qsPackingRepository.updateIsDeleteQSPackingMasterHistoryRecord(rejected_by, String.valueOf(pn_id));
//			qsPackingRepository.IsDeleteQSPackingItemMasterHistoryRecord(rejected_by, slno);
//			// >>>>>> delete the data from INVOICE_MASTER, QSPACKING_MASTER,
//			// QSPACKING_ITEM_MASTER
//			// invoiceRepository.listDeleteById(id);
//			invoiceRepository.deleteInvoiceMasterBasedOnId(id);
//			qsPackingRepository.deleteQSPackingBasedOnPnId(String.valueOf(pn_id));
//			qsPackingRepository.deleteQSPackingItemBasedOnPnId(String.valueOf(pn_id));
//			response.put("message", (count > 0 && count1 > 0) ? "Success" : "failure");
//			response.put("status", (count > 0 && count1 > 0) ? "yes" : "no");
//			response.put("action", "DELETE_Record_In_INVOICE_MASTER");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	@PostMapping("/invoicedc/releasebyid") // add Facory_id in val here
//	public @ResponseBody Map<String, Object> releaseById(@RequestBody Map<String, String> val) {
//		Map<String, Object> response = new HashMap<String, Object>();
//		Map<String, String> value = null;
//		try {
//			if (val != null && !val.isEmpty()) {
//				value = val;
//			}
//			String id = value.get("id");
//			String factory_id = value.get("factory_id");
//			String released_by = value.get("released_by");
//			int count = invoiceRepository.UpdateReleaseById(id, released_by);
//			invoiceRepository.insertIntoInvoiceHistoryisReleased(id, released_by);
//			long contact_id = invoiceRepository.getContractIdFromInvoiceMaster(Integer.parseInt(id), factory_id);
//			String qs_packing_item_slno = val.get("qs_packing_item_slno");
//			Optional<Integer> qsData = qsPackingRepository.getPn_id(qs_packing_item_slno);
//			if (qsData.isPresent()) {
//				int pn_id = qsData.get();
//				qsPackingRepository.updateisLockinQSPacking(pn_id, "0");
//				qsPackingRepository.updateisLockinQSPackingItem(pn_id, "0");
//			}
//			assignToContractRepository.updateISReleaseInContractMaster(contact_id, "0", factory_id);
//			response.put("message", (count > 0) ? "Success" : "failure");
//			response.put("status", (count > 0) ? "yes" : "no");
//			response.put("action", "RELEASED_Record_In_INVOICE_MASTER");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	@PostMapping("/invoicedc/verification") // add factory_id in the val
//	private @ResponseBody Map<String, Object> invoiceVerfication(@RequestBody Map<String, Object> data) {
//		Map<String, Object> response = new HashMap<String, Object>();
//		int count = 0;
//		String newSeriesNumber = null;
//		Map<String, String> val = null;
//		List<Map<String, String>> obj = null;
//		try {
//			val = (Map<String, String>) data.get("val");
//			obj = (List<Map<String, String>>) data.get("obj");
//			String verified_by = val.get("verified_by");
//			String non_tax_adv = val.get("non_tax_adv");
//			String tax_adv = val.get("tax_adv");
//			String total = val.get("total");
//			String payable_by_customer = val.get("payable_by_customer");
//			String payable_to_dept = val.get("payable_to_dept");
//			String open_tax_adv = val.get("open_tax_adv");
//			String open_non_tax_adv = val.get("open_non_tax_adv");
//			String recovery_amt = val.get("recovery_amt");
//			String factory_id = val.get("factory_id");
//			int id = Integer.parseInt(val.get("id"));
//			String qs_packing_item_slno = val.get("qs_packing_item_slno");
//			Optional<Long> optionalInvoiceId = invoiceRepository.getInvoiceValue(id);
//			Optional<Long> optionalSeriesNumber = invoiceRepository.getSeriesNumberbasedOnId(id);
//			Optional<Long> optionalInvoiceNumber = invoiceRepository.getInvoiceNumber();
//			long invoiceNumber = optionalInvoiceNumber.orElse((long) 0); // read from i=INVOICE_MASTER last invoice no
//			long serieNumber = optionalSeriesNumber.orElse((long) 0);
//			if (optionalInvoiceId.isPresent()) { // if invoice_no already generate and it's release and coming for
//													// approve once again.
//				if (invoiceNumber == 0) {
//					String num = String.valueOf(serieNumber);
//					if (num != null && !num.equals("0")) {
//						int size = num.lastIndexOf("00000");
//						String partAfterZero = null;
//						if (size != -1) {
//							partAfterZero = num.substring(size + 5);// total length from start to till 5(00000)
//							int a = Integer.parseInt(partAfterZero);
//							a = a + 1;
//							partAfterZero = String.valueOf(a);
//						}
//						Long incrementedValue = Long.parseLong(partAfterZero);
//						newSeriesNumber = num.substring(0, size + 5) + incrementedValue;
//						count = invoiceRepository.updateInvoiceVerificationDetails(non_tax_adv, tax_adv, total,
//								payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt,
//								verified_by, newSeriesNumber, id);
//						long contact_id = invoiceRepository.getContractIdFromInvoiceMaster(id, factory_id);
//						assignToContractRepository.updateISReleaseInContractMaster(contact_id, "1", factory_id);
//						Optional<Integer> qsData = qsPackingRepository.getPn_id(qs_packing_item_slno);
//						if (qsData.isPresent()) {
//							int pn_id = qsData.get();
//							qsPackingRepository.updateisLockinQSPacking(pn_id, "1");
//							qsPackingRepository.updateisLockinQSPackingItem(pn_id, "1");
//						}
//						invoiceRepository.detele_INVOICE_TAXENTRY_DETAILS(String.valueOf(id));
//						for (Map<String, String> item : obj) {
//							String tax_id = item.get("tax_id");
//							String tax_per = item.get("tax_per");
//							String tax_value = item.get("tax_value");
//							String adv_tax = item.get("adv_tax");
//							String tax_payable_by_customer = item.get("tax_payable_by_customer");
//							String tax_payable_to_dept = item.get("tax_payable_to_dept");
//							String t_adv = item.get("t_adv");
//							String invoice_id = String.valueOf(id);
//							invoiceRepository.insertINVOICE_TAXENTRY_DETAILS(tax_id, tax_per, tax_value, adv_tax,
//									tax_payable_by_customer, tax_payable_to_dept, t_adv, invoice_id, verified_by);
//						}
//						invoiceRepository.verificationInvoiceHistory(verified_by, id);
//					} else {
//						response.put("Generate_Series_Number", "Please Generate Series Number");
//						return response;
//					}
//				} else {
//					if (serieNumber > 0) {
//						String num = String.valueOf(invoiceNumber);
//						int size = num.lastIndexOf("00000");
//						String partAfterZero = null;
//						if (size != -1) {
//							partAfterZero = num.substring(size + 5);// total length from start to till 5(00000) (+5
//																	// means number of Zeros)
//							int a = Integer.parseInt(partAfterZero);
//							a = a + 1;
//							partAfterZero = String.valueOf(a);
//						}
//						Long incrementedValue = Long.parseLong(partAfterZero);
//						newSeriesNumber = num.substring(0, size + 5) + incrementedValue;
//						count = invoiceRepository.updateInvoiceVerificationDetails(non_tax_adv, tax_adv, total,
//								payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt,
//								verified_by, newSeriesNumber, id);
//						long contact_id = invoiceRepository.getContractIdFromInvoiceMaster(id, factory_id);
//						assignToContractRepository.updateISReleaseInContractMaster(contact_id, "1", factory_id);
//						Optional<Integer> qsData = qsPackingRepository.getPn_id(qs_packing_item_slno);
//						if (qsData.isPresent()) {
//							int pn_id = qsData.get();
//							qsPackingRepository.updateisLockinQSPacking(pn_id, "1");
//							qsPackingRepository.updateisLockinQSPackingItem(pn_id, "1");
//						}
//						for (Map<String, String> item : obj) {
//							String tax_id = item.get("tax_id");
//							String tax_per = item.get("tax_per");
//							String tax_value = item.get("tax_value");
//							String adv_tax = item.get("adv_tax");
//							String tax_payable_by_customer = item.get("tax_payable_by_customer");
//							String tax_payable_to_dept = item.get("tax_payable_to_dept");
//							String t_adv = item.get("t_adv");
//							String invoice_id = String.valueOf(id);
//							invoiceRepository.insertINVOICE_TAXENTRY_DETAILS(tax_id, tax_per, tax_value, adv_tax,
//									tax_payable_by_customer, tax_payable_to_dept, t_adv, invoice_id, verified_by);
//						}
//						invoiceRepository.verificationInvoiceHistory(verified_by, id);
//					} else {
//						response.put("Generate_Series_Number", "Please Generate Series Number");
//						return response;
//					}
//				}
//			} else {
//				if (invoiceNumber == 0) {
//					String num = String.valueOf(serieNumber);
//					if (num != null && !num.equals("0")) {
//						int size = num.lastIndexOf("00000");
//						String partAfterZero = null;
//						if (size != -1) {
//							partAfterZero = num.substring(size + 5);// total length from start to till 5(00000)
//							int a = Integer.parseInt(partAfterZero);
//							a = a + 1;
//							partAfterZero = String.valueOf(a);
//						}
//						Long incrementedValue = Long.parseLong(partAfterZero);
//						newSeriesNumber = num.substring(0, size + 5) + incrementedValue;
//						count = invoiceRepository.updateInvoiceVerificationDetails(non_tax_adv, tax_adv, total,
//								payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt,
//								verified_by, newSeriesNumber, id);
//						long contact_id = invoiceRepository.getContractIdFromInvoiceMaster(id, factory_id);
//						assignToContractRepository.updateISReleaseInContractMaster(contact_id, "1", factory_id);
//						Optional<Integer> qsData = qsPackingRepository.getPn_id(qs_packing_item_slno);
//						if (qsData.isPresent()) {
//							int pn_id = qsData.get();
//							qsPackingRepository.updateisLockinQSPacking(pn_id, "1");
//							qsPackingRepository.updateisLockinQSPackingItem(pn_id, "1");
//						}
//						for (Map<String, String> item : obj) {
//							String tax_id = item.get("tax_id");
//							String tax_per = item.get("tax_per");
//							String tax_value = item.get("tax_value");
//							String adv_tax = item.get("adv_tax");
//							String tax_payable_by_customer = item.get("tax_payable_by_customer");
//							String tax_payable_to_dept = item.get("tax_payable_to_dept");
//							String t_adv = item.get("t_adv");
//							String invoice_id = String.valueOf(id);
//							invoiceRepository.insertINVOICE_TAXENTRY_DETAILS(tax_id, tax_per, tax_value, adv_tax,
//									tax_payable_by_customer, tax_payable_to_dept, t_adv, invoice_id, verified_by);
//
//						}
//						invoiceRepository.verificationInvoiceHistory(verified_by, id);
//					} else {
//						response.put("Generate_Series_Number", "Please Generate Series Number");
//						return response;
//					}
//
//				} else {
//					if (serieNumber > 0) {
//						String num = String.valueOf(invoiceNumber);
//						int size = num.lastIndexOf("00000");
//						String partAfterZero = null;
//						if (size != -1) {
//							partAfterZero = num.substring(size + 5);// total length from start to till 5(00000) (+5
//																	// means number of Zeros)
//							int a = Integer.parseInt(partAfterZero);
//							a = a + 1;
//							partAfterZero = String.valueOf(a);
//						}
//						Long incrementedValue = Long.parseLong(partAfterZero);
//						newSeriesNumber = num.substring(0, size + 5) + incrementedValue;
//						count = invoiceRepository.updateInvoiceVerificationDetails(non_tax_adv, tax_adv, total,
//								payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt,
//								verified_by, newSeriesNumber, id);
//						long contact_id = invoiceRepository.getContractIdFromInvoiceMaster(id, factory_id);
//						assignToContractRepository.updateISReleaseInContractMaster(contact_id, "1", factory_id);
//						Optional<Integer> qsData = qsPackingRepository.getPn_id(qs_packing_item_slno);
//						if (qsData.isPresent()) {
//							int pn_id = qsData.get();
//							qsPackingRepository.updateisLockinQSPacking(pn_id, "1");
//							qsPackingRepository.updateisLockinQSPackingItem(pn_id, "1");
//						}
//						for (Map<String, String> item : obj) {
//							String tax_id = item.get("tax_id");
//							String tax_per = item.get("tax_per");
//							String tax_value = item.get("tax_value");
//							String adv_tax = item.get("adv_tax");
//							String tax_payable_by_customer = item.get("tax_payable_by_customer");
//							String tax_payable_to_dept = item.get("tax_payable_to_dept");
//							String t_adv = item.get("t_adv");
//							String invoice_id = String.valueOf(id);
//							invoiceRepository.insertINVOICE_TAXENTRY_DETAILS(tax_id, tax_per, tax_value, adv_tax,
//									tax_payable_by_customer, tax_payable_to_dept, t_adv, invoice_id, verified_by);
//						}
//						invoiceRepository.verificationInvoiceHistory(verified_by, id);
//					} else {
//						response.put("Generate_Series_Number", "Please Generate Series Number");
//						return response;
//					}
//				}
//			}
//
//			response.put("message", (count > 0) ? "Success" : "failure");
//			response.put("status", (count > 0) ? "yes" : "no");
//			response.put("action", "VERIFICATION_Record_In_INVOICE_MASTER");
//			response.put("Invoice_NO", newSeriesNumber);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//}


