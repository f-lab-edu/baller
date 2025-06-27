package com.baller.common.sse;

import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
public class SseEmitterWrapper {

    private final SseEmitter emitter;
    private final long createdAt;

    public SseEmitterWrapper(SseEmitter emitter) {
        this.emitter = emitter;
        this.createdAt = System.currentTimeMillis();
    }

    public boolean isExpired(long maxAgeMillis) {
        return System.currentTimeMillis() - createdAt > maxAgeMillis;
    }

}
