package com.baller.presentation.dto.request.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameRecordResponse {

    String hostClubName;
    String guestClubName;
    int hostTotalScore;
    int guestTotalScore;
    private LocalDateTime startTime;
    private String status;

    public static GameRecordResponse of(String hostClubName, String guestClubName, int hostTotalScore, int guestTotalScore, LocalDateTime startTime, String status) {
        return GameRecordResponse.builder()
                .hostClubName(hostClubName)
                .guestClubName(guestClubName)
                .hostTotalScore(hostTotalScore)
                .guestTotalScore(guestTotalScore)
                .startTime(startTime)
                .status(status)
                .build();
    }

}
