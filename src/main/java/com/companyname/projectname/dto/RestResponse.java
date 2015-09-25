package com.companyname.projectname.dto;

public class RestResponse<T> {
    private T data;
    private boolean success;
    private String errorMessage;
    int total;

    public RestResponse(T data, boolean success, String errorMessage) {
        this(data, success, errorMessage, 0);
    }

    public RestResponse(T data, boolean success, String errorMessage, int total) {
        this.data = data;
        this.success = success;
        this.errorMessage = errorMessage;
        this.total = total;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}