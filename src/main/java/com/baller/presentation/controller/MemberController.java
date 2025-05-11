package com.baller.presentation.controller;

import com.baller.application.service.MemberService;
import com.baller.domain.model.Member;
import com.baller.presentation.dto.request.member.SignUpRequest;
import com.baller.presentation.dto.request.member.UpdateMemberRequest;
import com.baller.presentation.dto.response.member.MemberResponse;
import com.baller.security.domain.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        memberService.signUp(signUpRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * @param userDetails
     * AuthenticationPrincipal 어노테이션을 사용하여 SecurityContextHolder.getContext().getAuthentication().getPrincipal() 자동 주입
     * 요청에 대한 인증 정보는 presentation 계층의 책임
     * Service 계층은 인증과 무관한 비즈니스 로직만 처리
     */
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(MemberResponse.fromMember(userDetails.getMember()));
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateMember(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateMemberRequest updateMemberRequest
    ) {
        memberService.updateMember(userDetails.getMember().getId(), updateMemberRequest);
        return ResponseEntity.ok().build();
    }

}
