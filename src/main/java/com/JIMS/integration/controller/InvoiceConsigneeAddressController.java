package com.JIMS.integration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.interfaces.InvoiceConsigneeAddressInterface;
import com.JIMS.integration.repository.InvoiceConsigneeAdressRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class InvoiceConsigneeAddressController {

	@Autowired
	private InvoiceConsigneeAdressRepository invoiceconsigneerepo;

	Logger logger = LogManager.getLogger(InvoiceConsigneeAddressController.class);

	@PostMapping("/invoiceConsigneeAddress/addInvoiceConsigneeAddress")
	public @ResponseBody Map<String, Object> addInvoiceConsigneeAddressNew(
	        @RequestParam String name_of_add,
	        @RequestParam String add1,
	        @RequestParam(required = false, defaultValue = "") String add2,
	        @RequestParam String city,
	        @RequestParam String district,
	        @RequestParam String state_id,
	        @RequestParam String country_id,
	        @RequestParam String created_by,
	        @RequestParam String is_invoice,
	        @RequestParam String is_consignee,
	        @RequestParam String gst_no,
	        @RequestParam String pan_no,
	        @RequestParam String pin_no,
	        @RequestParam(required = false) String factory_id) {

	    name_of_add = name_of_add.toUpperCase();
	    add1        = add1.toUpperCase();
	    add2        = add2.toUpperCase();
	    city        = city.toUpperCase();
	    district    = district.toUpperCase();
	    gst_no      = gst_no.toUpperCase();
	    pan_no      = pan_no.toUpperCase();
	    created_by  = created_by.toUpperCase();

	    Map<String, Object> map = new HashMap<>();
	    try {
	        int count = invoiceconsigneerepo.countNumberOfRows();
	        count = count + 1;

	        // Duplicate name check removed — address is created regardless of whether the name already exists
	        int result = invoiceconsigneerepo.addInvoiceAddress(
	            name_of_add, add1, add2, city, district,
	            state_id, country_id, pin_no, created_by,
	            is_invoice, is_consignee, count,
	            gst_no, pan_no, factory_id
	        );
	        map.put("message", result > 0 ? "Success" : "Address Not Created");
	        map.put("status", result > 0 ? "yes" : "no");
	        map.put("action", "AddInvoice/ConsigneeAddress");

	    } catch (Exception e) {
	        logger.error("ERROR IN addInvoiceConsigneeAddress -> " + e.getMessage());
	        map.put("message", "Server error");
	        map.put("status", "no");
	    }

	    return map;
	}

	/*
	 * @PostMapping("/invoiceConsigneeAddress/updateInvoiceConsigneeAddress")
	 * public @ResponseBody Map<String, Object> updateInvoiceConsigneeAddress(
	 * 
	 * @RequestBody Map<String, String> updateInvoConsigneeAddressMap) {
	 * 
	 * logger.info("EXECUTING METHOD :: updateInvoiceConsigneeAddress");
	 * 
	 * int count = 0; int check = 0; String address =
	 * updateInvoConsigneeAddressMap.get("address"); String city =
	 * updateInvoConsigneeAddressMap.get("city"); String district =
	 * updateInvoConsigneeAddressMap.get("district"); String state_id =
	 * updateInvoConsigneeAddressMap.get("state_id"); String country_id =
	 * updateInvoConsigneeAddressMap.get("country_id"); String modified_by =
	 * updateInvoConsigneeAddressMap.get("modified_by"); String is_invoice =
	 * updateInvoConsigneeAddressMap.get("is_invoice"); String is_consignee =
	 * updateInvoConsigneeAddressMap.get("is_consignee"); String address_id =
	 * updateInvoConsigneeAddressMap.get("address_id"); String gst_no =
	 * updateInvoConsigneeAddressMap.get("gst_no"); String pan_no =
	 * updateInvoConsigneeAddressMap.get("pan_no"); String pin_no =
	 * updateInvoConsigneeAddressMap.get("pin_no"); Map<String, Object>
	 * updateInvoConAddressmap = new HashMap<String, Object>();
	 * 
	 * try { if (is_consignee.equals("1")) { check =
	 * invoiceconsigneerepo.checkConsigneeAddressIdPresentInContractMaster(
	 * address_id); if (check > 0) { updateInvoConAddressmap.put("message",
	 * "Invoice Already Generated Not Able to Update"); return
	 * updateInvoConAddressmap; } } if (is_invoice.equals("1")) { check =
	 * invoiceconsigneerepo.checkInvoiceAddressIdPresentInContractMaster(address_id)
	 * ; if (check > 0) { updateInvoConAddressmap.put("message",
	 * "Invoice Already Generated Not Able to Update"); return
	 * updateInvoConAddressmap; } }
	 * 
	 * int moveExistingtoHistoryTable =
	 * invoiceconsigneerepo.moveToHistoryTable(modified_by, address_id); count =
	 * invoiceconsigneerepo.updateAddressRecord(address, city, district, state_id,
	 * country_id, modified_by, is_invoice, is_consignee, address_id, gst_no,
	 * pan_no, pin_no);
	 * 
	 * updateInvoConAddressmap.put("message", (count > 0) ? "Success" :
	 * "Invoice/Consignee Address Not updated");
	 * updateInvoConAddressmap.put("status", (count > 0) ? "yes" : "no");
	 * updateInvoConAddressmap.put("action", "UpdateInvoice/ConsigneeAddress");
	 * 
	 * } catch (Exception e) {
	 * logger.error("ERROR IN THE METHOD :: updateInvoiceConsigneeAddress  -> " +
	 * e.getMessage());
	 * 
	 * } return updateInvoConAddressmap; }
	 */

	@SuppressWarnings("unused")
	@PostMapping("/invoiceConsigneeAddress/updateInvoiceConsigneeAddressNew")
	public @ResponseBody Map<String, Object> updateInvoiceConsigneeAddressNew(
	        @RequestParam String name_of_add,
	        @RequestParam String add1,
	        @RequestParam(required = false, defaultValue = "") String add2,
	        @RequestParam String city,
	        @RequestParam String district,
	        @RequestParam String state_id,
	        @RequestParam String country_id,
	        @RequestParam String modified_by,
	        @RequestParam(required = false) String is_invoice,
	        @RequestParam(required = false) String is_consignee,
	        @RequestParam String gst_no,
	        @RequestParam String pan_no,
	        @RequestParam String pin_no,
	        @RequestParam String address_id) {

	    logger.info("EXECUTING METHOD :: updateInvoiceConsigneeAddress");

	    // ✅ Uppercase all text fields
	    name_of_add = name_of_add.toUpperCase();
	    add1        = add1.toUpperCase();
	    add2        = add2.toUpperCase();
	    city        = city.toUpperCase();
	    district    = district.toUpperCase();
	    gst_no      = gst_no.toUpperCase();
	    pan_no      = pan_no.toUpperCase();
	    modified_by = modified_by.toUpperCase();

	    int count = 0;
	    Map<String, Object> updateInvoConAddressmap = new HashMap<String, Object>();

	    try {
	        String message = invoiceconsigneerepo
	            .checkWhetherAddressInvolvedInTransactionsOrNot(address_id);

	        if (message != null) {
	            updateInvoConAddressmap.put("message", message);
	            updateInvoConAddressmap.put("status", "no");
	            updateInvoConAddressmap.put("action", "UpdateInvoice/ConsigneeAddress");
	        } else {
	            // ✅ Pass name_of_add, add1, add2 separately
	            count = invoiceconsigneerepo.updateAddressRecord(
	                name_of_add, add1, add2,
	                city, district, state_id, country_id,
	                modified_by, gst_no, pan_no, pin_no, address_id
	            );

	            updateInvoConAddressmap.put("message",
	                (count > 0) ? "Success" : "Invoice/Consignee Address Not updated");
	            updateInvoConAddressmap.put("status", (count > 0) ? "yes" : "no");
	            updateInvoConAddressmap.put("action", "UpdateInvoice/ConsigneeAddress");
	        }

	    } catch (Exception e) {
	        logger.error("ERROR IN THE METHOD :: updateInvoiceConsigneeAddress -> "
	            + e.getMessage());
	    }

	    return updateInvoConAddressmap;
	}

	@PostMapping("/invoiceConsigneeAddress/deleteInvoiceConsigneeAddress")
	public @ResponseBody Map<String, Object> deleteInvoiceOrConsigneeAddress(@RequestParam String address_id,
			@RequestParam String user_id) {

		logger.info("EXECUTING METHOD :: deleteInvoiceOrConsigneeAddress");

		Map<String, Object> deleteInvoiceConsigneeAddressmap = new HashMap<String, Object>();

		try {
			String message = invoiceconsigneerepo.checkWhetherAddressInvolvedInTransactionsOrNot(address_id);
			if (message != null) {
				deleteInvoiceConsigneeAddressmap.put("message", message);
				deleteInvoiceConsigneeAddressmap.put("status", "no");
				deleteInvoiceConsigneeAddressmap.put("action", "DeleteInvoice/ConsigneeAddress");

				return deleteInvoiceConsigneeAddressmap;
			} else {
				int deleteInvoiceConsigneeAddrecord = invoiceconsigneerepo.deleteInvConsigneeAddress(address_id,
						user_id);

				deleteInvoiceConsigneeAddressmap.put("message",
						(deleteInvoiceConsigneeAddrecord > 0) ? "Success" : "Invoice/Consignee Address Not deleted");
				deleteInvoiceConsigneeAddressmap.put("status", (deleteInvoiceConsigneeAddrecord > 0) ? "yes" : "no");
				deleteInvoiceConsigneeAddressmap.put("action", "DeleteInvoice/ConsigneeAddress");
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: deleteInvoiceOrConsigneeAddress  -> " + e.getMessage());

		}
		return deleteInvoiceConsigneeAddressmap;
	}

	@GetMapping("invoiceConsigneeAddress/getInvoiceAddress")
	public @ResponseBody Map<String, Object> getInvoiceConsigneeAddress(@RequestParam(required=false) String factory_id) {

		logger.info("EXECUTING METHOD :: getInvoiceConsigneeAddress");

		Map<String, Object> allAddressmap = new HashMap<String, Object>();
		List<InvoiceConsigneeAddressInterface> invoiceAddlist = null;
		try {
			invoiceAddlist = invoiceconsigneerepo.getInvoiceAddressDetails(factory_id);

			allAddressmap.put("message", ((invoiceAddlist != null) && (!invoiceAddlist.isEmpty())) ? "Success"
					: "Invoice Address not available");
			allAddressmap.put("status", ((invoiceAddlist != null) && (!invoiceAddlist.isEmpty())) ? "yes" : "no");
			allAddressmap.put("action", "InvoiceAddress");

			if ((invoiceAddlist != null) && (!invoiceAddlist.isEmpty())) {
				allAddressmap.put("InvoiceAddress", invoiceAddlist);
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getInvoiceConsigneeAddress  -> " + e.getMessage());

		}
		return allAddressmap;
	}

	@GetMapping("invoiceConsigneeAddress/getConsigneeAddress")
	public @ResponseBody Map<String, Object> getConsigneeAddress(@RequestParam(required=false) String factory_id) {

		logger.info("EXECUTING METHOD :: getConsigneeAddress");

		Map<String, Object> allAddressmap = new HashMap<String, Object>();
		List<InvoiceConsigneeAddressInterface> consigneeAddlist = null;
		try {
			consigneeAddlist = invoiceconsigneerepo.getConsigneeAddressDetails(factory_id);

			allAddressmap.put("message", ((consigneeAddlist != null) && (!consigneeAddlist.isEmpty())) ? "Success"
					: "Consignee Address Not Available");
			allAddressmap.put("status", ((consigneeAddlist != null) && (!consigneeAddlist.isEmpty())) ? "yes" : "no");
			allAddressmap.put("action", "ConsigneeAddress");

			if ((consigneeAddlist != null) && (!consigneeAddlist.isEmpty())) {
				allAddressmap.put("ConsigneeAddress", consigneeAddlist);
			}
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getConsigneeAddress  -> " + e.getMessage());
		}
		return allAddressmap;
	}

	@GetMapping("invoiceConsigneeAddress/getAddressById/{address_id}")
	public @ResponseBody Map<String, Object> getConsigneeAddressById(@PathVariable String address_id) {

		logger.info("EXECUTING METHOD :: getConsigneeAddressById");

		Map<String, Object> consigneeAddressByIdmap = new HashMap<String, Object>();
		InvoiceConsigneeAddressInterface consigneeAddressById = null;

		try {

			logger.info(
					"EXECUTING METHOD :: getConsigneeAddressById :: BEFORE executing getConsigneeAddressDetailsById");

			consigneeAddressById = invoiceconsigneerepo.getConsigneeAddressDetailsById(address_id);

			logger.info(
					"EXECUTING METHOD :: getConsigneeAddressById :: AFTER executing getConsigneeAddressDetailsById");

			consigneeAddressByIdmap.put("message",
					(consigneeAddressById != null) ? "Success" : "Invoice/Consignee Address By Id not available");
			consigneeAddressByIdmap.put("status", (consigneeAddressById != null) ? "yes" : "no");
			consigneeAddressByIdmap.put("action", "Invoice/ConsigneeAddressNotAvailable");

			if (consigneeAddressById != null) {
				ObjectMapper mapper = new ObjectMapper();
				consigneeAddressByIdmap
						.putAll(mapper.convertValue(consigneeAddressById, new TypeReference<Map<String, Object>>() {
						}));
				mapper.clearProblemHandlers();
				mapper = null;
			}
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getConsigneeAddressById  -> " + e.getMessage());
		}
		return consigneeAddressByIdmap;

	}

	@GetMapping("invoiceConsigneeAddress/getAddressById")
	public @ResponseBody Map<String, Object> getConsigneeAddressByIdNew(@RequestParam String address_id) {

		logger.info("EXECUTING METHOD :: getConsigneeAddressById");

		Map<String, Object> consigneeAddressByIdmap = new HashMap<String, Object>();
		InvoiceConsigneeAddressInterface consigneeAddressById = null;

		try {

			logger.info(
					"EXECUTING METHOD :: getConsigneeAddressById :: BEFORE executing getConsigneeAddressDetailsById");

			consigneeAddressById = invoiceconsigneerepo.getConsigneeAddressDetailsById(address_id);

			logger.info(
					"EXECUTING METHOD :: getConsigneeAddressById :: AFTER executing getConsigneeAddressDetailsById");

			consigneeAddressByIdmap.put("message",
					(consigneeAddressById != null) ? "Success" : "Invoice/Consignee Address By Id not available");
			consigneeAddressByIdmap.put("status", (consigneeAddressById != null) ? "yes" : "no");
			consigneeAddressByIdmap.put("action", "Invoice/ConsigneeAddressNotAvailable");

			if (consigneeAddressById != null) {
				ObjectMapper mapper = new ObjectMapper();
				consigneeAddressByIdmap
						.putAll(mapper.convertValue(consigneeAddressById, new TypeReference<Map<String, Object>>() {
						}));
				mapper.clearProblemHandlers();
				mapper = null;
			}
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getConsigneeAddressById  -> " + e.getMessage());
		}
		return consigneeAddressByIdmap;

	}

	@GetMapping("invoiceConsigneeAddress/getInvoiceAddressNew")
	public @ResponseBody Map<String, Object> getInvoiceConsigneeAddressNew(@RequestParam(required=false) String factory_id) {

		logger.info("EXECUTING METHOD :: getInvoiceConsigneeAddress");

		Map<String, Object> allAddressmap = new HashMap<String, Object>();
		List<InvoiceConsigneeAddressInterface> invoiceAddlist = null;
		try {
			invoiceAddlist = invoiceconsigneerepo.getInvoiceAddressDetails(factory_id);

			allAddressmap.put("message", ((invoiceAddlist != null) && (!invoiceAddlist.isEmpty())) ? "Success"
					: "Invoice Address not available");
			allAddressmap.put("status", ((invoiceAddlist != null) && (!invoiceAddlist.isEmpty())) ? "yes" : "no");
			allAddressmap.put("action", "InvoiceAddress");

			if ((invoiceAddlist != null) && (!invoiceAddlist.isEmpty())) {
				allAddressmap.put("InvoiceAddress", invoiceAddlist);
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getInvoiceConsigneeAddress  -> " + e.getMessage());

		}
		return allAddressmap;
	}

	@GetMapping("invoiceConsigneeAddress/getConsigneeAddressNew")
	public @ResponseBody Map<String, Object> getConsigneeAddressNew(@RequestParam(required=false) String factory_id) {

		logger.info("EXECUTING METHOD :: getConsigneeAddress");

		Map<String, Object> allAddressmap = new HashMap<String, Object>();
		List<InvoiceConsigneeAddressInterface> consigneeAddlist = null;
		try {
			consigneeAddlist = invoiceconsigneerepo.getConsigneeAddressDetails(factory_id);

			allAddressmap.put("message", ((consigneeAddlist != null) && (!consigneeAddlist.isEmpty())) ? "Success"
					: "Consignee Address Not Available");
			allAddressmap.put("status", ((consigneeAddlist != null) && (!consigneeAddlist.isEmpty())) ? "yes" : "no");
			allAddressmap.put("action", "ConsigneeAddress");

			if ((consigneeAddlist != null) && (!consigneeAddlist.isEmpty())) {
				allAddressmap.put("ConsigneeAddress", consigneeAddlist);
			}
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getConsigneeAddress  -> " + e.getMessage());
		}
		return allAddressmap;
	}
	
	@PostMapping("/invoiceConsigneeAddress/verifyInvoiceConsigneeAddress")
	public @ResponseBody Map<String, Object> verifyInvoiceConsigneeAddress(
	        @RequestParam String id,
	        @RequestParam String verified_by) {

	    logger.info("EXECUTING METHOD :: verifyInvoiceConsigneeAddress");

	    Map<String, Object> responseMap = new HashMap<>();

	    try {
	        // Check if already verified
	        String alreadyVerified = invoiceconsigneerepo.checkIfAlreadyVerified(id);
	        if (alreadyVerified != null) {
	            responseMap.put("message", "This address is already verified.");
	            responseMap.put("status", "no");
	            responseMap.put("action", "VerifyInvoice/ConsigneeAddress");
	            return responseMap;
	        }

	        int count = invoiceconsigneerepo.verifyAddressRecord(id, verified_by);

	        responseMap.put("message", (count > 0) ? "Success" : "Failed to verify address");
	        responseMap.put("status", (count > 0) ? "yes" : "no");
	        responseMap.put("action", "VerifyInvoice/ConsigneeAddress");

	    } catch (Exception e) {
	        logger.error("ERROR IN THE METHOD :: verifyInvoiceConsigneeAddress -> " + e.getMessage());
	        responseMap.put("message", "An error occurred during verification.");
	        responseMap.put("status", "no");
	    }

	    return responseMap;
	}
	
	@PostMapping("/invoiceConsigneeAddress/releaseInvoiceConsigneeAddress")
	public Map<String, Object> releaseInvoiceConsigneeAddress(
	        @RequestParam Integer id,
	        @RequestParam Integer released_by) {

	    Map<String, Object> res = new HashMap<>();

	    try {
	        // ✅ No "already released" check — it's a toggle
	        int count = invoiceconsigneerepo.releaseAddressRecord(id, released_by);

	        res.put("status", count > 0 ? "yes" : "no");
	        res.put("message", count > 0 ? "Release toggled successfully" : "Failed to toggle release");

	    } catch (Exception e) {
	        res.put("status", "no");
	        res.put("message", "Error occurred");
	    }

	    return res;
	}

	@PostMapping("/consigneeAddress/verifyConsigneeAddress")
	public @ResponseBody Map<String, Object> verifyConsigneeAddress(
	        @RequestParam String id,
	        @RequestParam String verified_by) {

	    logger.info("EXECUTING METHOD :: verifyConsigneeAddress");

	    Map<String, Object> responseMap = new HashMap<>();

	    try {

	        // CHECK ALREADY VERIFIED
	        String alreadyVerified =
	                invoiceconsigneerepo.checkIfAlreadyVerifiedConsignee(id);

	        if (alreadyVerified != null) {

	            responseMap.put("message",
	                    "This consignee address is already verified.");

	            responseMap.put("status", "no");

	            responseMap.put("action",
	                    "VerifyConsigneeAddress");

	            return responseMap;
	        }

	        // VERIFY ADDRESS
	        int count =
	                invoiceconsigneerepo.verifyConsigneeAddressRecord(
	                        id,
	                        verified_by
	                );

	        responseMap.put(
	                "message",
	                (count > 0)
	                        ? "Success"
	                        : "Failed to verify consignee address"
	        );

	        responseMap.put(
	                "status",
	                (count > 0)
	                        ? "yes"
	                        : "no"
	        );

	        responseMap.put(
	                "action",
	                "VerifyConsigneeAddress"
	        );

	    } catch (Exception e) {

	        logger.error(
	                "ERROR IN METHOD :: verifyConsigneeAddress -> "
	                        + e.getMessage()
	        );

	        responseMap.put(
	                "message",
	                "An error occurred during verification."
	        );

	        responseMap.put("status", "no");
	    }

	    return responseMap;
	}
	
	@PostMapping("/consigneeAddress/releaseConsigneeAddress")
	public Map<String, Object> releaseConsigneeAddress(
	        @RequestParam Integer id,
	        @RequestParam Integer released_by) {

	    Map<String, Object> res = new HashMap<>();

	    try {
	        // ✅ No "already released" check — it's a toggle
	        int count = invoiceconsigneerepo.releaseConsigneeAddressRecord(id, released_by);

	        res.put("status", count > 0 ? "yes" : "no");
	        res.put("message", count > 0 ? "Release toggled successfully" : "Failed to toggle release");

	    } catch (Exception e) {
	        res.put("status", "no");
	        res.put("message", "Error occurred");
	    }

	    return res;
	}
}
