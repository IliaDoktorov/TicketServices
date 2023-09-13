package com.stm.services;

import com.stm.DAO.RefreshTokenDAO;
import com.stm.models.RefreshToken;
import com.stm.util.RefreshTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenDAO refreshTokenDAO;

    @Value("${properties.refreshTokenDurationS}")
    private int refreshTokenDurationS;

    @Autowired
    public RefreshTokenService(RefreshTokenDAO refreshTokenDAO) {
        this.refreshTokenDAO = refreshTokenDAO;
    }

    @Transactional
    public RefreshToken createRefreshToken(int userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUserId(userId);
        refreshToken.setExpiryDate(Date.from(Instant.now().plusSeconds(refreshTokenDurationS)));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshTokenDAO.create(refreshToken);

        return refreshToken;
    }

    @Transactional
    public RefreshToken checkExpiration(RefreshToken refreshToken){
        if(refreshToken.getExpiryDate().before(Date.from(Instant.now()))) {
            refreshTokenDAO.delete(refreshToken);
            throw new RefreshTokenException("Refresh token expired, please login again to get new one");
        }
        return refreshToken;
    }

    @Transactional
    public Optional<RefreshToken> getByUserId(int userId){
        return refreshTokenDAO.getByUserId(userId);
    }

    @Transactional
    public void delete(RefreshToken refreshToken){
        refreshTokenDAO.delete(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String refreshToken) {
        return refreshTokenDAO.findByToken(refreshToken);
    }
}
