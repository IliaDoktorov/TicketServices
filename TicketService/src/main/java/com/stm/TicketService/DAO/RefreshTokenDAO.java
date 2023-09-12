package com.stm.TicketService.DAO;

import com.stm.TicketService.models.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RefreshTokenDAO {
    private final JdbcTemplate jdbcTemplate;
    private final RefreshTokenMapper refreshTokenMapper;

    @Autowired
    public RefreshTokenDAO(JdbcTemplate jdbcTemplate, RefreshTokenMapper refreshTokenMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    public void create(RefreshToken refreshToken) {
        jdbcTemplate.update("insert into token(token, expiry_date, user_id) values(?, ?, ?)",
                refreshToken.getToken(),
                refreshToken.getExpiryDate(),
                refreshToken.getUserId());
    }

    public void delete(RefreshToken refreshToken) {
        jdbcTemplate.update("delete from token where id=?", refreshToken.getId());
    }

    public Optional<RefreshToken> getByUserId(int userId){
        return jdbcTemplate.query("select * from token where user_id=?",refreshTokenMapper, userId).stream().findFirst();
    }

    public Optional<RefreshToken> findByToken(String token) {
        return jdbcTemplate.query("select * from token where token=?", refreshTokenMapper, token).stream().findFirst();
    }
}
