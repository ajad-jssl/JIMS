package com.JIMS.integration.entity;

public class AssetSummaryDTO {
	private Integer typeId;
    private String assetType;
    private Long totalAsset;
    private Long allocated;
    private Long stock;
    private Long working;
    private Long notWorking;
    private Long scrap;
    private Long repair;
    private Long others;
    private Long totalStatus;

    public AssetSummaryDTO(Integer typeId, String assetType, Long totalAsset, Long allocated, 
            Long stock, Long working, Long notWorking, Long scrap, 
            Long repair, Long others, Long totalStatus) {
        this.typeId = typeId;
        this.assetType = assetType;
        this.totalAsset = totalAsset;
        this.allocated = allocated;
        this.stock = stock;
        this.working = working;
        this.notWorking = notWorking;
        this.scrap = scrap;
        this.repair = repair;
        this.others = others;
        this.totalStatus = totalStatus;
    }

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public Long getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(Long totalAsset) {
		this.totalAsset = totalAsset;
	}

	public Long getAllocated() {
		return allocated;
	}

	public void setAllocated(Long allocated) {
		this.allocated = allocated;
	}

	public Long getStock() {
		return stock;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	}

	public Long getWorking() {
		return working;
	}

	public void setWorking(Long working) {
		this.working = working;
	}

	public Long getNotWorking() {
		return notWorking;
	}

	public void setNotWorking(Long notWorking) {
		this.notWorking = notWorking;
	}

	public Long getScrap() {
		return scrap;
	}

	public void setScrap(Long scrap) {
		this.scrap = scrap;
	}

	public Long getRepair() {
		return repair;
	}

	public void setRepair(Long repair) {
		this.repair = repair;
	}

	public Long getOthers() {
		return others;
	}

	public void setOthers(Long others) {
		this.others = others;
	}

	public Long getTotalStatus() {
		return totalStatus;
	}

	public void setTotalStatus(Long totalStatus) {
		this.totalStatus = totalStatus;
	}
}
