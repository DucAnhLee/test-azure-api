# Swagger/OpenAPI Documentation Guide

## Overview

This project includes comprehensive Swagger/OpenAPI documentation for all REST endpoints. The documentation is automatically generated from code annotations and provides an interactive interface for testing the API.

## Accessing Swagger UI

### 1. Start the Application

```bash
mvn spring-boot:run
```

### 2. Open Swagger UI in Browser

Navigate to: **http://localhost:8080/swagger-ui.html**

### 3. Alternative: OpenAPI JSON Specification

For the raw OpenAPI specification: **http://localhost:8080/api-docs**

## Features

### Interactive API Testing
- **Try it out**: Test endpoints directly from the browser
- **Request examples**: Pre-filled example values for all parameters
- **Response schemas**: See the structure of success and error responses
- **HTTP status codes**: Complete documentation of all possible responses

### API Documentation Includes

#### Azure Key Vault Tag
All Key Vault related endpoints are grouped under the "Azure Key Vault" tag.

#### Endpoints Documented

**1. GET /api/keyvault/secret/{name}**
- **Summary**: Retrieve a secret from Azure Key Vault
- **Description**: Retrieves a secret value from Azure Key Vault by its name
- **Parameters**:
  - `name` (path parameter): Name of the secret to retrieve
  - Example: `database-password`
- **Responses**:
  - `200 OK`: Secret retrieved successfully
  - `401 Unauthorized`: Authentication failed
  - `404 Not Found`: Secret not found
  - `500 Internal Server Error`: Server error

**2. GET /api/keyvault/health**
- **Summary**: Health check endpoint
- **Description**: Checks if the Azure Key Vault integration service is running
- **Responses**:
  - `200 OK`: Service is healthy

### Response Model Schema

The `SecretResponse` model includes:
- `name` (string): Name of the secret
- `value` (string): Value of the secret (only on success)
- `retrievedAt` (datetime): Timestamp of retrieval
- `success` (boolean): Operation success indicator
- `errorMessage` (string): Error details (only on failure)

## Configuration

### Swagger Configuration Location
`src/main/java/com/example/keyvault/config/OpenApiConfig.java`

### Application Properties
```yaml
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
    operationsSorter: method
    tagsSorter: alpha
```

### Customization Options

#### Change Swagger UI Path
```yaml
springdoc:
  swagger-ui:
    path: /custom-swagger-path
```

#### Disable Swagger in Production
```yaml
springdoc:
  swagger-ui:
    enabled: false
```

#### Change API Docs Path
```yaml
springdoc:
  api-docs:
    path: /custom-api-docs
```

## Using Swagger UI

### Step-by-Step Guide

1. **Navigate to Swagger UI**
   - Open http://localhost:8080/swagger-ui.html

2. **Expand an Endpoint**
   - Click on any endpoint to see details

3. **Try an Endpoint**
   - Click "Try it out" button
   - Enter parameter values (e.g., secret name: `test-secret`)
   - Click "Execute"

4. **View Response**
   - See the actual response from your Azure Key Vault
   - Check response code, headers, and body

### Example: Testing Secret Retrieval

1. Expand `GET /api/keyvault/secret/{name}`
2. Click "Try it out"
3. Enter secret name: `database-password`
4. Click "Execute"
5. View the response:

**Success Response (200):**
```json
{
  "name": "database-password",
  "value": "mySecretValue123",
  "retrievedAt": "2024-01-15T10:30:00",
  "success": true,
  "errorMessage": null
}
```

**Error Response (404):**
```json
{
  "name": "non-existent-secret",
  "value": null,
  "retrievedAt": "2024-01-15T10:30:00",
  "success": false,
  "errorMessage": "Secret not found"
}
```

## Annotations Used

### Controller Level
```java
@Tag(name = "Azure Key Vault", description = "APIs for retrieving secrets")
```

### Method Level
```java
@Operation(
    summary = "Short description",
    description = "Detailed description"
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Success"),
    @ApiResponse(responseCode = "404", description = "Not found")
})
```

### Parameter Level
```java
@Parameter(
    description = "Parameter description",
    required = true,
    example = "example-value"
)
```

### Model Level
```java
@Schema(description = "Model description")
private String fieldName;
```

## Benefits

✅ **Interactive Testing**: Test APIs without external tools like Postman
✅ **Auto-Generated**: Documentation stays in sync with code
✅ **Comprehensive**: All endpoints, parameters, and responses documented
✅ **Developer Friendly**: Easy to understand and use
✅ **Standards Compliant**: Uses OpenAPI 3.0 specification
✅ **Export Capability**: Download OpenAPI JSON for use in other tools

## Integration with Other Tools

### Postman
1. Download OpenAPI JSON from http://localhost:8080/api-docs
2. Import into Postman: File → Import → Paste Raw Text
3. All endpoints will be available in Postman

### API Clients
Use the OpenAPI specification to generate client libraries in various languages:
- Java
- Python
- JavaScript/TypeScript
- C#
- Go
- And many more

## Production Considerations

### Security
- Consider disabling Swagger UI in production
- Use authentication/authorization for Swagger endpoints
- Restrict access to internal networks only

### Configuration for Production
```yaml
spring:
  profiles: prod

springdoc:
  swagger-ui:
    enabled: false  # Disable in production
  api-docs:
    enabled: false  # Disable in production
```

## Troubleshooting

### Swagger UI Not Loading
- Check if application is running: http://localhost:8080/api/keyvault/health
- Verify port 8080 is not blocked
- Check application logs for errors

### Endpoints Not Showing
- Ensure controllers have proper annotations
- Check if springdoc dependency is in pom.xml
- Restart the application

### Authentication Issues in Swagger
- Swagger UI tests use the same authentication as regular API calls
- Ensure .env file has valid Azure credentials
- Check application logs for authentication errors

## Additional Resources

- [SpringDoc OpenAPI Documentation](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Swagger UI Documentation](https://swagger.io/tools/swagger-ui/)
