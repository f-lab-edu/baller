package com.baller.application.service;

import com.baller.application.dto.GameUpdatedEvent;
import com.baller.common.sse.SseEmitterManager;
import com.baller.domain.enums.SseChannelKeyType;
import com.baller.domain.enums.SseEventType;
import com.baller.presentation.dto.request.game.GameRecordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class GameRecordService {

    private final SseEmitterManager sseEmitterManager;
    private final GameService gameService;

    public GameRecordResponse getCurrentRecord(Long gameId) {
        return gameService.getGame(gameId);
    }

    public SseEmitter subscribe(Long gameId) {

        return sseEmitterManager.subscribe(SseChannelKeyType.GAME_RECORD.of(gameId));

    }

    @Async("sseListenerPool")
    @EventListener
    public void gameRecordUpdate(GameUpdatedEvent gameUpdatedEvent) {
        sseEmitterManager.broadcast(SseChannelKeyType.GAME_RECORD.of(gameUpdatedEvent.getGameId()), gameUpdatedEvent.getData(), SseEventType.GAME_UPDATE);
    }

}
