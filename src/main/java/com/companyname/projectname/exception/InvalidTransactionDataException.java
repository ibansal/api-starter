package com.companyname.projectname.exception;

/**
 * Created by ishan.bansal on 6/30/15.
 */
public class InvalidTransactionDataException extends Exception{

    public InvalidTransactionDataException() {

    };

    public InvalidTransactionDataException(String message){
        super(message);
    }
}
