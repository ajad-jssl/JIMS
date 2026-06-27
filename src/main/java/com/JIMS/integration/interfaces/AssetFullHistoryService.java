package com.JIMS.integration.interfaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.AssetFullHistory;
import com.JIMS.integration.repository.AssetFullHistoryRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AssetFullHistoryService {

    @Autowired
    private AssetFullHistoryRepository repository;
    
    private Date addOneDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1); // adds 24 hours
        return cal.getTime();
    }

    // Fetch all full asset history
    public List<AssetFullHistory> getAllAssetHistory() {
        return repository. findAll(); // JPA will handle fetching from the view
    }

    
    
    public List<AssetFullHistory> getAllHistorybydesc(){
    	return repository.findAllOrderByTransactionDateAsc();
    }
    
    public List<AssetFullHistory> getHistoryBetweenDates(Date startDate, Date endDate) {
    	 
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        cal.add(Calendar.DATE, 1);   // ✅ include entire end date
        Date endDatePlusOne = cal.getTime();
 
        return repository
            .findByTransactionDateGreaterThanEqualAndTransactionDateLessThanOrderByTransactionDateAsc(
                startDate, endDatePlusOne
            );
    }
    
    
    public List<AssetFullHistory> getHistoryFromDate(Date startDate) {

       

        return repository.findByTransactionDateGreaterThanEqualOrderByTransactionDateAsc(startDate);
    }

    
    
    
    
    public List<AssetFullHistory> getHistoryUntilDate(Date endDate) {

    	Date newEndDate = addOneDay(endDate);  // include full day
        return repository.findByTransactionDateLessThanOrderByTransactionDateAsc(newEndDate);
      
    }
    
    
}
