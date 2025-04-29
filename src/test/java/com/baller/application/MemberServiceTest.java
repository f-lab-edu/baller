package com.baller.application;

import com.baller.application.service.MemberService;
import com.baller.common.exception.AlreadyExistsEmailException;
import com.baller.infrastructure.mapper.MemberMapper;
import com.baller.presentation.dto.request.member.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberMapper memberMapper;

    @Test
    @DisplayName("회원가입 성공")
    public void signUpSuccessTest() {

        //given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@flab.com")
                .password("1245678")
                .name("테스트")
                .phoneNumber("01022223333")
                .build();
        //when
        memberService.signUp(request);

        //then
        assertTrue(memberMapper.existsByEmail(request.getEmail()));

    }

    @Test
    @DisplayName("회원가입 시 이메일 중복으로 인한 실패")
    public void signUpFailTest() {

        //given
        SignUpRequest request1 = SignUpRequest.builder()
                .email("test@flab.com")
                .password("1245678")
                .name("테스트")
                .phoneNumber("01022223333")
                .build();

        memberService.signUp(request1);

        SignUpRequest request2 = SignUpRequest.builder()
                .email("test@flab.com")
                .password("87654321")
                .name("에프렙")
                .phoneNumber("01044445555")
                .build();

        // expected
        assertThrows(AlreadyExistsEmailException.class, () -> memberService.signUp(request2));

    }

}
