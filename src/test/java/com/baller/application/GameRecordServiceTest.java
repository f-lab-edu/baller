package com.baller.application;

import com.baller.application.dto.GameUpdatedEvent;
import com.baller.application.service.GameRecordService;
import com.baller.common.sse.SseEmitterManager;
import com.baller.domain.enums.GameStatusType;
import com.baller.domain.enums.SseChannelKeyType;
import com.baller.domain.enums.SseEventType;
import com.baller.presentation.dto.request.game.GameRecordResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameRecordServiceTest {

    @Mock
    private SseEmitterManager sseEmitterManager;

    @InjectMocks
    private GameRecordService gameRecordService;

    @Test
    @DisplayName("SSE 구독 테스트")
    void subscribeTest() {
        // given
        Long gameId = 3L;
        SseEmitter dummyEmitter = new SseEmitter();
        when(sseEmitterManager.subscribe(SseChannelKeyType.GAME_RECORD.of(gameId))).thenReturn(dummyEmitter);

        // when
        SseEmitter result = gameRecordService.subscribe(gameId);

        // then
        verify(sseEmitterManager).subscribe(SseChannelKeyType.GAME_RECORD.of(gameId));
        assertThat(result).isEqualTo(dummyEmitter);
    }

    @Test
    @DisplayName("SSE 데이터 전송 테스트")
    void updateLiveRecord_ShouldBroadcastWithCorrectParameters() {
        // given
        Long gameId = 3L;
        GameRecordResponse dummyData = GameRecordResponse.of(
                "테스트 동아리1",
                "테스트 동아리2",
                58,
                61,
                LocalDateTime.of(2025, 6, 15, 19, 30),
                GameStatusType.IN_PROGRESS.getValue()
        );

        // when
        gameRecordService.gameRecordUpdate(new GameUpdatedEvent(gameId, dummyData));

        // then
        verify(sseEmitterManager).broadcast(SseChannelKeyType.GAME_RECORD.of(gameId), dummyData, SseEventType.GAME_UPDATE);
    }

}
