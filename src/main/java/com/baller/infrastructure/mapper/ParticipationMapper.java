package com.baller.infrastructure.mapper;

import com.baller.domain.model.Participation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ParticipationMapper {

    @Insert("INSERT INTO PARTICIPATIONS (MEMBER_ID, GAME_ID, CLUB_ID) VALUES (#{memberId}, #{gameId}, #{clubId})")
    void insertParticipation(Participation participation);

}
