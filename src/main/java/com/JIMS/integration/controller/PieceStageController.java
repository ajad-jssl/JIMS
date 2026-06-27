package com.JIMS.integration.controller;

import com.JIMS.MIS.model.PieceStage;
import com.JIMS.MIS.services.PieceStageServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/piecestages")
@CrossOrigin
public class PieceStageController {	

    private final PieceStageServices PieceStageServices;
    
    @Autowired
    public PieceStageController(PieceStageServices PieceStageServices) {
        this.PieceStageServices = PieceStageServices;
    }

    @GetMapping
    public ResponseEntity<List<PieceStage>> getAllPieceStages() {
        List<PieceStage> pieceStages = PieceStageServices.getAllPieceStages();
        return new ResponseEntity<>(pieceStages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PieceStage> getPieceStageById(@PathVariable Integer id) {
        Optional<PieceStage> pieceStage = PieceStageServices.getPieceStageById(id);
        return pieceStage.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<PieceStage> createPieceStage(@RequestBody PieceStage PieceStageModel) {
        PieceStage createdPieceStage = PieceStageServices.createPieceStage(PieceStageModel);
        return new ResponseEntity<>(createdPieceStage, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PieceStage> updatePieceStage(@PathVariable Integer id, 
                                                       @RequestBody PieceStage pieceStageModel) {
        PieceStage updatedPieceStage = PieceStageServices.updatePieceStage(id, pieceStageModel);
        return updatedPieceStage != null
               ? ResponseEntity.ok(updatedPieceStage)
               : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePieceStage(@PathVariable Integer id) {
        boolean isDeleted = PieceStageServices.deletePieceStage(id);
        return isDeleted
               ? ResponseEntity.noContent().build()
               : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}