package com.baller.common.sse;

import com.baller.domain.enums.SseEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@EnableScheduling
public class SseEmitterManager {

    @Value("${sse.emitter.timeout}")
    private Long emitterTimeout;

    private final Map<String, List<SseEmitterWrapper>> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String channelKey) {

        SseEmitter emitter = new SseEmitter(emitterTimeout);
        SseEmitterWrapper emitterWrapper = new SseEmitterWrapper(emitter);
        emitters.computeIfAbsent(channelKey, k -> new CopyOnWriteArrayList<>()).add(emitterWrapper);

        emitter.onCompletion(() -> removeEmitter(channelKey, emitterWrapper));
        emitter.onTimeout(() -> {
            log.warn("SSE 타임아웃 발생");
            removeEmitter(channelKey, emitterWrapper);
        });
        emitter.onError((e) -> {
            log.error("SSE 연결 오류 발생", e);
            removeEmitter(channelKey, emitterWrapper);
        });

        try {
            emitter.send(SseEmitter.event().name(String.valueOf(SseEventType.INIT)).data(true));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public void broadcast(String channelKey, Object data, SseEventType sseEventType) {

        List<SseEmitterWrapper> gameEmitters = emitters.getOrDefault(channelKey, List.of());

        for(SseEmitterWrapper emitterWrapper : gameEmitters) {
            SseEmitter emitter = emitterWrapper.getEmitter();
            try {
                emitter.send(SseEmitter.event().name(String.valueOf(sseEventType)).data(data));
            } catch (Exception e) {
                emitter.completeWithError(e);
                removeEmitter(channelKey, emitterWrapper);
            }
        }

    }

    private void removeEmitter(String channelKey, SseEmitterWrapper emitterWrapper) {
        List<SseEmitterWrapper> list = emitters.get(channelKey);

        if (list != null) {
            list.remove(emitterWrapper);
        }

        try {
            emitterWrapper.getEmitter().complete();
        } catch (Exception e) {
            log.warn("Emitter 종료 중 예외 발생", e);
        }
    }

    @Scheduled(fixedRate = 30000)
    public void cleanUpEmitters() {

        log.info("Emitter CleanUp");

        long expirationThreshold = 90000;

        for (Map.Entry<String, List<SseEmitterWrapper>> entry : emitters.entrySet()) {
            String channelKey = entry.getKey();
            List<SseEmitterWrapper> wrapperList = entry.getValue();

            wrapperList.removeIf(wrapper -> {
                SseEmitter emitter = wrapper.getEmitter();
                boolean expired = wrapper.isExpired(expirationThreshold);
                if (expired) {
                    try {
                        emitter.complete();
                        removeEmitter(channelKey, wrapper);
                        log.info("만료된 emitter 제거: {}", channelKey);
                    } catch (Exception e) {
                        log.warn("만료된 emitter 종료 중 예외 발생", e);
                    }
                }
                return expired;
            });
        }
    }

    @Scheduled(fixedRate = 10000)
    public void logActiveEmitterCount() {
        int count = countActiveEmitters();
        log.info("현재 SSE 커넥션 수: {}", count);
    }

    public int countActiveEmitters() {
        return emitters.values().stream()
                .mapToInt(List::size)
                .sum();
    }

}
