package com.baller.domain.model;

import com.baller.domain.enums.SportType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Club {

    private Long id;

    private String name;

    private SportType sportType;

    private String description;

}
