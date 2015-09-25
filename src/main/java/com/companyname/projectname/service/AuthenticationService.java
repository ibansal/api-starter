package com.companyname.projectname.service;

import com.companyname.projectname.common.RoleType;
import com.companyname.projectname.constant.CommonConstants;
import com.companyname.projectname.exception.CustomAuthenticationException;
import com.google.common.collect.Sets;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Value("${url.user.permission}")
    private String fetchUserRolesUrl;

    @Resource
    private RestTemplate restTemplate;

    public AuthenticationStatus authenticate(Set<RoleType> roles, String user, String token, HttpServletRequest httpServletRequest) {
        logger.debug(fetchUserRolesUrl);
        //TODO - remove this block
        if (true) {
            return new AuthenticationStatus(true, null);
        }
        AuthenticationStatus authenticationStatus = null;
        try {
            HttpEntity<String> entity = new HttpEntity<>(getUserTokenHeaders(user, token));;
            ResponseEntity<String> exchanged = restTemplate.exchange(fetchUserRolesUrl, HttpMethod.POST, entity, String.class);
            if(!exchanged.getStatusCode().equals(HttpStatus.OK)) {
                throw new Exception("Roles API returned non 200 status.");
            }
            Set<RoleType> rolesFromServer = getRoles(exchanged.getBody());
            if (!CollectionUtils.isEmpty(rolesFromServer)) {
                Sets.SetView<RoleType> intersection = Sets.intersection(roles, rolesFromServer);
                for (RoleType roleType : intersection.immutableCopy()) {
                    if(roleType.isAllowed(httpServletRequest)) {
                        return new AuthenticationStatus(true, null);
                    }
                }
            }
            authenticationStatus = new AuthenticationStatus(false, new CustomAuthenticationException("User does not have enough permission to access this url"));
        } catch (Exception ex) {
            authenticationStatus = new AuthenticationStatus(false, ex);
        }
        return authenticationStatus;
    }

    private HttpHeaders getUserTokenHeaders(String user, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CommonConstants.HEADER_USER, user);
        headers.add(CommonConstants.HEADER_TOKEN, token);
        return headers;
    }

    private Set<RoleType> getRoles(String rolesResponse) {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(rolesResponse);
        JSONObject data = (JSONObject) jsonObject.get("data");
        JSONArray roles = (JSONArray) data.get("roles");
        Set<RoleType> roleTypes = new HashSet<>(roles.size());
        for (int i = 0; i < roles.size(); i++) {
            String roleString = (String) roles.get(i);
            roleString = roleString.split("_")[0];
            RoleType role = RoleType.getRole(roleString);
            if(role == null) {
                logger.warn("Invalid role name sent from authentication server : " + roleString);
            } else {
                roleTypes.add(role);
            }
        }
        return roleTypes;
    }

    public static class AuthenticationStatus {
        private Boolean status;
        private Exception exception;

        public AuthenticationStatus(Boolean status, Exception exception) {
            this.status = status;
            this.exception = exception;
        }

        public Boolean getStatus() {
            return status;
        }

        public Exception getException() {
            return exception;
        }
    }
}
