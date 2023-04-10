package com.example.learnenglish.responsestatus;

public class ResponseStatus {
    private String status;
    private String message;

    public ResponseStatus(Message message) {
        this.status = message.getStatus();
        this.message = message.getMessage();
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return status + '\'' +
                ", message='" + message;
    }
}

