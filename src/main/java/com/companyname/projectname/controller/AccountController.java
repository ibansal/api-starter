package com.companyname.projectname.controller;

import com.companyname.projectname.annotation.ReadOnly;
import com.companyname.projectname.dto.RestResponse;
import com.companyname.projectname.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ishan.bansal on 6/24/15.
 */

@RestController
@RequestMapping("/v1/project-base-path")
public class AccountController {
    private static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    AccountService accountService;

    @ReadOnly
    @RequestMapping("status")
    @ResponseBody
    public ResponseEntity<RestResponse<String>> testApi() {
        RestResponse<String> restResponse = getRestResponse("Testing api.. It works!!!", true, null);
        ResponseEntity<RestResponse<String>> response = getOkRestResponseResponseEntity(restResponse);
        return response;
    }

    private <T> ResponseEntity<RestResponse<T>> getOkRestResponseResponseEntity(RestResponse<T> restResponse) {
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    private <T> RestResponse<T> getRestResponse(T data, boolean status, String errorMessage) {
        return new RestResponse<>(data, status, errorMessage);
    }

    private <T> RestResponse<T> getRestResponse(T data, boolean status, String errorMessage, int total) {
        return new RestResponse<>(data, status, errorMessage, total);
    }

    private String getErrors(List<FieldError> fieldErrors) {
        StringBuffer buffer = new StringBuffer();
        String prefix = "";
        for (FieldError error : fieldErrors) {
            buffer.append(prefix);
            prefix = " , ";
            buffer.append(error.getDefaultMessage());
        }
        return buffer.toString();
    }
}
