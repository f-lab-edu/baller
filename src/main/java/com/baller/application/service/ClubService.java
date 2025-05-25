package com.baller.application.service;

import com.baller.common.annotation.RequireClubRole;
import com.baller.common.exception.AlreadyExistsClubJoinApplyException;
import com.baller.common.exception.AlreadyExistsMemberClubException;
import com.baller.common.exception.ClubNotFoundException;
import com.baller.domain.enums.ClubRoleType;
import com.baller.domain.enums.ClubStatusType;
import com.baller.domain.model.Club;
import com.baller.domain.model.ClubJoinApply;
import com.baller.domain.model.Member;
import com.baller.domain.model.MemberClub;
import com.baller.infrastructure.mapper.ClubJoinApplyMapper;
import com.baller.infrastructure.mapper.ClubMapper;
import com.baller.infrastructure.mapper.MemberClubMapper;
import com.baller.infrastructure.mapper.MemberMapper;
import com.baller.presentation.dto.request.club.CreateClubRequest;
import com.baller.presentation.dto.request.club.UpdateClubRequest;
import com.baller.presentation.dto.response.club.ClubJoinApplyResponse;
import com.baller.presentation.dto.response.club.ClubResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubMapper clubMapper;
    private final MemberMapper memberMapper;
    private final MemberClubMapper memberClubMapper;
    private final ClubJoinApplyMapper clubJoinApplyMapper;

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

    @Transactional
    @RequireClubRole({ClubRoleType.LEADER, ClubRoleType.MANAGER})
    public void updateClub(Long clubId, UpdateClubRequest request) {
        clubMapper.updateClub(
                Club.builder()
                        .id(clubId)
                        .name(request.getName())
                        .sportType(request.getSportType())
                        .description(request.getDescription())
                        .build()
        );
    }

    @Transactional
    @RequireClubRole({ClubRoleType.LEADER})
    public void deleteClub(Long clubId) {
        clubMapper.deleteClub(clubId, ClubStatusType.DELETE.toString());
    }

    @Transactional
    public void clubJoinApply(Long memberId, Long clubId) {
        if (!clubMapper.existsByClubId(clubId)) {
            throw new ClubNotFoundException(clubId);
        }

        if (memberClubMapper.existsByMemberIdAndClubId(memberId, clubId)) {
            throw new AlreadyExistsMemberClubException();
        }

        if (clubJoinApplyMapper.existsByMemberIdAndClubId(memberId, clubId)) {
            throw new AlreadyExistsClubJoinApplyException();
        }

        clubJoinApplyMapper.createClubJoinApply(ClubJoinApply.ofRequested(memberId, clubId));
    }

    @RequireClubRole({ClubRoleType.LEADER})
    public List<ClubJoinApplyResponse> getClubJoinApply(Long clubId) {

        List<ClubJoinApply> applies = clubJoinApplyMapper.findByClubId(clubId);

        List<Long> memberIds = applies.stream()
                .map(ClubJoinApply::getMemberId)
                .distinct()
                .toList();

        List<Member> members = memberMapper.findById(memberIds);

        Map<Long, Member> memberMap = members.stream()
                .collect(Collectors.toMap(Member::getId, Function.identity()));

        return applies.stream()
                .map(apply -> ClubJoinApplyResponse.from(apply, memberMap.get(apply.getMemberId())))
                .toList();

    }

    @Transactional
    @RequireClubRole({ClubRoleType.LEADER})
    public void approveClubJoinApply(Long clubId, Long handleId, Long applyId) {
        clubJoinApplyMapper.updateApplyClub(ClubJoinApply.ofApprove(applyId, handleId));

        ClubJoinApply apply = clubJoinApplyMapper.findByApplyId(applyId);
        memberClubMapper.createMemberClub(MemberClub.ofParticipant(apply.getMemberId(), clubId));
    }

    @Transactional
    @RequireClubRole({ClubRoleType.LEADER})
    public void rejectClubJoinApply(Long clubId, Long handleId, Long applyId, String reason) {
        clubJoinApplyMapper.updateApplyClub(ClubJoinApply.ofRejected(applyId, handleId, reason));
    }

}
