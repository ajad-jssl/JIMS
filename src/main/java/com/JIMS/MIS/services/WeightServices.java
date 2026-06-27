package com.JIMS.MIS.services;
import com.JIMS.MIS.Repository.WeightRepository;
import com.JIMS.MIS.model.WeightModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WeightServices {
 
	 @Autowired
	    private WeightRepository WeightRepository;

	    public List<WeightModel> getAssemblyStagesForLoadSubcon(int Load_Id,int Sup_Id) { //String loadno, 
	        return WeightRepository.getAssemblyStagesForLoadSubcon(Load_Id,Sup_Id);//loadno, 
	    }
}
 