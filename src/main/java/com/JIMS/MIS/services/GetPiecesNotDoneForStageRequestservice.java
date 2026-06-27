package com.JIMS.MIS.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.GetPiecesNotDoneForStageRequestInterfaces;

import java.util.List;
import java.util.Map;

@Service
public class GetPiecesNotDoneForStageRequestservice {
	
    @Autowired
    private GetPiecesNotDoneForStageRequestInterfaces piecesRepository;

    public List<Map<String, Object>> getPiecesNotDoneForStage(Integer piecesId, Integer loadId, Integer pieceStagesId) {
        return piecesRepository.getPiecesNotDoneForStage(piecesId, loadId, pieceStagesId);
    }
}
