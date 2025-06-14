package com.baller.infrastructure.mapper;

import com.baller.domain.enums.GameStatusType;
import com.baller.domain.model.Game;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GameMapper {

    @Insert("INSERT INTO GAMES (TITLE, START_TIME, HOST_CLUB_ID, GUEST_CLUB_ID, HOST_SCORE, GUEST_SCORE, STATUS, SPORT_TYPE) VALUES(#{title}, #{startTime}, #{hostClubId}, #{guestClubId}, #{hostScore}, #{guestScore}, #{status}, #{sportType})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createGame(Game game);

    @Select("SELECT ID, TITLE, START_TIME, END_TIME, HOST_CLUB_ID, GUEST_CLUB_ID, HOST_SCORE, GUEST_SCORE, STATUS, SPORT_TYPE FROM GAMES WHERE HOST_CLUB_ID = #{clubId} OR GUEST_CLUB_ID = #{clubId}")
    List<Game> getAllGames(Long clubId);

    @Select("SELECT ID, TITLE, START_TIME, END_TIME, HOST_CLUB_ID, GUEST_CLUB_ID, HOST_SCORE, GUEST_SCORE, STATUS, SPORT_TYPE FROM GAMES WHERE ID = #{gameId}")
    Game findById(Long gameId);

    @Update("UPDATE GAMES SET STATUS = #{gameStatus} WHERE ID = #{gameId}")
    void startGame(GameStatusType gameStatus, Long gameId);

}
