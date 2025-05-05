package com.baller.presentation;

import com.baller.application.service.MemberService;
import com.baller.presentation.dto.request.member.LoginRequest;
import com.baller.presentation.dto.request.member.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원가입")
    void signUpControllerTest() throws Exception {
        // given
        SignUpRequest signup = SignUpRequest.builder()
                .email("test@flab.com")
                .password("Flab@1234")
                .name("테스트")
                .phoneNumber("010-2222-3333")
                .build();

        // expected
        mockMvc.perform(post("/api/members")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인")
    void loginControllerTest() throws Exception {
        // given
        SignUpRequest signup = SignUpRequest.builder()
                .email("test@flab.com")
                .password("Flab@1234")
                .name("테스트")
                .phoneNumber("010-2222-3333")
                .build();

        memberService.signUp(signup);

        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@flab.com")
                .password("Flab@1234")
                .build();

        // expected
        mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
