package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_qc_weld_mst")
public class TbQcWeldMst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer weld_id;

    private String fabricator_ID;
    private String welder_ID;
    private String revision;
    private String position;
    private String process;
    private String status;
    private String created_by;
    private LocalDateTime created_date;
    private String modified_by;
    private LocalDateTime modified_date;

    public Integer getWeld_id() {
        return weld_id;
    }
    public void setWeld_id(Integer weld_id) {
        this.weld_id = weld_id;
    }

    public String getFabricator_ID() {
        return fabricator_ID;
    }
    public void setFabricator_ID(String fabricator_ID) {
        this.fabricator_ID = fabricator_ID;
    }

    public String getWelder_ID() {
        return welder_ID;
    }
    public void setWelder_ID(String welder_ID) {
        this.welder_ID = welder_ID;
    }
    
    public String getRevision() {
        return revision;
    }
    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }

    public String getProcess() {
        return process;
    }
    public void setProcess(String process) {
        this.process = process;
    }
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_by() {
        return created_by;
    }
    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public LocalDateTime getCreated_date() {
        return created_date;
    }
    public void setCreated_date(LocalDateTime created_date) {
        this.created_date = created_date;
    }
    public String getModified_by() {
        return modified_by;
    }
    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public LocalDateTime getModified_date() {
        return modified_date;
    }
    public void setModified_date(LocalDateTime modified_date) {
        this.modified_date = modified_date;
    }
}
