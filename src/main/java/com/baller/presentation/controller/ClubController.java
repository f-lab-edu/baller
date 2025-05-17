package com.baller.presentation.controller;

import com.baller.application.service.ClubService;
import com.baller.presentation.dto.request.club.CreateClubRequest;
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

}
