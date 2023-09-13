package com.stm.DAO;


import com.stm.models.Transporter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TransporterMapper implements RowMapper<Transporter> {
    @Override
    public Transporter mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transporter transporter = new Transporter();
        transporter.setId(rs.getInt("id"));
        transporter.setTitle(rs.getString("title"));
        transporter.setPhoneNumber(rs.getString("phone_number"));
        return transporter;
    }
}
