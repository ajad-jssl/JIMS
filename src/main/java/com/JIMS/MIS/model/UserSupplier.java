package com.JIMS.MIS.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usersupplier")
public class UserSupplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // assuming auto-increment primary key

    @Column(name = "uid")
    private Long uid;

    @Column(name = "supid")
    private Long supid;

    @Column(name = "euid")
    private String euid;

    @Column(name = "edt")
    private LocalDateTime edt;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }
    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getSupid() {
        return supid;
    }
    public void setSupid(Long supid) {
        this.supid = supid;
    }

    public String getEuid() {
        return euid;
    }
    public void setEuid(String euid) {
        this.euid = euid;
    }

    public LocalDateTime getEdt() {
        return edt;
    }
    public void setEdt(LocalDateTime edt) {
        this.edt = edt;
    }
}
