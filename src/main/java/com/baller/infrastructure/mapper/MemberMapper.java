package com.baller.infrastructure.mapper;

import com.baller.domain.model.Member;
import com.baller.domain.model.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    @Insert("INSERT INTO MEMBER (EMAIL,PASSWORD,NAME,PHONE_NUMBER) VALUES (#{email}, #{password}, #{name}, #{phoneNumber})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void signUp(Member member);

    @Select("SELECT EXISTS (SELECT 1 FROM MEMBER WHERE EMAIL = #{email})")
    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    @Insert("INSERT INTO MEMBER_ROLE (MEMBER_ID, ROLE) VALUES (#{memberId}, #{role})")
    void insertMemberRole(Role role);

}
