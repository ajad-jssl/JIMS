package com.JIMS.integration.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Pages")
public class Pagesmodel {
   
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int PageID;
    private int ModuleID;
    private String PageName;
    
    
    private String DisplayName;
    
    
    private Integer STATUS;

    @Column(name="CREATED_BY")
    private String created_by;

    private LocalDateTime CREATED_DATE;
    
    @Column(name="MODIFIED_BY")
    private String modified_by;

    private LocalDateTime MODIFIED_DATE;
    
  
    
    
    
    
    
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getModified_by() {
		return modified_by;
	}
	public void setModified_by(String modified_by) {
		this.modified_by = modified_by;
	}
	public Integer getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(Integer sTATUS) {
		STATUS = sTATUS;
	}
	
	public LocalDateTime getCREATED_DATE() {
		return CREATED_DATE;
	}
	public void setCREATED_DATE(LocalDateTime cREATED_DATE) {
		CREATED_DATE = cREATED_DATE;
	}
	
	public LocalDateTime getMODIFIED_DATE() {
		return MODIFIED_DATE;
	}
	public void setMODIFIED_DATE(LocalDateTime mODIFIED_DATE) {
		MODIFIED_DATE = mODIFIED_DATE;
	}
	public String getDisplayName() {
		return DisplayName;
	}
	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}
	public int getPageID() {
		return PageID;
	}
	public void setPageID(int pageID) {
		PageID = pageID;
	}
	public int getModuleID() {
		return ModuleID;
	}
	public void setModuleID(int moduleID) {
		ModuleID = moduleID;
	}
	public String getPageName() {
		return PageName;
	}
	public void setPageName(String pageName) {
		PageName = pageName;
	}
    
}
