package com.JIMS.integration.controller;

public class PageWithModuleDTO {
    private String pageUrl;
    private String moduleName;
    private String  moduleId;
    
    private String DisplayName;
    public PageWithModuleDTO(String pageUrl, String moduleName, String moduleId,String DisplayName) {
        this.pageUrl = pageUrl;
        this.moduleName = moduleName;
        this.moduleId = moduleId;
        this.DisplayName = DisplayName;
    }
    
    

    public String getDisplayName() {
		return DisplayName;
	}



	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}



	public String getPageUrl() {
        return pageUrl;
    }

    public String getModuleName() {
        return moduleName;
    }
    
    public String getModuleId() {
        return moduleId;
    }
    
    
}
