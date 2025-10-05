package com.baller.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    
    private final StringRedisTemplate redisTemplate;
    
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final String BLACKLIST_PREFIX = "blacklist:";
    
    /**
     * Refresh Token을 Redis에 저장
     */
    public void saveRefreshToken(String username, String refreshToken, long expirationSeconds) {
        String key = REFRESH_TOKEN_PREFIX + username;
        redisTemplate.opsForValue().set(key, refreshToken, expirationSeconds, TimeUnit.SECONDS);
        log.debug("Refresh token saved for user: {}", username);
    }
    
    /**
     * Refresh Token을 Redis에서 조회
     */
    public String getRefreshToken(String username) {
        String key = REFRESH_TOKEN_PREFIX + username;
        return redisTemplate.opsForValue().get(key);
    }
    
    /**
     * Refresh Token을 Redis에서 삭제
     */
    public void deleteRefreshToken(String username) {
        String key = REFRESH_TOKEN_PREFIX + username;
        redisTemplate.delete(key);
        log.debug("Refresh token deleted for user: {}", username);
    }
    
    /**
     * 토큰이 Blacklist에 있는지 확인
     */
    public boolean isTokenBlacklisted(String token) {
        String tokenHash = hashToken(token);
        String key = BLACKLIST_PREFIX + tokenHash;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    
    /**
     * 토큰을 Blacklist에 추가
     */
    public void addToBlacklist(String token, long expirationSeconds) {
        String tokenHash = hashToken(token);
        String key = BLACKLIST_PREFIX + tokenHash;
        redisTemplate.opsForValue().set(key, "blacklisted", expirationSeconds, TimeUnit.SECONDS);
        log.debug("Token added to blacklist: {}", tokenHash);
    }
    
    /**
     * 토큰을 해시화 (보안을 위해)
     */
    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 algorithm not found", e);
            return token; // fallback to original token
        }
    }
}

