package com.baller.presentation.dto.response.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenPair {
    private final String accessToken;
    private final String refreshToken;
}

