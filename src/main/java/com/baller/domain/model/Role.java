package com.baller.domain.model;

import com.baller.domain.enums.EnumRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Long id;
    private Long memberId;
    private EnumRole role;

}
