package com.hundsun.demo.springcloud.security.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * @author hulei
 * @since 2025/7/24 22:05
 */

public class VerificationCodeException extends AuthenticationException {

    public VerificationCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public VerificationCodeException(String msg) {
        super(msg);
    }
}
