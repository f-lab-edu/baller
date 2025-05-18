package com.baller.presentation.dto.request.club;

import com.baller.domain.enums.SportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClubRequest {

    @NotBlank(message = "동아리 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "종목을 입력해주세요.")
    private SportType sportType;

    @NotBlank(message = "동아리 설명을 입력해주세요.")
    private String description;

}
