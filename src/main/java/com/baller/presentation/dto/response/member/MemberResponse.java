package com.baller.presentation.dto.response.member;

import com.baller.domain.enums.RoleType;
import com.baller.domain.model.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {

    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private RoleType role;

    public static MemberResponse fromMember(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .role(member.getRole())
                .build();
    }

}
