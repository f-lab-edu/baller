package com.baller.application.service;

import com.baller.common.sse.SseEmitterManager;
import com.baller.domain.enums.SseChannelKeyType;
import com.baller.domain.enums.SseEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class GameRecordService {

    private final SseEmitterManager sseEmitterManager;

    public SseEmitter subscribe(Long gameId) {

        return sseEmitterManager.subscribe(SseChannelKeyType.GAME_RECORD.of(gameId));

    }

    public void updateLiveRecord(Long gameId, Object data) {
        sseEmitterManager.broadcast(SseChannelKeyType.GAME_RECORD.of(gameId), data, SseEventType.GAME_UPDATE);
    }

}
