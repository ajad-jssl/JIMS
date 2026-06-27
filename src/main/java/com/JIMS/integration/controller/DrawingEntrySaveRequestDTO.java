package com.JIMS.integration.controller;

import java.util.List;

import com.JIMS.integration.entity.Drawing_Entry_Entity;

public class DrawingEntrySaveRequestDTO {

	private List<Drawing_Entry_Entity> saveOrUpdateList;
    private List<Integer> deleteIds;

    public List<Drawing_Entry_Entity> getSaveOrUpdateList() {
        return saveOrUpdateList;
    }

    public void setSaveOrUpdateList(List<Drawing_Entry_Entity> saveOrUpdateList) {
        this.saveOrUpdateList = saveOrUpdateList;
    }

    public List<Integer> getDeleteIds() {
        return deleteIds;
    }

    public void setDeleteIds(List<Integer> deleteIds) {
        this.deleteIds = deleteIds;
    }
}
