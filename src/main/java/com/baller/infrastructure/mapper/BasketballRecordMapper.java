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
                SET POINTS = #{points},
                    ASSISTS = #{assists},
                    REBOUNDS = #{rebounds},
                    STEALS = #{steals},
                    BLOCKS = #{blocks},
                    PLAY_TIME = #{playTime},
                    FOULS = #{fouls}
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

}
