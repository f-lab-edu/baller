package com.baller.domain.model;

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

}
