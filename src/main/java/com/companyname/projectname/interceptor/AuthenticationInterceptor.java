package com.companyname.projectname.interceptor;

import com.companyname.projectname.annotation.RolesAllowed;
import com.companyname.projectname.common.RoleType;
import com.companyname.projectname.constant.CommonConstants;
import com.companyname.projectname.exception.CustomAuthenticationException;
import com.companyname.projectname.service.AuthenticationService;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import AuthenticationService;

@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Resource
    private AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RolesAllowed rolesAllowed = handlerMethod.getMethod().getAnnotation(RolesAllowed.class);
        if (rolesAllowed == null) {
            //No need to authenticate this method. Moving forward with interceptor chain.
            logger.debug("@RolesAllowed not specified. Moving forward without authentication." + httpServletRequest.getRequestURI());
            return true;
        }

        String user = httpServletRequest.getHeader(CommonConstants.HEADER_USER);
        String token = httpServletRequest.getHeader(CommonConstants.HEADER_TOKEN);

        if (StringUtils.isEmpty(user)) {
            logger.debug("Authentication headers missing for : " + httpServletRequest.getRequestURI());
            throw new CustomAuthenticationException("Authentication headers missing.");
        }
        RoleType[] roles = rolesAllowed.values();

        AuthenticationService.AuthenticationStatus authenticationStatus = authenticationService.authenticate(Sets.newHashSet(roles), user, token, httpServletRequest);
        if (!authenticationStatus.getStatus()) {
            logger.debug("Authentication failed for : " + httpServletRequest.getRequestURI() + " : Exception is : " + authenticationStatus.getException().getMessage());
            throw new CustomAuthenticationException(authenticationStatus.getException().getMessage());
        }
        return true;
    }
}
