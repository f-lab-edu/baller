package com.baller.application.service;

import com.baller.common.exception.ClubNotFoundException;
import com.baller.domain.enums.ClubStatusType;
import com.baller.domain.model.Club;
import com.baller.domain.model.MemberClub;
import com.baller.infrastructure.mapper.ClubMapper;
import com.baller.infrastructure.mapper.MemberClubMapper;
import com.baller.presentation.dto.request.club.CreateClubRequest;
import com.baller.presentation.dto.response.club.ClubResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                .status(ClubStatusType.ACTIVE)
                .build();

        clubMapper.createClub(club);

        memberClubMapper.createMemberClub(MemberClub.ofLeader(memberId, club.getId()));

    }

    public List<ClubResponse> getAllClubs() {
        return clubMapper.getAllClubs(ClubStatusType.ACTIVE.toString())
                .stream()
                .map(ClubResponse::fromClub)
                .collect(Collectors.toList());
    }

    public ClubResponse getClubById(Long clubId) {
        Club club = clubMapper.getClubById(clubId);
        if (club == null) {
            throw new ClubNotFoundException(clubId);
        }
        return ClubResponse.fromClub(club);
    }

}
