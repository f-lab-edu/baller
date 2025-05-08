package com.baller.domain.model;

import com.baller.domain.enums.RoleType;
import com.baller.presentation.dto.request.member.SignUpRequest;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    public static Member ofUser(SignUpRequest request, PasswordEncoder encoder) {
        return Member.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .role(RoleType.ROLE_USER)
                .build();
    }

    public static Member ofAdmin(SignUpRequest request, PasswordEncoder encoder) {
        return Member.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .role(RoleType.ROLE_ADMIN)
                .build();
    }

}
