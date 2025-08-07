package com.baller.presentation.controller;

import com.baller.application.service.GameRecordService;
import com.baller.presentation.dto.request.game.GameRecordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/games")
public class GameRecordController {

    private final GameRecordService gameRecordService;

    @GetMapping("/{gameId}/records")
    public ResponseEntity<SseEmitter> subscribe(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameRecordService.subscribe(gameId));
    }

    @GetMapping("/{gameId}/records/polling")
    public ResponseEntity<GameRecordResponse> pollGameRecord(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameRecordService.getCurrentRecord(gameId));
    }

}
