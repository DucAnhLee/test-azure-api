package com.example.keyvault.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "azure.keyvault")
@Validated
public class AzureKeyVaultProperties {

    @NotBlank(message = "Azure Key Vault client ID is required")
    private String clientId;

    @NotBlank(message = "Azure Key Vault client secret is required")
    private String clientSecret;

    @NotBlank(message = "Azure Key Vault tenant ID is required")
    private String tenantId;

    @NotBlank(message = "Azure Key Vault URL is required")
    private String vaultUrl;

    private boolean enabled = true;

    // Getters and setters
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getVaultUrl() {
        return vaultUrl;
    }

    public void setVaultUrl(String vaultUrl) {
        this.vaultUrl = vaultUrl;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}