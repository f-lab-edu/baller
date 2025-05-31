package com.baller.presentation.controller;

import com.baller.application.service.ClubService;
import com.baller.domain.enums.ClubRoleType;
import com.baller.presentation.dto.request.club.CreateClubRequest;
import com.baller.presentation.dto.request.club.RejectClubApplyRequest;
import com.baller.presentation.dto.request.club.UpdateClubRequest;
import com.baller.presentation.dto.response.club.ClubApplyResponse;
import com.baller.presentation.dto.response.club.ClubResponse;
import com.baller.security.domain.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    public ResponseEntity<Void> createClub(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateClubRequest createClubRequest
    ) {
        clubService.createClub(userDetails.getMember().getId(), createClubRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ClubResponse>> getAllClubs() {
        return ResponseEntity.ok(clubService.getAllClubs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubResponse> getClubById(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.getClubById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateClub(@PathVariable long id, @Valid @RequestBody UpdateClubRequest updateClubRequest) {
        clubService.updateClub(id, updateClubRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/apply")
    public ResponseEntity<Void> applyClub(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        clubService.applyClub(userDetails.getMember().getId(), id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/apply")
    public ResponseEntity<List<ClubApplyResponse>> getClubApplyRequests(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.getClubApplyRequests(id));
    }

    @PostMapping("/{clubId}/apply/{applyId}/approve")
    public ResponseEntity<Void> approveClubApply(
            @PathVariable Long clubId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long applyId
    ) {
        clubService.approveClubApply(clubId, userDetails.getMember().getId(), applyId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{clubId}/apply/{applyId}/reject")
    public ResponseEntity<Void> rejectClubApply(
            @PathVariable Long clubId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long applyId,
            @RequestBody RejectClubApplyRequest reason
    ) {
        clubService.rejectClubApply(clubId, userDetails.getMember().getId(), applyId, reason.getReason());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdrawMemberClub(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        clubService.withdrawMemberClub(userDetails.getMember().getId(), id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{clubId}/members/{memberId}/role")
    public ResponseEntity<Void> updateMemberClubRole(@PathVariable Long clubId, @PathVariable Long memberId) {
        clubService.updateMemberClubRole(clubId, memberId, ClubRoleType.MANAGER);
        return ResponseEntity.ok().build();
    }

}
