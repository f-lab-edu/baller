package com.baller.domain.model;

import com.baller.presentation.dto.request.game.UpdateBasketballRecordRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BasketballRecord extends GameRecord {

    private Long id;
    private int points = 0;
    private int assists = 0;
    private int rebounds = 0;
    private int steals = 0;
    private int blocks = 0;
    private int playTime = 0;
    private int fouls = 0;

    public static BasketballRecord of(Long id, UpdateBasketballRecordRequest request) {
        return BasketballRecord.builder()
                .id(id)
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
