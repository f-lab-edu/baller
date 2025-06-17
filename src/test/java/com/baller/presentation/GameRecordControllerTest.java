package com.baller.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class GameRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("SSE 구독 요청 시 SseEmitter 반환")
    void subscribe_ShouldReturnSseEmitter() throws Exception {
        // given
        Long gameId = 3L;

        mockMvc.perform(get("/api/games/{gameId}/records", gameId))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
