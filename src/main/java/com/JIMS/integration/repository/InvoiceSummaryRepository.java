package com.JIMS.integration.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.User;

import java.util.List;

@Repository
public interface InvoiceSummaryRepository extends JpaRepository<User, Integer>  {
 
 @Query(value = """
     SELECT 
         'STEEL' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
         SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
         COUNT(*) 
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2)))
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
     FROM INVOICE_MASTER WHERE invoice_type = 'steel'
     
     UNION ALL
     
     SELECT 
         'ADVANCED' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
         SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
         COUNT(*) 
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2)))
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
     FROM INVOICE_MASTER WHERE invoice_type = 'ADV'
     
     UNION ALL
     
     SELECT 
         'DELIVERY_CHALLAN' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
         SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
         COUNT(*) 
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2)))
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
     FROM INVOICE_MASTER WHERE invoice_type = 'DLY'
     
     UNION ALL
     
     SELECT 
         'CREDIT_INVOICE' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(grand_total) AS total_amount,
         SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END) AS release_amount,
         SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
         COUNT(*) 
         - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
         SUM(grand_total)
         - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
     FROM DEBIT_CREDIT_INVOICE_MASTER WHERE note_type = 'Credit'
     
     UNION ALL
     
     SELECT 
         'DEBIT_NOTE' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(grand_total) AS total_amount,
         SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END) AS release_amount,
         SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
         COUNT(*) 
         - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
         SUM(grand_total)
         - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
     FROM DEBIT_CREDIT_INVOICE_MASTER WHERE note_type = 'Debit'
     
     UNION ALL
     
     SELECT 
         'CONSOLIDATED_INVOICE' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
         SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
         COUNT(*) 
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2)))
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
     FROM CONSOLIDATED_INVOICE_MASTER
     
     UNION ALL
     
     SELECT 
         'OTHERS_INVOICE' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(grand_total) AS total_amount,
         SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN grand_total ELSE 0 END) AS release_amount,
         SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
         COUNT(*) 
         - SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN 1 ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
         SUM(grand_total)
         - SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN grand_total ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
     FROM Others_invoice_master
     
     UNION ALL
     
     SELECT 
         'SCRAP_INVOICE' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(ROUND(spni.total + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2)) AS total_amount,
         SUM(CASE WHEN spn.verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN spn.verified_status = 'NOT VERIFIED' THEN ROUND(spni.total + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS release_amount,
         SUM(CASE WHEN spn.cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN spn.cancel = 1 THEN ROUND(spni.total + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS cancel_amount,
         SUM(CASE WHEN spn.verified_status = 'VERIFIED' AND spn.cancel IS NULL THEN 1 ELSE 0 END) AS verified_count,
         SUM(CASE WHEN spn.verified_status = 'VERIFIED' AND spn.cancel IS NULL THEN ROUND(spni.total + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS verified_amount
     FROM SCRAP_INVOICE_MASTER spn
     INNER JOIN SALE_ORDER_ENTRY soe ON soe.sale_order_code = spn.load_id
     INNER JOIN SCRAP_PACKING_NOTE scppnnote ON scppnnote.scp_load = spn.scp_load
     INNER JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.scp_load = spn.scp_load
     LEFT JOIN TAX_MASTER tm1 ON tm1.tax_id = soe.tax1
     LEFT JOIN TAX_MASTER tm2 ON tm2.tax_id = soe.tax2
     LEFT JOIN TAX_MASTER tm3 ON tm3.tax_id = soe.tax3
     
     ORDER BY invoice_type
     """, nativeQuery = true)
 List<Object[]> getInvoiceSummaryByType();
 
