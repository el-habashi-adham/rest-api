package com.aeh.api.payloads;

public class ApiResponse {
    private Boolean success;
    private String message;

    public ApiResponse() {}

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return String.format("success:" + this.success + ",message:" + this.message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiResponse that = (ApiResponse) o;

        if (!success.equals(that.success)) return false;
        return message.equals(that.message);
    }

    @Override
    public int hashCode() {
        int result = success.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}
