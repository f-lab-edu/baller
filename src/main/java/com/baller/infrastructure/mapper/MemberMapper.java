package com.baller.infrastructure.mapper;

import com.baller.domain.model.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper {

    @Insert("INSERT INTO MEMBER (EMAIL,PASSWORD,NAME,PHONE_NUMBER) VALUES (#{email}, #{password}, #{name}, #{phoneNumber})")
    void signUp(Member member);

    @Select("SELECT EXISTS (SELECT 1 FROM MEMBER WHERE EMAIL = #{email})")
    boolean existsByEmail(String email);

}
