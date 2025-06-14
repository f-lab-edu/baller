package com.baller.infrastructure.mapper;

import com.baller.domain.model.GameRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GameRecordMapper {

    @Insert("INSERT INTO GAME_RECORDS (GAME_ID, MEMBER_ID, CLUB_ID, CREATED_AT) VALUES (#{gameId}, #{memberId}, #{clubId}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertGameRecord(GameRecord gameRecord);

    @Select("SELECT ID FROM GAME_RECORDS WHERE GAME_ID = #{gameId} AND MEMBER_ID = #{memberId}")
    Long findByGameIdAndMemberId(Long gameId, Long memberId);

}
