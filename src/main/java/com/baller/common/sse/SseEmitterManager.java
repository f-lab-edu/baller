package com.baller.common.sse;

import com.baller.domain.enums.SseEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class SseEmitterManager {

    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String channelKey) {

        SseEmitter emitter = new SseEmitter(60000L);
        emitters.computeIfAbsent(channelKey, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(channelKey, emitter));
        emitter.onTimeout(() -> {
            log.warn("SSE 타임아웃 발생");
            removeEmitter(channelKey, emitter);
        });
        emitter.onError((e) -> {
            log.error("SSE 연결 오류 발생", e);
            removeEmitter(channelKey, emitter);
        });

        try {
            emitter.send(SseEmitter.event().name(String.valueOf(SseEventType.INIT)).data(true));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public void broadcast(String channelKey, Object data, SseEventType sseEventType) {

        List<SseEmitter> gameEmitters = emitters.getOrDefault(channelKey, List.of());

        for(SseEmitter emitter : gameEmitters) {
            try {
                emitter.send(SseEmitter.event().name(String.valueOf(sseEventType)).data(data));
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }

    }

    private void removeEmitter(String channelKey, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(channelKey);

        if (list != null) {
            list.remove(emitter);
        }

        try {
            emitter.complete();
        } catch (Exception e) {
            log.warn("Emitter 종료 중 예외 발생", e);
        }
    }

}
