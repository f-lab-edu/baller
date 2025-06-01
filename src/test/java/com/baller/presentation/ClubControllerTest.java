package com.baller.presentation;

import com.baller.domain.enums.ClubApplyType;
import com.baller.domain.enums.ClubRoleType;
import com.baller.domain.enums.SportType;
import com.baller.domain.model.Club;
import com.baller.domain.model.ClubApplyRequest;
import com.baller.infrastructure.mapper.ClubApplyRequestMapper;
import com.baller.infrastructure.mapper.ClubMapper;
import com.baller.presentation.dto.request.club.CreateClubRequest;
import com.baller.presentation.dto.request.club.RejectClubApplyRequest;
import com.baller.presentation.dto.request.club.UpdateClubRequest;
import com.baller.presentation.dto.request.club.UpdateMemberClubRoleRequest;
import com.baller.presentation.dto.request.member.LoginRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ClubControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private ClubApplyRequestMapper clubApplyRequestMapper;

    @Test
    @DisplayName("동아리 생성")
    void createClubControllerTest() throws Exception {

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

        CreateClubRequest request = CreateClubRequest.builder()
                .name("농동")
                .sportType(SportType.BASKETBALL)
                .description("농구 동아리입니다.")
                .build();

        // expected
        mockMvc.perform(post("/api/clubs")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("동아리 목록 조회")
    void getAllClubsControllerTest() throws Exception {

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
        mockMvc.perform(get("/api/clubs")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("동아리 상세 조회")
    void getClubByIdControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        Club club = Club.builder()
                .name("농동")
                .sportType(SportType.BASKETBALL)
                .description("농구 동아리입니다.")
                .build();

        clubMapper.createClub(club);

        // expected
        mockMvc.perform(get("/api/clubs/"+club.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("농동"))
                .andExpect(jsonPath("$.sportType").value(SportType.BASKETBALL.toString()))
                .andDo(print());

    }

    @Test
    @DisplayName("동아리 상세 조회 실패")
    void getClubByIdFailControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        Club club = Club.builder()
                .name("농동")
                .sportType(SportType.BASKETBALL)
                .description("농구 동아리입니다.")
                .build();

        clubMapper.createClub(club);

        // expected
        mockMvc.perform(get("/api/clubs/"+999)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    @DisplayName("동아리 정보 수정")
    void updateClubControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("club@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        UpdateClubRequest request = UpdateClubRequest.builder()
                .name("농동2")
                .sportType(SportType.BASKETBALL)
                .description("농구동아리 수정")
                .build();

        // expected
        mockMvc.perform(patch("/api/clubs/"+6)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(get("/api/clubs/"+6)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("농동2"))
                .andExpect(jsonPath("$.description").value("농구동아리 수정"))
                .andDo(print());

    }

    @Test
    @DisplayName("동아리 정보 수정 실패")
    void updateClubFailControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        UpdateClubRequest request = UpdateClubRequest.builder()
                .name("농동2")
                .sportType(SportType.BASKETBALL)
                .description("농구동아리 수정")
                .build();

        // expected
        mockMvc.perform(patch("/api/clubs/"+6)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());

    }

    @Test
    @DisplayName("동아리 삭제")
    void deleteClubControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("club@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        // expected
        mockMvc.perform(delete("/api/clubs/"+6)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("동아리 삭제 실패")
    void deleteClubFailControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        // expected
        mockMvc.perform(delete("/api/clubs/"+6)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isUnauthorized())
                .andDo(print());

    }

    @Test
    @DisplayName("동아리 가입 신청")
    void joinClubControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        // expected
        mockMvc.perform(post("/api/clubs/"+1+"/apply")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("동아리 가입 신청 실패 - 없는 동아리")
    void joinClubFail1ControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        // expected
        mockMvc.perform(post("/api/clubs/"+99+"/apply")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    @DisplayName("동아리 가입 신청 실패 - 이미 가입한 동아리")
    void joinClubFail2ControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        // expected
        mockMvc.perform(post("/api/clubs/"+6+"/apply")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    @DisplayName("동아리 가입 신청 실패 - 이미 신청한 동아리")
    void joinClubFail3ControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        // expected
        mockMvc.perform(post("/api/clubs/"+1+"/apply")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    @DisplayName("동아리 가입 신청 회원 조회")
    void getClubApplyControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("baltol@naver.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        // expected
        mockMvc.perform(get("/api/clubs/"+1+"/apply")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("동아리 가입 신청 회원 승인")
    void approveClubApplyControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("baltol@naver.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        // expected
        mockMvc.perform(post("/api/clubs/"+1+"/apply/"+2+"/approve")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print());

        ClubApplyRequest apply = clubApplyRequestMapper.findByRequestId(2L);
        assertNotNull(apply);
        assertEquals(ClubApplyType.APPROVED, apply.getStatus());

    }

    @Test
    @DisplayName("동아리 가입 신청 회원 거절")
    void rejectClubApplyControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("baltol@naver.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        // expected
        mockMvc.perform(patch("/api/clubs/"+1+"/apply/"+2+"/reject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsString(new RejectClubApplyRequest("그냥")))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        ClubApplyRequest apply = clubApplyRequestMapper.findByRequestId(2L);
        assertNotNull(apply);
        assertEquals(ClubApplyType.REJECTED, apply.getStatus());

    }

    @Test
    @DisplayName("동아리 탈퇴")
    void withdrawMemberClubControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        // expected
        mockMvc.perform(patch("/api/clubs/"+6+"/withdraw")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("동아리 역할 변경")
    void updateMemberClubRoleClubControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("club@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        // expected
        mockMvc.perform(patch("/api/clubs/"+6+"/members/"+28+"/role")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsString(new UpdateMemberClubRoleRequest(ClubRoleType.MANAGER)))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

}
