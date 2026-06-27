package com.JIMS.integration.controller;

import com.JIMS.integration.entity.Pagesmodel;
import com.JIMS.integration.repository.pageinterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class userpagecontroller {

    @Autowired
    private pageinterface pageRepository;
    
	  @GetMapping("/ALL") 
	 public List<Pagesmodel> getPages() {
		  return pageRepository.findAll(); 
		  }
	  
    @GetMapping("/pages")
    public List<Pagesmodel> getPages(@RequestParam(value = "ModuleID", required = false) Integer moduleId) {
        if (moduleId != null) {
            return pageRepository.findByModuleId(moduleId);
        }
        return pageRepository.findAll();
    }
}