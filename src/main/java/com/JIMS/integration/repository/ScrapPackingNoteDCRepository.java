package com.JIMS.integration.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.JIMS.integration.entity.DcPackingNoteInfo;
import com.JIMS.integration.interfaces.DCScrapPackingNoteBsdOnNoteTypeInterface;
import com.JIMS.integration.interfaces.DCScrapPackingNoteInterface;
import com.JIMS.integration.interfaces.DCScrapPackingNoteItemsInterface;

import jakarta.transaction.Transactional;

public interface ScrapPackingNoteDCRepository extends JpaRepository<DcPackingNoteInfo, Integer> {

	@Query(value = "select top 1 dc_load from PACKING_NOTE_DC where note_type like :note_type order by dc_load DESC", nativeQuery = true)
	String findLastDcLoad(String note_type);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO PACKING_NOTE_ITEMS_DC (dc_pn_id, note_type, dc_load, uom_id, quantities, kgs, unit_price, total, type_id, factory_id, created_by, created_date) VALUES (:dc_pn_id, :note_type, :newDCLoad, :uom_id, :quantities, :kgs, :unit_price, :total, :type_id, :factory_id, :created_by, :created_date)", nativeQuery = true)
	int saveDCPackingNoteItemsRecord(int dc_pn_id, String note_type, String newDCLoad, String uom_id, String quantities,
			String kgs, String unit_price, String total, String type_id, String factory_id, String created_by,
			LocalDateTime created_date);

	@Transactional
	@Modifying
	@Query(value = "UPDATE PACKING_NOTE_DC set filepath = :filepath, modified_by =:modified_by, modified_date = :modified_date where dc_load = :dc_load ", nativeQuery = true)
	int updateDCPackingNoteDetails(String filepath, String modified_by, LocalDateTime modified_date, String dc_load);

	@Transactional
	@Modifying
	@Query(value = "UPDATE PACKING_NOTE_ITEMS_DC \r\n" + "SET uom_id = :uom_id, \r\n"
			+ "    quantities = :quantities, \r\n" + "    kgs = :kgs, \r\n" + "    unit_price = :unit_price, \r\n"
			+ "    total = :total, \r\n" + "    type_id = :type_id, \r\n" + "    modified_by = :modified_by, \r\n"
			+ "    modified_date = :modified_date \r\n" + "WHERE dc_load = :dc_load \r\n"
			+ "  AND dc_pn_items_id = :dc_pn_items_id;\r\n" + "", nativeQuery = true)
	int updateDCPackingNote(String uom_id, String quantities, String kgs, String unit_price, String total,
			String type_id, String modified_by, LocalDateTime modified_date, String dc_load, String dc_pn_items_id);

	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query(value =
	 * "INSERT INTO PACKING_NOTE_ITEMS_DC (dc_pn_id, note_type, dc_load, uom_id, quantities, kgs, unit_price, total, type_id, factory_id, modified_by, modified_date) VALUES (:dc_pn_id, :note_type, :dc_load, :uom_id, :quantities, :kgs, :unit_price, :total, :type_id, :factory_id, :modified_by, :modified_date)"
	 * , nativeQuery = true) int insertDCPackingNote(int dc_pn_id, String note_type,
	 * String dc_load,String uom_id, String quantities, String kgs, String
	 * unit_price, String total, String type_id, String factory_id, String
	 * modified_by, String modified_date);
	 */

	@Query(value = "select dc_pn_id from PACKING_NOTE_DC where dc_load = :dc_load", nativeQuery = true)
	int getDCPackingNoteIdForTheDcLoad(String dc_load);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO PACKING_NOTE_ITEMS_DC (dc_pn_id, note_type, dc_load, uom_id, quantities, kgs, unit_price, total, type_id, factory_id, modified_by, modified_date) VALUES (:dc_pn_id, :note_type, :dc_load, :uom_id, :quantities, :kgs, :unit_price, :total, :type_id, :factory_id, :modified_by, :modified_date)", nativeQuery = true)
	int insertDCPackingNote(int dc_pn_id, String note_type, String dc_load, String uom_id, String quantities,
			String kgs, String unit_price, String total, String type_id, String modified_by,
			LocalDateTime modified_date, String factory_id);

	@Query(value = "SELECT * from PACKING_NOTE_DC where factory_id = :factory_id AND is_delete = 0", nativeQuery = true)
	List<DCScrapPackingNoteInterface> getDCPackingNoteDetails(String factory_id);

	@Query(value = "SELECT * from PACKING_NOTE_ITEMS_DC where dc_load = :dc_load AND factory_id = :factory_id AND is_delete = 0", nativeQuery = true)
	List<DCScrapPackingNoteItemsInterface> getDCPackingNoteItemDetails(String dc_load, String factory_id);

	@Query(value = "SELECT * from PACKING_NOTE_DC where dc_load = :dc_load and is_delete = 0", nativeQuery = true)
	DCScrapPackingNoteInterface getDCScrapPackingNoteDetailsBasedOnPackingNote(String dc_load);

	@Query(value = "SELECT * from PACKING_NOTE_ITEMS_DC where dc_load = :dc_load and is_delete = 0", nativeQuery = true)
	List<DCScrapPackingNoteItemsInterface> getDCScrapItemDetails(String dc_load);

	@Query(value = "SELECT note_type,dc_load from PACKING_NOTE_DC where note_type = :note_type AND is_delete = 0", nativeQuery = true)
	List<DCScrapPackingNoteBsdOnNoteTypeInterface> getDCScrapPackingNoteDetailsBasedOnNoteType(String note_type);

}
