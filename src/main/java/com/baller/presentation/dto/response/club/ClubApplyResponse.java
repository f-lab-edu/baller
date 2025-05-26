package com.baller.presentation.dto.response.club;

import com.baller.domain.enums.ClubApplyType;
import com.baller.domain.model.ClubApplyRequest;
import com.baller.domain.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClubApplyResponse {

    private Long applyId;
    private Long memberId;
    private Long clubId;
    private ClubApplyType status;
    private String reason;
    private LocalDateTime createdDate;
    private String memberEmail;
    private String memberName;
    private String phoneNumber;


    public static ClubApplyResponse from(ClubApplyRequest apply, Member member) {
        return ClubApplyResponse.builder()
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
    public ClubApplyResponse(Long applyId, ClubApplyType status, String reason, LocalDateTime createdDate, Long memberId, String memberEmail, String memberName, String phoneNumber) {
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
