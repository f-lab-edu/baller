package com.baller.infrastructure.redis;

import com.baller.common.sse.SseEmitterManager;
import com.baller.domain.enums.SseChannelKeyType;
import com.baller.domain.enums.SseEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScoreUpdateSubscriber implements MessageListener {

    private final SseEmitterManager sseEmitterManager;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel()); // "pubsub:game:{id}"
            String json    = new String(message.getBody());

            Long gameId = Long.valueOf(channel.substring(channel.lastIndexOf(':') + 1));

            // 상태형 payload를 그대로 전송 (무-id broadcast)
            sseEmitterManager.broadcast(
                    SseChannelKeyType.GAME_RECORD.of(gameId),
                    json,
                    SseEventType.GAME_UPDATE
            );
        } catch (Exception e) {
            log.error("Pub/Sub 수신 처리 실패", e);
        }
    }

}