 @Query(value = """
     SELECT 
         'GRAND_TOTAL' AS invoice_type,
         SUM(total_count) AS total_count,
         SUM(total_amount) AS total_amount,
         SUM(release_count) AS release_count,
         SUM(release_amount) AS release_amount,
         SUM(cancel_count) AS cancel_count,
         SUM(cancel_amount) AS cancel_amount,
         SUM(verified_count) AS verified_count,
         SUM(verified_amount) AS verified_amount
     FROM (
         SELECT 
             COUNT(*) AS total_count,
             SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
             SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
             SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
             SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
             SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
             COUNT(*) 
             - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
             SUM(TRY_CAST(total AS DECIMAL(18,2)))
             - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
         FROM INVOICE_MASTER
         
         UNION ALL
         
         SELECT 
             COUNT(*) AS total_count,
             SUM(grand_total) AS total_amount,
             SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
             SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END) AS release_amount,
             SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
             SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
             COUNT(*) 
             - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
             SUM(grand_total)
             - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
         FROM DEBIT_CREDIT_INVOICE_MASTER
         
         UNION ALL
         
         SELECT 
             COUNT(*) AS total_count,
             SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
             SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
             SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
             SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
             SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
             COUNT(*) 
             - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
             SUM(TRY_CAST(total AS DECIMAL(18,2)))
             - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
         FROM CONSOLIDATED_INVOICE_MASTER
         
         UNION ALL
         
         SELECT 
             COUNT(*) AS total_count,
             SUM(grand_total) AS total_amount,
             SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN 1 ELSE 0 END) AS release_count,
             SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN grand_total ELSE 0 END) AS release_amount,
             SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
             SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
             COUNT(*) 
             - SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN 1 ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
             SUM(grand_total)
             - SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN grand_total ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
         FROM Others_invoice_master
         
         UNION ALL
         
         SELECT 
             COUNT(*) AS total_count,
             SUM(ROUND(spni.total + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2)) AS total_amount,
             SUM(CASE WHEN spn.verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
             SUM(CASE WHEN spn.verified_status = 'NOT VERIFIED' THEN ROUND(spni.total + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS release_amount,
             SUM(CASE WHEN spn.cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
             SUM(CASE WHEN spn.cancel = 1 THEN ROUND(spni.total + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS cancel_amount,
             SUM(CASE WHEN spn.verified_status = 'VERIFIED' AND spn.cancel IS NULL THEN 1 ELSE 0 END) AS verified_count,
             SUM(CASE WHEN spn.verified_status = 'VERIFIED' AND spn.cancel IS NULL THEN ROUND(spni.total + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS verified_amount
         FROM SCRAP_INVOICE_MASTER spn
         INNER JOIN SALE_ORDER_ENTRY soe ON soe.sale_order_code = spn.load_id
         INNER JOIN SCRAP_PACKING_NOTE scppnnote ON scppnnote.scp_load = spn.scp_load
         INNER JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.scp_load = spn.scp_load
         LEFT JOIN TAX_MASTER tm1 ON tm1.tax_id = soe.tax1
         LEFT JOIN TAX_MASTER tm2 ON tm2.tax_id = soe.tax2
         LEFT JOIN TAX_MASTER tm3 ON tm3.tax_id = soe.tax3
     ) combined_data
     """, nativeQuery = true)
 Object getGrandTotalSummary();
 
 
 
 
 
 
 @Query(value = """
	    SELECT 
         'STEEL' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
         SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
         COUNT(*) 
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2)))
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
     FROM INVOICE_MASTER WHERE invoice_type = 'steel' and factory_id =:factory_id
     
     UNION ALL
     
     SELECT 
         'ADVANCED' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
         SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
         COUNT(*) 
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2)))
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
     FROM INVOICE_MASTER WHERE invoice_type = 'ADV' and factory_id =:factory_id
     
     UNION ALL
     
     SELECT 
         'DELIVERY_CHALLAN' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
         SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
         COUNT(*) 
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2)))
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
     FROM INVOICE_MASTER WHERE invoice_type = 'DLY' and factory_id =:factory_id
     
     UNION ALL
     
     SELECT 
         'CREDIT_INVOICE' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(grand_total) AS total_amount,
         SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END) AS release_amount,
         SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
         COUNT(*) 
         - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
         SUM(grand_total)
         - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
     FROM DEBIT_CREDIT_INVOICE_MASTER WHERE note_type = 'Credit' and factory_id =:factory_id
     
     UNION ALL
     
     SELECT 
         'DEBIT_NOTE' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(grand_total) AS total_amount,
         SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END) AS release_amount,
         SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
         COUNT(*) 
         - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
         SUM(grand_total)
         - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
     FROM DEBIT_CREDIT_INVOICE_MASTER WHERE note_type = 'Debit' and factory_id =:factory_id
     
     UNION ALL
     
     SELECT 
         'CONSOLIDATED_INVOICE' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
         SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
         COUNT(*) 
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
         SUM(TRY_CAST(total AS DECIMAL(18,2)))
         - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
     FROM CONSOLIDATED_INVOICE_MASTER where factory_id =:factory_id
     
     UNION ALL
     
     SELECT 
         'OTHERS_INVOICE' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(grand_total) AS total_amount,
         SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN grand_total ELSE 0 END) AS release_amount,
         SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
         COUNT(*) 
         - SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN 1 ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
         SUM(grand_total)
         - SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN grand_total ELSE 0 END)
         - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
     FROM Others_invoice_master where factory_id =:factory_id
     
     UNION ALL
     
     SELECT 
         'SCRAP_INVOICE' AS invoice_type,
         COUNT(*) AS total_count,
         SUM(ROUND(spni.total + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2)) AS total_amount,
         SUM(CASE WHEN spn.verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
         SUM(CASE WHEN spn.verified_status = 'NOT VERIFIED' THEN ROUND(spni.total + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS release_amount,
         SUM(CASE WHEN spn.cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
         SUM(CASE WHEN spn.cancel = 1 THEN ROUND(spni.total + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS cancel_amount,
         SUM(CASE WHEN spn.verified_status = 'VERIFIED' AND spn.cancel IS NULL THEN 1 ELSE 0 END) AS verified_count,
         SUM(CASE WHEN spn.verified_status = 'VERIFIED' AND spn.cancel IS NULL THEN ROUND(spni.total + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
         ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS verified_amount
     FROM SCRAP_INVOICE_MASTER spn
     INNER JOIN SALE_ORDER_ENTRY soe ON soe.sale_order_code = spn.load_id
     INNER JOIN SCRAP_PACKING_NOTE scppnnote ON scppnnote.scp_load = spn.scp_load
     INNER JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.scp_load = spn.scp_load
     LEFT JOIN TAX_MASTER tm1 ON tm1.tax_id = soe.tax1
     LEFT JOIN TAX_MASTER tm2 ON tm2.tax_id = soe.tax2
     LEFT JOIN TAX_MASTER tm3 ON tm3.tax_id = soe.tax3
     where spn.factory_id =:factory_id
     ORDER BY invoice_type
	     """, nativeQuery = true)
	 List<Object[]> getInvoiceSummaryByTypeFactory(@Param("factory_id") String factory_id);
	 
	 
	 
	 
	 @Query(value = """
		  SELECT 
         'GRAND_TOTAL' AS invoice_type,
         SUM(total_count) AS total_count,
         SUM(total_amount) AS total_amount,
         SUM(release_count) AS release_count,
         SUM(release_amount) AS release_amount,
         SUM(cancel_count) AS cancel_count,
         SUM(cancel_amount) AS cancel_amount,
         SUM(verified_count) AS verified_count,
         SUM(verified_amount) AS verified_amount
     FROM (
         SELECT 
             COUNT(*) AS total_count,
             SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
             SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
             SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
             SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
             SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
             COUNT(*) 
             - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
             SUM(TRY_CAST(total AS DECIMAL(18,2)))
             - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
         FROM INVOICE_MASTER WHERE factory_id =:factory_id
         
         UNION ALL
         
         SELECT 
             COUNT(*) AS total_count,
             SUM(grand_total) AS total_amount,
             SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
             SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END) AS release_amount,
             SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
             SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
             COUNT(*) 
             - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
             SUM(grand_total)
             - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
         FROM DEBIT_CREDIT_INVOICE_MASTER
		  WHERE factory_id =:factory_id
         
         UNION ALL
         
         SELECT 
             COUNT(*) AS total_count,
             SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
             SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
             SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
             SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
             SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
             COUNT(*) 
             - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
             SUM(TRY_CAST(total AS DECIMAL(18,2)))
             - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
         FROM CONSOLIDATED_INVOICE_MASTER  WHERE factory_id =:factory_id
         
         UNION ALL
         
         SELECT 
             COUNT(*) AS total_count,
             SUM(grand_total) AS total_amount,
             SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN 1 ELSE 0 END) AS release_count,
             SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN grand_total ELSE 0 END) AS release_amount,
             SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
             SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
             COUNT(*) 
             - SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN 1 ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
             SUM(grand_total)
             - SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN grand_total ELSE 0 END)
             - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
         FROM Others_invoice_master  WHERE factory_id =:factory_id
         
         UNION ALL
         
         SELECT 
             COUNT(*) AS total_count,
             SUM(ROUND(spni.total + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2)) AS total_amount,
             SUM(CASE WHEN spn.verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
             SUM(CASE WHEN spn.verified_status = 'NOT VERIFIED' THEN ROUND(spni.total + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS release_amount,
             SUM(CASE WHEN spn.cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
             SUM(CASE WHEN spn.cancel = 1 THEN ROUND(spni.total + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS cancel_amount,
             SUM(CASE WHEN spn.verified_status = 'VERIFIED' AND spn.cancel IS NULL THEN 1 ELSE 0 END) AS verified_count,
             SUM(CASE WHEN spn.verified_status = 'VERIFIED' AND spn.cancel IS NULL THEN ROUND(spni.total + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) + 
             ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS verified_amount
         FROM SCRAP_INVOICE_MASTER spn  
         INNER JOIN SALE_ORDER_ENTRY soe ON soe.sale_order_code = spn.load_id
         INNER JOIN SCRAP_PACKING_NOTE scppnnote ON scppnnote.scp_load = spn.scp_load
         INNER JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.scp_load = spn.scp_load
         LEFT JOIN TAX_MASTER tm1 ON tm1.tax_id = soe.tax1
         LEFT JOIN TAX_MASTER tm2 ON tm2.tax_id = soe.tax2
         LEFT JOIN TAX_MASTER tm3 ON tm3.tax_id = soe.tax3  WHERE spn.factory_id=:factory_id
     ) combined_data
		     """, nativeQuery = true)
		 Object getGrandTotalSummaryFactory(@Param("factory_id") String factory_id);
	 
	 
	 
	 
	 
