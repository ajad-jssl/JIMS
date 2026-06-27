package com.JIMS.integration.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.interfaces.AdvanceSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.ChallanSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.DebitCreditSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.DeliveryChallanSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.GSTSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.PaymentSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.SelfInvoiceSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.SeriesMasterList;
import com.JIMS.integration.repository.SeriesMasterRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class SeriesMasterController {

	@Autowired
	SeriesMasterRepository seriesMasterRepository;

	@PostMapping("/seriesmaster/add")
	public @ResponseBody Map<String, Object> insertSeriesMaster(@RequestBody Map<String, String> val) {
		// yyyy-mm-dd
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		String key = val.get("action");
		String state_id = null;
		String invoice_series = null;
		String start_date = null;
		String end_date = null;
		String user_id = null;
		LocalDate startDate = null;
		LocalDate endDate = null;
		String factory_id = null;
		String status = "Open";
		try {
			switch (key) {
			case "GST":
				state_id = val.get("state_id");
				invoice_series = val.get("invoice_series");
				start_date = val.get("start_date");
				end_date = val.get("end_date");
				user_id = val.get("user_id");
				startDate = LocalDate.parse(start_date);
				endDate = LocalDate.parse(end_date);
				factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0";
				if (startDate.isAfter(endDate)) {
					status = "Closed";
				}
				if(!invoice_series.matches(".*0000\\d.*")) {
					response.put("Message", "Series Number Should be in this format 2910100001");
					return response;
				}else {
					count = seriesMasterRepository.insertGST(state_id, invoice_series, start_date, end_date, user_id,
							status);
					response.put("message", (count > 0) ? "Success" : "failure");
					response.put("status", (count > 0) ? "yes" : "no");
					response.put("action", "Insert_Record_In_GSTSERIESMASTER");
					return response;

				}
							case "ADVANCE":
				state_id = val.get("state_id");
				invoice_series = val.get("invoice_series");
				start_date = val.get("start_date");
				end_date = val.get("end_date");
				user_id = val.get("user_id");
				startDate = LocalDate.parse(start_date);
				endDate = LocalDate.parse(end_date);
				factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0";
				if (startDate.isAfter(endDate)) {
					status = "Closed";
				}
				count = seriesMasterRepository.insertAdvance(state_id, invoice_series, start_date, end_date, user_id,
						status, factory_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Insert_Record_In_ADVANCESERIESMASTER");
				return response;
			case "DEBITCREDIT":
				state_id = val.get("state_id");
				invoice_series = val.get("invoice_series");
				start_date = val.get("start_date");
				end_date = val.get("end_date");
				user_id = val.get("user_id");
				String debitcredit_type = val.get("debitcredit_type");
				startDate = LocalDate.parse(start_date);
				endDate = LocalDate.parse(end_date);
				factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0";
				if (startDate.isAfter(endDate)) {
					status = "Closed";
				}
				count = seriesMasterRepository.insertDebitCredit(state_id, invoice_series, start_date, end_date,
						user_id, debitcredit_type, status, factory_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Insert_Record_In_DEBITCREDITSERIESMASTER");
				return response;
			case "CHALLAN":
				state_id = val.get("state_id");
				invoice_series = val.get("invoice_series");
				start_date = val.get("start_date");
				end_date = val.get("end_date");
				user_id = val.get("user_id");
				startDate = LocalDate.parse(start_date);
				endDate = LocalDate.parse(end_date);
				factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0";
				if (startDate.isAfter(endDate)) {
					status = "Closed";
				}
				count = seriesMasterRepository.insertChallan(state_id, invoice_series, start_date, end_date, user_id,
						status, factory_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Insert_Record_In_CHALLANSERIESMASTER");
				return response;
			case "PAYMENT":
				state_id = val.get("state_id");
				invoice_series = val.get("invoice_series");
				start_date = val.get("start_date");
				end_date = val.get("end_date");
				user_id = val.get("user_id");
				startDate = LocalDate.parse(start_date);
				endDate = LocalDate.parse(end_date);
				factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0";
				if (startDate.isAfter(endDate)) {
					status = "Closed";
				}
				count = seriesMasterRepository.insertPaymentVoucher(state_id, invoice_series, start_date, end_date,
						user_id, status, factory_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Insert_Record_In_PAYMENTVOUCHERSERIESMASTER");
				return response;
			case "SELFINVOICE":
				state_id = val.get("state_id");
				invoice_series = val.get("invoice_series");
				start_date = val.get("start_date");
				end_date = val.get("end_date");
				user_id = val.get("user_id");
				startDate = LocalDate.parse(start_date);
				endDate = LocalDate.parse(end_date);
				factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0";
				if (startDate.isAfter(endDate)) {
					status = "Closed";
				}
				count = seriesMasterRepository.insertSelfInvoice(state_id, invoice_series, start_date, end_date,
						user_id, status, factory_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Insert_Record_In_SELFINVOICESERIESMASTER");
				return response;
			case "DELIVERYCHALAN":
				state_id = val.get("state_id");
				invoice_series = val.get("invoice_series");
				start_date = val.get("start_date");
				end_date = val.get("end_date");
				user_id = val.get("user_id");
				startDate = LocalDate.parse(start_date);
				endDate = LocalDate.parse(end_date);
				factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0";
				if (startDate.isAfter(endDate)) {
					status = "Closed";
				}
				count = seriesMasterRepository.insertDeliveryChallan(state_id, invoice_series, start_date, end_date,
						user_id, status, factory_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Insert_Record_In_DELIVERYCHALANSERIESMASTER");
				return response;

			default:
				response.put("message", "Please Give Valid Action");
				response.put("status", "no");
				response.put("action", "Record is Not Inserted");
				return response;
			}
		} catch (Exception e) {
		}
		return response;

	}

	@PostMapping("/seriesmaster/update")
	public @ResponseBody Map<String, Object> updateSeriesMaster(@RequestBody Map<String, String> val) {
		// yyyy-mm-dd
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		String key = val.get("action");
		String state_id = null;
		String invoice_series = null;
		String start_date = null;
		String end_date = null;
		String user_id = null;
		String series_id = null;
		try {
			switch (key) {
			case "GST":
				state_id = val.get("state_id");
				invoice_series = val.get("invoice_series");
				start_date = val.get("start_date");
				end_date = val.get("end_date");
				user_id = val.get("user_id");
				series_id = val.get("series_id");
				count = seriesMasterRepository.updateGST(state_id, invoice_series, start_date, end_date, user_id,
						series_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Update_Record_In_GSTSERIESMASTER");
				return response;
			case "ADVANCE":
				state_id = val.get("state_id");
				invoice_series = val.get("invoice_series");
				start_date = val.get("start_date");
				end_date = val.get("end_date");
				user_id = val.get("user_id");
				series_id = val.get("series_id");
				count = seriesMasterRepository.updateAdvance(state_id, invoice_series, start_date, end_date, user_id,
						series_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Update_Record_In_ADVANCESERIESMASTER");
				return response;
			case "DEBITCREDIT":
				state_id = val.get("state_id");
				invoice_series = val.get("invoice_series");
				start_date = val.get("start_date");
				end_date = val.get("end_date");
				user_id = val.get("user_id");
				series_id = val.get("series_id");
				count = seriesMasterRepository.updateDebitCredit(state_id, invoice_series, start_date, end_date,
						user_id, series_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Update_Record_In_DEBITCREDITSERIESMASTER");
				return response;
			case "CHALLAN":
				state_id = val.get("state_id");
				invoice_series = val.get("invoice_series");
				start_date = val.get("start_date");
				end_date = val.get("end_date");
				user_id = val.get("user_id");
				series_id = val.get("series_id");
				count = seriesMasterRepository.updateChallan(state_id, invoice_series, start_date, end_date, user_id,
						series_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Update_Record_In_CHALLANSERIESMASTER");
				return response;
			case "PAYMENT":
				state_id = val.get("state_id");
				invoice_series = val.get("invoice_series");
				start_date = val.get("start_date");
				end_date = val.get("end_date");
				user_id = val.get("user_id");
				series_id = val.get("series_id");
				count = seriesMasterRepository.updatePaymentVoucher(state_id, invoice_series, start_date, end_date,
						user_id, series_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Update_Record_In_PAYMENTVOUCHERSERIESMASTER");
				return response;
			case "SELFINVOICE":
				state_id = val.get("state_id");
				invoice_series = val.get("invoice_series");
				start_date = val.get("start_date");
				end_date = val.get("end_date");
				user_id = val.get("user_id");
				series_id = val.get("series_id");
				count = seriesMasterRepository.updateSelfInvoice(state_id, invoice_series, start_date, end_date,
						user_id, series_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Update_Record_In_SELFINVOICESERIESMASTER");
				return response;
			case "DELIVERYCHALAN":
				state_id = val.get("state_id");
				invoice_series = val.get("invoice_series");
				start_date = val.get("start_date");
				end_date = val.get("end_date");
				user_id = val.get("user_id");
				series_id = val.get("series_id");
				count = seriesMasterRepository.updateDeliveryChallan(state_id, invoice_series, start_date, end_date,
						user_id, series_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Update_Record_In_DELIVERYCHALANSERIESMASTER");
				return response;
			default:
				response.put("message", "Please Give Valid Action");
				response.put("status", "no");
				response.put("action", "Record is Not Inserted");
				return response;
			}
		} catch (Exception e) {
		}
		return response;
	}

	@PostMapping("/seriesmaster/delete")
	public @ResponseBody Map<String, Object> deleteSeriesMaster(@RequestBody Map<String, String> val) {
		// yyyy-mm-dd
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		String key = val.get("action");
		String user_id = null;
		String series_no = null;
		try {
			switch (key) {
			case "GST":
				user_id = val.get("user_id");
				series_no = val.get("series_no");
				count = seriesMasterRepository.deleteGSTSeries(series_no, user_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "delete_Record_In_GSTSERIESMASTER");
				return response;
			case "ADVANCE":
				user_id = val.get("user_id");
				series_no = val.get("series_no");
				count = seriesMasterRepository.deleteAdvanceSeries(series_no, user_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "delete_Record_In_ADVANCESERIESMASTER");
				return response;
			case "DEBITCREDIT":
				user_id = val.get("user_id");
				series_no = val.get("series_no");
				count = seriesMasterRepository.deleteDebitCreditSeries(series_no, user_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "delete_Record_In_DEBITCREDITSERIESMASTER");
				return response;
			case "CHALLAN":
				user_id = val.get("user_id");
				series_no = val.get("series_id");
				count = seriesMasterRepository.deleteChallanSeries(series_no, user_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "delete_Record_In_CHALLANSERIESMASTER");
				return response;
			case "PAYMENT":
				user_id = val.get("user_id");
				series_no = val.get("series_id");
				count = seriesMasterRepository.deletePaymentVoucherSeries(series_no, user_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "delete_Record_In_PAYMENTVOUCHERSERIESMASTER");
				return response;
			case "SELFINVOICE":
				user_id = val.get("user_id");
				series_no = val.get("series_id");
				count = seriesMasterRepository.deleteSelfInvoiceSeries(series_no, user_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "delete_Record_In_SELFINVOICESERIESMASTER");
				return response;
			case "DELIVERYCHALAN":
				user_id = val.get("user_id");
				series_no = val.get("series_id");
				count = seriesMasterRepository.deleteDeliveryChallanSeries(series_no, user_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "delete_Record_In_DELIVERYCHALANSERIESMASTER");
				return response;
			default:
				response.put("message", "Please Give Valid Action");
				response.put("status", "no");
				response.put("action", "Record is Not Inserted");
				return response;
			}
		} catch (Exception e) {
		}
		return response;
	}

	@GetMapping("/seriesmaster/search/{series_id}/{key}")
	public @ResponseBody Map<String, Object> searchSeriesMaster(@PathVariable String series_id,
			@PathVariable String key) {
		// yyyy-mm-dd
		Map<String, Object> response = new HashMap<String, Object>();
		GSTSeriesMasterInterfaces gstSeriesMasterInterfaces = null;
		AdvanceSeriesMasterInterfaces advanceSeriesMasterInterfaces = null;
		DebitCreditSeriesMasterInterfaces debitCreditSeriesMasterInterfaces = null;
		ChallanSeriesMasterInterfaces challanSeriesMasterInterfaces = null;
		PaymentSeriesMasterInterfaces paymentSeriesMasterInterfaces = null;
		SelfInvoiceSeriesMasterInterfaces selfInvoiceSeriesMasterInterfaces = null;
		DeliveryChallanSeriesMasterInterfaces deliveryChallanSeriesMasterInterfaces = null;

		try {
			switch (key) {
			case "GST":
				gstSeriesMasterInterfaces = seriesMasterRepository.searchGSTSeriesById(series_id);
				response.put("message", (gstSeriesMasterInterfaces != null) ? "Success" : "failure");
				response.put("status", (gstSeriesMasterInterfaces != null) ? "yes" : "no");
				response.put("Data", gstSeriesMasterInterfaces);
				response.put("action", "search_Record_In_GSTSERIESMASTER");
				return response;
			case "ADVANCE":
				advanceSeriesMasterInterfaces = seriesMasterRepository.searchAdvanceSeriesById(series_id);
				response.put("message", (advanceSeriesMasterInterfaces != null) ? "Success" : "failure");
				response.put("status", (advanceSeriesMasterInterfaces != null) ? "yes" : "no");
				response.put("Data", advanceSeriesMasterInterfaces);
				response.put("action", "search_Record_In_ADVANCESERIESMASTER");
				return response;
			case "DEBITCREDIT":
				debitCreditSeriesMasterInterfaces = seriesMasterRepository.searchDebitCreditSeriesById(series_id);
				response.put("message", (debitCreditSeriesMasterInterfaces != null) ? "Success" : "failure");
				response.put("status", (debitCreditSeriesMasterInterfaces != null) ? "yes" : "no");
				response.put("Data", debitCreditSeriesMasterInterfaces);
				response.put("action", "search_Record_In_DEBITCREDITSERIESMASTER");
				return response;
			case "CHALLAN":
				challanSeriesMasterInterfaces = seriesMasterRepository.searchChallanSeriesById(series_id);
				response.put("message", (challanSeriesMasterInterfaces != null) ? "Success" : "failure");
				response.put("status", (challanSeriesMasterInterfaces != null) ? "yes" : "no");
				response.put("Data", challanSeriesMasterInterfaces);
				response.put("action", "search_Record_In_CHALLANSERIESMASTER");
				return response;
			case "PAYMENT":
				paymentSeriesMasterInterfaces = seriesMasterRepository.searchPaymentSeriesById(series_id);
				response.put("message", (paymentSeriesMasterInterfaces != null) ? "Success" : "failure");
				response.put("status", (paymentSeriesMasterInterfaces != null) ? "yes" : "no");
				response.put("Data", paymentSeriesMasterInterfaces);
				response.put("action", "search_Record_In_PAYMENTVOUCHERSERIESMASTER");
				return response;
			case "SELFINVOICE":
				selfInvoiceSeriesMasterInterfaces = seriesMasterRepository.searchSelfInvoiceSeriesById(series_id);
				response.put("message", (selfInvoiceSeriesMasterInterfaces != null) ? "Success" : "failure");
				response.put("status", (selfInvoiceSeriesMasterInterfaces != null) ? "yes" : "no");
				response.put("Data", selfInvoiceSeriesMasterInterfaces);
				response.put("action", "search_Record_In_SELFINVOICESERIESMASTER");
				return response;
			case "DELIVERYCHALAN":
				deliveryChallanSeriesMasterInterfaces = seriesMasterRepository
						.searchDeliveryChallanSeriesById(series_id);
				response.put("message", (deliveryChallanSeriesMasterInterfaces != null) ? "Success" : "failure");
				response.put("status", (deliveryChallanSeriesMasterInterfaces != null) ? "yes" : "no");
				response.put("Data", deliveryChallanSeriesMasterInterfaces);
				response.put("action", "search_Record_In_DELIVERYCHALANSERIESMASTER");
				return response;
			default:
				response.put("message", "Please Give Valid Action");
				response.put("status", "no");
				response.put("action", "Record is Not Inserted");
				return response;
			}
		} catch (Exception e) {
		}
		return response;
	}
	
	
//	 Get All Series Master of  GsT
	
	
	@GetMapping("series/GstSeries")
	public @ResponseBody Map<String, Object> listGstTypeSeriesMaster(){
		
		Map<String, Object> response = new HashMap<String, Object>();
		List<GSTSeriesMasterInterfaces> gstSeriesMasterInterfaces = null;
		
		gstSeriesMasterInterfaces = seriesMasterRepository.getAllSeriesByGst();
		response.put("message", (gstSeriesMasterInterfaces != null) ? "Success" : "failure");
		response.put("status", (gstSeriesMasterInterfaces != null) ? "yes" : "no");
		response.put("Data", gstSeriesMasterInterfaces);
		response.put("action", "search_Record_In_GSTSERIESMASTER");
		return response;
	}
	
	
	
	
	
	
	
	
	
	

	/*
	 * public @ResponseBody Map<String, Object> listSeriesMaster(@PathVariable
	 * String series_id, @PathVariable String key) { //yyyy-mm-dd Map<String,Object>
	 * response = new HashMap<String, Object>(); GSTSeriesMasterInterfaces
	 * gstSeriesMasterInterfaces = null; AdvanceSeriesMasterInterfaces
	 * advanceSeriesMasterInterfaces =null; DebitCreditSeriesMasterInterfaces
	 * debitCreditSeriesMasterInterfaces = null; ChallanSeriesMasterInterfaces
	 * challanSeriesMasterInterfaces = null; PaymentSeriesMasterInterfaces
	 * paymentSeriesMasterInterfaces = null; SelfInvoiceSeriesMasterInterfaces
	 * selfInvoiceSeriesMasterInterfaces = null;
	 * DeliveryChallanSeriesMasterInterfaces deliveryChallanSeriesMasterInterfaces =
	 * null;
	 * 
	 * try { switch (key) { case "GST": gstSeriesMasterInterfaces =
	 * seriesMasterRepository.searchGSTSeriesById(series_id);
	 * response.put("message", (gstSeriesMasterInterfaces !=
	 * null)?"Success":"failure"); response.put("status", (gstSeriesMasterInterfaces
	 * != null)?"yes":"no"); response.put("Data", gstSeriesMasterInterfaces);
	 * response.put("action", "List_Record_In_GSTSERIESMASTER"); return response;
	 * case "ADVANCE": advanceSeriesMasterInterfaces =
	 * seriesMasterRepository.searchAdvanceSeriesById(series_id);
	 * response.put("message", (advanceSeriesMasterInterfaces !=
	 * null)?"Success":"failure"); response.put("status",
	 * (advanceSeriesMasterInterfaces != null)?"yes":"no"); response.put("Data",
	 * advanceSeriesMasterInterfaces); response.put("action",
	 * "List_Record_In_ADVANCESERIESMASTER"); return response; case "DEBITCREDIT":
	 * debitCreditSeriesMasterInterfaces =
	 * seriesMasterRepository.searchDebitCreditSeriesById(series_id);
	 * response.put("message", (debitCreditSeriesMasterInterfaces !=
	 * null)?"Success":"failure"); response.put("status",
	 * (debitCreditSeriesMasterInterfaces != null)?"yes":"no"); response.put("Data",
	 * debitCreditSeriesMasterInterfaces); response.put("action",
	 * "List_Record_In_DEBITCREDITSERIESMASTER"); return response; case "CHALLAN":
	 * challanSeriesMasterInterfaces =
	 * seriesMasterRepository.searchChallanSeriesById(series_id);
	 * response.put("message", (challanSeriesMasterInterfaces !=
	 * null)?"Success":"failure"); response.put("status",
	 * (challanSeriesMasterInterfaces != null)?"yes":"no"); response.put("Data",
	 * challanSeriesMasterInterfaces); response.put("action",
	 * "List_Record_In_CHALLANSERIESMASTER"); return response; case "PAYMENT":
	 * paymentSeriesMasterInterfaces =
	 * seriesMasterRepository.searchPaymentSeriesById(series_id);
	 * response.put("message", (paymentSeriesMasterInterfaces !=
	 * null)?"Success":"failure"); response.put("status",
	 * (paymentSeriesMasterInterfaces != null)?"yes":"no"); response.put("Data",
	 * paymentSeriesMasterInterfaces); response.put("action",
	 * "List_Record_In_PAYMENTVOUCHERSERIESMASTER"); return response; case
	 * "SELFINVOICE": selfInvoiceSeriesMasterInterfaces =
	 * seriesMasterRepository.searchSelfInvoiceSeriesById(series_id);
	 * response.put("message", (selfInvoiceSeriesMasterInterfaces !=
	 * null)?"Success":"failure"); response.put("status",
	 * (selfInvoiceSeriesMasterInterfaces != null)?"yes":"no"); response.put("Data",
	 * selfInvoiceSeriesMasterInterfaces); response.put("action",
	 * "List_Record_In_SELFINVOICESERIESMASTER"); return response; case
	 * "DELIVERYCHALAN": deliveryChallanSeriesMasterInterfaces =
	 * seriesMasterRepository.searchDeliveryChallanSeriesById(series_id);
	 * response.put("message", (deliveryChallanSeriesMasterInterfaces !=
	 * null)?"Success":"failure"); response.put("status",
	 * (deliveryChallanSeriesMasterInterfaces != null)?"yes":"no");
	 * response.put("Data", deliveryChallanSeriesMasterInterfaces);
	 * response.put("action", "List_Record_In_DELIVERYCHALANSERIESMASTER"); return
	 * response; default: response.put("message", "Please Give Valid Action");
	 * response.put("status", "no"); response.put("action",
	 * "Record is Not Inserted"); return response; } } catch (Exception e) { }
	 * return response; }
	 */

	@GetMapping("/seriesmaster/list/{factory_id}")
	public @ResponseBody Map<String, Object> listSeriesMaster(@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<SeriesMasterList> val = null;
		try {
			val = seriesMasterRepository.valList(factory_id);

			response.put("message", (val != null && !val.isEmpty()) ? "Success" : "failure");
			response.put("status", (val != null && !val.isEmpty()) ? "yes" : "no");
			response.put("action", "List_Record_In_DELIVERYCHALANSERIESMASTER");
			response.put("Data", val);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	
	
	

	@GetMapping("/seriesmaster/typelist/{typeValue}/{factory_id}")
	public @ResponseBody Map<String, Object> listTypeSeriesMaster(@PathVariable String typeValue,
			@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<SeriesMasterList> val = null;
		try {

			// val = seriesMasterRepository.typeList();
			switch (typeValue) {
			case "GST":
				val = seriesMasterRepository.typeListGST(factory_id);
				break;
			case "ADVANCE":
				val = seriesMasterRepository.typeListADVANCE(factory_id);
				break;
			case "DEBITCREDIT":
				val = seriesMasterRepository.typeListDEBITCREDIT(factory_id);
				break;
			case "CHALLAN":
				val = seriesMasterRepository.typeListCHALLAN(factory_id);
				break;
			case "PAYMENT":
				val = seriesMasterRepository.typeListPAYMENT(factory_id);
				break;
			case "SELFINVOICE":
				val = seriesMasterRepository.typeListSELFINVOICE(factory_id);
				break;
			case "DELIVERYCHALAN":
				val = seriesMasterRepository.typeListDELIVERYCHALAN(factory_id);
				break;
			default:
				break;

			}
			response.put("message", (val != null && !val.isEmpty()) ? "Success" : "failure");
			response.put("status", (val != null && !val.isEmpty()) ? "yes" : "no");
			response.put("action", "List_Record_In_TYPELIST_SERIESMASTER");
			response.put("Data", val);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
