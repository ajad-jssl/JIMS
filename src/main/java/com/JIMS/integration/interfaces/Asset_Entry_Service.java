package com.JIMS.integration.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.AssetSummaryDTO;
import com.JIMS.integration.entity.Asset_Entry;
import com.JIMS.integration.repository.Asset_Entryinter;



@Service
public class Asset_Entry_Service {
	
	
	@Autowired
private	Asset_Entryinter asset_entry_inter;

public Asset_Entry insertIntoasserentry(Asset_Entry asset_entry) {
	return asset_entry_inter.save(asset_entry);
}

public Optional<Asset_Entry> getAssetEntry(Integer asset_entry_id) {
	return asset_entry_inter.findById(asset_entry_id);
}
	

public List<Asset_Entry> getAllAssetEntry(){
	return asset_entry_inter.findAllByOrderByAssetenIdDesc();
}

public Asset_Entry updateAssetEntry(Asset_Entry asset_entry) {
	return asset_entry_inter.save(asset_entry);
}

 

public Boolean getAssetType(Integer asset_type_id) {
	return asset_entry_inter.existsByTypeId(asset_type_id);
}

public Boolean getAssetMode(Integer asset_model_id) {
	return asset_entry_inter.existsByModelId(asset_model_id);
}


public Boolean getAssetStatus(Integer asset_status) {
	return asset_entry_inter.existsByStatusId(asset_status);
}

public Boolean getAssetOnwed(Integer asset_ownedBy) {
	return asset_entry_inter.existsByAssertorId(asset_ownedBy);
}

public Boolean getAssetMaker(Integer asset_make) {
	return asset_entry_inter.existsByMakeId(asset_make);
}


public Boolean duplicationAsset(String assetsr) {
	return asset_entry_inter.existsByAssetSrNoIgnoreCase(assetsr);
}


public Optional<Asset_Entry> duplcationAssetSr(String assetsr){
	return asset_entry_inter.findByAssetSrNoIgnoreCase(assetsr);
}

public List<Asset_Entry> getAvailableAssets() {
	return asset_entry_inter.getAvailableAssets();
}





  public List<AssetSummaryDTO> getAssetSummary() {
        List<Object[]> rows = asset_entry_inter.getAssetSummary();
        List<AssetSummaryDTO> result = new ArrayList<>();

        for(Object[] row : rows) {
            result.add(new AssetSummaryDTO(
                    (Integer) row[0], (String) row[1],
                    ((Number) row[2]).longValue(),
                    ((Number) row[3]).longValue(),
                    ((Number) row[4]).longValue(),
                    ((Number) row[5]).longValue(),
                    ((Number) row[6]).longValue(),
                    ((Number) row[7]).longValue(),
                    ((Number) row[8]).longValue(),
                    ((Number) row[9]).longValue(),
                    ((Number) row[10]).longValue()
            ));
        }
        return result;
    }


}
