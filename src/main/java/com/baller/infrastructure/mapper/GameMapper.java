package com.baller.infrastructure.mapper;

import com.baller.domain.model.Game;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GameMapper {

    @Insert("INSERT INTO GAMES (TITLE, START_TIME, HOST_CLUB_ID, GUEST_CLUB_ID, HOST_SCORE, GUEST_SCORE, STATUS, SPORT_TYPE) VALUES(#{title}, #{startTime}, #{hostClubId}, #{guestClubId}, #{hostScore}, #{guestScore}, #{status}, #{sportType})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createGame(Game game);

    @Select("SELECT ID, TITLE, START_TIME, END_TIME, HOST_CLUB_ID, GUEST_CLUB_ID, HOST_SCORE, GUEST_SCORE, STATUS, SPORT_TYPE FROM GAMES")
    List<Game> getAllGames();

}
