package com.baller.common.sse;

import com.baller.domain.enums.SseEventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SseEmitterManager {

    private static final long CLEANUP_INTERVAL_MS = 1000 * 30;
    private static final long EMITTER_TTL_MS      = 1000 * 90;
    private static final long LOG_INTERVAL_MS     = 1000 * 10;

    @Value("${sse.emitter.timeout}")
    private Long emitterTimeout;

    private final ObjectMapper objectMapper;
    private final Map<String, Set<SseEmitterWrapper>> emitters = new ConcurrentHashMap<>();
    // connectionId 기반 중복 연결 방지를 위한 맵
    private final Map<String, String> connectionIdToChannelKey = new ConcurrentHashMap<>();

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

    public void send(SseEmitter emitter, String eventName, String idOrNull, String json) {
        try {
            var evt = SseEmitter.event().name(eventName).data(json);
            if (idOrNull != null) evt = evt.id(idOrNull);
            emitter.send(evt);
        } catch (Exception e) {
            try {
                emitter.completeWithError(e);
            } catch (Exception ignore) {

            }
        }
    }

    public SseEmitter subscribe(String channelKey) {
        return subscribe(channelKey, null);
    }

    public SseEmitter subscribe(String channelKey, String connectionId) {
        // connectionId가 제공된 경우, 이전 연결이 있다면 정리 (같은 채널이든 다른 채널이든)
        if (connectionId != null) {
            String previousChannelKey = connectionIdToChannelKey.get(connectionId);
            if (previousChannelKey != null) {
                if (previousChannelKey.equals(channelKey)) {
                    log.info("동일한 connectionId로 같은 채널에 재연결 (새로고침): {}", channelKey);
                } else {
                    log.info("동일한 connectionId로 다른 채널에 재연결: {} -> {}", previousChannelKey, channelKey);
                }
                // 이전 채널에서 해당 connectionId의 연결들을 정리
                cleanupConnectionIdFromChannel(previousChannelKey, connectionId);
            }
        }

        SseEmitter emitter = new SseEmitter(emitterTimeout);
        SseEmitterWrapper emitterWrapper = new SseEmitterWrapper(emitter, connectionId);
        emitters.computeIfAbsent(channelKey, k -> ConcurrentHashMap.newKeySet()).add(emitterWrapper);

        // connectionId 매핑 저장
        if (connectionId != null) {
            connectionIdToChannelKey.put(connectionId, channelKey);
        }

        emitter.onCompletion(() -> removeEmitter(channelKey, emitterWrapper));
        emitter.onTimeout(() -> {
            log.warn("SSE 타임아웃 발생 - connectionId: {}", connectionId);
            removeEmitter(channelKey, emitterWrapper);
        });
        emitter.onError((e) -> {
            log.error("SSE 연결 오류 발생 - connectionId: {}", connectionId, e);
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
            //64개씩 묶어 병렬 전송
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
            return (data instanceof String) ? (String) data : objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("SSE 직렬화 실패", e);
        }
    }

    private void removeEmitter(String channelKey, SseEmitterWrapper emitterWrapper) {
        Set<SseEmitterWrapper> list = emitters.get(channelKey);

        if (list != null) {
            list.remove(emitterWrapper);
        }

        // connectionId 매핑에서도 제거
        String connectionId = emitterWrapper.getConnectionId();
        if (connectionId != null) {
            connectionIdToChannelKey.remove(connectionId);
        }

        try {
            emitterWrapper.getEmitter().complete();
        } catch (Exception e) {
            log.warn("Emitter 종료 중 예외 발생", e);
        }
    }

    private void cleanupConnectionIdFromChannel(String channelKey, String connectionId) {
        Set<SseEmitterWrapper> list = emitters.get(channelKey);
        if (list != null) {
            list.removeIf(wrapper -> {
                if (connectionId.equals(wrapper.getConnectionId())) {
                    try {
                        wrapper.getEmitter().complete();
                        log.info("중복 연결 정리: channelKey={}, connectionId={}", channelKey, connectionId);
                    } catch (Exception e) {
                        log.warn("중복 연결 정리 중 예외 발생", e);
                    }
                    return true;
                }
                return false;
            });
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
                        log.info("만료된 emitter 제거: channelKey={}, connectionId={}", channelKey, wrapper.getConnectionId());
                        
                        // connectionId 매핑에서도 제거
                        String connectionId = wrapper.getConnectionId();
                        if (connectionId != null) {
                            connectionIdToChannelKey.remove(connectionId);
                        }
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
