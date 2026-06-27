package com.JIMS.integration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.interfaces.Advance_QSPacking_QSPackingItem_LIST_INTERFACES;
import com.JIMS.integration.interfaces.AssignToContract;
import com.JIMS.integration.interfaces.InvoiceMasterInterface;
import com.JIMS.integration.repository.AssignToContractRepository;
import com.JIMS.integration.repository.InvoiceRepository;
import com.JIMS.integration.repository.QSAdvancePackingRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class AdvanceInvoiceController {
	
	@Autowired
	public AssignToContractRepository assignToContractRepository;
	@Autowired
	public QSAdvancePackingRepository qsAdvancePackingRepository;
	@Autowired
	public InvoiceRepository invoiceRepository;
	
	@PostMapping("/advanceinvoice/add")
	public @ResponseBody Map<String, Object> createAdvanceInvoice(@RequestParam int contract_id,
			@RequestParam String load_id, @RequestParam String invoice_type, @RequestParam int con_slno,
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
			@RequestParam(required = false) List<String> slno) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			
			int count =  qsAdvancePackingRepository.insertAdvanceInvoice(contract_id, load_id, con_slno , product_desc, remarks, date_of_notification, date_val, bg_type,
			date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,contract_name,pn_id,created_by);
			response.put("action", "ADVANCEINVOICE_ADD");
			response.put("message",	(count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");	
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/advanceinvoice/update")
	public @ResponseBody Map<String, Object> updateAdvanceInvoice(@RequestParam Integer id,
			@RequestParam String modified_by, @RequestParam String product_desc, @RequestParam String remarks,
			@RequestParam(required = false) String date_of_notification, @RequestParam(required = false) String date_val, @RequestParam String pn_id,
			@RequestParam(required = false) String bg_type, @RequestParam(required = false) String date_of_issue, @RequestParam(required = false) String reference_no,
			@RequestParam(required = false) String lc_number, @RequestParam(required = false) String supply_place, @RequestParam(required = false) String s_t_exempted,
			@RequestParam(required = false) String lr_docketno, @RequestParam(required = false) String bg_no, @RequestParam(required = false) String date_of_expiry,
			@RequestParam(required = false) String date_of_ref, @RequestParam(required = false) String lc_issue_date,
			@RequestParam(required = false) List<String> service_code_id,
			@RequestParam(required = false) List<String> hsn_code_id, @RequestParam(required = false) String invoice_type_id,//invoice_type_id for type_remarks
			@RequestParam(required = false) List<String> slno) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			int count =  qsAdvancePackingRepository.updateAdvanceInvoice(product_desc, remarks, date_of_notification, date_val, bg_type,
			date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,pn_id,modified_by,id);
			response.put("action", "ADVANCEINVOICE_UPDATE");
			response.put("message",	(count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");	
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping("/advanceinvoice/list")
	public @ResponseBody Map<String,Object> advanceInvoicelist(@RequestParam int factory_id){
		Map<String, Object> response = new HashMap<String, Object>();
		List<InvoiceMasterInterface> invoiceMasterInterfaces = null;
		try {
			invoiceMasterInterfaces = qsAdvancePackingRepository.listAdvanceInvoiceMasterInfo(factory_id);
			response.put("message", (invoiceMasterInterfaces != null)?"Success":"failure");
			response.put("status", (invoiceMasterInterfaces != null)?"yes":"no");
			response.put("action", "List_Record_In_ADVANCEINVOICE_MASTER");
			response.put("Data", invoiceMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	@GetMapping("/advanceinvoicemaster/getloadid")
	public @ResponseBody Map<String, Object> serachListLoadId() {
		Map<String, Object> response = new HashMap<String, Object>();
		String invoiceValue = "ADV-";
		try {
			int numberOfRows = qsAdvancePackingRepository.getRowCount();
			if(numberOfRows == 0) {
				invoiceValue = "ADV-1";
				numberOfRows++;
			}else {
				numberOfRows = numberOfRows+1;
				invoiceValue = invoiceValue+numberOfRows;
			}
			response.put("message", (numberOfRows > 0) ? "Success" : "failure");
			response.put("status", (numberOfRows > 0) ? "yes" : "no");
			response.put("action", "ListLoadId_Record");
			response.put("data", invoiceValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/advanceinvoicemaster/delete")
	public @ResponseBody Map<String, Object> deleteId(@RequestParam int id,@RequestParam String modified_by) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			int pn_id = qsAdvancePackingRepository.getPnId(id);
			int numberOfRows = qsAdvancePackingRepository.deleteAdvanceInvoice(id,modified_by);
			qsAdvancePackingRepository.deleteQSAdvancePackingMasterRecord(String.valueOf(pn_id), modified_by);
			qsAdvancePackingRepository.deleteQSAdvancePackingItemMasterRecord(String.valueOf(pn_id), modified_by);
			response.put("message", (numberOfRows > 0) ? "Success" : "failure");
			response.put("status", (numberOfRows > 0) ? "yes" : "no");
			response.put("action", "DELETE_ADVANCEINVOICE_RECORD");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	@GetMapping("/advanceinvoicemaster/search")
	public @ResponseBody Map<String, Object> searchId(@RequestParam int id, @RequestParam int contract_id, @RequestParam String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<AssignToContract> assignToContract = null;
		List<Advance_QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
		InvoiceMasterInterface invoiceMasterInterfaces = null;
		try {
			invoiceMasterInterfaces = qsAdvancePackingRepository.getInfo(id);
			String pn_id = (invoiceMasterInterfaces != null) ? invoiceMasterInterfaces.getPn_id() : "0";			
			qsPackingInterfaces = qsAdvancePackingRepository.getAdvancePackingAndItemNote(pn_id);
			assignToContract = assignToContractRepository.searchContract(String.valueOf(contract_id),factory_id);
			response.put("message", (assignToContract != null &&qsPackingInterfaces != null && invoiceMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (assignToContract != null &&qsPackingInterfaces != null && invoiceMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "SEARCH_ADVANCEINVOICE_RECORD");
			response.put("AdvanceInvoice_DATA", invoiceMasterInterfaces);
			response.put("AdvanceQSPACKING_DATA", qsPackingInterfaces);
 			response.put("ASSIGN_TO_CONTRACT_DATA", assignToContract);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/advanceinvoice/verification")
	public @ResponseBody Map<String, Object> invoiceVerfication(@RequestParam String verified_by,@RequestParam String id) {
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		String newSeriesNumber = null;
		try {
			Optional<Long> optionalSeriesNumber = qsAdvancePackingRepository.getAdvanceSeriesNumberbasedOnId(Integer.parseInt(id));
			Optional<Long> optionalInvoiceNumber = qsAdvancePackingRepository.getInvoiceNumber();
			long invoiceNumber = optionalInvoiceNumber.orElse((long) 0);
			long serieNumber = optionalSeriesNumber.orElse((long) 0);
			
			if(invoiceNumber  == 0) {
				String num = String.valueOf(serieNumber);
				if(num != null && !num.equals("0")) {
					int size = num.lastIndexOf("0000");
					String partAfterZero = null;
					if(size != -1) {
						  partAfterZero = num.substring(size + 4); 
						int  a = Integer.parseInt(partAfterZero);
						a = a+1;
						partAfterZero = String.valueOf(a);
					}
					Long incrementedValue = Long.parseLong(partAfterZero);
					newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
				count = qsAdvancePackingRepository.updateAdvanceInvoiceVerification(verified_by,newSeriesNumber,Integer.parseInt(id));
				}
				else {
					response.put("Generate_Series_Number","Please Generate Series Number" );
					return response;
				}
				
			}else {
				if(serieNumber > 0) {
				String num = String.valueOf(invoiceNumber);
				int size = num.lastIndexOf("0000");
				String partAfterZero = null;
				if(size != -1) {
					  partAfterZero = num.substring(size + 4);// total length from start to till 5(00000) (+5 means number of Zeros)
					int  a = Integer.parseInt(partAfterZero);
					a = a+1;
					partAfterZero = String.valueOf(a);
				}
				Long incrementedValue = Long.parseLong(partAfterZero);
				newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
				count = qsAdvancePackingRepository.updateAdvanceInvoiceVerification(verified_by,newSeriesNumber,Integer.parseInt(id));
				//invoiceRepository.verificationInvoiceHistory(verified_by, id);
				}
				else {
					response.put("Generate_Series_Number","Please Generate Series Number" );
					return response;
				}
			}
			
			response.put("message", (count > 0)?"Success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "VERIFICATION_Record_In_INVOICE_MASTER");
			response.put("Invoice_NO", newSeriesNumber);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
