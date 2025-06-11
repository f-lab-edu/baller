package com.baller.infrastructure.mapper;

import com.baller.domain.model.GameRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface GameRecordMapper {

    @Insert("INSERT INTO GAME_RECORDS (GAME_ID, MEMBER_ID, CLUB_ID, CREATED_AT) VALUES (#{gameId}, #{memberId}, #{clubId}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertGameRecord(GameRecord gameRecord);

}
