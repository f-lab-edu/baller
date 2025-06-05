package com.baller.domain.model;

import com.baller.domain.enums.GameStatusType;
import com.baller.presentation.dto.request.game.CreateGameRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    private Long id;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long hostClubId;
    private Long guestClubId;
    private int hostScore;
    private int guestScore;
    private GameStatusType status;

    public static Game ofGameScheduled(CreateGameRequest request) {
        return Game.builder()
                .title(request.getTitle())
                .startTime(request.getStartTime())
                .hostClubId(request.getHostClubId())
                .guestClubId(request.getGuestClubId())
                .hostScore(0)
                .guestScore(0)
                .status(GameStatusType.SCHEDULED)
                .build();
    }

}
