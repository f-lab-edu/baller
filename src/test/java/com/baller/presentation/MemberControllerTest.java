package com.baller.presentation;

import com.baller.application.service.MemberService;
import com.baller.presentation.dto.request.member.LoginRequest;
import com.baller.presentation.dto.request.member.SignUpRequest;
import com.baller.presentation.dto.request.member.UpdateMemberRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    @DisplayName("로그인 성공")
    void loginSuccessControllerTest() throws Exception {
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

    @Test
    @DisplayName("로그인 실패")
    void loginFailControllerTest() throws Exception {
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
                .password("Flab@12345")
                .build();

        // expected
        mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("내 정보 조회")
    void findMemberControllerTest() throws Exception {
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

        MvcResult result = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        // expected
        mockMvc.perform(get("/api/members/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@flab.com"))
                .andDo(print());

    }

    @Test
    @DisplayName("내 정보 수정")
    void updateMemberControllerTest() throws Exception {
        // given
        //회원가입
        SignUpRequest signup = SignUpRequest.builder()
                .email("test@flab.com")
                .password("Flab@1234")
                .name("테스트")
                .phoneNumber("010-2222-3333")
                .build();

        memberService.signUp(signup);

        //로그인
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult result = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //내 정보 수정
        UpdateMemberRequest updateMemberRequest = UpdateMemberRequest.builder()
                .password("Flab@12345")
                .name("테스트둘")
                .phoneNumber("010-3333-4444")
                .build();

        //accessToken 추출
        String responseJson = result.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        // expected
        mockMvc.perform(patch("/api/members/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMemberRequest))
                ).andExpect(status().isOk())
                .andDo(print());

        // then - 수정된 정보 조회 및 검증
        mockMvc.perform(get("/api/members/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("테스트둘"))
                .andExpect(jsonPath("$.phoneNumber").value("010-3333-4444"))
                .andDo(print());

    }

}
