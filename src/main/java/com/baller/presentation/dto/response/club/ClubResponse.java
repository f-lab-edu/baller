package com.baller.presentation.dto.response.club;

import com.baller.domain.enums.SportType;
import com.baller.domain.model.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClubResponse {

    private Long id;
    private String name;
    private SportType sportType;
    private String description;

    public static ClubResponse fromClub(Club club) {
        return new ClubResponse(
                club.getId(),
                club.getName(),
                club.getSportType(),
                club.getDescription()
        );
    }

}
