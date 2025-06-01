package com.baller.infrastructure.mapper;

import com.baller.domain.enums.ClubMemberStatusType;
import com.baller.domain.enums.ClubRoleType;
import com.baller.domain.model.MemberClub;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Optional;

@Mapper
public interface MemberClubMapper {

    @Insert("INSERT INTO MEMBER_CLUBS (MEMBER_ID, CLUB_ID, MEMBER_ROLE, STATUS) VALUES (#{memberId}, #{clubId}, #{memberRole}, #{status})")
    void createMemberClub(MemberClub memberClub);

    @Select("SELECT ID, MEMBER_ID, CLUB_ID, MEMBER_ROLE, STATUS FROM MEMBER_CLUBS WHERE MEMBER_ID = #{memberId} AND CLUB_ID = #{clubId}")
    Optional<MemberClub> findByMemberIdAndClubId(Long memberId, Long clubId);

    @Select("SELECT EXISTS (SELECT 1 FROM MEMBER_CLUBS WHERE MEMBER_ID = #{memberId} AND CLUB_ID = #{clubId})")
    boolean existsByMemberIdAndClubId(Long memberId, Long clubId);

    @Update("UPDATE MEMBER_CLUBS SET STATUS = #{memberStatusType} WHERE MEMBER_ID = #{memberId} AND CLUB_ID = #{clubId}")
    void withdrawMemberClub(Long memberId, Long clubId, ClubMemberStatusType memberStatusType);

    @Update("UPDATE MEMBER_CLUBS SET MEMBER_ROLE = #{roleType} WHERE CLUB_ID = #{clubId} AND MEMBER_ID = #{memberId}")
    void updateMemberClubRole(Long clubId, Long memberId, ClubRoleType roleType);

}
