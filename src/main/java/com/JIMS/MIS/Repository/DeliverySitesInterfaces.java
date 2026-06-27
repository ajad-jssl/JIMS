package com.JIMS.MIS.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.DeliverySites;
@Repository

public interface  DeliverySitesInterfaces  extends JpaRepository<DeliverySites, Integer> {
	@Query(value = " select delsite_id, shortname as descr ,del1,del2,del3,del4 from Delivery_Sites order by delsite_id ",nativeQuery = true)
	List<DeliverySites> finddelsite(); 
}



