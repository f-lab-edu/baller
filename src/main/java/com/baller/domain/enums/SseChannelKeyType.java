package com.baller.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SseChannelKeyType {

    GAME_RECORD("game:%d:record");

    private final String format;

    public String of(Long id) {
        return String.format(format, id);
    }

}
