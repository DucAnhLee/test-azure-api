package com.example.keyvault.client;

import com.azure.core.exception.ClientAuthenticationException;
import com.azure.core.exception.ResourceNotFoundException;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.example.keyvault.config.AzureKeyVaultProperties;
import com.example.keyvault.exception.AuthenticationFailedException;
import com.example.keyvault.exception.SecretNotFoundException;
import com.example.keyvault.exception.SecretRetrievalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AzureKeyVaultClient {

    private static final Logger logger = LoggerFactory.getLogger(AzureKeyVaultClient.class);
    
    private final SecretClient secretClient;
    private final AzureKeyVaultProperties properties;

    public AzureKeyVaultProperties getProperties() {
        return this.properties;
    }

    public AzureKeyVaultClient(AzureKeyVaultProperties properties) {
        logger.info("Initializing Azure Key Vault client for vault: {}", properties.getVaultUrl());
        this.properties = properties;
        try {
            ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                    .clientId(properties.getClientId())
                    .clientSecret(properties.getClientSecret())
                    .tenantId(properties.getTenantId())
                    .build();

            this.secretClient = new SecretClientBuilder()
                    .vaultUrl(properties.getVaultUrl())
                    .credential(credential)
                    .buildClient();
                    
            logger.info("Azure Key Vault client initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize Azure Key Vault client", e);
            throw new AuthenticationFailedException(
                "Failed to initialize Azure Key Vault client: " + e.getMessage(), 
                e, 
                "N/A"
            );
        }
    }

    public String getSecret(String secretName) {
        logger.debug("Retrieving secret: {}", secretName);
        
        try {
            KeyVaultSecret secret = secretClient.getSecret(secretName);
            logger.debug("Successfully retrieved secret: {}", secretName);
            return secret.getValue();
            
        } catch (ClientAuthenticationException e) {
            logger.error("Authentication failed while retrieving secret: {}", secretName, e);
            throw new AuthenticationFailedException(
                "Authentication failed for secret: " + secretName, 
                e, 
                secretName
            );
            
        } catch (ResourceNotFoundException e) {
            logger.error("Secret not found: {}", secretName, e);
            throw new SecretNotFoundException(
                "Secret not found: " + secretName, 
                e, 
                secretName
            );
            
        } catch (Exception e) {
            logger.error("Failed to retrieve secret: {}", secretName, e);
            throw new SecretRetrievalException(
                "Failed to retrieve secret: " + secretName + ". Error: " + e.getMessage(), 
                e, 
                secretName
            );
        }
    }
}