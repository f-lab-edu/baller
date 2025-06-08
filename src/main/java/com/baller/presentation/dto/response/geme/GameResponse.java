package com.baller.presentation.dto.response.geme;

import com.baller.domain.enums.GameStatusType;
import com.baller.domain.enums.SportType;
import com.baller.domain.model.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {

    private Long id;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long hostClubId;
    private Long guestClubId;
    private int hostScore;
    private int guestScore;
    private GameStatusType status;
    private SportType sportType;

    public static GameResponse from(Game game) {
        return GameResponse.builder()
                .id(game.getId())
                .title(game.getTitle())
                .startTime(game.getStartTime())
                .endTime(game.getEndTime())
                .hostClubId(game.getHostClubId())
                .guestClubId(game.getGuestClubId())
                .hostScore(game.getHostScore())
                .guestScore(game.getGuestScore())
                .status(game.getStatus())
                .sportType(game.getSportType())
                .build();
    }

}
