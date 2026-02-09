package com.example.keyvault.exception;

public class SecretRetrievalException extends KeyVaultException {

    public SecretRetrievalException(String message, String secretName) {
        super(message, secretName, ErrorCode.UNKNOWN_ERROR);
    }

    public SecretRetrievalException(String message, Throwable cause, String secretName) {
        super(message, cause, secretName, ErrorCode.UNKNOWN_ERROR);
    }
}