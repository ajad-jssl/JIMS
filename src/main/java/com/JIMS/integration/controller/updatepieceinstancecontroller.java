package com.JIMS.integration.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.services.updatepieceinstanceservice;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class updatepieceinstancecontroller {

    @Autowired
    private updatepieceinstanceservice pieceInstanceService;

//    @PostMapping("/updatePieceInstance")
//    public String updatePieceInstance(@RequestParam int qty,
//    								  @RequestParam String lblLoadId,
//                                      @RequestParam String contractId,
//                                      @RequestParam String phaseLoadId,
//                                      @RequestParam String chkLoadItemValue) {
//        // Call the service method to update the Piece_Instance
//        return pieceInstanceService.updatePieceInstance(qty,lblLoadId, contractId, phaseLoadId, chkLoadItemValue);
//    }
    
    @PostMapping("/piece-instance/update")
    public String updatePieceInstance(
            @RequestParam int qty,
            @RequestParam String lblLoadId,
            @RequestParam String contractId,
            @RequestParam String phaseLoadId,
            @RequestParam String chkLoadItemValue,
            @RequestParam int factory_id
    ) {

        int rows = pieceInstanceService.updatePieceInstanceFactoryWise(
                qty,
                lblLoadId,
                contractId,
                phaseLoadId,
                chkLoadItemValue,
                factory_id
        );

        return (rows > 0) ? "Success" : "Failure";
    }

}
