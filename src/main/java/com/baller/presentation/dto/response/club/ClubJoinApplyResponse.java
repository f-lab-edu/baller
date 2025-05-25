package com.baller.presentation.dto.response.club;

import com.baller.domain.enums.ClubJoinApplyType;
import com.baller.domain.model.ClubJoinApply;
import com.baller.domain.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClubJoinApplyResponse {

    private Long applyId;
    private Long memberId;
    private Long clubId;
    private ClubJoinApplyType status;
    private String reason;
    private LocalDateTime createdDate;
    private String memberEmail;
    private String memberName;
    private String phoneNumber;


    public static ClubJoinApplyResponse from(ClubJoinApply apply, Member member) {
        return ClubJoinApplyResponse.builder()
                .applyId(apply.getId())
                .memberId(apply.getMemberId())
                .status(apply.getStatus())
                .reason(apply.getReason())
                .createdDate(apply.getCreatedAt())
                .memberEmail(member.getEmail())
                .memberName(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }

    @Builder
    public ClubJoinApplyResponse(Long applyId, ClubJoinApplyType status, String reason, LocalDateTime createdDate, Long memberId, String memberEmail, String memberName, String phoneNumber) {
        this.applyId = applyId;
        this.status = status;
        this.reason = reason;
        this.createdDate = createdDate;
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.memberName = memberName;
        this.phoneNumber = phoneNumber;
    }

}
