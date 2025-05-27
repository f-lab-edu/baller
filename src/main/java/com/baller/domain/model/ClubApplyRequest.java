package com.baller.domain.model;

import com.baller.domain.enums.ClubApplyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubApplyRequest {

    private Long id;
    private Long memberId;
    private Long clubId;
    private ClubApplyType status;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime handledAt;
    private Long handledBy;

    public static ClubApplyRequest ofRequested(Long memberId, Long clubId) {
        return ClubApplyRequest.builder()
                .memberId(memberId)
                .clubId(clubId)
                .status(ClubApplyType.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ClubApplyRequest ofApprove(Long requestId, Long memberId) {
        return ClubApplyRequest.builder()
                .id(requestId)
                .status(ClubApplyType.APPROVED)
                .handledAt(LocalDateTime.now())
                .handledBy(memberId)
                .build();
    }

    public static ClubApplyRequest ofRejected(Long requestId, Long memberId, String reason) {
        return ClubApplyRequest.builder()
                .id(requestId)
                .status(ClubApplyType.REJECTED)
                .reason(reason)
                .handledAt(LocalDateTime.now())
                .handledBy(memberId)
                .build();
    }


}
