package com.logalyses.loglibjava.service;

import java.util.concurrent.CompletableFuture;

public interface HealthService {
    CompletableFuture<Boolean> isServiceAvailableAsync(String service);
}
