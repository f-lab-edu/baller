package com.baller.presentation.dto.request.member;

import com.baller.domain.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    public Member toMemberDomain(String encryptedPassword) {
        return new Member(null, email, encryptedPassword, name, phoneNumber);
    }

}
