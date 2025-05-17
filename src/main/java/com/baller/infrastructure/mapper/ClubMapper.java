package com.baller.infrastructure.mapper;

import com.baller.domain.model.Club;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClubMapper {

    @Insert("INSERT INTO CLUBS (NAME, SPORT_TYPE, DESCRIPTION, STATUS) VALUES (#{name}, #{sportType}, #{description}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createClub(Club club);

    @Select("SELECT ID, NAME, SPORT_TYPE, DESCRIPTION FROM CLUBS WHERE STATUS = #{status}")
    List<Club> getAllClubs(String status);

    @Select("SELECT ID, NAME, SPORT_TYPE, DESCRIPTION FROM CLUBS WHERE id = #{id}")
    Club getClubById(Long id);

}
