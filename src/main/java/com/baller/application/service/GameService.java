package com.baller.application.service;

import com.baller.application.dto.UpdateLiveBasketballRecord;
import com.baller.common.annotation.RequireClubRole;
import com.baller.common.exception.ClubNotFoundException;
import com.baller.domain.enums.ClubRoleType;
import com.baller.domain.enums.GameStatusType;
import com.baller.domain.enums.SportType;
import com.baller.domain.model.BasketballRecord;
import com.baller.domain.model.Game;
import com.baller.domain.model.GameRecord;
import com.baller.domain.model.Participation;
import com.baller.infrastructure.mapper.*;
import com.baller.presentation.dto.request.game.CreateGameRequest;
import com.baller.presentation.dto.request.game.ParticipationRequest;
import com.baller.presentation.dto.request.game.StartGameRequest;
import com.baller.presentation.dto.request.game.UpdateBasketballRecordRequest;
import com.baller.presentation.dto.response.geme.GameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameMapper gameMapper;
    private final ClubMapper clubMapper;
    private final ParticipationMapper participationMapper;
    private final GameRecordMapper gameRecordMapper;
    private final BasketballRecordMapper basketballRecordMapper;
    private final GameRecordService gameRecordService;

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

    @Transactional
    @RequireClubRole({ClubRoleType.LEADER, ClubRoleType.MANAGER})
    public void startGame(Long clubId, Long gameId, StartGameRequest request){

        Game game = gameMapper.findById(gameId);

        gameMapper.startGame(GameStatusType.IN_PROGRESS, gameId);

        for (ParticipationRequest participation : request.getParticipants()) {

            Participation p = Participation.builder()
                    .gameId(gameId)
                    .memberId(participation.getMemberId())
                    .clubId(participation.getClubId())
                    .build();

            participationMapper.insertParticipation(p);

            GameRecord gameRecord = BasketballRecord.builder()
                    .gameId(gameId)
                    .memberId(participation.getMemberId())
                    .clubId(participation.getClubId())
                    .createdAt(LocalDateTime.now())
                    .build();

            gameRecordMapper.insertGameRecord(gameRecord);

            if(SportType.BASKETBALL.equals(game.getSportType())) {
                BasketballRecord basketballRecord = BasketballRecord.builder()
                        .id(gameRecord.getId())
                        .build();

                basketballRecordMapper.insertBasketballRecord(basketballRecord);

            }

        }

    }

    @Transactional
    public void updateBasketballRecord(Long gameId, Long memberId, UpdateBasketballRecordRequest request) {

        Long gameRecordId = gameRecordMapper.findByGameIdAndMemberId(gameId, memberId);

        basketballRecordMapper.updateBasketballRecord(BasketballRecord.of(gameRecordId, request));

        gameRecordService.updateLiveRecord(gameId, UpdateLiveBasketballRecord.from(memberId, request));

    }

    public GameResponse getGame(Long gameId){
        Game game = gameMapper.findById(gameId);
        return GameResponse.from(game);
    }

}
