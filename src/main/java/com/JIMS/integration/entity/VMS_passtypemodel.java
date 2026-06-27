package com.JIMS.integration.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "VMS_Pass_type")
public class VMS_passtypemodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("passId")
    private int Pass_id;

    @Column(length = 100, nullable = false)
    @JsonProperty("passName")
    private String Pass_name;

    @JsonProperty("createdBy")
    private Integer Created_by;

    @JsonProperty("createdDate")
    private LocalDateTime Created_date;

    @JsonProperty("modifiedBy")
    private Integer Modified_by;

    @JsonProperty("modifiedDate")
    private LocalDateTime Modified_date;

    public int getPassId() {
        return Pass_id;
    }

    public void setPassId(int passId) {
        this.Pass_id = passId;
    }

    public String getPassName() {
        return Pass_name;
    }

    public void setPassName(String passName) {
        this.Pass_name = passName;
    }

    public Integer getCreatedBy() {
        return Created_by;
    }

    public void setCreatedBy(Integer createdBy) {
        this.Created_by = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return Created_date;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.Created_date = createdDate;
    }

    public Integer getModifiedBy() {
        return Modified_by;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.Modified_by = modifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return Modified_date;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.Modified_date = modifiedDate;
    }
}
