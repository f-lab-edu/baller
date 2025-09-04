package com.baller.infrastructure.redis;

import com.baller.application.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScoreUpdatePublisher {

    private final StringRedisTemplate redis;
    private final GameService gameService;
    private final ObjectMapper objectMapper;

    /** 상태형(state) 메시지: 이 시점의 '정답'을 그대로 보낸다 */
    public void publishCurrentState(Long gameId) {
        try {
            var snapshot = gameService.getGame(gameId); // 합계·팀명·상태 포함
            String json  = objectMapper.writeValueAsString(snapshot);
            redis.convertAndSend(RedisChannels.gameChannel(gameId), json);
        } catch (Exception e) {
            throw new IllegalStateException("Pub/Sub publish 실패", e);
        }
    }

}
