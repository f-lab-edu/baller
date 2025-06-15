package com.baller.application.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameRecordService {

    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long gameId) {

        SseEmitter emitter = new SseEmitter(0L);
        emitters.computeIfAbsent(gameId, k -> new ArrayList<>()).add(emitter);

        emitter.onCompletion(() -> emitters.get(gameId).remove(emitter));
        emitter.onTimeout(() -> emitters.get(gameId).remove(emitter));
        emitter.onError((e) -> emitters.get(gameId).remove(emitter));

        try {
            emitter.send(SseEmitter.event().name("init").data("connected"));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }

        return emitter;

    }

    public void updateLiveRecord(Long gameId, Object data) {

        List<SseEmitter> gameEmitters = emitters.getOrDefault(gameId, List.of());

        for(SseEmitter emitter : gameEmitters) {
            try {
                emitter.send(SseEmitter.event().name("record-update").data(data));
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }

    }

}
