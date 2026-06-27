package com.JIMS.integration.interfaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.Asset_Maker;
import com.JIMS.integration.repository.Asset_Makerinter;

import java.util.*;

@Service
public class Asset_Maker_Service {

    @Autowired
    private Asset_Makerinter assetMakerInter;

    // Insert or update a Maker
    public Asset_Maker insertOrUpdateMaker(Asset_Maker assetMaker) {
        return assetMakerInter.save(assetMaker);
    }

    // Get one Maker by ID
    public Optional<Asset_Maker> getOneMaker(Integer makeId) {
        return assetMakerInter.findById(makeId);
    }

    // Get all Makers
    public List<Asset_Maker> getAllMakers() {
        return assetMakerInter.findAll();
    }

    // Optional: find by description
    public List<Asset_Maker> findByMakeDescription(String makeDescription) {
        return assetMakerInter.findByMakeDescription(makeDescription);
    }
}