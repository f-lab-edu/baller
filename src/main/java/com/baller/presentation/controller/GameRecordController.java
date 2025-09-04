package com.baller.presentation.controller;

import com.baller.application.service.GameRecordService;
import com.baller.presentation.dto.request.game.GameRecordResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/games")
public class GameRecordController {

    private final ObjectMapper objectMapper;
    private final GameRecordService gameRecordService;

    //Polling
    @GetMapping("/{gameId}/records/polling")
    public ResponseEntity<GameRecordResponse> pollGameRecord(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameRecordService.getCurrentRecord(gameId));
    }

    //SSE
    @GetMapping("/{gameId}/records")
    public ResponseEntity<SseEmitter> subscribe(@PathVariable Long gameId) {

        SseEmitter emitter = gameRecordService.subscribe(gameId);

        try {
            GameRecordResponse snapshot = gameRecordService.getCurrentRecord(gameId);
            String json = objectMapper.writeValueAsString(snapshot);
            gameRecordService.send(emitter, "SNAPSHOT", null, json);
        } catch (Exception e) {
            log.error("SSE 연결 실패", e);
        }

        return ResponseEntity.ok(emitter);
    }

}
