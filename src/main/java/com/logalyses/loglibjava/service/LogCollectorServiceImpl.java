package com.logalyses.loglibjava.service;

import com.google.common.base.Preconditions;
import com.logalyses.loglibjava.config.ServiceConfig;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import com.logalyses.loglibjava.LogCollectorServiceGrpc;
import com.logalyses.loglibjava.LogsMessages.LogForCreate;
import com.logalyses.loglibjava.LogCreatedResponse;

import java.util.concurrent.CompletableFuture;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LogCollectorServiceImpl implements LogCollectorService {
    private ManagedChannel channel;
    private LogCollectorServiceGrpc.LogCollectorServiceStub stub;
    private final ServiceConfig config;

    @Autowired
    public LogCollectorServiceImpl(ServiceConfig config) {
        this.config = config;
    }

    @PostConstruct
    public void init() {
        try {
            Preconditions.checkNotNull(config.getLogcollector().getHost(), "Host cannot be null");
            Preconditions.checkNotNull(config.getLogcollector().getPort(), "Port cannot be null");

            this.channel = ManagedChannelBuilder
                    .forAddress(config.getLogcollector().getHost(), config.getLogcollector().getPort())
                    .usePlaintext()
                    .build();
            this.stub = LogCollectorServiceGrpc.newStub(channel);
        } catch (NullPointerException e) {
            System.out.println("Failed to initialize LogCollectorServiceImpl: " + e.getMessage());
            throw e;
        }
    }


    @Override
    public CompletableFuture<Void> createLogAsync(LogForCreate logForCreate) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        stub.create(logForCreate, new StreamObserver<LogCreatedResponse>() {
            @Override
            public void onNext(LogCreatedResponse logCreatedResponse) {
                if (logCreatedResponse.getCreated()) {
                    future.complete(null);
                } else {
                    future.completeExceptionally(new RuntimeException("Log creation failed"));
                }
            }

            @Override
            public void onError(Throwable t) {
                future.completeExceptionally(t);
            }

            @Override
            public void onCompleted() {
                // No operation needed here.
            }
        });
        return future;
    }
}
