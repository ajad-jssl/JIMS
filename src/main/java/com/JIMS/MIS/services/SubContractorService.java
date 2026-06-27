package com.JIMS.MIS.services;

import org.springframework.stereotype.Service;
import com.JIMS.MIS.Repository.SupplierRepository;
import com.JIMS.MIS.Repository.UsersRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.*;

@Service
public class SubContractorService {

    private final SupplierRepository supplierRepository;
    private final UsersRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    public SubContractorService(SupplierRepository supplierRepository, UsersRepository userRepository) {
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }

    // 1️⃣ Get all available suppliers
    public List<Map<String, Object>> getAvailableSuppliers() {
        List<Object[]> data = supplierRepository.findAvailableSuppliers();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("supplier_id", row[0]);
            map.put("name", row[1]);
            map.put("suppcat_id", row[2]);
            list.add(map);
        }
        return list;
    }

    // 2️⃣ Get all available users
    public List<Map<String, Object>> getAvailableUsers() {
        List<Object[]> data = userRepository.findAvailableUsers();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", row[0]);
            map.put("user_fullname", row[1]);
            map.put("user_name", row[2]);
            list.add(map);
        }
        return list;
    }
    
    public List<Map<String, Object>> getSubconUsers() {

        List<Object[]> data = userRepository.findSubconUsers();
        List<Map<String, Object>> list = new ArrayList<>();

        for (Object[] row : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", row[1]);
            map.put("user_name", row[1]);
            list.add(map);
        }

        return list;
    }
    
    
    
    public List<Map<String, Object>> getG2Users() {

        List<Object[]> data = userRepository.findOnlyG2Users();
        List<Map<String, Object>> list = new ArrayList<>();

        for (Object[] row : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", row[0]);
            map.put("user_name", row[1]);
            list.add(map);
        }

        return list;
    }

    // 3️⃣ Get contracts by supplier
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getContractsBySupplier(Integer supplierId) {
        String sql = "SELECT DISTINCT contract_id, cname " +
                     "FROM MIS.dbo.VW_JSSL_SubconContractLoad " +
                     "WHERE supplier_id = :supplierId and cname is not null";

        Query query = em.createNativeQuery(sql);
        
        // Match the DB type (use string if supplier_id is varchar in DB)
        query.setParameter("supplierId", supplierId); 

        List<Object[]> data = query.getResultList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("cid", row[0]);
            map.put("cname", row[1]);
            list.add(map);
        }

        return list;
    }

    // 4️⃣ Get distinct PZones by contract and supplier
    @SuppressWarnings("unchecked")
    public List<Integer> getDistinctPZones(String contractId, Integer supplierId) {
        String sql = "SELECT DISTINCT l.pzone " +
                     "FROM MIS.dbo.Loads l " +
                     "INNER JOIN MIS.dbo.VW_JSSL_SubconContractLoad s " +
                     "ON s.contract_id = l.contract_id AND s.load_id = l.load_id " +
                     "WHERE l.contract_id = :contractId AND s.supplier_id = :supplierId " +
                     "ORDER BY l.pzone";

        Query query = em.createNativeQuery(sql);
        query.setParameter("contractId", contractId);
        query.setParameter("supplierId", supplierId);

        return query.getResultList();
    }

    // 5️⃣ Get distinct PLoad by contract, supplier, and PZone
    public List<Map<String, Object>> getDistinctPLoads(String contractId, Integer supplierId, String pzone) {
        String sql = "SELECT DISTINCT l.pload, l.load_id " +
                     "FROM MIS.dbo.Loads l " +
                     "INNER JOIN MIS.dbo.VW_JSSL_SubconContractLoad s " +
                     "ON s.contract_id = l.contract_id AND s.load_id = l.load_id " +
                     "WHERE l.contract_id = :contractId AND s.supplier_id = :supplierId AND l.pzone = :pzone " +
                     "ORDER BY l.pload";
        Query query = em.createNativeQuery(sql);
        query.setParameter("contractId", contractId);
        query.setParameter("supplierId", supplierId.intValue());
        query.setParameter("pzone", pzone);

        @SuppressWarnings("unchecked")
        List<Object[]> data = query.getResultList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("pload", row[0]);
            map.put("load_id", row[1]);
            list.add(map);
        }
        return list;
    }
    
    public List<Map<String, Object>> getDistinctcontractloads(String contract_id, Integer supid) {
        String sql = "select distinct tload_id, loadno " +
                     "from MIS.dbo.[Tra_Loads] " +
                     "WHERE contract_id = :contract_id AND supid = :supid " +
                     "ORDER BY tload_id";
        Query query = em.createNativeQuery(sql);
        query.setParameter("contract_id", contract_id);
        query.setParameter("supid", supid.intValue());

        @SuppressWarnings("unchecked")
        List<Object[]> data = query.getResultList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("tload_id", row[0]);
            map.put("loadno", row[1]);
            list.add(map);
        }
        return list;
    }

    // 6️⃣ Get detailed load piece information
    public List<Map<String, Object>> getLoadDetails(String contractId, String pzone, String loadId, Integer supplierId) {
        String sql = "SELECT c.contract, c.JobCode, c.descr, " +
                     "l.pzone, l.pload, p.mark, p.descr AS pdescr, " +
                     "p.weight, p.area, vw.pqty, " +
                     "CAST(ROUND((vw.pqty * p.weight), 2) AS numeric(36,2)) AS tweight, " +
                     "CAST(ROUND((vw.pqty * p.area), 2) AS numeric(36,2)) AS tarea " +
                     "FROM MIS.dbo.Contracts c " +
                     "INNER JOIN MIS.dbo.Loads l ON l.contract_id = c.contract_id " +
                     "INNER JOIN MIS.dbo.vw_JSSLsumLoadPieceProgress vw ON vw.load_id = l.load_id " +
                     "INNER JOIN MIS.dbo.Pieces p ON l.contract_id = p.contract_id AND vw.pieces_id = p.pieces_id " +
                     "INNER JOIN MIS.dbo.VW_JSSL_SubconContractLoad s " +
                     "ON s.load_id = l.load_id AND s.contract_id = c.contract_id AND s.pzone = l.pzone " +
                     "WHERE c.contract_id = :contractId " +
                     "AND l.pzone = :pzone " +
                     "AND l.load_id = :loadId " +
                     "AND s.supplier_id = :supplierId " +
                     "ORDER BY c.contract, l.pzone, l.pload";
        Query query = em.createNativeQuery(sql);
        query.setParameter("contractId", contractId);
        query.setParameter("pzone", pzone);
        query.setParameter("loadId", loadId);
        query.setParameter("supplierId", supplierId.intValue());

        @SuppressWarnings("unchecked")
        List<Object[]> data = query.getResultList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("contract", row[0]);
            map.put("JobCode", row[1]);
            map.put("descr", row[2]);
            map.put("pzone", row[3]);
            map.put("pload", row[4]);
            map.put("mark", row[5]);
            map.put("pdescr", row[6]);
            map.put("weight", row[7]);
            map.put("area", row[8]);
            map.put("pqty", row[9]);
            map.put("tweight", row[10]);
            map.put("tarea", row[11]);
            list.add(map);
        }
        return list;
    }
    
    public List<Map<String, Object>> getPackingItems(String contractId, Integer loadId) {

        String sql = """
            SELECT DISTINCT c.descr, t.loadno, t.wb_weight,
                v.descr AS pdescr, v.mark, v.ssize, v.qty,
                (v.tweight / v.qty) * 1000 AS iawght, v.pload
            FROM MIS.dbo.vw_JSSLunionTraItemsAndExtras v
            INNER JOIN MIS.dbo.vw_JSSLtraFirstRun f ON f.tload_id = v.tload_id
            INNER JOIN MIS.dbo.Tra_Loads t ON f.tload_id = t.tload_id
            INNER JOIN MIS.dbo.Contracts c ON t.contract_id = c.contract_id
            WHERE c.contract_id = :contractId AND t.tload_id = :loadId
            ORDER BY v.descr, v.mark
            """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("contractId", contractId);
        query.setParameter("loadId", loadId);

        @SuppressWarnings("unchecked")
        List<Object[]> data = query.getResultList();
        List<Map<String, Object>> list = new ArrayList<>();

        for (Object[] row : data) {
            Map<String, Object> map = new HashMap<>();
            map.put("contractDescr", row[0]);
            map.put("loadNo", row[1]);
            map.put("weight", row[2]);
            map.put("pdescr", row[3]);
            map.put("mark", row[4]);
            map.put("ssize", row[5]);
            map.put("qty", row[6]);
            map.put("itemWght", row[7]);
            map.put("pload", row[8]);
            list.add(map);
        }

        return list;
    }
    
    public Map<String, Object> getHeader(String contractId, Integer loadId) {

        String sql = """
            SELECT 
                Contracts.descr,                      -- 0
                Tra_Loads.loadno,                     -- 1
                ISNULL(Tra_Loads.wb_weight * 1000,0), -- 2
                vw.descr,                             -- 3
                vw.mark,                              -- 4
                vw.ssize,                             -- 5
                vw.qty,                               -- 6
                vw.source,                            -- 7
                Contracts.fcontract,                  -- 8
                Tra_Loads.trailerref,                 -- 9
                Delivery_Sites.del1,                  -- 10
                Delivery_Sites.del2,                  -- 11
                Delivery_Sites.del3,                  -- 12
                Delivery_Sites.del4,                  -- 13
                CASE 
                    WHEN CONVERT(date, Tra_LoadRuns.despatchdate, 103) = '1900-01-01'
                    THEN Tra_Loads.created
                    ELSE Tra_LoadRuns.despatchdate
                END AS despatchdate,                  -- 14
                Hauliers.company,                     -- 15
                Tra_LoadRuns.notes,                   -- 16
                Tra_LoadRuns.invoiceno,               -- 17
                Contracts.contract,                   -- 18
                Delivery_Sites.phone,                 -- 19
                Delivery_Sites.contact,               -- 20
                Tra_Loads.trackingref,                -- 21
                Contracts.lc_ref,                     -- 22
                Contracts.client_po                   -- 23

            FROM MIS.dbo.vw_JSSLunionTraItemsAndExtras vw
            INNER JOIN MIS.dbo.vw_JSSLtraFirstRun fr ON fr.tload_id = vw.tload_id
            INNER JOIN MIS.dbo.Tra_Loads Tra_Loads ON fr.tload_id = Tra_Loads.tload_id
            INNER JOIN MIS.dbo.Tra_LoadRuns Tra_LoadRuns ON fr.run_id = Tra_LoadRuns.run_id
            INNER JOIN MIS.dbo.Delivery_Sites Delivery_Sites ON Tra_LoadRuns.destination_id = Delivery_Sites.delsite_id
            INNER JOIN MIS.dbo.Hauliers Hauliers ON Tra_LoadRuns.haulier_id = Hauliers.haulier_id
            INNER JOIN MIS.dbo.Contracts Contracts ON Tra_Loads.contract_id = Contracts.contract_id

            WHERE Contracts.contract_id = :contractId
            AND Tra_Loads.tload_id = :loadId

            ORDER BY vw.source, vw.descr, vw.mark, Tra_LoadRuns.createddate DESC
            """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("contractId", contractId);
        query.setParameter("loadId", loadId);

        @SuppressWarnings("unchecked")
		List<Object[]> rows = query.getResultList();

        if (rows.isEmpty()) {
            return Collections.emptyMap();
        }

        Object[] r = rows.get(0);   // <<< SAME AS SqlDataReader.Read() !!!

        Map<String, Object> map = new HashMap<>();
        map.put("descr", r[0]);
        map.put("loadNo", r[1]);
        map.put("weight", r[2]);
        map.put("itemDescr", r[3]);
        map.put("mark", r[4]);
        map.put("ssize", r[5]);
        map.put("qty", r[6]);
        map.put("source", r[7]);
        map.put("fcontract", r[8]);
        map.put("vehicle", r[9]);
        map.put("del1", r[10]);
        map.put("del2", r[11]);
        map.put("del3", r[12]);
        map.put("del4", r[13]);
        map.put("despatchDate", r[14]);
        map.put("haulier", r[15]);
        map.put("notes", r[16]);
        map.put("invoiceNo", r[17]);
        map.put("contract", r[18]);
        map.put("phone", r[19]);
        map.put("contact", r[20]);
        map.put("trackingRef", r[21]);
        map.put("lcRef", r[22]);
        map.put("clientPO", r[23]);

        return map;
    }
}
