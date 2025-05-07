package com.baller.domain.model;

import com.baller.domain.enums.RoleType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {

    private Long id;

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    private RoleType role;

}
