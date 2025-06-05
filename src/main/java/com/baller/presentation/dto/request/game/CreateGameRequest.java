package com.baller.presentation.dto.request.game;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGameRequest {

    @NotBlank(message = "경기 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "경기 시작 시간을 입력해주세요.")
    private LocalDateTime startTime;

    @NotBlank(message = "홈팀 아이디를 입력해주세요.")
    private Long hostClubId;

    @NotBlank(message = "원정팀 아이디를 입력해주세요.")
    private Long guestClubId;

}
