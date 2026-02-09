package com.example.keyvault.exception;

public class KeyVaultException extends RuntimeException {
    
    private final String secretName;
    private final ErrorCode errorCode;

    public KeyVaultException(String message, String secretName, ErrorCode errorCode) {
        super(message);
        this.secretName = secretName;
        this.errorCode = errorCode;
    }

    public KeyVaultException(String message, Throwable cause, String secretName, ErrorCode errorCode) {
        super(message, cause);
        this.secretName = secretName;
        this.errorCode = errorCode;
    }

    public String getSecretName() {
        return secretName;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public enum ErrorCode {
        AUTHENTICATION_FAILED,
        SECRET_NOT_FOUND,
        NETWORK_ERROR,
        CONFIGURATION_ERROR,
        UNKNOWN_ERROR
    }
}