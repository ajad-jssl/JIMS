package com.JIMS.MIS.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GetPiecesNotDoneForStageRequestInterfaces {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getPiecesNotDoneForStage(Integer piecesId, Integer loadId, Integer pieceStagesId) {
        String query = "EXEC spDTL_GetPiecesNotDoneForStage ?, ?, ?";
        return jdbcTemplate.queryForList(query, piecesId, loadId, pieceStagesId);
    }
}
