package com.baller.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SseEventType {

    INIT,
    GAME_UPDATE

}
