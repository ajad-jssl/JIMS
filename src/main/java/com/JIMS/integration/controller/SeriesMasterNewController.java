package com.JIMS.integration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.interfaces.AdvanceSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.ChallanSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.DebitCreditSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.DeliveryChallanSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.GSTSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.GstStatesInterface;
import com.JIMS.integration.interfaces.PaymentSeriesMasterInterfaces;
import com.JIMS.integration.interfaces.SelfInvoiceSeriesMasterInterfaces;
import com.JIMS.integration.repository.SeriesMasterRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class SeriesMasterNewController {

	Logger logger = LogManager.getLogger(SeriesMasterNewController.class);
	@Autowired
	SeriesMasterRepository seriesMasterRepository;

//	@PostMapping("/seriesmaster/addGstMaster")
//	public @ResponseBody Map<String, Object> addGstMaster(@RequestParam String state_id,
//			@RequestParam String invoice_series, @RequestParam String start_date, @RequestParam String end_date,
//			@RequestParam String created_by) {
//		logger.info("EXECUTING METHOD :: addGstMaster");
//		Map<String, Object> addGstMastermap = new HashMap<String, Object>();
//		GstStatesInterface lastestStatus = null;
//		try {
//			if(!invoice_series.matches(".*0000\\d.*")) {
//				 addGstMastermap.put("message","Start with number continous with 0000 end with number EG:2310100002");
//				return addGstMastermap;
//			}
//			int  status_count = 0;
//			
//		lastestStatus  = seriesMasterRepository.getLatestGstStatus();
//		
//		if(lastestStatus.getGst_status() !=null) {
//			
//			if(lastestStatus.getGst_status() == "open") {
//				status_count = 1;
//			}
//			else {
//				status_count = 0;
//			}
//		}
//			
//		if(status_count > 0) {
//			addGstMastermap.put("message","The previous State master is No Closed yet , cant't create a new one");
//			return addGstMastermap;
//		}
//			
//			
//			
//			
//			logger.info("EXECUTING METHOD :: BEFORE ADDING addGstMaster");
//			int addGstRecord = seriesMasterRepository.addGstMasterNew(state_id, invoice_series, start_date, end_date,
//					created_by);
//			logger.info("EXECUTING METHOD :: AFTER ADDING addGstMaster");
//			addGstMastermap.put("message", (addGstRecord > 0) ? "Success" : "failure");
//			addGstMastermap.put("status", (addGstRecord > 0) ? "yes" : "no");
//			addGstMastermap.put("action", "AddGstMaster");
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("ERROR IN THE METHOD FOR addGstMaster ::   -> " + e.getMessage());
//		}
//		logger.info("EXECUTED METHOD :: addGstMaster");
//		return addGstMastermap;
//
//	}

	
	
	@PostMapping("/seriesmaster/addGstMaster")
	public @ResponseBody Map<String, Object> addGstMaster(
	        @RequestParam String state_id,
	        @RequestParam String invoice_series,
	        @RequestParam String start_date,
	        @RequestParam String end_date,
	        @RequestParam String created_by) {

	    logger.info("EXECUTING METHOD :: addGstMaster");

	    Map<String, Object> response = new HashMap<>();

	    try {
	        // 1️⃣ Invoice series validation
	        if (!invoice_series.matches(".*0000\\d.*")) {
	            response.put("status", "no");
	            response.put("message",
	                    "Invoice series must contain 0000 followed by number (eg: 2310100002)");
	            return response;
	        }

	        GstStatesInterface latestStatus =
	                seriesMasterRepository.getLatestGstStatusByState(state_id);

	        if (latestStatus != null) {

	            logger.info("GST Status from DB : {}", latestStatus.getGst_status());

	            if (latestStatus.getGst_status().equalsIgnoreCase("OPEN")) {
	                response.put("status", "no");
	                response.put("message",
	                        "GST series is still OPEN");
	                return response;
	            }
	        }

	        
	       

	        // 4️⃣ Allow creation (CLOSE or no previous record)
	        logger.info("EXECUTING METHOD :: BEFORE ADDING addGstMaster");

	        int addGstRecord = seriesMasterRepository.addGstMasterNew(
	                state_id, invoice_series, start_date, end_date, created_by);

	        logger.info("EXECUTING METHOD :: AFTER ADDING addGstMaster");

	        response.put("status", addGstRecord > 0 ? "yes" : "no");
	        response.put("message", addGstRecord > 0 ? "Success" : "Failure");
	        response.put("action", "AddGstMaster");

	    } catch (Exception e) {
			/* logger.error("ERROR IN addGstMaster", e); */
	        response.put("status", "no");
	        response.put("message", "Gst series Invoice No is Already Exist ");
	    }

	    logger.info("EXECUTED METHOD :: addGstMaster");
	    return response;
	}

	
	
	
	
	@PostMapping("/seriesmaster/addAdvanceMaster")
	public @ResponseBody Map<String, Object> addAdvanceMaster(@RequestParam String state_id,
			@RequestParam String invoice_series, @RequestParam String start_date, @RequestParam String end_date,
			@RequestParam String created_by) {

		Map<String, Object> addAdvanceMastermap = new HashMap<String, Object>();

		try {
		    if (!invoice_series.matches(".*0000\\d.*")) {
		    	addAdvanceMastermap.put("status", "no");
		    	addAdvanceMastermap.put("message",
	                    "Invoice series must contain 0000 followed by number (eg: 2310100002)");
	            return addAdvanceMastermap;
	        }
		    
		    AdvanceStatesInterface latestStatus =
	                seriesMasterRepository.getLatestAdvanceStatusByState(state_id);

	        if (latestStatus != null) {

	            logger.info("ADVANCE Status from DB : {}",
	                    latestStatus.getGst_status());

	   
	            if ("OPEN".equalsIgnoreCase(latestStatus.getGst_status())) {
	            	addAdvanceMastermap.put("status", "no");
	            	addAdvanceMastermap.put("message",
	                        "Advance series is still OPEN.");
	                return addAdvanceMastermap;
	            }
	        }
		    

			int addAdvanceRecord = seriesMasterRepository.addAdvanceMasterNew(state_id, invoice_series, start_date,
					end_date, created_by);

			addAdvanceMastermap.put("message", (addAdvanceRecord > 0) ? "Success" : "failure");
			addAdvanceMastermap.put("status", (addAdvanceRecord > 0) ? "yes" : "no");
			addAdvanceMastermap.put("action", "AddAdvanceMaster");

		} catch (Exception e) {
			/* e.printStackTrace(); */
			logger.info(e.getMessage());
		}

		return addAdvanceMastermap;

	}

	@PostMapping("/seriesmaster/addDebitCreditMaster")
	public @ResponseBody Map<String, Object> addDebitCreditMaster(@RequestParam String state_id,
			@RequestParam String invoice_series, @RequestParam String start_date, @RequestParam String end_date,
			@RequestParam String debitcredit_type, @RequestParam String created_by) {

		Map<String, Object> addDebitCreditMastermap = new HashMap<String, Object>();

		try {
			
			
			  if (!invoice_series.matches(".*0000\\d.*")) {
				  addDebitCreditMastermap.put("status", "no");
				  addDebitCreditMastermap.put("message",
		                    "Invoice series must contain 0000 followed by number (eg: 2910000001)");
		            return addDebitCreditMastermap;
		        }
			  
			  DebitCreditInterface latestStatus =
		                seriesMasterRepository.getLatestDebitCreditStatusByStateAndType(
		                        state_id, debitcredit_type
		                );

		        if (latestStatus != null) {

		            logger.info("Debit/Credit Status from DB : {}",
		                    latestStatus.getGst_status());

		            if ("OPEN".equalsIgnoreCase(latestStatus.getGst_status())) {
		            	addDebitCreditMastermap.put("status", "no");
		            	addDebitCreditMastermap.put("message",
		                        debitcredit_type.toUpperCase() + " series is still OPEN");
		                return addDebitCreditMastermap;
		            }
		        }

			int addDebitCreditRecord = seriesMasterRepository.addDebitCreditMasterNew(state_id, invoice_series,
					start_date, end_date, debitcredit_type, created_by);

			addDebitCreditMastermap.put("message", (addDebitCreditRecord > 0) ? "Success" : "failure");
			addDebitCreditMastermap.put("status", (addDebitCreditRecord > 0) ? "yes" : "no");
			addDebitCreditMastermap.put("action", "AddDebitCreditMaster");

		} catch (Exception e) {
			/* e.printStackTrace(); */
			
			logger.info(e.getMessage());
		}

		return addDebitCreditMastermap;

	}

	@PostMapping("/seriesmaster/addChallanMaster")
	public @ResponseBody Map<String, Object> addChallanMaster(@RequestParam String state_id,
			@RequestParam String invoice_series, @RequestParam String start_date, @RequestParam String end_date,
			@RequestParam String created_by) {

		Map<String, Object> addChallanMastermap = new HashMap<String, Object>();

		try {
			
			
			   if (!invoice_series.matches(".*0000\\d.*")) {
				   addChallanMastermap.put("status", "no");
				   addChallanMastermap.put("message",
		                    "Invoice series must contain 0000 followed by number (eg: 2310100002)");
		            return addChallanMastermap;
		        }

			   ChallanSeriesInterface latestStatus =
		                seriesMasterRepository.getLatestChallanStatusByState(state_id);

		        if (latestStatus != null) {

		            logger.info("Challan Status from DB : {}", latestStatus.getGst_status());

		            if ("OPEN".equalsIgnoreCase(latestStatus.getGst_status())) {
		            	addChallanMastermap.put("status", "no");
		            	addChallanMastermap.put("message",
		                        "Challan series is still OPEN");
		                return addChallanMastermap;
		            }
		        }


			int addChallanRecord = seriesMasterRepository.addChallanMasterNew(state_id, invoice_series, start_date,
					end_date, created_by);

			addChallanMastermap.put("message", (addChallanRecord > 0) ? "Success" : "failure");
			addChallanMastermap.put("status", (addChallanRecord > 0) ? "yes" : "no");
			addChallanMastermap.put("action", "AddChallanMaster");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return addChallanMastermap;

	}

	@PostMapping("/seriesmaster/addPaymentVoucherMaster")
	public @ResponseBody Map<String, Object> addPaymentVoucherMaster(@RequestParam String state_id,
			@RequestParam String invoice_series, @RequestParam String start_date, @RequestParam String end_date,
			@RequestParam String created_by) {

		Map<String, Object> addPaymentVouchermap = new HashMap<String, Object>();

		try {
			
			  if (!invoice_series.matches(".*0000\\d.*")) {
				  addPaymentVouchermap.put("status", "no");
				  addPaymentVouchermap.put("message",
		                    "Invoice series must contain 0000 followed by number (eg: 2310100002)");
		            return addPaymentVouchermap;
		        }
			
			  PaymentSeriesInterface latestStatus =
		                seriesMasterRepository.getLatestPaymentVoucherStatusByState(state_id);

		        if (latestStatus != null) {

		            logger.info("Payment Voucher Status from DB : {}",
		                    latestStatus.getGst_status());

		            if ("OPEN".equalsIgnoreCase(latestStatus.getGst_status())) {
		            	addPaymentVouchermap.put("status", "no");
		            	addPaymentVouchermap.put("message",
		                        "Payment Voucher series is still OPEN");
		                return addPaymentVouchermap;
		            }
		        }

			int addPaymentVoucherRecord = seriesMasterRepository.addPaymentVoucherNew(state_id, invoice_series,
					start_date, end_date, created_by);

			addPaymentVouchermap.put("message", (addPaymentVoucherRecord > 0) ? "Success" : "failure");
			addPaymentVouchermap.put("status", (addPaymentVoucherRecord > 0) ? "yes" : "no");
			addPaymentVouchermap.put("action", "AddPaymentVoucherMaster");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return addPaymentVouchermap;

	}

	@PostMapping("/seriesmaster/addSelfInvoiceMaster")
	public @ResponseBody Map<String, Object> addSelfInvoiceMaster(@RequestParam String state_id,
			@RequestParam String invoice_series, @RequestParam String start_date, @RequestParam String end_date,
			@RequestParam String created_by) {

		Map<String, Object> addSelfInvoicemap = new HashMap<String, Object>();

		try {
			
			 if (!invoice_series.matches(".*0000\\d.*")) {
				 addSelfInvoicemap.put("status", "no");
				 addSelfInvoicemap.put("message",
		                    "Invoice series must contain 0000 followed by number (eg: 2310100002)");
		            return addSelfInvoicemap;
		        }
			 
			 
			 SelfInvoiceSeriesInterface latestStatus = seriesMasterRepository.getLatestInVoiceStatusByState(state_id);
			 
			 
			 if (latestStatus != null) {

		            logger.info("GST Status from DB : {}", latestStatus.getGst_status());

		            if (latestStatus.getGst_status().equalsIgnoreCase("OPEN")) {
		            	addSelfInvoicemap.put("status", "no");
		            	addSelfInvoicemap.put("message",
		                        "Self Invoice series is still OPEN");
		                return addSelfInvoicemap;
		            }
		        }

			int addSelfInvoiceRecord = seriesMasterRepository.addSelfInvoiceNew(state_id, invoice_series, start_date,
					end_date, created_by);

			addSelfInvoicemap.put("message", (addSelfInvoiceRecord > 0) ? "Success" : "failure");
			addSelfInvoicemap.put("status", (addSelfInvoiceRecord > 0) ? "yes" : "no");
			addSelfInvoicemap.put("action", "AddSelfInvoiceMaster");

		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		return addSelfInvoicemap;

	}

	@PostMapping("/seriesmaster/addDeliveryChallanMaster")
	public @ResponseBody Map<String, Object> addDeliveryChallanMaster(@RequestParam String state_id,
			@RequestParam String invoice_series, @RequestParam String start_date, @RequestParam String end_date,
			@RequestParam String created_by) {

		Map<String, Object> addDeliveryChallanmap = new HashMap<String, Object>();

		try {
			
			if (!invoice_series.matches(".*0000\\d.*")) {
				addDeliveryChallanmap.put("status", "no");
				addDeliveryChallanmap.put("message",
	                    "Invoice series must contain 0000 followed by number (eg: 2310100002)");
	            return addDeliveryChallanmap;
	        }
			DeliverySeriesInterface latestStatus = seriesMasterRepository.getLatestDeliveryStatusByState(state_id);
			
			 if (latestStatus != null) {

		            logger.info("GST Status from DB : {}", latestStatus.getGst_status());

		            if (latestStatus.getGst_status().equalsIgnoreCase("OPEN")) {
		            	addDeliveryChallanmap.put("status", "no");
		            	addDeliveryChallanmap.put("message",
		                        "Delivery Challan series is still OPEN");
		                return addDeliveryChallanmap;
		            }
		        }

			

			int addDeliveryChallanRecord = seriesMasterRepository.addDeliveryChallan(state_id, invoice_series,
					start_date, end_date, created_by);

			addDeliveryChallanmap.put("message", (addDeliveryChallanRecord > 0) ? "Success" : "failure");
			addDeliveryChallanmap.put("status", (addDeliveryChallanRecord > 0) ? "yes" : "no");
			addDeliveryChallanmap.put("action", "AddDeliveryChallanMaster");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return addDeliveryChallanmap;

	}

	@GetMapping("/seriesmaster/getGstMasterListNew")
	public @ResponseBody Map<String, Object> getGstMasterList() {
		logger.info("EXECUTING METHOD :: getGstMasterList");
		Map<String, Object> getGstMasterListmap = new HashMap<String, Object>();
		List<GSTSeriesMasterInterfaces> allGstMasterlist = null;
		try {
			allGstMasterlist = seriesMasterRepository.getGstMasterListNew();
			getGstMasterListmap.put("action", "GstMasterInfo");
			getGstMasterListmap.put("message", (allGstMasterlist.size() > 0) ? "Success" : "Gst details not found!");
			getGstMasterListmap.put("status", (allGstMasterlist.size() > 0) ? "yes" : "no");
			if ((allGstMasterlist != null) && (!allGstMasterlist.isEmpty())) {
				getGstMasterListmap.put("GstMasterList", allGstMasterlist);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getGstMasterList ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: getGstMasterList");
		return getGstMasterListmap;
	}

	@GetMapping("/seriesmaster/getAdvanceMasterListNew")
	public @ResponseBody Map<String, Object> getAdvanceMasterList() {

		Map<String, Object> getAdvanceMasterListmap = new HashMap<String, Object>();
		List<AdvanceSeriesMasterInterfaces> allAdvanceMasterlist = null;
		try {
			allAdvanceMasterlist = seriesMasterRepository.getAdvanceMasterListNew();

			getAdvanceMasterListmap.put("action", "AdvanceMasterInfo");
			getAdvanceMasterListmap.put("message",
					(allAdvanceMasterlist.size() > 0) ? "Success" : "Advance Master details not found!");
			getAdvanceMasterListmap.put("status", (allAdvanceMasterlist.size() > 0) ? "yes" : "no");

			if ((allAdvanceMasterlist != null) && (!allAdvanceMasterlist.isEmpty())) {

				getAdvanceMasterListmap.put("AdvanceMasterList", allAdvanceMasterlist);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getAdvanceMasterListmap;

	}

	@GetMapping("/seriesmaster/getDebitCreditListNew")
	public @ResponseBody Map<String, Object> getDebitCreditMasterListNew() {

		Map<String, Object> getDebitCreditMasterListmap = new HashMap<String, Object>();
		List<DebitCreditSeriesMasterInterfaces> allDebitCreditlist = null;
		try {
			allDebitCreditlist = seriesMasterRepository.getDebitCreditListNew();

			getDebitCreditMasterListmap.put("action", "DebitCreditInfo");
			getDebitCreditMasterListmap.put("message",
					(allDebitCreditlist.size() > 0) ? "Success" : "Debit/Credit Master details not found!");
			getDebitCreditMasterListmap.put("status", (allDebitCreditlist.size() > 0) ? "yes" : "no");

			if ((allDebitCreditlist != null) && (!allDebitCreditlist.isEmpty())) {

				getDebitCreditMasterListmap.put("DebitCreditList", allDebitCreditlist);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getDebitCreditMasterListmap;

	}

	@GetMapping("/seriesmaster/getChallansMasterListNew")
	public @ResponseBody Map<String, Object> getChallansMasterListNew() {

		Map<String, Object> getChallanMasterListmap = new HashMap<String, Object>();
		List<ChallanSeriesMasterInterfaces> allChallanMasterlist = null;
		try {
			allChallanMasterlist = seriesMasterRepository.getChallansMasterListNew();

			getChallanMasterListmap.put("action", "ChallanInfo");
			getChallanMasterListmap.put("message",
					(allChallanMasterlist.size() > 0) ? "Success" : "Challan Master details not found!");
			getChallanMasterListmap.put("status", (allChallanMasterlist.size() > 0) ? "yes" : "no");

			if ((allChallanMasterlist != null) && (!allChallanMasterlist.isEmpty())) {

				getChallanMasterListmap.put("ChallansMasterList", allChallanMasterlist);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getChallanMasterListmap;

	}

	@GetMapping("/seriesmaster/getPaymentVoucherMasterListNew")
	public @ResponseBody Map<String, Object> getPaymentVoucherMasterListNew() {

		Map<String, Object> getPaymentMasterListmap = new HashMap<String, Object>();
		List<PaymentSeriesMasterInterfaces> allPaymentVoucherlist = null;
		try {
			allPaymentVoucherlist = seriesMasterRepository.getPaymentVoucherMasterListNew();

			getPaymentMasterListmap.put("action", "PaymentVoucherInfo");
			getPaymentMasterListmap.put("message",
					(allPaymentVoucherlist.size() > 0) ? "Success" : "Payment Voucher Master details not found!");
			getPaymentMasterListmap.put("status", (allPaymentVoucherlist.size() > 0) ? "yes" : "no");

			if ((allPaymentVoucherlist != null) && (!allPaymentVoucherlist.isEmpty())) {

				getPaymentMasterListmap.put("PaymentVoucherList", allPaymentVoucherlist);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getPaymentMasterListmap;

	}

	@GetMapping("/seriesmaster/getSelfInvoiceMasterListNew")
	public @ResponseBody Map<String, Object> getSelfInvoiceListNew() {

		Map<String, Object> getSelfInvoiceListmap = new HashMap<String, Object>();
		List<SelfInvoiceSeriesMasterInterfaces> allSelfInvoicelist = null;
		try {
			allSelfInvoicelist = seriesMasterRepository.getSelfInvoiceListNew();

			getSelfInvoiceListmap.put("action", "PaymentVoucherInfo");
			getSelfInvoiceListmap.put("message",
					(allSelfInvoicelist.size() > 0) ? "Success" : "SelfInvoice Master details not found!");
			getSelfInvoiceListmap.put("status", (allSelfInvoicelist.size() > 0) ? "yes" : "no");

			if ((allSelfInvoicelist != null) && (!allSelfInvoicelist.isEmpty())) {

				getSelfInvoiceListmap.put("SelfInvoiceList", allSelfInvoicelist);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getSelfInvoiceListmap;

	}

	@GetMapping("/seriesmaster/getDeliveryChallanMasterListNew")
	public @ResponseBody Map<String, Object> getDeliveryChallanMasterListNew() {

		Map<String, Object> getDeliveryChallanListmap = new HashMap<String, Object>();
		List<DeliveryChallanSeriesMasterInterfaces> allDeliveryChallanlist = null;
		try {
			allDeliveryChallanlist = seriesMasterRepository.getDeliveryChallanListNew();

			getDeliveryChallanListmap.put("action", "DeliveryChallanInfo");
			getDeliveryChallanListmap.put("message",
					(allDeliveryChallanlist.size() > 0) ? "Success" : "DelievryChallan Master details not found!");
			getDeliveryChallanListmap.put("status", (allDeliveryChallanlist.size() > 0) ? "yes" : "no");

			if ((allDeliveryChallanlist != null) && (!allDeliveryChallanlist.isEmpty())) {

				getDeliveryChallanListmap.put("DeliveryChallanList", allDeliveryChallanlist);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getDeliveryChallanListmap;

	}

	@GetMapping("/seriesmaster/getGstMasterListById")
	public @ResponseBody Map<String, Object> getGstMasterById(@RequestParam String series_id) {

		Map<String, Object> getGstMasterInfoBasedOnIdmap = new HashMap<String, Object>();
		GSTSeriesMasterInterfaces gstMasterByid = null;
		try {
			gstMasterByid = seriesMasterRepository.getGstMasterBasedOnId(series_id);

			getGstMasterInfoBasedOnIdmap.put("action", "GstMasterInfoBasedOnId");
			getGstMasterInfoBasedOnIdmap.put("message",
					(gstMasterByid != null) ? "Success" : "Gst details not available");
			getGstMasterInfoBasedOnIdmap.put("status", (gstMasterByid != null) ? "yes" : "no");

			if (gstMasterByid != null) {
				ObjectMapper mapper = new ObjectMapper();
				getGstMasterInfoBasedOnIdmap
						.putAll(mapper.convertValue(gstMasterByid, new TypeReference<Map<String, Object>>() {
						}));
				mapper.clearProblemHandlers();
				mapper = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getGstMasterInfoBasedOnIdmap;

	}

	@GetMapping("/seriesmaster/getAdvanceinfoById")
	public @ResponseBody Map<String, Object> getAdvanceInfoBasedOnId(@RequestParam String series_id) {

		Map<String, Object> getAdvanceDetailsBasedOnIdmap = new HashMap<String, Object>();
		AdvanceSeriesMasterInterfaces advanceDetailsBsdOnId = null;
		try {
			advanceDetailsBsdOnId = seriesMasterRepository.getAdvanceMasterBaedOnId(series_id);

			getAdvanceDetailsBasedOnIdmap.put("action", "AdvanceInfoBasedOnId");
			getAdvanceDetailsBasedOnIdmap.put("message",
					(advanceDetailsBsdOnId != null) ? "Success" : "AdvanceInfo not available");
			getAdvanceDetailsBasedOnIdmap.put("status", (advanceDetailsBsdOnId != null) ? "yes" : "no");

			if (advanceDetailsBsdOnId != null) {
				ObjectMapper mapper = new ObjectMapper();
				getAdvanceDetailsBasedOnIdmap
						.putAll(mapper.convertValue(advanceDetailsBsdOnId, new TypeReference<Map<String, Object>>() {
						}));
				mapper.clearProblemHandlers();
				mapper = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getAdvanceDetailsBasedOnIdmap;

	}

	@GetMapping("/seriesmaster/getDebitCreditinfoById")
	public @ResponseBody Map<String, Object> getDebitCreditDetailBsdOnId(@RequestParam String series_id) {

		Map<String, Object> getDebitCreditInfoBasedOnIdmap = new HashMap<String, Object>();
		DebitCreditSeriesMasterInterfaces debitCreditInfoById = null;
		try {
			debitCreditInfoById = seriesMasterRepository.getDebitCreditBasedOnId(series_id);

			getDebitCreditInfoBasedOnIdmap.put("action", "DebitCreditInfo");
			getDebitCreditInfoBasedOnIdmap.put("message",
					(debitCreditInfoById != null) ? "Success" : "Debit/Credit Info not found!");
			getDebitCreditInfoBasedOnIdmap.put("status", (debitCreditInfoById != null) ? "yes" : "no");

			if (debitCreditInfoById != null) {
				ObjectMapper mapper = new ObjectMapper();
				getDebitCreditInfoBasedOnIdmap
						.putAll(mapper.convertValue(debitCreditInfoById, new TypeReference<Map<String, Object>>() {
						}));
				mapper.clearProblemHandlers();
				mapper = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getDebitCreditInfoBasedOnIdmap;

	}

	@GetMapping("/seriesmaster/getChallansinfoById")
	public @ResponseBody Map<String, Object> getChallansMasterBsdOnId(@RequestParam String series_id) {

		Map<String, Object> getChallanMasterBasedOnIdmap = new HashMap<String, Object>();
		ChallanSeriesMasterInterfaces challanInfoBsdOnId = null;
		try {
			challanInfoBsdOnId = seriesMasterRepository.getChallansMasterBasedOnId(series_id);

			getChallanMasterBasedOnIdmap.put("action", "ChallanInfoBsdOnid");
			getChallanMasterBasedOnIdmap.put("message",
					(challanInfoBsdOnId != null) ? "Success" : "Challan Info not available");
			getChallanMasterBasedOnIdmap.put("status", (challanInfoBsdOnId != null) ? "yes" : "no");

			if (challanInfoBsdOnId != null) {
				ObjectMapper mapper = new ObjectMapper();
				getChallanMasterBasedOnIdmap
						.putAll(mapper.convertValue(challanInfoBsdOnId, new TypeReference<Map<String, Object>>() {
						}));
				mapper.clearProblemHandlers();
				mapper = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getChallanMasterBasedOnIdmap;

	}

	@GetMapping("/seriesmaster/getPaymentVoucherinfoById")
	public @ResponseBody Map<String, Object> getPaymentVoucherBasedOnId(@RequestParam String series_id) {

		Map<String, Object> getPaymentMasterBsdOnIdmap = new HashMap<String, Object>();
		PaymentSeriesMasterInterfaces allPaymentVoucherBasedOnId = null;
		try {
			allPaymentVoucherBasedOnId = seriesMasterRepository.getPaymentVoucherMasterBasedOnId(series_id);

			getPaymentMasterBsdOnIdmap.put("action", "PaymentVoucherInfoBsdOnId");
			getPaymentMasterBsdOnIdmap.put("message",
					(allPaymentVoucherBasedOnId != null) ? "Success" : "PaymentVoucher details not found!");
			getPaymentMasterBsdOnIdmap.put("status", (allPaymentVoucherBasedOnId != null) ? "yes" : "no");

			if (allPaymentVoucherBasedOnId != null) {
				ObjectMapper mapper = new ObjectMapper();
				getPaymentMasterBsdOnIdmap.putAll(
						mapper.convertValue(allPaymentVoucherBasedOnId, new TypeReference<Map<String, Object>>() {
						}));
				mapper.clearProblemHandlers();
				mapper = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getPaymentMasterBsdOnIdmap;

	}

	@GetMapping("/seriesmaster/getSelfInvoiceinfoById")
	public @ResponseBody Map<String, Object> getSelfInvoiceById(@RequestParam String series_id) {

		Map<String, Object> getSelfInvoiceByIdmap = new HashMap<String, Object>();
		SelfInvoiceSeriesMasterInterfaces selfInvoiceByid = null;
		try {
			selfInvoiceByid = seriesMasterRepository.getSelfInvoiceBasedOnId(series_id);

			getSelfInvoiceByIdmap.put("action", "SelfInvoiceInfoBsdOnId");
			getSelfInvoiceByIdmap.put("message",
					(selfInvoiceByid != null) ? "Success" : "SelfInvoice details not found!");
			getSelfInvoiceByIdmap.put("status", (selfInvoiceByid != null) ? "yes" : "no");

			if (selfInvoiceByid != null) {
				ObjectMapper mapper = new ObjectMapper();
				getSelfInvoiceByIdmap
						.putAll(mapper.convertValue(selfInvoiceByid, new TypeReference<Map<String, Object>>() {
						}));
				mapper.clearProblemHandlers();
				mapper = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getSelfInvoiceByIdmap;

	}

	@GetMapping("/seriesmaster/getDeliveryChallaninfoById")
	public @ResponseBody Map<String, Object> getDeliveryChallanBasedOnId(@RequestParam String series_id) {

		Map<String, Object> getDeliveryChallanListmap = new HashMap<String, Object>();
		DeliveryChallanSeriesMasterInterfaces DeliveryChallanDetailBsdOnid = null;
		try {
			DeliveryChallanDetailBsdOnid = seriesMasterRepository.getDeliveryChallanBasedOnId(series_id);

			getDeliveryChallanListmap.put("action", "DeliveryChallanInfoBsdOnId");
			getDeliveryChallanListmap.put("message",
					(DeliveryChallanDetailBsdOnid != null) ? "Success" : "DelievryChallan details not found!");
			getDeliveryChallanListmap.put("status", (DeliveryChallanDetailBsdOnid != null) ? "yes" : "no");

			if (DeliveryChallanDetailBsdOnid != null) {
				ObjectMapper mapper = new ObjectMapper();
				getDeliveryChallanListmap.putAll(
						mapper.convertValue(DeliveryChallanDetailBsdOnid, new TypeReference<Map<String, Object>>() {
						}));
				mapper.clearProblemHandlers();
				mapper = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getDeliveryChallanListmap;

	}

	@GetMapping("/seriesmaster/getGstStates")
	public @ResponseBody Map<String, Object> getAllStatesForGst() {

		Map<String, Object> allGstStatesmap = new HashMap<String, Object>();
		List<GstStatesInterface> allGstStateslist = null;
		try {

			allGstStateslist = seriesMasterRepository.getGstStatesOnly();

			allGstStatesmap.put("action", "GSTStatesInfo");
			allGstStatesmap.put("message", (allGstStateslist.size() > 0) ? "Success" : "GST States not available");
			allGstStatesmap.put("status", (allGstStateslist.size() > 0) ? "yes" : "no");

			if ((allGstStateslist != null) && (!allGstStateslist.isEmpty())) {

				allGstStatesmap.put("GSTStatesList", allGstStateslist);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return allGstStatesmap;
	}

	@GetMapping("/seriesmaster/getClosedStatusGstStates")
	public @ResponseBody Map<String, Object> getOpenStatusStatesList() {

		Map<String, Object> openStatusStatesmap = new HashMap<String, Object>();
		List<GstStatesInterface> closedStatusStatesList = null;
		try {

			closedStatusStatesList = seriesMasterRepository.getClosedStatusStatesList();

			openStatusStatesmap.put("action", "GSTStatesClosedStatusInfo");
			openStatusStatesmap.put("message",
					(closedStatusStatesList.size() > 0) ? "Success" : "GST States List not available");
			openStatusStatesmap.put("status", (closedStatusStatesList.size() > 0) ? "yes" : "no");

			if ((closedStatusStatesList != null) && (!closedStatusStatesList.isEmpty())) {

				openStatusStatesmap.put("GSTStatesClosedStatusList", closedStatusStatesList);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return openStatusStatesmap;
	}

	@GetMapping("/seriesmaster/getGstStateInfo")
	public @ResponseBody Map<String, Object> getAllStatesForGst(@RequestParam String state_id) {

		Map<String, Object> gstStatesInfomap = new HashMap<String, Object>();
		List<GstStatesInterface> allGstStateslist = null;
		try {

			allGstStateslist = seriesMasterRepository.getGstStateInfoBasedOnId(state_id);

			gstStatesInfomap.put("action", "GSTStateInfoBasedOnState");
			gstStatesInfomap.put("message", (allGstStateslist != null) ? "Success" : "GST State Info not available");
			gstStatesInfomap.put("status", (allGstStateslist != null) ? "yes" : "no");

			if ((allGstStateslist != null) && (!allGstStateslist.isEmpty())) {

				gstStatesInfomap.put("GSTInfoBasedOnStateId", allGstStateslist);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return gstStatesInfomap;
	}
	
	
	
	
	@GetMapping("/seriesmaster/getAllGstSeries")
	public @ResponseBody Map<String, Object> getAllStatesForGstSeries() {

		Map<String, Object> gstStatesInfomap = new HashMap<String, Object>();
		List<GstStatesInterface> allGstStateslist = null;
		try {

			allGstStateslist = seriesMasterRepository.getGstStateInfoBasedOnIdS();

			gstStatesInfomap.put("action", "GSTStateInfoBasedOnState");
			gstStatesInfomap.put("message", (allGstStateslist != null) ? "Success" : "GST State Info not available");
			gstStatesInfomap.put("status", (allGstStateslist != null) ? "yes" : "no");

			if ((allGstStateslist != null) && (!allGstStateslist.isEmpty())) {

				gstStatesInfomap.put("GSTInfoBasedOnStateId", allGstStateslist);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return gstStatesInfomap;
	}
	
	
	@GetMapping("/seriesmaster/getAdvanceStates")
	public @ResponseBody Map<String, Object> getAllAdvanceStates() {

	    Map<String, Object> allAdvanceStatesMap = new HashMap<>();
	    List<AdvanceStatesInterface> allAdvanceStatesList = null;
	    try {

	        allAdvanceStatesList =  seriesMasterRepository.getAdvanceStates();

	        allAdvanceStatesMap.put("action", "AdvanceStatesInfo");
	        allAdvanceStatesMap.put("message", (allAdvanceStatesList.size() > 0) ? "Success" : "Advance States not available");
	        allAdvanceStatesMap.put("status", (allAdvanceStatesList.size() > 0) ? "yes" : "no");

	        if ((allAdvanceStatesList != null) && (!allAdvanceStatesList.isEmpty())) {
	            allAdvanceStatesMap.put("AdvanceStatesList", allAdvanceStatesList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return allAdvanceStatesMap;
	}
	@GetMapping("/seriesmaster/getClosedStatusAdvanceStates")
	public @ResponseBody Map<String, Object> getClosedStatusAdvanceStatesList() {

	    Map<String, Object> closedStatusAdvanceStatesMap = new HashMap<>();
	    List<AdvanceStatesInterface> closedStatusAdvanceStatesList = null;
	    try {

	        closedStatusAdvanceStatesList = seriesMasterRepository.getClosedStatusAdvanceStatesList();

	        closedStatusAdvanceStatesMap.put("action", "AdvanceStatesClosedStatusInfo");
	        closedStatusAdvanceStatesMap.put("message", 
	                (closedStatusAdvanceStatesList.size() > 0) ? "Success" : "Advance States List not available");
	        closedStatusAdvanceStatesMap.put("status", (closedStatusAdvanceStatesList.size() > 0) ? "yes" : "no");

	        if ((closedStatusAdvanceStatesList != null) && (!closedStatusAdvanceStatesList.isEmpty())) {
	            closedStatusAdvanceStatesMap.put("AdvanceStatesClosedStatusList", closedStatusAdvanceStatesList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return closedStatusAdvanceStatesMap;
	}
	@GetMapping("/seriesmaster/getAdvanceStateInfo")
	public @ResponseBody Map<String, Object> getAdvanceStateInfo(@RequestParam String state_id) {

	    Map<String, Object> advanceStateInfoMap = new HashMap<>();
	    List<AdvanceStatesInterface> advanceStateInfoList = null;
	    try {

	        advanceStateInfoList = seriesMasterRepository.getAdvanceStateInfoBasedOnId(state_id);

	        advanceStateInfoMap.put("action", "AdvanceStateInfoBasedOnState");
	        advanceStateInfoMap.put("message", (advanceStateInfoList != null) ? "Success" : "Advance State Info not available");
	        advanceStateInfoMap.put("status", (advanceStateInfoList != null) ? "yes" : "no");

	        if ((advanceStateInfoList != null) && (!advanceStateInfoList.isEmpty())) {
	            advanceStateInfoMap.put("AdvanceInfoBasedOnStateId", advanceStateInfoList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return advanceStateInfoMap;
	}
	
	
	
	@GetMapping("/seriesmaster/getAllAdvanceStateInfo")
	public @ResponseBody Map<String, Object> getAllAdvanceStateInfo() {

	    Map<String, Object> advanceStateInfoMap = new HashMap<>();
	    List<AdvanceStatesInterface> advanceStateInfoList = null;
	    try {

	        advanceStateInfoList = seriesMasterRepository.getAdvanceStateInfoBasedOnIds();

	        advanceStateInfoMap.put("action", "AdvanceStateInfoBasedOnState");
	        advanceStateInfoMap.put("message", (advanceStateInfoList != null) ? "Success" : "Advance State Info not available");
	        advanceStateInfoMap.put("status", (advanceStateInfoList != null) ? "yes" : "no");

	        if ((advanceStateInfoList != null) && (!advanceStateInfoList.isEmpty())) {
	            advanceStateInfoMap.put("AdvanceInfoBasedOnStateId", advanceStateInfoList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return advanceStateInfoMap;
	}
	
	
	
	@GetMapping("/seriesmaster/getAllDebitCredit")
	public @ResponseBody Map<String, Object> getAllDebitCredit() {

	    Map<String, Object> debitCreditMap = new HashMap<>();
	    List<DebitCreditInterface> debitCreditList = null;

	    try {
	        debitCreditList = seriesMasterRepository.getAllDebitCredit();

	        debitCreditMap.put("action", "DebitCreditInfo");
	        debitCreditMap.put("message", (debitCreditList.size() > 0) ? "Success" : "Debit/Credit records not available");
	        debitCreditMap.put("status", (debitCreditList.size() > 0) ? "yes" : "no");

	        if ((debitCreditList != null) && (!debitCreditList.isEmpty())) {
	            debitCreditMap.put("DebitCreditList", debitCreditList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return debitCreditMap;
	}

	@GetMapping("/seriesmaster/getClosedDebitCredit")
	public @ResponseBody Map<String, Object> getClosedDebitCreditList() {

	    Map<String, Object> closedDebitCreditMap = new HashMap<>();
	    List<DebitCreditInterface> closedDebitCreditList = null;

	    try {
	        closedDebitCreditList = seriesMasterRepository.getClosedDebitCreditStatusList();

	        closedDebitCreditMap.put("action", "ClosedDebitCreditInfo");
	        closedDebitCreditMap.put("message", (closedDebitCreditList.size() > 0) ? "Success" : "Closed Debit/Credit records not available");
	        closedDebitCreditMap.put("status", (closedDebitCreditList.size() > 0) ? "yes" : "no");

	        if ((closedDebitCreditList != null) && (!closedDebitCreditList.isEmpty())) {
	            closedDebitCreditMap.put("ClosedDebitCreditList", closedDebitCreditList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return closedDebitCreditMap;
	}

	@GetMapping("/seriesmaster/getDebitCreditById")
	public @ResponseBody Map<String, Object> getDebitCreditById(@RequestParam String id) {

	    Map<String, Object> debitCreditInfoMap = new HashMap<>();
	    List<DebitCreditInterface> debitCreditInfoList = null;

	    try {
	        debitCreditInfoList = seriesMasterRepository.getDebitCreditById(id);

	        debitCreditInfoMap.put("action", "DebitCreditByIdInfo");
	        debitCreditInfoMap.put("message", (debitCreditInfoList != null) ? "Success" : "Debit/Credit Info not available");
	        debitCreditInfoMap.put("status", (debitCreditInfoList != null) ? "yes" : "no");

	        if ((debitCreditInfoList != null) && (!debitCreditInfoList.isEmpty())) {
	            debitCreditInfoMap.put("DebitCreditInfo", debitCreditInfoList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return debitCreditInfoMap;
	}
	
	

	
	
	
	
	@GetMapping("/seriesmaster/getAllDebitCreaditseries")
	public @ResponseBody Map<String, Object> getDebitCreditAll() {

	    Map<String, Object> debitCreditInfoMap = new HashMap<>();
	    List<DebitCreditInterface> debitCreditInfoList = null;

	    try {
	        debitCreditInfoList = seriesMasterRepository.getDebitCreditByIdS();

	        debitCreditInfoMap.put("action", "DebitCreditByIdInfo");
	        debitCreditInfoMap.put("message", (debitCreditInfoList != null) ? "Success" : "Debit/Credit Info not available");
	        debitCreditInfoMap.put("status", (debitCreditInfoList != null) ? "yes" : "no");

	        if ((debitCreditInfoList != null) && (!debitCreditInfoList.isEmpty())) {
	            debitCreditInfoMap.put("DebitCreditInfo", debitCreditInfoList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return debitCreditInfoMap;
	}
	
	
	
	
	
	@GetMapping("/seriesmaster/getAllChallanSeries")
	public @ResponseBody Map<String, Object> getAllChallanSeries() {

	    Map<String, Object> challanSeriesMap = new HashMap<>();
	    List<ChallanSeriesInterface> challanSeriesList = null;

	    try {
	        challanSeriesList = seriesMasterRepository.getAllChallanSeries();

	        challanSeriesMap.put("action", "ChallanSeriesInfo");
	        challanSeriesMap.put("message", (challanSeriesList.size() > 0) ? "Success" : "Challan Series not available");
	        challanSeriesMap.put("status", (challanSeriesList.size() > 0) ? "yes" : "no");

	        if ((challanSeriesList != null) && (!challanSeriesList.isEmpty())) {
	            challanSeriesMap.put("ChallanSeriesList", challanSeriesList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return challanSeriesMap;
	}

	@GetMapping("/seriesmaster/getClosedChallanSeries")
	public @ResponseBody Map<String, Object> getClosedChallanSeries() {

	    Map<String, Object> closedChallanMap = new HashMap<>();
	    List<ChallanSeriesInterface> closedChallanList = null;

	    try {
	        closedChallanList = seriesMasterRepository.getClosedChallanSeries();

	        closedChallanMap.put("action", "ClosedChallanSeriesInfo");
	        closedChallanMap.put("message", (closedChallanList.size() > 0) ? "Success" : "Closed Challan Series not available");
	        closedChallanMap.put("status", (closedChallanList.size() > 0) ? "yes" : "no");

	        if ((closedChallanList != null) && (!closedChallanList.isEmpty())) {
	            closedChallanMap.put("ClosedChallanSeriesList", closedChallanList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return closedChallanMap;
	}

	@GetMapping("/seriesmaster/getChallanSeriesById")
	public @ResponseBody Map<String, Object> getChallanSeriesById(@RequestParam String challan_id) {

	    Map<String, Object> challanSeriesInfoMap = new HashMap<>();
	    List<ChallanSeriesInterface> challanSeriesInfoList = null;

	    try {
	        challanSeriesInfoList = seriesMasterRepository.getChallanSeriesById(challan_id);

	        challanSeriesInfoMap.put("action", "ChallanSeriesByIdInfo");
	        challanSeriesInfoMap.put("message", (challanSeriesInfoList != null) ? "Success" : "Challan Series not found");
	        challanSeriesInfoMap.put("status", (challanSeriesInfoList != null) ? "yes" : "no");

	        if ((challanSeriesInfoList != null) && (!challanSeriesInfoList.isEmpty())) {
	            challanSeriesInfoMap.put("ChallanSeriesInfo", challanSeriesInfoList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return challanSeriesInfoMap;
	}
	
	
	
	@GetMapping("/seriesmaster/getAllChallanSeriesByAll")
	public @ResponseBody Map<String, Object> getChallanSeriesByIds() {

	    Map<String, Object> challanSeriesInfoMap = new HashMap<>();
	    List<ChallanSeriesInterface> challanSeriesInfoList = null;

	    try {
	        challanSeriesInfoList = seriesMasterRepository.getChallanSeriesByIdS();

	        challanSeriesInfoMap.put("action", "ChallanSeriesByIdInfo");
	        challanSeriesInfoMap.put("message", (challanSeriesInfoList != null) ? "Success" : "Challan Series not found");
	        challanSeriesInfoMap.put("status", (challanSeriesInfoList != null) ? "yes" : "no");

	        if ((challanSeriesInfoList != null) && (!challanSeriesInfoList.isEmpty())) {
	            challanSeriesInfoMap.put("ChallanSeriesInfo", challanSeriesInfoList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return challanSeriesInfoMap;
	}
	
	
	
	
	
	@GetMapping("/seriesmaster/getAllPaymentSeries")
	public @ResponseBody Map<String, Object> getAllPaymentSeries() {

	    Map<String, Object> paymentSeriesMap = new HashMap<>();
	    List<PaymentSeriesInterface> paymentSeriesList = null;

	    try {
	        paymentSeriesList = seriesMasterRepository.getAllPaymentSeries();

	        paymentSeriesMap.put("action", "PaymentSeriesInfo");
	        paymentSeriesMap.put("message", (paymentSeriesList.size() > 0) ? "Success" : "Payment Series not available");
	        paymentSeriesMap.put("status", (paymentSeriesList.size() > 0) ? "yes" : "no");

	        if (paymentSeriesList != null && !paymentSeriesList.isEmpty()) {
	            paymentSeriesMap.put("PaymentSeriesList", paymentSeriesList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return paymentSeriesMap;
	}
	@GetMapping("/seriesmaster/getClosedPaymentSeries")
	public @ResponseBody Map<String, Object> getClosedPaymentSeries() {

	    Map<String, Object> closedPaymentSeriesMap = new HashMap<>();
	    List<PaymentSeriesInterface> closedPaymentSeriesList = null;

	    try {
	        closedPaymentSeriesList = seriesMasterRepository.getClosedPaymentSeries();

	        closedPaymentSeriesMap.put("action", "ClosedPaymentSeriesInfo");
	        closedPaymentSeriesMap.put("message", (closedPaymentSeriesList.size() > 0) ? "Success" : "Closed Payment Series not available");
	        closedPaymentSeriesMap.put("status", (closedPaymentSeriesList.size() > 0) ? "yes" : "no");

	        if (closedPaymentSeriesList != null && !closedPaymentSeriesList.isEmpty()) {
	            closedPaymentSeriesMap.put("ClosedPaymentSeriesList", closedPaymentSeriesList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return closedPaymentSeriesMap;
	}
	@GetMapping("/seriesmaster/getPaymentSeriesById")
	public @ResponseBody Map<String, Object> getPaymentSeriesById(@RequestParam String payment_id) {

	    Map<String, Object> paymentSeriesInfoMap = new HashMap<>();
	    List<PaymentSeriesInterface> paymentSeriesInfoList = null;

	    try {
	        paymentSeriesInfoList = seriesMasterRepository.getPaymentSeriesById(payment_id);

	        paymentSeriesInfoMap.put("action", "PaymentSeriesByIdInfo");
	        paymentSeriesInfoMap.put("message", (paymentSeriesInfoList != null) ? "Success" : "Payment Series not found");
	        paymentSeriesInfoMap.put("status", (paymentSeriesInfoList != null) ? "yes" : "no");

	        if (paymentSeriesInfoList != null && !paymentSeriesInfoList.isEmpty()) {
	            paymentSeriesInfoMap.put("PaymentSeriesInfo", paymentSeriesInfoList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return paymentSeriesInfoMap;
	}
	
	
	
	@GetMapping("/seriesmaster/getAllPaymentSeriesAll")
	public @ResponseBody Map<String, Object> getPaymentSeriesByIds() {

	    Map<String, Object> paymentSeriesInfoMap = new HashMap<>();
	    List<PaymentSeriesInterface> paymentSeriesInfoList = null;

	    try {
	        paymentSeriesInfoList = seriesMasterRepository.getPaymentSeriesByIdS();

	        paymentSeriesInfoMap.put("action", "PaymentSeriesByIdInfo");
	        paymentSeriesInfoMap.put("message", (paymentSeriesInfoList != null) ? "Success" : "Payment Series not found");
	        paymentSeriesInfoMap.put("status", (paymentSeriesInfoList != null) ? "yes" : "no");

	        if (paymentSeriesInfoList != null && !paymentSeriesInfoList.isEmpty()) {
	            paymentSeriesInfoMap.put("PaymentSeriesInfo", paymentSeriesInfoList);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return paymentSeriesInfoMap;
	}
	

	@GetMapping("/seriesmaster/getAllSelfInvoiceSeries")
	public @ResponseBody Map<String, Object> getAllSelfInvoiceSeries() {

	    Map<String, Object> selfInvMap = new HashMap<>();
	    List<SelfInvoiceSeriesInterface> selfInvList = null;

	    try {
	        selfInvList = seriesMasterRepository.getAllSelfInvoiceSeries();

	        selfInvMap.put("action", "SelfInvoiceSeriesInfo");
	        selfInvMap.put("message", (selfInvList.size() > 0) ? "Success"
	                                                            : "Self-Invoice Series not available");
	        selfInvMap.put("status", (selfInvList.size() > 0) ? "yes" : "no");

	        if (selfInvList != null && !selfInvList.isEmpty()) {
	            selfInvMap.put("SelfInvoiceSeriesList", selfInvList);
	        }
	    } catch (Exception e) { e.printStackTrace(); }

	    return selfInvMap;
	}

	@GetMapping("/seriesmaster/getClosedSelfInvoiceSeries")
	public @ResponseBody Map<String, Object> getClosedSelfInvoiceSeries() {

	    Map<String, Object> closedSelfInvMap = new HashMap<>();
	    List<SelfInvoiceSeriesInterface> closedSelfInvList = null;

	    try {
	        closedSelfInvList = seriesMasterRepository.getClosedSelfInvoiceSeries();

	        closedSelfInvMap.put("action", "ClosedSelfInvoiceSeriesInfo");
	        closedSelfInvMap.put("message", (closedSelfInvList.size() > 0) ? "Success"
	                                                                      : "Closed Self-Invoice Series not available");
	        closedSelfInvMap.put("status", (closedSelfInvList.size() > 0) ? "yes" : "no");

	        if (closedSelfInvList != null && !closedSelfInvList.isEmpty()) {
	            closedSelfInvMap.put("ClosedSelfInvoiceSeriesList", closedSelfInvList);
	        }
	    } catch (Exception e) { e.printStackTrace(); }

	    return closedSelfInvMap;
	}

	@GetMapping("/seriesmaster/getSelfInvoiceSeriesById")
	public @ResponseBody Map<String, Object> getSelfInvoiceSeriesById(@RequestParam String self_invoice_id) {

	    Map<String, Object> selfInvInfoMap = new HashMap<>();
	    List<SelfInvoiceSeriesInterface> selfInvInfoList = null;

	    try {
	        selfInvInfoList = seriesMasterRepository.getSelfInvoiceSeriesById(self_invoice_id);

	        selfInvInfoMap.put("action", "SelfInvoiceSeriesByIdInfo");
	        selfInvInfoMap.put("message", (selfInvInfoList != null) ? "Success"
	                                                                : "Self-Invoice Series not found");
	        selfInvInfoMap.put("status", (selfInvInfoList != null) ? "yes" : "no");

	        if (selfInvInfoList != null && !selfInvInfoList.isEmpty()) {
	            selfInvInfoMap.put("SelfInvoiceSeriesInfo", selfInvInfoList);
	        }
	    } catch (Exception e) { e.printStackTrace(); }

	    return selfInvInfoMap;
	}
	
	
	@GetMapping("/seriesmaster/getSelfAllInvoiceSeries")
	public @ResponseBody Map<String, Object> getSelfInvoiceSeriesByIdS() {

	    Map<String, Object> selfInvInfoMap = new HashMap<>();
	    List<SelfInvoiceSeriesInterface> selfInvInfoList = null;

	    try {
	        selfInvInfoList = seriesMasterRepository.getSelfInvoiceSeriesByIdS();

	        selfInvInfoMap.put("action", "SelfInvoiceSeriesByIdInfo");
	        selfInvInfoMap.put("message", (selfInvInfoList != null) ? "Success"
	                                                                : "Self-Invoice Series not found");
	        selfInvInfoMap.put("status", (selfInvInfoList != null) ? "yes" : "no");

	        if (selfInvInfoList != null && !selfInvInfoList.isEmpty()) {
	            selfInvInfoMap.put("SelfInvoiceSeriesInfo", selfInvInfoList);
	        }
	    } catch (Exception e) { e.printStackTrace(); }

	    return selfInvInfoMap;
	}

	
	
	

	@GetMapping("/seriesmaster/getAllDeliverySeries")
	public @ResponseBody Map<String, Object> getAllDeliverySeries() {

	    Map<String, Object> deliveryMap = new HashMap<>();
	    List<DeliverySeriesInterface> deliveryList = null;

	    try {
	        deliveryList = seriesMasterRepository.getAllDeliverySeries();

	        deliveryMap.put("action", "DeliverySeriesInfo");
	        deliveryMap.put("message", (deliveryList.size() > 0) ? "Success"
	                                                             : "Delivery Series not available");
	        deliveryMap.put("status", (deliveryList.size() > 0) ? "yes" : "no");

	        if (deliveryList != null && !deliveryList.isEmpty()) {
	            deliveryMap.put("DeliverySeriesList", deliveryList);
	        }
	    } catch (Exception e) { e.printStackTrace(); }

	    return deliveryMap;
	}

	@GetMapping("/seriesmaster/getClosedDeliverySeries")
	public @ResponseBody Map<String, Object> getClosedDeliverySeries() {

	    Map<String, Object> closedDeliveryMap = new HashMap<>();
	    List<DeliverySeriesInterface> closedDeliveryList = null;

	    try {
	        closedDeliveryList = seriesMasterRepository.getClosedDeliverySeries();

	        closedDeliveryMap.put("action", "ClosedDeliverySeriesInfo");
	        closedDeliveryMap.put("message", (closedDeliveryList.size() > 0) ? "Success"
	                                                                        : "Closed Delivery Series not available");
	        closedDeliveryMap.put("status", (closedDeliveryList.size() > 0) ? "yes" : "no");

	        if (closedDeliveryList != null && !closedDeliveryList.isEmpty()) {
	            closedDeliveryMap.put("ClosedDeliverySeriesList", closedDeliveryList);
	        }
	    } catch (Exception e) { e.printStackTrace(); }

	    return closedDeliveryMap;
	}

	@GetMapping("/seriesmaster/getDeliverySeriesById")
	public @ResponseBody Map<String, Object> getDeliverySeriesById(@RequestParam String delivery_id) {

	    Map<String, Object> deliveryInfoMap = new HashMap<>();
	    List<DeliverySeriesInterface> deliveryInfoList = null;

	    try {
	        deliveryInfoList = seriesMasterRepository.getDeliverySeriesById(delivery_id);

	        deliveryInfoMap.put("action", "DeliverySeriesByIdInfo");
	        deliveryInfoMap.put("message", (deliveryInfoList != null) ? "Success"
	                                                                 : "Delivery Series not found");
	        deliveryInfoMap.put("status", (deliveryInfoList != null) ? "yes" : "no");

	        if (deliveryInfoList != null && !deliveryInfoList.isEmpty()) {
	            deliveryInfoMap.put("DeliverySeriesInfo", deliveryInfoList);
	        }
	    } catch (Exception e) { e.printStackTrace(); }

	    return deliveryInfoMap;
	}
	
	@GetMapping("/seriesmaster/getAllDeliverySeriesBy")
	public @ResponseBody Map<String, Object> getDeliverySeriesByIdS() {

	    Map<String, Object> deliveryInfoMap = new HashMap<>();
	    List<DeliverySeriesInterface> deliveryInfoList = null;

	    try {
	        deliveryInfoList = seriesMasterRepository.getDeliverySeriesByIdS();

	        deliveryInfoMap.put("action", "DeliverySeriesByIdInfo");
	        deliveryInfoMap.put("message", (deliveryInfoList != null) ? "Success"
	                                                                 : "Delivery Series not found");
	        deliveryInfoMap.put("status", (deliveryInfoList != null) ? "yes" : "no");

	        if (deliveryInfoList != null && !deliveryInfoList.isEmpty()) {
	            deliveryInfoMap.put("DeliverySeriesInfo", deliveryInfoList);
	        }
	    } catch (Exception e) { e.printStackTrace(); }

	    return deliveryInfoMap;
	}
	
	

}