package com.baller.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameStatusType {
    SCHEDULED("진행 예정"),
    IN_PROGRESS("진행 중"),
    ENDED("종료");

    private final String value;
}
