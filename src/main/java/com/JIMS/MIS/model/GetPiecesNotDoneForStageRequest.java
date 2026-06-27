package com.JIMS.MIS.model;

public class GetPiecesNotDoneForStageRequest {
    private Integer piecesId;
    private Integer loadId;
    private Integer pieceStagesId;

    // Getters and Setters
    public Integer getPiecesId() {
        return piecesId;
    }

    public void setPiecesId(Integer piecesId) {
        this.piecesId = piecesId;
    }

    public Integer getLoadId() {
        return loadId;
    }

    public void setLoadId(Integer loadId) {
        this.loadId = loadId;
    }

    public Integer getPieceStagesId() {
        return pieceStagesId;
    }

    public void setPieceStagesId(Integer pieceStagesId) {
        this.pieceStagesId = pieceStagesId;
    }
}

