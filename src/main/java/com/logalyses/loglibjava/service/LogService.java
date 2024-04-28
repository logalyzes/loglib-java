package com.logalyses.loglibjava.service;

import com.logalyses.loglibjava.LogsMessages;

import java.util.concurrent.CompletableFuture;

public class LogService {

    private final LogCollectorClient logCollectorClient;

    public LogService(LogCollectorClient client) {
        this.logCollectorClient = client;
    }

    public CompletableFuture<Void> sendLog(String message, LogsMessages.LOG_LEVEL level, Throwable exception) {
        // Build the LogForCreate message
        LogsMessages.LogForCreate logForCreate = LogsMessages.LogForCreate.newBuilder()
                .setMessage(message)
                .setLevel(level)
                // You can add more fields such as stack trace, timestamp etc. based on your application needs
                .build();

        // Send the log message using the client
        return logCollectorClient.log(logForCreate)
                .thenAccept(response -> {
                    if (!response.getCreated()) {
                        throw new RuntimeException("Failed to create log");
                    }
                });
    }
}
