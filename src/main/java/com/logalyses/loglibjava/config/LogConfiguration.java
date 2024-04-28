package com.logalyses.loglibjava.config;

public class LogConfiguration {

    private String grpcServerAddress;
    private int grpcServerPort;
    private int maxRetryAttempts;
    private int retryIntervalSeconds;
    private String applicationName;
    private String applicationVersion;

    // Getters and setters for each field

    public String getGrpcServerAddress() {
        return grpcServerAddress;
    }

    public void setGrpcServerAddress(String grpcServerAddress) {
        this.grpcServerAddress = grpcServerAddress;
    }

    public int getGrpcServerPort() {
        return grpcServerPort;
    }

    public void setGrpcServerPort(int grpcServerPort) {
        this.grpcServerPort = grpcServerPort;
    }

    public int getMaxRetryAttempts() {
        return maxRetryAttempts;
    }

    public void setMaxRetryAttempts(int maxRetryAttempts) {
        this.maxRetryAttempts = maxRetryAttempts;
    }

    public int getRetryIntervalSeconds() {
        return retryIntervalSeconds;
    }

    public void setRetryIntervalSeconds(int retryIntervalSeconds) {
        this.retryIntervalSeconds = retryIntervalSeconds;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    // Potentially, load from properties file or environment variables
    public static LogConfiguration loadConfiguration() {
        // TODO: Implement loading logic
        return new LogConfiguration();
    }
}
