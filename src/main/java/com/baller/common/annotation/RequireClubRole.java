package com.baller.common.annotation;

import com.baller.domain.enums.ClubRoleType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireClubRole {
    ClubRoleType[] value();
    String clubIdParam() default "clubId";
}
