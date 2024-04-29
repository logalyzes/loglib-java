package com.logalyses.loglibjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import com.logalyses.loglibjava.service.LogCollectorService;
import com.logalyses.loglibjava.service.HealthService;
import com.logalyses.loglibjava.LogsMessages.LogForCreate;
import com.logalyses.loglibjava.LogsMessages.LOG_LEVEL;

import java.time.Instant;
import java.util.concurrent.*;

@Component
public class Logger extends PrintStream {
    private final LogCollectorService logService;
    private final HealthService healthService;
    private final BlockingQueue<LogForCreate> logQueue;
    private final ExecutorService executorService;

    @Autowired
    public Logger(LogCollectorService logService, HealthService healthService) {
        super(System.out);
        this.logService = logService;
        this.healthService = healthService;
        this.logQueue = new LinkedBlockingQueue<>();
        this.executorService = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "LoggerThread");
            t.setDaemon(true);
            return t;
        });
        processLogs();
    }

    public void log(LOG_LEVEL level, String message, String stackTrace) {
        super.println("[" + Instant.now() + "] [" + level + "] " + message);

        LogForCreate log = LogForCreate.newBuilder()
                .setLevel(level)
                .setMessage(message)
                .setStackTrace(stackTrace != null ? stackTrace : "")
                .build();

        this.logQueue.add(log);
    }

    private void processLogs() {
        executorService.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    LogForCreate log = logQueue.take();
                    CompletableFuture<Boolean> availableFuture = healthService.isServiceAvailableAsync("LogService");
                    availableFuture.thenAccept(available -> {
                        if (available) {
                            logService.createLogAsync(log);
                        }
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    @Override
    public void close() {
        super.close();
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