	 @Query(value = """
			    SELECT
			        'GRAND_TOTAL' AS invoice_type,
			        SUM(total_count) AS total_count,
			        SUM(total_amount) AS total_amount,
			        SUM(release_count) AS release_count,
			        SUM(release_amount) AS release_amount,
			        SUM(cancel_count) AS cancel_count,
			        SUM(cancel_amount) AS cancel_amount,
			        SUM(verified_count) AS verified_count,
			        SUM(verified_amount) AS verified_amount
			    FROM (

			        SELECT
			            COUNT(*) AS total_count,
			            SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
			            SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
			            SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
			            SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
			            SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
			            COUNT(*)
			            - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
			            - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
			            SUM(TRY_CAST(total AS DECIMAL(18,2)))
			            - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
			            - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
			        FROM INVOICE_MASTER
			        WHERE
			            (:factoryId IS NULL OR factory_id = :factoryId)
			        AND
			            (:contractId IS NULL OR contract_id = :contractId)

			        UNION ALL

			        SELECT
			            COUNT(*) AS total_count,
			            SUM(grand_total) AS total_amount,
			            SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
			            SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END) AS release_amount,
			            SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
			            SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
			            COUNT(*)
			            - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END)
			            - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
			            SUM(grand_total)
			            - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END)
			            - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
			        FROM DEBIT_CREDIT_INVOICE_MASTER
			        WHERE
			            (:factoryId IS NULL OR factory_id = :factoryId)
			        AND
			            (:contractId IS NULL OR con_id = :contractId)

			        UNION ALL

			        SELECT
			            COUNT(*) AS total_count,
			            SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
			            SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
			            SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
			            SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
			            SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
			            COUNT(*)
			            - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
			            - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
			            SUM(TRY_CAST(total AS DECIMAL(18,2)))
			            - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
			            - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
			        FROM CONSOLIDATED_INVOICE_MASTER
			        WHERE
			            (:factoryId IS NULL OR factory_id = :factoryId)
			        AND
			            (:contractId IS NULL OR contract_id = :contractId)

			        UNION ALL

			        SELECT
			            COUNT(*) AS total_count,
			            SUM(grand_total) AS total_amount,
			            SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN 1 ELSE 0 END) AS release_count,
			            SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN grand_total ELSE 0 END) AS release_amount,
			            SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
			            SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
			            COUNT(*)
			            - SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN 1 ELSE 0 END)
			            - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
			            SUM(grand_total)
			            - SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN grand_total ELSE 0 END)
			            - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
			        FROM Others_invoice_master
			        WHERE
			            (:factoryId IS NULL OR factory_id = :factoryId)
			        AND
			            (:contractId IS NULL OR con_id = :contractId)

			        UNION ALL

			        SELECT
			            COUNT(*) AS total_count,
			            SUM(ROUND(spni.total +
			            ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) +
			            ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) +
			            ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2)) AS total_amount,
			            SUM(CASE WHEN spn.verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
			            SUM(CASE WHEN spn.verified_status = 'NOT VERIFIED' THEN ROUND(spni.total +
			            ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) +
			            ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) +
			            ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS release_amount,
			            SUM(CASE WHEN spn.cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
			            SUM(CASE WHEN spn.cancel = 1 THEN ROUND(spni.total +
			            ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) +
			            ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) +
			            ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS cancel_amount,
			            SUM(CASE WHEN spn.verified_status = 'VERIFIED' AND spn.cancel IS NULL THEN 1 ELSE 0 END) AS verified_count,
			            SUM(CASE WHEN spn.verified_status = 'VERIFIED' AND spn.cancel IS NULL THEN ROUND(spni.total +
			            ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) +
			            ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) +
			            ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS verified_amount
			        FROM SCRAP_INVOICE_MASTER spn
			        INNER JOIN SALE_ORDER_ENTRY soe ON soe.sale_order_code = spn.load_id
			        INNER JOIN SCRAP_PACKING_NOTE scppnnote ON scppnnote.scp_load = spn.scp_load
			        INNER JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.scp_load = spn.scp_load
			        LEFT JOIN TAX_MASTER tm1 ON tm1.tax_id = soe.tax1
			        LEFT JOIN TAX_MASTER tm2 ON tm2.tax_id = soe.tax2
			        LEFT JOIN TAX_MASTER tm3 ON tm3.tax_id = soe.tax3
			        WHERE
			            (:factoryId IS NULL OR spn.factory_id = :factoryId)
			        AND
			            (:contractId IS NULL OR spn.contract_id = :contractId)

			    ) combined_data
			""", nativeQuery = true)

