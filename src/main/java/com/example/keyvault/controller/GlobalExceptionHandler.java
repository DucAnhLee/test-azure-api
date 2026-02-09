package com.example.keyvault.controller;

import com.example.keyvault.exception.AuthenticationFailedException;
import com.example.keyvault.exception.KeyVaultException;
import com.example.keyvault.exception.SecretNotFoundException;
import com.example.keyvault.model.SecretResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<SecretResponse> handleAuthenticationFailed(AuthenticationFailedException e) {
        logger.error("Authentication failed: {}", e.getMessage());
        SecretResponse response = SecretResponse.error(e.getSecretName(), "Authentication failed");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(SecretNotFoundException.class)
    public ResponseEntity<SecretResponse> handleSecretNotFound(SecretNotFoundException e) {
        logger.error("Secret not found: {}", e.getMessage());
        SecretResponse response = SecretResponse.error(e.getSecretName(), "Secret not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(KeyVaultException.class)
    public ResponseEntity<SecretResponse> handleKeyVaultException(KeyVaultException e) {
        logger.error("Key Vault error: {}", e.getMessage());
        SecretResponse response = SecretResponse.error(e.getSecretName(), "Key Vault error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SecretResponse> handleGenericException(Exception e) {
        logger.error("Unexpected error: {}", e.getMessage(), e);
        SecretResponse response = SecretResponse.error("unknown", "Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}