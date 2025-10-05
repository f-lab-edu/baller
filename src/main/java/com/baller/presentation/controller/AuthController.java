package com.baller.presentation.controller;

import com.baller.presentation.dto.request.member.RefreshTokenRequest;
import com.baller.presentation.dto.response.ErrorResponse;
import com.baller.presentation.dto.response.member.TokenPair;
import com.baller.security.domain.CustomUserDetails;
import com.baller.security.service.TokenService;
import com.baller.security.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    /**
     * Refresh Token으로 새로운 Access Token과 Refresh Token 발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            
            // Refresh Token 유효성 검증
            if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ErrorResponse.builder()
                                .code(HttpStatus.UNAUTHORIZED.value())
                                .message("유효하지 않은 Refresh Token입니다.")
                                .build());
            }

            String username = jwtTokenProvider.extractUsername(refreshToken);
            
            // Redis에서 Refresh Token 확인
            String storedRefreshToken = tokenService.getRefreshToken(username);
            if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ErrorResponse.builder()
                                .code(HttpStatus.UNAUTHORIZED.value())
                                .message("유효하지 않은 Refresh Token입니다.")
                                .build());
            }

            // 사용자 정보 조회
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
            
            // Refresh Token 유효성 검증
            if (!jwtTokenProvider.isRefreshTokenValid(refreshToken, userDetails)) {
                // 유효하지 않은 Refresh Token 삭제
                tokenService.deleteRefreshToken(username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ErrorResponse.builder()
                                .code(HttpStatus.UNAUTHORIZED.value())
                                .message("만료된 Refresh Token입니다.")
                                .build());
            }

            // 새로운 토큰 생성
            String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

            // 기존 Refresh Token 삭제하고 새로운 Refresh Token 저장
            tokenService.deleteRefreshToken(username);
            long newRefreshTokenExpiration = jwtTokenProvider.getTokenExpiration(newRefreshToken);
            tokenService.saveRefreshToken(username, newRefreshToken, newRefreshTokenExpiration);

            log.info("Token refreshed for user: {}", username);

            return ResponseEntity.ok(TokenPair.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build());

        } catch (Exception e) {
            log.error("Token refresh failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("토큰 갱신 중 오류가 발생했습니다.")
                            .build());
        }
    }

    /**
     * 로그아웃 - Access Token을 Blacklist에 추가
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                // Access Token인지 확인
                if (jwtTokenProvider.isAccessToken(token)) {
                    // 토큰을 Blacklist에 추가
                    long tokenExpiration = jwtTokenProvider.getTokenExpiration(token);
                    if (tokenExpiration > 0) {
                        tokenService.addToBlacklist(token, tokenExpiration);
                    }
                    
                    // Refresh Token도 삭제
                    String username = jwtTokenProvider.extractUsername(token);
                    if (username != null) {
                        tokenService.deleteRefreshToken(username);
                    }
                    
                    log.info("User logged out: {}", username);
                }
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Logout failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("로그아웃 중 오류가 발생했습니다.")
                            .build());
        }
    }
}
