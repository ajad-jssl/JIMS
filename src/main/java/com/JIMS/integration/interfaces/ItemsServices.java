package com.JIMS.integration.interfaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.ItemsModel;
import com.JIMS.integration.repository.ItemsRepository;

import java.util.List;

@Service
public class ItemsServices {

	@Autowired
    private ItemsRepository ItemsRepository;

    // Get all Register Types 
    public List<ItemsModel> getunit() { 
        return ItemsRepository.findunit();     
    }
}