			List<Object[]> getSummaryContract(
			    @Param("factoryId") Integer factoryId,
			    @Param("contractId") String contractId
			);
 
 
			
			
			@Query(value = """
				    SELECT
				        'STEEL' AS invoice_type,
				        COUNT(*) AS total_count,
				        SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
				        SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
				        SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
				        SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
				        SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
				        COUNT(*)
				        - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
				        - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
				        SUM(TRY_CAST(total AS DECIMAL(18,2)))
				        - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
				        - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
				    FROM INVOICE_MASTER
				    WHERE invoice_type = 'steel'
				  AND
			            (:factoryId IS NULL OR factory_id = :factoryId)
			        AND
			            (:contractId IS NULL OR contract_id = :contractId)

				    UNION ALL

				    SELECT
				        'ADVANCED' AS invoice_type,
				        COUNT(*) AS total_count,
				        SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
				        SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
				        SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
				        SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
				        SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
				        COUNT(*)
				        - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
				        - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
				        SUM(TRY_CAST(total AS DECIMAL(18,2)))
				        - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
				        - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
				    FROM INVOICE_MASTER
				    WHERE invoice_type = 'ADV'
				   AND
			            (:factoryId IS NULL OR factory_id = :factoryId)
			        AND
			            (:contractId IS NULL OR contract_id = :contractId)

				    UNION ALL

				    SELECT
				        'DELIVERY_CHALLAN' AS invoice_type,
				        COUNT(*) AS total_count,
				        SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
				        SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
				        SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
				        SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
				        SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
				        COUNT(*)
				        - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
				        - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
				        SUM(TRY_CAST(total AS DECIMAL(18,2)))
				        - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
				        - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
				    FROM INVOICE_MASTER
				    WHERE invoice_type = 'DLY'
				 AND
			            (:factoryId IS NULL OR factory_id = :factoryId)
			        AND
			            (:contractId IS NULL OR contract_id = :contractId)
				    UNION ALL

				    SELECT
				        'CREDIT_INVOICE' AS invoice_type,
				        COUNT(*) AS total_count,
				        SUM(grand_total) AS total_amount,
				        SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
				        SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END) AS release_amount,
				        SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
				        SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
				        COUNT(*)
				        - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END)
				        - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
				        SUM(grand_total)
				        - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END)
				        - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
				    FROM DEBIT_CREDIT_INVOICE_MASTER
				    WHERE note_type = 'Credit'
				  AND
			            (:factoryId IS NULL OR factory_id = :factoryId)
			        AND
			            (:contractId IS NULL OR con_id = :contractId)

				    UNION ALL

				    SELECT
				        'DEBIT_NOTE' AS invoice_type,
				        COUNT(*) AS total_count,
				        SUM(grand_total) AS total_amount,
				        SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
				        SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END) AS release_amount,
				        SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
				        SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
				        COUNT(*)
				        - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END)
				        - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
				        SUM(grand_total)
				        - SUM(CASE WHEN is_release = 1 OR verified_status = 'NOT VERIFIED' THEN grand_total ELSE 0 END)
				        - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
				    FROM DEBIT_CREDIT_INVOICE_MASTER
				    WHERE note_type = 'Debit'
				    AND
			            (:factoryId IS NULL OR factory_id = :factoryId)
			        AND
			            (:contractId IS NULL OR con_id = :contractId)

				    UNION ALL

				    SELECT
				        'CONSOLIDATED_INVOICE' AS invoice_type,
				        COUNT(*) AS total_count,
				        SUM(TRY_CAST(total AS DECIMAL(18,2))) AS total_amount,
				        SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END) AS release_count,
				        SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS release_amount,
				        SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
				        SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS cancel_amount,
				        COUNT(*)
				        - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN 1 ELSE 0 END)
				        - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
				        SUM(TRY_CAST(total AS DECIMAL(18,2)))
				        - SUM(CASE WHEN is_release = 1 OR verified_status = 0 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END)
				        - SUM(CASE WHEN cancel = 1 THEN TRY_CAST(total AS DECIMAL(18,2)) ELSE 0 END) AS verified_amount
				    FROM CONSOLIDATED_INVOICE_MASTER
				   WHERE
			            (:factoryId IS NULL OR  factory_id = :factoryId)
			        AND
			            (:contractId IS NULL OR contract_id = :contractId)

				    UNION ALL

				    SELECT
				        'OTHERS_INVOICE' AS invoice_type,
				        COUNT(*) AS total_count,
				        SUM(grand_total) AS total_amount,
				        SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN 1 ELSE 0 END) AS release_count,
				        SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN grand_total ELSE 0 END) AS release_amount,
				        SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
				        SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS cancel_amount,
				        COUNT(*)
				        - SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN 1 ELSE 0 END)
				        - SUM(CASE WHEN cancel = 1 THEN 1 ELSE 0 END) AS verified_count,
				        SUM(grand_total)
				        - SUM(CASE WHEN is_release = 1 OR verified_status IS NULL THEN grand_total ELSE 0 END)
				        - SUM(CASE WHEN cancel = 1 THEN grand_total ELSE 0 END) AS verified_amount
				    FROM Others_invoice_master
				   WHERE
			            (:factoryId IS NULL OR factory_id = :factoryId)
			        AND
			            (:contractId IS NULL OR con_id = :contractId)

				    UNION ALL

				    SELECT
				        'SCRAP_INVOICE' AS invoice_type,
				        COUNT(*) AS total_count,
				        SUM(ROUND(spni.total +
				        ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) +
				        ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) +
				        ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2)) AS total_amount,
				        SUM(CASE WHEN spn.verified_status = 'NOT VERIFIED' THEN 1 ELSE 0 END) AS release_count,
				        SUM(CASE WHEN spn.verified_status = 'NOT VERIFIED' THEN ROUND(spni.total +
				        ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) +
				        ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) +
				        ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS release_amount,
				        SUM(CASE WHEN spn.cancel = 1 THEN 1 ELSE 0 END) AS cancel_count,
				        SUM(CASE WHEN spn.cancel = 1 THEN ROUND(spni.total +
				        ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) +
				        ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) +
				        ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS cancel_amount,
				        SUM(CASE WHEN spn.verified_status = 'VERIFIED' AND spn.cancel IS NULL THEN 1 ELSE 0 END) AS verified_count,
				        SUM(CASE WHEN spn.verified_status = 'VERIFIED' AND spn.cancel IS NULL THEN ROUND(spni.total +
				        ROUND(spni.total * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT), 0) / 100, 2) +
				        ROUND(spni.total * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT), 0) / 100, 2) +
				        ROUND(spni.total * COALESCE(TRY_CAST(tm3.tax_per AS FLOAT), 0) / 100, 2), 2) ELSE 0 END) AS verified_amount
				    FROM SCRAP_INVOICE_MASTER spn
				    INNER JOIN SALE_ORDER_ENTRY soe ON soe.sale_order_code = spn.load_id
				    INNER JOIN SCRAP_PACKING_NOTE scppnnote ON scppnnote.scp_load = spn.scp_load
				    INNER JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.scp_load = spn.scp_load
				    LEFT JOIN TAX_MASTER tm1 ON tm1.tax_id = soe.tax1
				    LEFT JOIN TAX_MASTER tm2 ON tm2.tax_id = soe.tax2
				    LEFT JOIN TAX_MASTER tm3 ON tm3.tax_id = soe.tax3
				   WHERE
			            (:factoryId IS NULL OR spn.factory_id = :factoryId)
			        AND
			            (:contractId IS NULL OR spn.contract_id = :contractId)
				    
				    ORDER BY invoice_type
				""", nativeQuery = true)

				List<Object[]> getSummaryByInvoiceType( @Param("factoryId") Integer factoryId,
					    @Param("contractId") String contractId);
}