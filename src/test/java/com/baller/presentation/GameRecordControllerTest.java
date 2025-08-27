package com.baller.presentation;

import com.baller.application.dto.GameUpdatedEvent;
import com.baller.application.service.GameRecordService;
import com.baller.domain.enums.GameStatusType;
import com.baller.presentation.dto.request.game.GameRecordResponse;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@Transactional
public class GameRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRecordService gameRecordService;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("SSE 구독 요청 시 SseEmitter 반환")
    void sseSubscribeTest() throws Exception {
        // given
        Long gameId = 3L;

        mockMvc.perform(get("/api/games/{gameId}/records", gameId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("SSE 스트리밍 수신 테스트")
    void sseSendEventTest() throws Exception {

        List<String> received = new CopyOnWriteArrayList<>();

        WebClient.create("http://localhost:" + port)
                .get()
                .uri("/api/games/999/records")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(received::add)
                .subscribe();

        Thread.sleep(300); // 연결 대기

        gameRecordService.gameRecordUpdate(new GameUpdatedEvent(999L, GameRecordResponse.of(
                "테스트 동아리1",
                "테스트 동아리2",
                58,
                61,
                LocalDateTime.of(2025, 6, 15, 19, 30),
                GameStatusType.IN_PROGRESS.getValue())));

        Awaitility.await().atMost(2, TimeUnit.SECONDS)
                .until(() -> received.stream().anyMatch(s -> s.contains("테스트 동아리")));

        assertThat(received).anyMatch(e -> e.contains("테스트 동아리"));
    }

}
