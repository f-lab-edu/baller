package com.baller.common.sse;

import com.baller.domain.enums.SseEventType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterManager {

    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String channelKey) {

        SseEmitter emitter = new SseEmitter(0L);
        emitters.computeIfAbsent(channelKey, k -> new ArrayList<>()).add(emitter);

        emitter.onCompletion(() -> emitters.get(channelKey).remove(emitter));
        emitter.onTimeout(() -> emitters.get(channelKey).remove(emitter));
        emitter.onError((e) -> emitters.get(channelKey).remove(emitter));

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

}
