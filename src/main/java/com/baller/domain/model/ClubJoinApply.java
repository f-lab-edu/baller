package com.baller.domain.model;

import com.baller.domain.enums.ClubJoinApplyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubJoinApply {

    private Long id;
    private Long memberId;
    private Long clubId;
    private ClubJoinApplyType status;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime handledAt;
    private Long handledBy;

    public static ClubJoinApply ofRequested(Long memberId, Long clubId) {
        return ClubJoinApply.builder()
                .memberId(memberId)
                .clubId(clubId)
                .status(ClubJoinApplyType.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
