package com.baller.presentation.controller;

import com.baller.application.service.ClubService;
import com.baller.presentation.dto.request.club.CreateClubRequest;
import com.baller.presentation.dto.request.club.RejectClubJoinApplyRequest;
import com.baller.presentation.dto.request.club.UpdateClubRequest;
import com.baller.presentation.dto.response.club.ClubJoinApplyResponse;
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
    public ResponseEntity<Void> clubJoinApply(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        clubService.clubJoinApply(userDetails.getMember().getId(), id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/apply")
    public ResponseEntity<List<ClubJoinApplyResponse>> getClubJoinApply(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.getClubJoinApply(id));
    }

    @PostMapping("/{clubId}/apply/{applyId}/approve")
    public ResponseEntity<Void> approveClubJoinApply(
            @PathVariable Long clubId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long applyId
    ) {
        clubService.approveClubJoinApply(clubId, userDetails.getMember().getId(), applyId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{clubId}/apply/{applyId}/reject")
    public ResponseEntity<Void> rejectClubJoinApply(
            @PathVariable Long clubId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long applyId,
            @RequestBody RejectClubJoinApplyRequest reason
    ) {
        clubService.rejectClubJoinApply(clubId, userDetails.getMember().getId(), applyId, reason.getReason());
        return ResponseEntity.ok().build();
    }

}
