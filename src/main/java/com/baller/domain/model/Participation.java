package com.baller.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participation {

    private Long id;
    private Long memberId;
    private Long gameId;
    private Long clubId;

}
