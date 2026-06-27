package com.JIMS.MIS.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.tra_rates;

@Repository
public interface tra_ratesInterfaces  extends JpaRepository<tra_rates, Integer> {
	@Query(value = "select trate_id, rtrim(country) as country, rtrim(county) as county from tra_rates ",nativeQuery = true)
	List<tra_rates> findtrarates();

}








