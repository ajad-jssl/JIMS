package com.JIMS.MIS.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class keyvalueInterfaces {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Method to update the keyvalue and return the updated keyvalue
    public Integer updateAndSelectKeyValue() {
        // Update query to increment the keyvalue
        String updateQuery = "UPDATE keyvalues SET keyvalue = keyvalue + 1 WHERE keyname = 'PCEPROGBTH'";

        // Execute the update query
        jdbcTemplate.update(updateQuery);

        // Select query to get the updated keyvalue
        String selectQuery = "SELECT keyvalue FROM keyvalues WHERE keyname = 'PCEPROGBTH'";

        // Execute the select query and retrieve the result
        Map<String, Object> result = jdbcTemplate.queryForMap(selectQuery);

        // Return the updated keyvalue (assumed to be an Integer)
        return (Integer) result.get("keyvalue");
    }
}
