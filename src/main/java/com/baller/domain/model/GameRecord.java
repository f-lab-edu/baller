package com.baller.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRecord {

    private Long id;
    private Long memberId;
    private Long gameId;
    private Long clubId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
