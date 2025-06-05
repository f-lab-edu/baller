package com.baller.application.service;

import com.baller.common.annotation.RequireClubRole;
import com.baller.common.exception.ClubNotFoundException;
import com.baller.domain.enums.ClubRoleType;
import com.baller.domain.model.Game;
import com.baller.infrastructure.mapper.ClubMapper;
import com.baller.infrastructure.mapper.GameMapper;
import com.baller.presentation.dto.request.game.CreateGameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameMapper gameMapper;
    private final ClubMapper clubMapper;

    @Transactional
    @RequireClubRole({ClubRoleType.LEADER, ClubRoleType.MANAGER})
    public void createGame(Long hostClubId, CreateGameRequest request){

        if(!clubMapper.existsByClubId(hostClubId)) {
            throw new ClubNotFoundException(hostClubId);
        }

        if(!clubMapper.existsByClubId(request.getGuestClubId())){
            throw new ClubNotFoundException(request.getGuestClubId());
        }

        gameMapper.createGame(Game.ofGameScheduled(request));
    }

}
