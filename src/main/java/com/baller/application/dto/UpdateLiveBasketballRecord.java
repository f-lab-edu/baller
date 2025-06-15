package com.baller.application.dto;

import com.baller.presentation.dto.request.game.UpdateBasketballRecordRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLiveBasketballRecord {

    private Long memberId;
    private int points;
    private int assists;
    private int rebounds;
    private int steals;
    private int blocks;
    private int playTime;
    private int fouls;

    public static UpdateLiveBasketballRecord from(Long memberId, UpdateBasketballRecordRequest request) {
        return UpdateLiveBasketballRecord.builder()
                .memberId(memberId)
                .points(request.getPoints())
                .assists(request.getAssists())
                .rebounds(request.getRebounds())
                .steals(request.getSteals())
                .blocks(request.getBlocks())
                .playTime(request.getPlayTime())
                .fouls(request.getFouls())
                .build();
    }

}
