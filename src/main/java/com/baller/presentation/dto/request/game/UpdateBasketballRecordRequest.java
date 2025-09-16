package com.baller.presentation.dto.request.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBasketballRecordRequest {

    private int points;
    private int assists;
    private int rebounds;
    private int steals;
    private int blocks;
    private int playTime;
    private int fouls;

}
