package com.JIMS.integration.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.SaleOrderEntry;
import com.JIMS.integration.interfaces.SaleOrderInterface;
import com.JIMS.integration.interfaces.SaleOrderItemDescLevelInterface;
import com.JIMS.integration.interfaces.SaleOrderItemLevelInterface;
import com.JIMS.integration.interfaces.allSaleOrderEntriesInterface;
import com.JIMS.integration.repository.SaleOrderEntryRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class SaleOrderEntryController {
	
	Logger logger = LogManager.getLogger(QSPackingController.class);

	@Autowired
	private SaleOrderEntryRepository saleOrderEntryRepository;

	//@SuppressWarnings({ "unused", "unchecked" })
	@PostMapping("/saleOrderEntry/addSaleOrderEntry")
	public @ResponseBody Map<String, Object> createSaleOrder(
	        @RequestParam("sale_order_type_id") int saleOrderTypeId,
	        @RequestParam("sale_order_code") String saleOrderCode,
	        @RequestParam("location_type_id") int locationTypeId,
	        @RequestParam("sale_order_to_id") int saleOrderToId,
	        @RequestParam("auction_date") String auctionDate,
	        @RequestParam("sale_order_duration") String saleOrderDuration,
	        @RequestParam("advance_payment") int advancePayment,
	        @RequestParam("billing_address_id") int billingAddressId,
	        @RequestParam("shipping_address_id") int shippingAddressId,
	        @RequestParam("business_unit_id") int businessUnitId,
	        @RequestParam("tax1") String tax1,
	        @RequestParam("tax1_value") String tax1Value,
	        @RequestParam("tax2") String tax2,
	        @RequestParam("tax2_value") String tax2Value,
	        @RequestParam("tax3") String tax3,
	        @RequestParam("tax3_value") String tax3Value,
	        @RequestParam("net_amount") String netAmount,
	        @RequestParam("total_tax") String totalTax,
	        @RequestParam("grand_total") String grandTotal,
	        @RequestParam("created_by") String createdBy,
	        @RequestParam("factory_id") String factoryId,
	        @RequestParam("auction_no") int auctionNo,
	        @RequestParam("scrap_type_id") String scrapTypeId,
	        @RequestParam("scrapitem_id") String scrapItemId,
	        @RequestParam("uom_id") String uomId,
	        @RequestParam("quantity") String quantity,
	        @RequestParam("kg") String kg,
	        @RequestParam("unit_price") String unitPrice,
	        @RequestParam("total") String total,
	        @RequestParam("tolerance") String tolerance,
	        @RequestParam List<String> termsConditions
	) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        // 1️⃣ Save Sale Order Entry
	        SaleOrderEntry saleEntry = new SaleOrderEntry();
	        saleEntry.setSale_order_type_id(saleOrderTypeId);
	        saleEntry.setSale_order_code(saleOrderCode);
	        saleEntry.setLocation_type_id(locationTypeId);
	        saleEntry.setSale_order_to_id(saleOrderToId);
	        saleEntry.setAdvance_payment(advancePayment);
	        saleEntry.setBilling_address_id(billingAddressId);
	        saleEntry.setShipping_address_id(shippingAddressId);
	        saleEntry.setBusiness_unit_id(businessUnitId);
	        saleEntry.setTax1(Float.parseFloat(tax1.isEmpty() ? "0" : tax1));
	        saleEntry.setTax1_value(Float.parseFloat(tax1Value.isEmpty() ? "0" : tax1Value));
	        saleEntry.setTax2(Float.parseFloat(tax2.isEmpty() ? "0" : tax2));
	        saleEntry.setTax2_value(Float.parseFloat(tax2Value.isEmpty() ? "0" : tax2Value));
	        saleEntry.setTax3(Float.parseFloat(tax3.isEmpty() ? "0" : tax3));
	        saleEntry.setTax3_value(Float.parseFloat(tax3Value.isEmpty() ? "0" : tax3Value));
	        saleEntry.setNet_amount(Float.parseFloat(netAmount.isEmpty() ? "0" : netAmount));
	        saleEntry.setTotal_tax(Float.parseFloat(totalTax.isEmpty() ? "0" : totalTax));
	        saleEntry.setGrand_total(Float.parseFloat(grandTotal.isEmpty() ? "0" : grandTotal));
	        saleEntry.setAuction_date(LocalDate.parse(auctionDate));
	        saleEntry.setSale_order_duration(LocalDate.parse(saleOrderDuration));
	        saleEntry.setCreated_by(createdBy);
	        saleEntry.setFactory_id(factoryId);
	        saleEntry.setCreated_date(LocalDateTime.now());

	        saleOrderEntryRepository.save(saleEntry);

	        // 2️⃣ Insert Sale Order Item Level Entry
	        saleOrderEntryRepository.insertIntoSaleOrderItemLevelEntry(
	                saleEntry.getSoe_id(), String.valueOf(auctionNo), scrapTypeId, scrapItemId, uomId,
	                quantity, kg, unitPrice, total, tolerance, createdBy
	        );

	        // 3️⃣ Insert Sale Order Description Level Entries
	        if (termsConditions != null && !termsConditions.isEmpty()) {
	            int sl_no = 1;
	            for (String terms : termsConditions) {
	                saleOrderEntryRepository.insertIntoSaleOrderDescriptionLevelEntry(
	                        saleEntry.getSoe_id(), sl_no, terms, createdBy
	                );
	                sl_no++;
	            }
	        }

	        response.put("message", "Success");
	        response.put("status", "yes");
	        response.put("action", "CreateSaleOrderEntry");
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("message", "Failed");
	        response.put("status", "no");
	        response.put("action", "CreateSaleOrderEntry");
	    }
	    return response;
	}


	@SuppressWarnings({ "unchecked", "unused" })
	@PostMapping("/saleOrderEntry/updateSaleOrderEntry")
	public @ResponseBody Map<String, Object> updateSaleOrder(@RequestBody Map<String, Object> saleOrder) {

		Map<String, Object> updateSaleOrderEntrymap = new HashMap<String, Object>();

		Map<String, String> saleOrderlevel = null;
		List<Map<String, String>> saleItemlevel = null;
		List<Map<String, String>> saleDesclevel = null;
		int updateSaleOrderItemLevelEntry = 0;
		int updateDescriptionLevelSaleOrderEntry = 0;

		try {

			saleOrderlevel = (Map<String, String>) saleOrder.get("orderlevel");
			saleItemlevel = (List<Map<String, String>>) saleOrder.get("itemlevel");
			saleDesclevel = (List<Map<String, String>>) saleOrder.get("desclevel");
			String modified_by = null;

			if ((saleOrderlevel != null && !saleOrderlevel.isEmpty())
					&& (saleItemlevel != null && !saleItemlevel.isEmpty())
					&& (saleDesclevel != null && !saleDesclevel.isEmpty())) {

				// Set Sale Order Entry details
				SaleOrderEntry existingSaleOrderEntry = new SaleOrderEntry();
				existingSaleOrderEntry.setSoe_id(parseInteger(saleOrderlevel.get("soe_id")));
				/*
				 * int value =
				 * saleOrderEntryRepository.checkisLocked(saleOrderlevel.get("soe_id"));
				 * if(value > 0) { updateSaleOrderEntrymap.put("message",
				 * "Invoice Already Generated Not Able to Update"); return
				 * updateSaleOrderEntrymap; }
				 */
				existingSaleOrderEntry.setSale_order_type_id(parseInteger(saleOrderlevel.get("sale_order_type_id")));
				existingSaleOrderEntry.setLocation_type_id(parseInteger(saleOrderlevel.get("location_type_id")));
				existingSaleOrderEntry.setSale_order_to_id(parseInteger(saleOrderlevel.get("sale_order_to_id")));
				existingSaleOrderEntry.setAdvance_payment(parseInteger(saleOrderlevel.get("advance_payment")));
				existingSaleOrderEntry.setBilling_address_id(parseInteger(saleOrderlevel.get("billing_address_id")));
				existingSaleOrderEntry.setShipping_address_id(parseInteger(saleOrderlevel.get("shipping_address_id")));
				existingSaleOrderEntry.setBusiness_unit_id(parseInteger(saleOrderlevel.get("business_unit_id")));
				existingSaleOrderEntry.setTax1(parseFloat(saleOrderlevel.get("tax1")));
				existingSaleOrderEntry.setTax1_value(parseFloat(saleOrderlevel.get("tax1_value")));
				existingSaleOrderEntry.setTax2(parseFloat(saleOrderlevel.get("tax2")));
				existingSaleOrderEntry.setTax2_value(parseFloat(saleOrderlevel.get("tax2_value")));
				existingSaleOrderEntry.setTax3(parseFloat(saleOrderlevel.get("tax3")));
				existingSaleOrderEntry.setTax3_value(parseFloat(saleOrderlevel.get("tax3_value")));
				existingSaleOrderEntry.setNet_amount(parseFloat(saleOrderlevel.get("net_amount")));
				existingSaleOrderEntry.setTotal_tax(parseFloat(saleOrderlevel.get("total_tax")));
				existingSaleOrderEntry.setGrand_total(parseFloat(saleOrderlevel.get("grand_total")));
				existingSaleOrderEntry.setSale_order_code(saleOrderlevel.get("sale_order_code"));

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				existingSaleOrderEntry
						.setAuction_date(LocalDate.parse(saleOrderlevel.get("auction_date"), formatter));
				existingSaleOrderEntry.setSale_order_duration(
						LocalDate.parse(saleOrderlevel.get("sale_order_duration"), formatter));
				existingSaleOrderEntry.setSoe_id(parseInteger(saleOrderlevel.get("soe_id")));
				existingSaleOrderEntry.setModified_by(saleOrderlevel.get("modified_by"));

				// Save Sale Order Entry
				saleOrderEntryRepository.save(existingSaleOrderEntry);

				modified_by = saleOrderlevel.get("modified_by");

				// Update Sale Order Item Level
				for (Map<String, String> itemsmap : saleItemlevel) {
					updateSaleOrderItemLevelEntry = saleOrderEntryRepository.updateSaleOrderItemLevel(
							parseInteger(itemsmap.get("auction_no")), parseInteger(itemsmap.get("scrap_type_id")),
							parseInteger(itemsmap.get("scrapitem_id")), parseInteger(itemsmap.get("uom_id")),
							parseInteger(itemsmap.get("quantity")), parseInteger(itemsmap.get("kg")),
							parseFloat(itemsmap.get("unit_price")), parseFloat(itemsmap.get("total")),
							parseInteger(itemsmap.get("tolerance")), modified_by, saleOrderlevel.get("soe_id"));
				}

				// Update Description Level Sale Order Entry
				for (Map<String, String> descmap : saleDesclevel) {
					String terms_conditions = descmap.get("terms_conditions");
					String sl_no = descmap.get("sl_no");
					System.out.println(">>>>>" + terms_conditions);

					// Check if the term condition exists for the given soe_id
					updateDescriptionLevelSaleOrderEntry = saleOrderEntryRepository
							.updateDescriptionLevelSaleOrderEntry(saleOrderlevel.get("soe_id"), sl_no, terms_conditions,
									modified_by);
				}

				updateSaleOrderEntrymap.put("message",
						(updateDescriptionLevelSaleOrderEntry > 0) ? "Success" : "SaleOrderNotUpdated");
				updateSaleOrderEntrymap.put("status", (updateDescriptionLevelSaleOrderEntry > 0) ? "yes" : "no");
				updateSaleOrderEntrymap.put("action", "UpdateSaleOrderEntry");

			}

		} catch (Exception e) {
			e.printStackTrace();
			updateSaleOrderEntrymap.put("message", "Error: " + e.getMessage());
			updateSaleOrderEntrymap.put("status", "no");
		}

		return updateSaleOrderEntrymap;
	}

	/*
	 * @GetMapping("/saleOrderEntry/getSaleOrderBasedOnId") public @ResponseBody
	 * Map<String, Object> getSaleOrderDetailsBasedOnId(@RequestParam String soe_id)
	 * {
	 * 
	 * Map<String, Object> saleOrderByIdmap = new HashMap<String, Object>();
	 * List<SaleOrderInterface> saleOrderlist = new ArrayList<SaleOrderInterface>();
	 * List<SaleOrderItemLevelInterface> saleOrderItemLevellist = new
	 * ArrayList<SaleOrderItemLevelInterface>();
	 * List<SaleOrderItemDescLevelInterface> saleOrderDescLevellist = new
	 * ArrayList<SaleOrderItemDescLevelInterface>();
	 * 
	 * try { saleOrderlist =
	 * saleOrderEntryRepository.getAllSaleOrderDetailsBAsedOnId(soe_id);
	 * saleOrderItemLevellist =
	 * saleOrderEntryRepository.getSaleOrderItemLevelDetails(soe_id);
	 * saleOrderDescLevellist =
	 * saleOrderEntryRepository.getSaleOrderDescriptionLevelDetails(soe_id);
	 * 
	 * saleOrderByIdmap.put("orderLevel", saleOrderlist);
	 * saleOrderByIdmap.put("orderItemLevel", saleOrderItemLevellist);
	 * saleOrderByIdmap.put("orderDescriptionLevel", saleOrderDescLevellist);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return saleOrderByIdmap;
	 * 
	 * }
	 */

	@GetMapping("/saleOrderEntry/getSaleOrderBasedOnId")
	public @ResponseBody Map<String, Object> getSaleOrderDetailsBasedOnId(@RequestParam String sale_order_code) {

		Map<String, Object> saleOrderByIdmap = new HashMap<String, Object>();
		List<SaleOrderInterface> saleOrderlist = new ArrayList<SaleOrderInterface>();
		List<SaleOrderItemLevelInterface> saleOrderItemLevellist = new ArrayList<SaleOrderItemLevelInterface>();
		List<SaleOrderItemDescLevelInterface> saleOrderDescLevellist = new ArrayList<SaleOrderItemDescLevelInterface>();

		try {

			String soe_id = saleOrderEntryRepository.getSoeIdBasedOnSaleOrderCode(sale_order_code);

			saleOrderlist = saleOrderEntryRepository.getAllSaleOrderDetailsBAsedOnId(soe_id);
			saleOrderItemLevellist = saleOrderEntryRepository.getSaleOrderItemLevelDetails(soe_id);
			saleOrderDescLevellist = saleOrderEntryRepository.getSaleOrderDescriptionLevelDetails(soe_id);

			saleOrderByIdmap.put("orderLevel", saleOrderlist);
			saleOrderByIdmap.put("orderItemLevel", saleOrderItemLevellist);
			saleOrderByIdmap.put("orderDescriptionLevel", saleOrderDescLevellist);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return saleOrderByIdmap;

	}
	
	@GetMapping("/saleOrderEntry/getSaleOrderBasedOnFactoryIdForScrap")
	public @ResponseBody Map<String, Object> getAllSaleOrderEntries(@RequestParam String factory_id) {
	    logger.info("EXECUTING METHOD :: getAllSaleOrderEntries");
	    
	    Map<String, Object> response = new HashMap<>();
	    List<allSaleOrderEntriesInterface> allSaleOrderList = null;
	    
	    try {
	        allSaleOrderList = saleOrderEntryRepository.getAllSaleOrderDetailsBasedOnFactory(factory_id);
	        
	        response.put("SaleOrderDetails", allSaleOrderList);
	        response.put("message", (allSaleOrderList != null && !allSaleOrderList.isEmpty()) ? "Success" : "Sale Order details not found!");
	        response.put("status", (allSaleOrderList != null && !allSaleOrderList.isEmpty()) ? "yes" : "no");
	        response.put("action", "AllSaleOrderDetails");
	        
	    } catch (Exception e) {
	        logger.error("ERROR IN METHOD getAllSaleOrderEntries :: " + e.getMessage(), e);
	        response.put("SaleOrderDetails", null);
	        response.put("message", "An error occurred while fetching data.");
	        response.put("status", "no");
	        response.put("action", "AllSaleOrderDetails");
	    }
	    
	    logger.info("EXECUTED METHOD :: getAllSaleOrderEntries");
	    return response;
	}
	
	
	
	
	@GetMapping("/saleOrderEntry/getSaleOrderBasedOnFactoryIdForScrapPaged")
	public @ResponseBody Map<String, Object> getAllSaleOrderEntriesPaged(
	        @RequestParam String factory_id,
	        @RequestParam(required = false) String search,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    Map<String, Object> response = new HashMap<>();
	    logger.info("EXECUTING METHOD :: getAllSaleOrderEntriesPaged");

	    try {
	        Pageable pageable = PageRequest.of(page, size);

	        Page<allSaleOrderEntriesInterface> pageResult =
	        		saleOrderEntryRepository.getAllSaleOrderDetailsBasedOnFactoryPaged(factory_id, search, pageable);

	        logger.info("TOTAL RECORDS :: " + pageResult.getTotalElements());

	        response.put("SaleOrderDetails", pageResult.getContent());
	        response.put("message",          pageResult.hasContent() ? "Success" : "Sale Order details not found!");
	        response.put("status",           pageResult.hasContent() ? "yes" : "no");
	        response.put("action",           "AllSaleOrderDetails");
	        response.put("totalItems",       pageResult.getTotalElements());
	        response.put("currentPage",      pageResult.getNumber());
	        response.put("totalPages",       pageResult.getTotalPages());

	    } catch (Exception e) {
	        logger.error("ERROR IN getAllSaleOrderEntriesPaged :: " + e.getMessage(), e);
	        response.put("SaleOrderDetails", null);
	        response.put("message",          "An error occurred while fetching data.");
	        response.put("status",           "error");
	        response.put("action",           "AllSaleOrderDetails");
	    }

	    logger.info("EXECUTED METHOD :: getAllSaleOrderEntriesPaged");
	    return response;
	}

	@SuppressWarnings({ "unchecked", "unused", "null" })
	@PostMapping("/saleOrderEntry/updateSaleOrder")
	public @ResponseBody Map<String, Object> updateSaleOrderByFlush(@RequestBody Map<String, Object> saleOrder) {

		Map<String, Object> updateSaleOrderWIthFlushmap = new HashMap<String, Object>();

		Map<String, String> saleOrderlevel = null;
		List<Map<String, String>> saleItemlevel = null;
		List<Map<String, String>> saleDesclevel = null;
		String modified_by = null;
		try {
			saleOrderlevel = (Map<String, String>) saleOrder.get("orderlevel");
			saleItemlevel = (List<Map<String, String>>) saleOrder.get("saleitemlevel");
			saleDesclevel = (List<Map<String, String>>) saleOrder.get("desclevel");

			if ((saleOrderlevel != null & !saleOrderlevel.isEmpty())
					&& (saleItemlevel != null & !saleItemlevel.isEmpty())
					&& (saleDesclevel != null && !saleDesclevel.isEmpty())) {

				SaleOrderEntry existingSaleOrderEntry = new SaleOrderEntry();
				existingSaleOrderEntry.setSale_order_type_id(parseInteger(saleOrderlevel.get("sale_order_type_id")));
				existingSaleOrderEntry.setLocation_type_id(parseInteger(saleOrderlevel.get("location_type_id")));
				existingSaleOrderEntry.setSale_order_to_id(parseInteger(saleOrderlevel.get("sale_order_to_id")));
				existingSaleOrderEntry.setAdvance_payment(parseInteger(saleOrderlevel.get("advance_payment")));
				existingSaleOrderEntry.setBilling_address_id(parseInteger(saleOrderlevel.get("billing_address_id")));
				existingSaleOrderEntry.setShipping_address_id(parseInteger(saleOrderlevel.get("shipping_address_id")));
				existingSaleOrderEntry.setBusiness_unit_id(parseInteger(saleOrderlevel.get("business_unit_id")));
				existingSaleOrderEntry.setTax1(parseFloat(saleOrderlevel.get("tax1")));
				existingSaleOrderEntry.setTax1_value(parseFloat(saleOrderlevel.get("tax1_value")));
				existingSaleOrderEntry.setTax2(parseFloat(saleOrderlevel.get("tax2")));
				existingSaleOrderEntry.setTax2_value(parseFloat(saleOrderlevel.get("tax2_value")));
				existingSaleOrderEntry.setTax3(parseFloat(saleOrderlevel.get("tax3")));
				existingSaleOrderEntry.setTax3_value(parseFloat(saleOrderlevel.get("tax3_value")));
				existingSaleOrderEntry.setNet_amount(parseFloat(saleOrderlevel.get("net_amount")));
				existingSaleOrderEntry.setTotal_tax(parseFloat(saleOrderlevel.get("total_tax")));
				existingSaleOrderEntry.setGrand_total(parseFloat(saleOrderlevel.get("grand_total")));

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				existingSaleOrderEntry
						.setAuction_date(LocalDate.parse(saleOrderlevel.get("auction_date"), formatter));
				existingSaleOrderEntry.setSale_order_duration(
						LocalDate.parse(saleOrderlevel.get("sale_order_duration"), formatter));
				existingSaleOrderEntry.setSoe_id(parseInteger(saleOrderlevel.get("soe_id")));
				existingSaleOrderEntry.setModified_by(saleOrderlevel.get("modified_by"));

				saleOrderEntryRepository.save(existingSaleOrderEntry);

				modified_by = saleOrderlevel.get("modified_by");

				for (Map<String, String> map : saleItemlevel) {

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateSaleOrderWIthFlushmap;

	}

	// Helper Methods to safely parse values
	private Integer parseInteger(String value) {
		try {
			return value != null ? Integer.parseInt(value) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private Float parseFloat(String value) {
		try {
			return value != null ? Float.parseFloat(value) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
