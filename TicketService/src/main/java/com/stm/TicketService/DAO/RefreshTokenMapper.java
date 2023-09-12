package com.stm.TicketService.DAO;

import com.stm.TicketService.models.RefreshToken;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RefreshTokenMapper implements RowMapper<RefreshToken> {
    @Override
    public RefreshToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setId(rs.getInt("id"));
        refreshToken.setToken(rs.getString("token"));
        refreshToken.setExpiryDate(rs.getTimestamp("expiry_date"));
        refreshToken.setUserId(rs.getInt("user_id"));

        return refreshToken;
    }
}
