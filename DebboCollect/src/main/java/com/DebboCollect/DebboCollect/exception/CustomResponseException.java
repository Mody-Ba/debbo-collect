package com.DebboCollect.DebboCollect.exception;

import lombok.Getter;

@Getter
public class CustomResponseException extends RuntimeException {

    private final int status;

    public CustomResponseException(
            String message,
            int status
    ) {

        super(message);

        this.status = status;
    }
}