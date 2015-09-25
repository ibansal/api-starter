package com.companyname.projectname.controller;

import com.companyname.projectname.dto.RestResponse;
import com.companyname.projectname.exception.CustomAuthenticationException;
import com.companyname.projectname.exception.InvalidTransactionDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by vijay.rawat01 on 6/30/15.
 */
@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<RestResponse<String>> global(HttpServletRequest httpServletRequest, Exception exception) {
        exception.printStackTrace();
        ResponseEntity<RestResponse<String>> response = new ResponseEntity<>(new RestResponse<>("", false, "System is experiencing some turbulence. Please try later. "+ exception.getMessage() ), HttpStatus.INTERNAL_SERVER_ERROR);
        return response;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<RestResponse<String>> invalidParams(HttpServletRequest httpServletRequest, Exception exception) {
        exception.printStackTrace();
        ResponseEntity<RestResponse<String>> response = new ResponseEntity<>(new RestResponse<>("", false, "Required params not present."), HttpStatus.INTERNAL_SERVER_ERROR);
        return response;
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    @ResponseBody
    public ResponseEntity<RestResponse<String>> authenticationExceptionHandler(HttpServletRequest httpServletRequest, CustomAuthenticationException exception) {
        ResponseEntity<RestResponse<String>> response = new ResponseEntity<>(new RestResponse<>("", false, exception.getMessage()), HttpStatus.UNAUTHORIZED);
        return response;
    }

    @ExceptionHandler(InvalidTransactionDataException.class)
    @ResponseBody
    public ResponseEntity<RestResponse<String>> transactionDataExceptionHandler(HttpServletRequest httpServletRequest, InvalidTransactionDataException exception) {
        ResponseEntity<RestResponse<String>> response = new ResponseEntity<>(new RestResponse<>("", false, exception.getMessage()), HttpStatus.BAD_REQUEST);
        return response;
    }

}
