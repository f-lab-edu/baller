package com.baller.presentation;

import com.baller.domain.enums.SportType;
import com.baller.presentation.dto.request.game.CreateGameRequest;
import com.baller.presentation.dto.request.game.ParticipationRequest;
import com.baller.presentation.dto.request.game.StartGameRequest;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class GameControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("경기 생성")
    void createGameControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("club@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult result = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        CreateGameRequest request = CreateGameRequest.builder()
                .title("테스트 경기")
                .startTime(LocalDateTime.parse("2025-06-10T14:00"))
                .hostClubId(6L)
                .guestClubId(1L)
                .sportType(SportType.BASKETBALL)
                .build();

        // expected
        mockMvc.perform(post("/api/games")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("경기 목록 조회")
    void getGamesControllerTest() throws Exception {

        mockMvc.perform(get("/api/games").param("clubId", "6"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("경기 시작")
    void startGameControllerTest() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .email("club@flab.com")
                .password("Flab@1234")
                .build();

        MvcResult result = mockMvc.perform(post("/api/members/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseJson).get("accessToken").asText();

        StartGameRequest request = new StartGameRequest(
                List.of(
                        new ParticipationRequest(1L, 10L),
                        new ParticipationRequest(2L, 10L),
                        new ParticipationRequest(3L, 10L),
                        new ParticipationRequest(4L, 10L),
                        new ParticipationRequest(5L, 10L),
                        new ParticipationRequest(6L, 11L),
                        new ParticipationRequest(7L, 11L),
                        new ParticipationRequest(8L, 11L),
                        new ParticipationRequest(9L, 11L),
                        new ParticipationRequest(10L, 11L)
                )
        );

        mockMvc.perform(post("/api/games/{gameId}/start", 3).param("hostClubId", "6")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());

    }

}
