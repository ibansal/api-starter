package com.companyname.projectname.annotation;

import com.companyname.projectname.common.RoleType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RolesAllowed {
    RoleType[] values();
}