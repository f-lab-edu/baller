package com.baller.infrastructure.mapper;

import com.baller.domain.model.ClubApplyRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ClubApplyRequestMapper {

    @Select("SELECT EXISTS (SELECT 1 FROM CLUB_APPLY_REQUESTS WHERE MEMBER_ID = #{memberId} AND CLUB_ID = #{clubId})")
    boolean existsByMemberIdAndClubId(Long memberId, Long clubId);

    @Insert("INSERT INTO CLUB_APPLY_REQUESTS (MEMBER_ID, CLUB_ID, STATUS, CREATED_AT) VALUES (#{memberId}, #{clubId}, #{status}, #{createdAt})")
    void createClubApplyRequest(ClubApplyRequest clubApplyRequest);

    @Select("SELECT ID, MEMBER_ID, CLUB_ID, STATUS, REASON, CREATED_AT, HANDLED_AT, HANDLED_BY FROM CLUB_APPLY_REQUESTS WHERE ID = #{applyId}")
    ClubApplyRequest findByApplyId(Long applyId);

    @Select("SELECT ID, MEMBER_ID, CLUB_ID, STATUS, REASON, CREATED_AT, HANDLED_AT, HANDLED_BY FROM CLUB_APPLY_REQUESTS WHERE CLUB_ID=#{clubId}")
    List<ClubApplyRequest> findByClubId(Long clubId);

    @Update("UPDATE CLUB_APPLY_REQUESTS SET STATUS=#{status}, REASON=#{reason}, HANDLED_AT=#{handledAt}, HANDLED_BY=#{handledBy} WHERE ID=#{id}")
    void updateClubApplyRequest(ClubApplyRequest clubApplyRequest);

}
