package com.baller.infrastructure.mapper;

import com.baller.domain.model.MemberClub;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberClubMapper {

    @Insert("INSERT INTO MEMBER_CLUBS (MEMBER_ID, CLUB_ID, MEMBER_ROLE, STATUS) VALUES (#{memberId}, #{clubId}, #{memberRole}, #{status})")
    void createMemberClub(MemberClub memberClub);
}
