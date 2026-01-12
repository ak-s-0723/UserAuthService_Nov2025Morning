package org.example.userauthservice_nov2025morning.exceptions;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String s) {
        super(s);
    }
}
