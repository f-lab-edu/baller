package com.baller.application.service;

import com.baller.common.exception.AlreadyExistsEmailException;
import com.baller.infrastructure.crypto.PasswordEncoder;
import com.baller.infrastructure.mapper.MemberMapper;
import com.baller.presentation.dto.request.member.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder encoder;

    public void signUp(SignUpRequest request) {

        validateDuplicateEmail(request.getEmail());

        memberMapper.signUp(request.toMemberDomain(encoder.encrypt(request.getPassword())));
    }

    private void validateDuplicateEmail(String email) {
        if (memberMapper.existsByEmail(email)) {
            throw new AlreadyExistsEmailException();
        }
    }

}
