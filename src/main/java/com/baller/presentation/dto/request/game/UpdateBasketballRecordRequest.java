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

    private int points = 0;
    private int assists = 0;
    private int rebounds = 0;
    private int steals = 0;
    private int blocks = 0;
    private int playTime = 0;
    private int fouls = 0;

}
