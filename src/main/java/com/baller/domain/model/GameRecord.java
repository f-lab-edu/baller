package com.baller.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class GameRecord {

    private Long id;
    private Long memberId;
    private Long gameId;
    private Long clubId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
