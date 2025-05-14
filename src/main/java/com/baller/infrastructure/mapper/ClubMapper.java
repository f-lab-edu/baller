package com.baller.infrastructure.mapper;

import com.baller.domain.model.Club;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface ClubMapper {

    @Insert("INSERT INTO CLUBS (NAME, SPORT_TYPE, DESCRIPTION) VALUES (#{name}, #{sportType}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createClub(Club club);

}
