package com.example.keyvault.exception;

public class SecretNotFoundException extends KeyVaultException {

    public SecretNotFoundException(String message, String secretName) {
        super(message, secretName, ErrorCode.SECRET_NOT_FOUND);
    }

    public SecretNotFoundException(String message, Throwable cause, String secretName) {
        super(message, cause, secretName, ErrorCode.SECRET_NOT_FOUND);
    }
}