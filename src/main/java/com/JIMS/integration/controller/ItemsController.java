package com.JIMS.integration.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.ItemsModel;
import com.JIMS.integration.interfaces.ItemsServices;

@CrossOrigin
@RestController
@RequestMapping("/api/items") 
public class ItemsController {

    @Autowired
    private ItemsServices itemsServices;  // variable name should be camelCase

    @GetMapping("/all")
    public List<ItemsModel> getUnits() {
        return itemsServices.getunit();  
    }
}
