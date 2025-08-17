package com.baller.common.sse;

import com.baller.domain.enums.SseEventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

@Slf4j
@Component
@EnableScheduling
public class SseEmitterManager {

    private static final long CLEANUP_INTERVAL_MS = 1000 * 30;
    private static final long EMITTER_TTL_MS      = 1000 * 90;
    private static final long LOG_INTERVAL_MS     = 1000 * 10;

    @Value("${sse.emitter.timeout}")
    private Long emitterTimeout;

    private final ObjectMapper om = new ObjectMapper();

    private final Map<String, Set<SseEmitterWrapper>> emitters = new ConcurrentHashMap<>();

    // 전용 브로드캐스트 풀
    private final ExecutorService broadcastPool =
            new ThreadPoolExecutor(
                    2,
                    4,
                    30, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(200),
                    r -> { Thread t = new Thread(r, "sse-broadcast"); t.setDaemon(true); return t; },
                    new ThreadPoolExecutor.CallerRunsPolicy()
            );

    public SseEmitter subscribe(String channelKey) {

        SseEmitter emitter = new SseEmitter(emitterTimeout);
        SseEmitterWrapper emitterWrapper = new SseEmitterWrapper(emitter);
        emitters.computeIfAbsent(channelKey, k -> ConcurrentHashMap.newKeySet()).add(emitterWrapper);

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

        Set<SseEmitterWrapper> gameEmitters = emitters.get(channelKey);
        if (gameEmitters == null || gameEmitters.isEmpty()) return;

        //한번만 직렬화해서 재사용
        final String json = toJson(data);

        var it = gameEmitters.iterator();
        while (it.hasNext()) {
            List<SseEmitterWrapper> batch = new ArrayList<>(64);
            for (int i=0; i<64 && it.hasNext(); i++) {
                batch.add(it.next());
            }
            broadcastPool.submit(() -> {
                for (var w : batch) sendOne(channelKey, w, sseEventType, json);
            });
        }

    }

    private void sendOne(String channelKey, SseEmitterWrapper emitterWrapper, SseEventType type, String json) {
        SseEmitter emitter = emitterWrapper.getEmitter();
        try {
            emitter.send(SseEmitter.event().name(type.name()).data(json));
        } catch (Exception e) {
            emitter.completeWithError(e);
            removeEmitter(channelKey, emitterWrapper);
        }
    }

    private String toJson(Object data) {
        try {
            return (data instanceof String s) ? s : om.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("SSE 직렬화 실패", e);
        }
    }

    private void removeEmitter(String channelKey, SseEmitterWrapper emitterWrapper) {
        Set<SseEmitterWrapper> list = emitters.get(channelKey);

        if (list != null) {
            list.remove(emitterWrapper);
        }

        try {
            emitterWrapper.getEmitter().complete();
        } catch (Exception e) {
            log.warn("Emitter 종료 중 예외 발생", e);
        }
    }

    @Scheduled(fixedRate = CLEANUP_INTERVAL_MS)
    public void cleanUpEmitters() {

        log.info("Emitter CleanUp");

        for (Map.Entry<String, Set<SseEmitterWrapper>> entry : emitters.entrySet()) {
            String channelKey = entry.getKey();
            Set<SseEmitterWrapper> wrapperList = entry.getValue();

            wrapperList.removeIf(wrapper -> {
                SseEmitter emitter = wrapper.getEmitter();
                boolean expired = wrapper.isExpired(EMITTER_TTL_MS);
                if (expired) {
                    try {
                        emitter.complete();
                        log.info("만료된 emitter 제거: {}", channelKey);
                    } catch (Exception e) {
                        log.warn("만료된 emitter 종료 중 예외 발생", e);
                    }
                }
                return expired;
            });
        }
    }

    @Scheduled(fixedRate = LOG_INTERVAL_MS)
    public void logActiveEmitterCount() {
        int count = countActiveEmitters();
        log.info("현재 SSE 커넥션 수: {}", count);
    }

    public int countActiveEmitters() {
        return emitters.values().stream()
                .mapToInt(Set::size)
                .sum();
    }

}
