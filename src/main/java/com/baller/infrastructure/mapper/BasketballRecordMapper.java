package com.baller.infrastructure.mapper;

import com.baller.domain.model.BasketballRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface BasketballRecordMapper {

    @Insert("INSERT INTO BASKETBALL_RECORDS (GAME_RECORD_ID, POINTS, ASSISTS, REBOUNDS, STEALS, BLOCKS, PLAY_TIME, FOULS) VALUES (#{gameRecordId}, #{points}, #{assists}, #{rebounds}, #{steals}, #{blocks}, #{playTime}, #{fouls})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertBasketballRecord(BasketballRecord basketballRecord);

}
