package com.baller.infrastructure.mapper;

import com.baller.domain.model.ClubJoinApply;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClubJoinApplyMapper {

    @Select("SELECT EXISTS (SELECT 1 FROM CLUB_JOIN_APPLYS WHERE MEMBER_ID = #{memberId} AND CLUB_ID = #{clubId})")
    boolean existsByMemberIdAndClubId(Long memberId, Long clubId);

    @Insert("INSERT INTO CLUB_JOIN_APPLYS (MEMBER_ID, CLUB_ID, STATUS, CREATED_AT) VALUES (#{memberId}, #{clubId}, #{status}, #{createdAt})")
    void createClubJoinApply(ClubJoinApply clubJoinApply);

}
