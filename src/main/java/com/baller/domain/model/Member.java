package com.baller.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private Long id;

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

}
