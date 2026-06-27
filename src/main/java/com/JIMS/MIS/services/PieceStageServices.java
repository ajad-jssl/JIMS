package com.JIMS.MIS.services;

import com.JIMS.MIS.Repository.PieceStageRepository;
import com.JIMS.MIS.model.PieceStage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class PieceStageServices {
	
    private final PieceStageRepository PieceStageRepository;

    @Autowired
    public PieceStageServices(PieceStageRepository PieceStageRepository) {
        this.PieceStageRepository = PieceStageRepository;
    }

    public List<PieceStage> getAllPieceStages() {
        return PieceStageRepository.findAll();
    }

    public Optional<PieceStage> getPieceStageById(Integer id) {
        return PieceStageRepository.findById(id);
    }

    public PieceStage createPieceStage(PieceStage pieceStageModel) {
        return PieceStageRepository.save(pieceStageModel);
    }

    public PieceStage updatePieceStage(Integer id, PieceStage pieceStageModel) {
        if (PieceStageRepository.existsById(id)) {
            pieceStageModel.setPiecestage_id(id);
            return PieceStageRepository.save(pieceStageModel);
        }
        return null; // Return null if not found
    }

    public boolean deletePieceStage(Integer id) {
        if (PieceStageRepository.existsById(id)) {
        	PieceStageRepository.deleteById(id);
            return true;
        }
        return false; // Return false if not found
    }
        
    // Save method using repository
    public PieceStage save(PieceStage pieceStage) {
        return PieceStageRepository.save(pieceStage);
    }
    
    
}