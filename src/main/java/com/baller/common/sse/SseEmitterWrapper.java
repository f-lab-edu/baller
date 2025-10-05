package com.baller.common.sse;

import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
public class SseEmitterWrapper {

    private final SseEmitter emitter;
    private final long createdAt;
    private final String connectionId;

    public SseEmitterWrapper(SseEmitter emitter) {
        this.emitter = emitter;
        this.createdAt = System.currentTimeMillis();
        this.connectionId = null;
    }

    public SseEmitterWrapper(SseEmitter emitter, String connectionId) {
        this.emitter = emitter;
        this.createdAt = System.currentTimeMillis();
        this.connectionId = connectionId;
    }

    public boolean isExpired(long maxAgeMillis) {
        return System.currentTimeMillis() - createdAt > maxAgeMillis;
    }

}
