# PowerShell script to load .env file and run Spring Boot

Write-Host "Loading environment variables from .env file..."

# Read .env file and set environment variables
if (Test-Path ".env") {
    Get-Content ".env" | ForEach-Object {
        if ($_ -match "^([^#][^=]+)=(.*)$") {
            $name = $matches[1].Trim()
            $value = $matches[2].Trim()
            [Environment]::SetEnvironmentVariable($name, $value, "Process")
            Write-Host "Set $name=$value"
        }
    }
    
    Write-Host "Starting Spring Boot application..."
    mvn spring-boot:run
} else {
    Write-Host ".env file not found!"
}