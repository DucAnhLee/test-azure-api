package com.example.keyvault.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AzureKeyVaultProperties.class)
@ConditionalOnProperty(prefix = "azure.keyvault", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AzureKeyVaultConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(AzureKeyVaultConfiguration.class);

    public AzureKeyVaultConfiguration(AzureKeyVaultProperties properties) {
        logger.info("Azure Key Vault configuration enabled for vault: {}", properties.getVaultUrl());
        
        // Validate configuration at startup
        validateConfiguration(properties);
    }

    private void validateConfiguration(AzureKeyVaultProperties properties) {
        if (properties.getClientId() == null || properties.getClientId().trim().isEmpty()) {
            throw new IllegalArgumentException("Azure Key Vault client ID is required");
        }
        if (properties.getClientSecret() == null || properties.getClientSecret().trim().isEmpty()) {
            throw new IllegalArgumentException("Azure Key Vault client secret is required");
        }
        if (properties.getTenantId() == null || properties.getTenantId().trim().isEmpty()) {
            throw new IllegalArgumentException("Azure Key Vault tenant ID is required");
        }
        if (properties.getVaultUrl() == null || properties.getVaultUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("Azure Key Vault URL is required");
        }
        
        logger.info("Azure Key Vault configuration validation passed");
    }
}