package com.baller.infrastructure.mapper;

import com.baller.domain.model.ClubJoinApply;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ClubJoinApplyMapper {

    @Select("SELECT EXISTS (SELECT 1 FROM CLUB_JOIN_APPLYS WHERE MEMBER_ID = #{memberId} AND CLUB_ID = #{clubId})")
    boolean existsByMemberIdAndClubId(Long memberId, Long clubId);

    @Insert("INSERT INTO CLUB_JOIN_APPLYS (MEMBER_ID, CLUB_ID, STATUS, CREATED_AT) VALUES (#{memberId}, #{clubId}, #{status}, #{createdAt})")
    void createClubJoinApply(ClubJoinApply clubJoinApply);

    @Select("SELECT ID, MEMBER_ID, CLUB_ID, STATUS, REASON, CREATED_AT, HANDLED_AT, HANDLED_BY FROM CLUB_JOIN_APPLYS WHERE ID = #{applyId}")
    ClubJoinApply findByApplyId(Long applyId);

    @Select("SELECT ID, MEMBER_ID, CLUB_ID, STATUS, REASON, CREATED_AT, HANDLED_AT, HANDLED_BY FROM CLUB_JOIN_APPLYS WHERE CLUB_ID=#{clubId}")
    List<ClubJoinApply> findByClubId(Long clubId);

    @Update("UPDATE CLUB_JOIN_APPLYS SET STATUS=#{status}, REASON=#{reason}, HANDLED_AT=#{handledAt}, HANDLED_BY=#{handledBy} WHERE ID=#{id}")
    void updateApplyClub(ClubJoinApply clubJoinApply);

}
