package com.baller.infrastructure.mapper;

import com.baller.domain.model.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {

    @Insert("INSERT INTO MEMBERS (EMAIL,PASSWORD,NAME,PHONE_NUMBER,ROLE) VALUES (#{email}, #{password}, #{name}, #{phoneNumber}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void signUp(Member member);

    @Select("SELECT EXISTS (SELECT 1 FROM MEMBERS WHERE EMAIL = #{email})")
    boolean existsByEmail(String email);

    @Select("SELECT ID, EMAIL, PASSWORD, NAME, PHONE_NUMBER, ROLE FROM MEMBERS WHERE EMAIL = #{email}")
    Optional<Member> findByEmail(String email);

    @Select({
            "<script>",
            "SELECT ID, EMAIL, NAME, PHONE_NUMBER FROM MEMBERS WHERE ID IN",
            "<foreach collection='list' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    List<Member> findByIds(@Param("list") List<Long> ids);

    @Update("UPDATE MEMBERS SET PASSWORD = #{password}, NAME = #{name}, PHONE_NUMBER = #{phoneNumber} WHERE ID = #{id}")
    void updateById(Member member);

    @Delete("DELETE FROM MEMBERS WHERE ID = #{id}")
    void deleteById(Long id);

}
