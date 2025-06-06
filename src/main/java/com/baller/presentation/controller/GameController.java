package com.baller.presentation.controller;

import com.baller.application.service.GameService;
import com.baller.presentation.dto.request.game.CreateGameRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    @PostMapping
    public ResponseEntity<Void> createGame(@Valid @RequestBody CreateGameRequest request) {
        gameService.createGame(request.getHostClubId(), request);
        return ResponseEntity.ok().build();
    }

}
