package com.JIMS.integration.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.JIMS.MIS.services.GetPiecesNotDoneForStageRequestservice;

import java.util.List;
import java.util.Map;
@CrossOrigin
@RestController
@RequestMapping("/api/pieces")
public class GetPiecesNotDoneForStageRequestcontroller {

    @Autowired
    private GetPiecesNotDoneForStageRequestservice piecesService;

    @GetMapping("/getPiecesNotDoneForStage")
    public ResponseEntity<List<Map<String, Object>>> getPiecesNotDoneForStage(
            @RequestParam("piecesId") Integer piecesId,
            @RequestParam("LoadId") Integer loadId,
            @RequestParam("PieceStagesId") Integer pieceStagesId) {
        List<Map<String, Object>> result = piecesService.getPiecesNotDoneForStage(piecesId, loadId, pieceStagesId);
        return ResponseEntity.ok(result);
    }
}
