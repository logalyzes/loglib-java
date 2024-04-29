package com.logalyses.loglibjava.service;

import com.logalyses.loglibjava.LogsMessages.LogForCreate;

import java.util.concurrent.CompletableFuture;

public interface LogCollectorService {
    CompletableFuture<Void> createLogAsync(LogForCreate logForCreate);
}
