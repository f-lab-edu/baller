package com.baller.infrastructure.mapper;

import com.baller.domain.model.Club;
import org.apache.ibatis.annotations.*;

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

    @Update("UPDATE CLUBS SET NAME=#{name}, SPORT_TYPE=#{sportType}, DESCRIPTION=#{description} WHERE ID = #{id}")
    void updateClub(Club club);

    @Update("UPDATE CLUBS SET STATUS = #{status} WHERE ID = #{id}")
    void deleteClub(Long id, String status);

    @Select("SELECT EXISTS (SELECT 1 FROM CLUBS WHERE ID = #{id})")
    boolean existsByClubId(Long id);

    @Select("SELECT NAME FROM CLUBS WHERE ID = #{id}")
    String findNameById(Long id);

}
