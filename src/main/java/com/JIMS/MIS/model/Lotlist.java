package com.JIMS.MIS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "Tra_loads")  // This should be the table name where lot information is stored
public class Lotlist {

    @Id
    @Column(name = "tload_id")  // Assuming 'tload_id' is the unique identifier for the lot
    private int tloadId;

    @Column(name = "loadno")
    private String loadno;

    @Column(name = "descr")
    private String description;

    @Column(name = "contract_id")
    private int contractId;

    @Column(name = "trailerref")
    private String trailerRef;

    @Column(name = "trackingref")
    private String trackingRef;

    @Column(name = "created")
    private String createdDate;

    @Column(name = "pieceweight")
    private double pieceWeight;

    @Column(name = "otherweight")
    private double otherWeight;

    @Column(name = "tweight")
    private double totalWeight;

    @Column(name = "wb_weight")
    private double wbWeight;

    @Column(name = "exciseinvoice")
    private String exciseInvoice;

    @Column(name = "status")
    private String status;

    @Column(name = "fabloads")
    private String fabLoads;

    @Column(name = "factory_id")
    private int factoryId;

    // Getters and setters
    public int getTloadId() {
        return tloadId;
    }

    public void setTloadId(int tloadId) {
        this.tloadId = tloadId;
    }

    public String getLoadno() {
        return loadno;
    }

    public void setLoadno(String loadno) {
        this.loadno = loadno;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public String getTrailerRef() {
        return trailerRef;
    }

    public void setTrailerRef(String trailerRef) {
        this.trailerRef = trailerRef;
    }

    public String getTrackingRef() {
        return trackingRef;
    }

    public void setTrackingRef(String trackingRef) {
        this.trackingRef = trackingRef;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public double getPieceWeight() {
        return pieceWeight;
    }

    public void setPieceWeight(double pieceWeight) {
        this.pieceWeight = pieceWeight;
    }

    public double getOtherWeight() {
        return otherWeight;
    }

    public void setOtherWeight(double otherWeight) {
        this.otherWeight = otherWeight;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public double getWbWeight() {
        return wbWeight;
    }

    public void setWbWeight(double wbWeight) {
        this.wbWeight = wbWeight;
    }

    public String getExciseInvoice() {
        return exciseInvoice;
    }

    public void setExciseInvoice(String exciseInvoice) {
        this.exciseInvoice = exciseInvoice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFabLoads() {
        return fabLoads;
    }

    public void setFabLoads(String fabLoads) {
        this.fabLoads = fabLoads;
    }

    public int getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(int factoryId) {
        this.factoryId = factoryId;
    }

    @Override
    public String toString() {
        return "Lotlist{" +
                "tloadId=" + tloadId +
                ", loadno='" + loadno + '\'' +
                ", description='" + description + '\'' +
                ", contractId=" + contractId +
                ", trailerRef='" + trailerRef + '\'' +
                ", trackingRef='" + trackingRef + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", pieceWeight=" + pieceWeight +
                ", otherWeight=" + otherWeight +
                ", totalWeight=" + totalWeight +
                ", wbWeight=" + wbWeight +
                ", exciseInvoice='" + exciseInvoice + '\'' +
                ", status='" + status + '\'' +
                ", fabLoads=" + fabLoads +
                ", factoryId=" + factoryId +
                '}';
    }
}
