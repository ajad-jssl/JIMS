package com.JIMS.MIS.services;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.lotdetailInterfaces;
import com.JIMS.MIS.model.lotdetail;

@Service
public class lotdetailservice {


    @Autowired
    private lotdetailInterfaces pieceInLoadRepository;

    public List<lotdetail> getPiecesInLoad(int tLoadId) {
        return pieceInLoadRepository.getPiecesInLoadByTLoadId(tLoadId);
    }
    
}
