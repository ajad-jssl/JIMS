package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.JIMS.integration.entity.TbQcWeldMst;

public interface TbQcWeldMstRepository extends JpaRepository<TbQcWeldMst, Integer> {

    // 🔹 EXISTING DUPLICATE CHECK (UNCHANGED)
    @Query("""
        SELECT COUNT(t) > 0 FROM TbQcWeldMst t
        WHERE t.fabricator_ID = :fabricator
           OR t.welder_ID = :welder
           OR t.revision = :revision
           OR t.position = :position
           OR t.process = :process
    """)
    boolean existsAny(
        String fabricator,
        String welder,
        String revision,
        String position,
        String process
    );

    // 🔹 NEW – SUMMARY COUNTS
    @Query("""
        SELECT 
            COUNT(DISTINCT t.fabricator_ID),
            COUNT(DISTINCT t.welder_ID),
            COUNT(DISTINCT t.revision),
            COUNT(DISTINCT t.position),
            COUNT(DISTINCT t.process)
        FROM TbQcWeldMst t
    """)
    Object getSummaryCounts();
}
