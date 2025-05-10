package com.baller.presentation.controller;

import com.baller.application.service.MemberService;
import com.baller.presentation.dto.request.member.SignUpRequest;
import com.baller.presentation.dto.request.member.UpdateMemberRequest;
import com.baller.presentation.dto.response.member.MemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMember() {
        return ResponseEntity.ok(memberService.getMember());
    }

    @PatchMapping("/me")
    public ResponseEntity<MemberResponse> updateMember(@Valid @RequestBody UpdateMemberRequest updateMemberRequest) {
        memberService.updateMember(updateMemberRequest);
        return ResponseEntity.ok().build();
    }

}
