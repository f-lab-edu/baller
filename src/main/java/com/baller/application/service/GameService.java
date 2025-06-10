package com.baller.application.service;

import com.baller.common.annotation.RequireClubRole;
import com.baller.common.exception.ClubNotFoundException;
import com.baller.domain.enums.ClubRoleType;
import com.baller.domain.model.Game;
import com.baller.infrastructure.mapper.ClubMapper;
import com.baller.infrastructure.mapper.GameMapper;
import com.baller.presentation.dto.request.game.CreateGameRequest;
import com.baller.presentation.dto.response.geme.GameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameMapper gameMapper;
    private final ClubMapper clubMapper;

    @Transactional
    @RequireClubRole({ClubRoleType.LEADER, ClubRoleType.MANAGER})
    public void createGame(Long clubId, CreateGameRequest request){

        if(!clubMapper.existsByClubId(request.getHostClubId())) {
            throw new ClubNotFoundException(request.getHostClubId());
        }

        if(!clubMapper.existsByClubId(request.getGuestClubId())){
            throw new ClubNotFoundException(request.getGuestClubId());
        }

        gameMapper.createGame(Game.ofGameScheduled(request));
    }

    public List<GameResponse> getGames(Long clubId){
        return gameMapper.getAllGames(clubId)
                .stream()
                .map(GameResponse::from)
                .toList();
    }

}
