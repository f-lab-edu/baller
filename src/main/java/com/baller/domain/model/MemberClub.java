package com.baller.domain.model;

import com.baller.domain.enums.ClubRoleType;
import com.baller.domain.enums.ClubMemberStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberClub {

    private Long id;

    private Long memberId;

    private Long clubId;

    private ClubRoleType memberRole;

    private ClubMemberStatusType status;

    public static MemberClub ofLeader(Long member, Long clubId) {
        return MemberClub.builder()
                .memberId(member)
                .clubId(clubId)
                .memberRole(ClubRoleType.LEADER)
                .status(ClubMemberStatusType.APPROVED)
                .build();
    }

}
