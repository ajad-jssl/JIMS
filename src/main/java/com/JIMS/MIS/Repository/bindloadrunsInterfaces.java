package com.JIMS.MIS.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.bindloadruns;

@Repository
public interface bindloadrunsInterfaces extends CrudRepository<bindloadruns, Integer> {

    // You can either use @Query or @Procedure annotation
    @Query(value = "EXEC  dbo.spTra_GetLoadRunsSubcon:tloadId", nativeQuery = true)
    List<bindloadruns> getLoadRunsSubcon(int tloadId);
}
