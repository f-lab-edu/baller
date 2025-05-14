package com.baller.domain.model;

import com.baller.domain.enums.MemberRoleType;
import com.baller.domain.enums.MemberStatusType;
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

    private MemberRoleType memberRole;

    private MemberStatusType status;

    public static MemberClub ofLeader(Long member, Long clubId) {
        return MemberClub.builder()
                .memberId(member)
                .clubId(clubId)
                .memberRole(MemberRoleType.LEADER)
                .status(MemberStatusType.APPROVED)
                .build();
    }

}
