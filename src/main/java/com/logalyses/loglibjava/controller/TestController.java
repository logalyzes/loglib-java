package com.logalyses.loglibjava.controller;

import com.logalyses.loglibjava.Logger;
import com.logalyses.loglibjava.LogsMessages;
import com.logalyses.loglibjava.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class TestController {
    private final Logger logger;
    private final HealthService healthService;

    @Autowired
    public TestController(Logger logger, HealthService healthService) {
        this.logger = logger;
        this.healthService = healthService;
    }

    @PostMapping
    public String take(String log){
        return log;
    }

    @GetMapping("/test")
    public CompletableFuture<String> testEndpoint(@RequestParam(name = "name", defaultValue = "World") String name) {
        logger.log(LogsMessages.LOG_LEVEL.INFO, "Test endpoint called with name: " + name, null);
        return healthService.isServiceAvailableAsync("test-service")
                .thenApply(isHealthy -> {
                    if (isHealthy) {
                        logger.log(LogsMessages.LOG_LEVEL.INFO, "Health check passed for test-service", null);
                    } else {
                        logger.log(LogsMessages.LOG_LEVEL.ERROR, "Health check failed for test-service", null);
                    }
                    return "Hello, " + name + "!";
                })
                .exceptionally(ex -> {
                    logger.log(LogsMessages.LOG_LEVEL.ERROR, "Error during health check: " + ex.getMessage(), null);
                    return "Error checking health: " + ex.getMessage();
                });

    }
}
