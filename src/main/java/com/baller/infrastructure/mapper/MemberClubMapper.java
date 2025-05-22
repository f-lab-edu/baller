package com.baller.infrastructure.mapper;

import com.baller.domain.model.MemberClub;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface MemberClubMapper {

    @Insert("INSERT INTO MEMBER_CLUBS (MEMBER_ID, CLUB_ID, MEMBER_ROLE, STATUS) VALUES (#{memberId}, #{clubId}, #{memberRole}, #{status})")
    void createMemberClub(MemberClub memberClub);

    @Select("SELECT ID, MEMBER_ID, CLUB_ID, MEMBER_ROLE, STATUS FROM MEMBER_CLUBS WHERE MEMBER_ID = #{memberId} AND CLUB_ID = #{clubId}")
    Optional<MemberClub> findByMemberIdAndClubId(Long memberId, Long clubId);

    @Select("SELECT EXISTS (SELECT 1 FROM MEMBER_CLUBS WHERE MEMBER_ID = #{memberId} AND CLUB_ID = #{clubId})")
    boolean existsByMemberIdAndClubId(Long memberId, Long clubId);
}
