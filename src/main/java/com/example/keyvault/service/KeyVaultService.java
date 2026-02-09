package com.example.keyvault.service;

import com.azure.core.exception.ClientAuthenticationException;
import com.azure.core.exception.ResourceNotFoundException;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.example.keyvault.client.AzureKeyVaultClient;
import com.example.keyvault.exception.AuthenticationFailedException;
import com.example.keyvault.exception.SecretNotFoundException;
import com.example.keyvault.exception.SecretRetrievalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KeyVaultService {

    private static final Logger logger = LoggerFactory.getLogger(KeyVaultService.class);
    
    private final AzureKeyVaultClient keyVaultClient;

    public KeyVaultService(AzureKeyVaultClient keyVaultClient) {
        this.keyVaultClient = keyVaultClient;
    }

    public String getSecretV0(String secretName) {
        logger.debug("Retrieving secret: {}", secretName);
        
        try {
            ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                    .clientId(keyVaultClient.getProperties().getClientId())
                    .clientSecret(keyVaultClient.getProperties().getClientSecret())
                    .tenantId(keyVaultClient.getProperties().getTenantId())
                    .build();

            SecretClient secretClient = new SecretClientBuilder()
                    .vaultUrl(keyVaultClient.getProperties().getVaultUrl())
                    .credential(credential)
                    .buildClient();
            
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

    public String retrieveSecret(String secretName) throws SecretRetrievalException {
        logger.info("Retrieving secret from Key Vault: {}", secretName);
        
        try {
            String secretValue = keyVaultClient.getSecret(secretName);
            logger.info("Successfully retrieved secret: {}", secretName);
            return secretValue;
            
        } catch (Exception e) {
            logger.error("Failed to retrieve secret: {}", secretName, e);
            throw e; // Re-throw the specific exception from the client
        }
    }
}