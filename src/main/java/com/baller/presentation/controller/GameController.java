package com.baller.presentation.controller;

import com.baller.application.service.GameService;
import com.baller.presentation.dto.request.game.CreateGameRequest;
import com.baller.presentation.dto.request.game.StartGameRequest;
import com.baller.presentation.dto.request.game.UpdateBasketballRecordRequest;
import com.baller.presentation.dto.response.geme.GameResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<GameResponse>> getGames(@RequestParam Long clubId) {
        return ResponseEntity.ok(gameService.getGames(clubId));
    }

    @PostMapping("/{gameId}/start")
    public ResponseEntity<Void> startGame(@PathVariable Long gameId, @RequestParam Long hostClubId, @RequestBody StartGameRequest request) {
        gameService.startGame(hostClubId, gameId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{gameId}/members/{memberId}/basketball/record")
    public ResponseEntity<Void> updateBasketballRecord(@PathVariable Long gameId, @PathVariable Long memberId, @RequestBody UpdateBasketballRecordRequest request) {
        gameService.updateBasketballRecord(gameId, memberId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> getGame(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameService.getGame(gameId));
    }

}
