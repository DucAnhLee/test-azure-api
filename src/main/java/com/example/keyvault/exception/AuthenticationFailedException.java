package com.example.keyvault.exception;

public class AuthenticationFailedException extends KeyVaultException {

    public AuthenticationFailedException(String message, String secretName) {
        super(message, secretName, ErrorCode.AUTHENTICATION_FAILED);
    }

    public AuthenticationFailedException(String message, Throwable cause, String secretName) {
        super(message, cause, secretName, ErrorCode.AUTHENTICATION_FAILED);
    }
}