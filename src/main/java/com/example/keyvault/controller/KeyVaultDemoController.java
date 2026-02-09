package com.example.keyvault.controller;

import com.example.keyvault.exception.AuthenticationFailedException;
import com.example.keyvault.exception.SecretNotFoundException;
import com.example.keyvault.model.SecretResponse;
import com.example.keyvault.service.KeyVaultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/keyvault")
@Tag(name = "Azure Key Vault", description = "APIs for retrieving secrets from Azure Key Vault")
public class KeyVaultDemoController {

    private static final Logger logger = LoggerFactory.getLogger(KeyVaultDemoController.class);
    
    private final KeyVaultService keyVaultService;

    public KeyVaultDemoController(KeyVaultService keyVaultService) {
        this.keyVaultService = keyVaultService;
    }

    @Operation(
        summary = "Retrieve a secret from Azure Key Vault",
        description = "Retrieves a secret value from Azure Key Vault by its name. Requires valid service principal authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Secret retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SecretResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication failed - Invalid service principal credentials",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SecretResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Secret not found in Key Vault",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SecretResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SecretResponse.class)
            )
        )
    })
    @GetMapping("/secret/{name}")
    public ResponseEntity<SecretResponse> getSecret(
            @Parameter(description = "Name of the secret to retrieve from Azure Key Vault", required = true, example = "database-password")
            @PathVariable String name) {
        logger.info("Received request to retrieve secret: {}", name);
        
        try {
            String secretValue = keyVaultService.retrieveSecret(name);
            SecretResponse response = SecretResponse.success(name, secretValue);
            logger.info("Successfully returned secret: {}", name);
            return ResponseEntity.ok(response);
            
        } catch (AuthenticationFailedException e) {
            logger.error("Authentication failed for secret: {}", name, e);
            SecretResponse response = SecretResponse.error(name, "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            
        } catch (SecretNotFoundException e) {
            logger.error("Secret not found: {}", name, e);
            SecretResponse response = SecretResponse.error(name, "Secret not found: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            
        } catch (Exception e) {
            logger.error("Internal error retrieving secret: {}", name, e);
            SecretResponse response = SecretResponse.error(name, "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(
        summary = "Retrieve a secret from Azure Key Vault (V0 - Direct SDK call)",
        description = "Alternative endpoint that creates a new SecretClient for each request. " +
                "This is the V0 implementation that doesn't use the cached client. " +
                "Use this for testing or when you need a fresh connection."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Secret retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SecretResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication failed - Invalid service principal credentials",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SecretResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Secret not found in Key Vault",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SecretResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SecretResponse.class)
            )
        )
    })
    @GetMapping("/secret/v0/{name}")
    public ResponseEntity<SecretResponse> getSecretV0(
            @Parameter(description = "Name of the secret to retrieve from Azure Key Vault", required = true, example = "database-password")
            @PathVariable String name) {
        logger.info("Received V0 request to retrieve secret: {}", name);
        
        try {
            String secretValue = keyVaultService.getSecretV0(name);
            SecretResponse response = SecretResponse.success(name, secretValue);
            logger.info("Successfully returned secret (V0): {}", name);
            return ResponseEntity.ok(response);
            
        } catch (AuthenticationFailedException e) {
            logger.error("Authentication failed for secret (V0): {}", name, e);
            SecretResponse response = SecretResponse.error(name, "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            
        } catch (SecretNotFoundException e) {
            logger.error("Secret not found (V0): {}", name, e);
            SecretResponse response = SecretResponse.error(name, "Secret not found: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            
        } catch (Exception e) {
            logger.error("Internal error retrieving secret (V0): {}", name, e);
            SecretResponse response = SecretResponse.error(name, "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(
        summary = "Health check endpoint",
        description = "Checks if the Azure Key Vault integration service is running"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Service is healthy and running",
            content = @Content(mediaType = "text/plain")
        )
    })
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Azure Key Vault integration is running");
    }
}
