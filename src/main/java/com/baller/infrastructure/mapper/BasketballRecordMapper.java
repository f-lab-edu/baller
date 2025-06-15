package com.baller.infrastructure.mapper;

import com.baller.domain.model.BasketballRecord;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BasketballRecordMapper {

    @Insert("INSERT INTO BASKETBALL_RECORDS (ID, POINTS, ASSISTS, REBOUNDS, STEALS, BLOCKS, PLAY_TIME, FOULS) VALUES (#{id}, #{points}, #{assists}, #{rebounds}, #{steals}, #{blocks}, #{playTime}, #{fouls})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertBasketballRecord(BasketballRecord basketballRecord);

    @Update("""
            UPDATE BASKETBALL_RECORDS
                SET POINTS = POINTS + #{points},
                    ASSISTS = ASSISTS + #{assists},
                    REBOUNDS = REBOUNDS + #{rebounds},
                    STEALS = STEALS + #{steals},
                    BLOCKS = BLOCKS + #{blocks},
                    PLAY_TIME = PLAY_TIME + #{playTime},
                    FOULS = FOULS + #{fouls}
                WHERE ID = #{id}""")
    void updateBasketballRecord(BasketballRecord basketballRecord);

    @Select("""
            SELECT
                  ID
                , POINTS
                , ASSISTS
                , REBOUNDS
                , STEALS
                , BLOCKS
                , PLAY_TIME
                , FOULS
            FROM BASKETBALL_RECORDS
            WHERE ID=#{id}""")
    BasketballRecord findById(Long id);

    @Select("""
            SELECT SUM(B.POINTS) AS TOTAL_POINTS
            FROM BASKETBALL_RECORDS B
            JOIN GAME_RECORDS G
              ON B.ID = G.ID
            WHERE G.GAME_ID = #{gameId}
              AND G.CLUB_ID = #{clubId}""")
    int sumPoints(Long gameId, Long clubId);

}
