package com.JIMS.integration.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.services.piecestounloadservice;
@CrossOrigin
@RestController
@RequestMapping("/api/piecesunload")
public class piecestounloadcontroller {


    @Autowired
    private piecestounloadservice pieceInstanceService;

//    @PostMapping("/update")
//    public String updatePieceInstance(
//    		@RequestParam int qty,
//            @RequestParam int tloadId,
//            @RequestParam int piecesId,
//            @RequestParam int loadId,
//            @RequestParam int supplierId,
//            @RequestParam int locfgId) {
//    	
//     return  pieceInstanceService.updatePiecestounload(qty,tloadId, piecesId, loadId, supplierId, locfgId);
//    }
    
    @PostMapping("/update")
    public ResponseEntity<String> updatePiecestounload(
            @RequestParam int qty,
            @RequestParam int tloadId,
            @RequestParam int piecesId,
            @RequestParam int loadId,
            @RequestParam int supplierId,
            @RequestParam int locfgId,
            @RequestParam("factory_id") int factoryId
    ) {

        int updated = pieceInstanceService.updatePiecestounload(
                qty, tloadId, piecesId, loadId, supplierId, locfgId, factoryId
        );

        return ResponseEntity.ok("Updated rows: " + updated);
    }

}


