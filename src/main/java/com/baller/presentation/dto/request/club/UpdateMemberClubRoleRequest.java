package com.baller.presentation.dto.request.club;

import com.baller.domain.enums.ClubRoleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberClubRoleRequest {

    @NotNull(message = "동아리 역할을 입력해주세요.")
    private ClubRoleType role;

}
