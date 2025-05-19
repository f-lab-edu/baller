package com.baller.application.aop;

import com.baller.common.annotation.RequireClubRole;
import com.baller.domain.enums.ClubRoleType;
import com.baller.domain.model.MemberClub;
import com.baller.infrastructure.mapper.MemberClubMapper;
import com.baller.security.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class ClubRoleCheckAspect {

    private final MemberClubMapper memberClubMapper;

    @Before("@annotation(requireClubRole)")
    public void beforeClubRoleCheck(JoinPoint joinPoint, RequireClubRole requireClubRole) {

        String paramName = requireClubRole.clubIdParam(); //어노테이션으로부터 파라미터 이름 추출(clubId)
        Long clubId = extractClubIdFromArgs(joinPoint, paramName);

        CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMember().getId();

        MemberClub memberClub = memberClubMapper.findByMemberIdAndClubId(memberId, clubId)
                .orElseThrow(() -> new AccessDeniedException("해당 동아리의 회원이 아닙니다."));

        ClubRoleType userRole = memberClub.getMemberRole();
        List<ClubRoleType> allowRoles = List.of(requireClubRole.value());

        if(!allowRoles.contains(userRole)) {
            throw new AccessDeniedException("해당 기능을 사용할 동아리 권한이 없습니다.");
        }
    }

    private Long extractClubIdFromArgs(JoinPoint joinPoint, String paramName) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames(); //메서드 파라미터 이름들 추출
        Object[] args = joinPoint.getArgs(); //메서드 실제 값들

        //파라미터 이름 배열을 돌면서 clubIdParam에 해당하는 값 찾기
        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(paramName)) {
                return (Long) args[i];
            }
        }

        throw new IllegalArgumentException("clubId 파라미터를 찾을 수 없습니다.");

    }

}
