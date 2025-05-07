package com.baller.infrastructure.mapper;

import com.baller.domain.model.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    @Insert("INSERT INTO MEMBER (EMAIL,PASSWORD,NAME,PHONE_NUMBER,ROLE) VALUES (#{email}, #{password}, #{name}, #{phoneNumber}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void signUp(Member member);

    @Select("SELECT EXISTS (SELECT 1 FROM MEMBER WHERE EMAIL = #{email})")
    boolean existsByEmail(String email);

    @Select("SELECT ID, EMAIL, PASSWORD, NAME, PHONE_NUMBER, ROLE FROM MEMBER WHERE EMAIL = #{email}")
    Optional<Member> findByEmail(String email);

}
