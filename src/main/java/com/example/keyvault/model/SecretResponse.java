package com.example.keyvault.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Response model for secret retrieval operations")
public class SecretResponse {
    
    @Schema(description = "Name of the secret", example = "database-password")
    private String name;
    
    @Schema(description = "Value of the secret (only present on successful retrieval)", example = "mySecretValue123")
    private String value;
    
    @Schema(description = "Timestamp when the secret was retrieved", example = "2024-01-15T10:30:00")
    private LocalDateTime retrievedAt;
    
    @Schema(description = "Indicates whether the operation was successful", example = "true")
    private boolean success;
    
    @Schema(description = "Error message (only present when success is false)", example = "Secret not found")
    private String errorMessage;

    public SecretResponse() {
    }

    // Static factory methods to avoid constructor ambiguity
    public static SecretResponse success(String name, String value) {
        SecretResponse response = new SecretResponse();
        response.name = name;
        response.value = value;
        response.retrievedAt = LocalDateTime.now();
        response.success = true;
        return response;
    }

    public static SecretResponse error(String name, String errorMessage) {
        SecretResponse response = new SecretResponse();
        response.name = name;
        response.errorMessage = errorMessage;
        response.retrievedAt = LocalDateTime.now();
        response.success = false;
        return response;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getRetrievedAt() {
        return retrievedAt;
    }

    public void setRetrievedAt(LocalDateTime retrievedAt) {
        this.retrievedAt = retrievedAt;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}