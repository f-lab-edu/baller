package com.baller.application.service;

import com.baller.domain.model.Club;
import com.baller.domain.model.MemberClub;
import com.baller.infrastructure.mapper.ClubMapper;
import com.baller.infrastructure.mapper.MemberClubMapper;
import com.baller.presentation.dto.request.club.CreateClubRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubMapper clubMapper;
    private final MemberClubMapper memberClubMapper;

    @Transactional
    public void createClub(Long memberId, CreateClubRequest createClubRequest) {

        Club club = Club.builder()
                .name(createClubRequest.getName())
                .sportType(createClubRequest.getSportType())
                .description(createClubRequest.getDescription())
                .build();

        clubMapper.createClub(club);

        memberClubMapper.createMemberClub(MemberClub.ofLeader(memberId, club.getId()));

    }

}
