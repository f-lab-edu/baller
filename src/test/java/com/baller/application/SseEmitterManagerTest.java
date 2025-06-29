package com.baller.application;

import com.baller.common.sse.SseEmitterManager;
import com.baller.domain.enums.SseEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "sse.emitter.timeout=60000")
public class SseEmitterManagerTest {

    @Autowired
    private SseEmitterManager manager;

    private final String channelKey = "test-channel";

    @BeforeEach
    void setup() {
        manager.subscribe(channelKey);
    }

    @Test
    @DisplayName("SSE 이벤트 전송")
    void broadcastTest() {
        manager.broadcast(channelKey, "hello", SseEventType.GAME_UPDATE);

        assertThat(manager.countActiveEmitters()).isEqualTo(1);
    }

}
