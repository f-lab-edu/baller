package com.baller.application.service;

import com.baller.common.exception.AlreadyExistsEmailException;
import com.baller.domain.model.Member;
import com.baller.infrastructure.mapper.MemberMapper;
import com.baller.presentation.dto.request.member.SignUpRequest;
import com.baller.presentation.dto.request.member.UpdateMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder encoder;

    @Transactional
    public void signUp(SignUpRequest request) {

        validateDuplicateEmail(request.getEmail());

        memberMapper.signUp(Member.ofUser(request, encoder));

    }

    private void validateDuplicateEmail(String email) {
        if (memberMapper.existsByEmail(email)) {
            throw new AlreadyExistsEmailException();
        }
    }

    @Transactional
    public void updateMember(Long memberId, UpdateMemberRequest request) {
        memberMapper.updateById(
                Member.builder()
                        .id(memberId)
                        .password(encoder.encode(request.getPassword()))
                        .name(request.getName())
                        .phoneNumber(request.getPhoneNumber())
                        .build()
        );
    }

}
