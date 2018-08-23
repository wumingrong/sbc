package com.barton.sbc.exception;

import org.springframework.security.core.AuthenticationException;

public class MyAuthenticationException extends AuthenticationException {


    public MyAuthenticationException(String msg) {
        super(msg);
    }

    public MyAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }


}
