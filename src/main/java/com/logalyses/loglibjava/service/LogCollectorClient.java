package com.logalyses.loglibjava.service;

import com.logalyses.loglibjava.LogCollectorServiceGrpc;
import com.logalyses.loglibjava.LogsMessages.LogForCreate;
import com.logalyses.loglibjava.LogCreatedResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CompletableFuture;

public class LogCollectorClient {

    private final LogCollectorServiceGrpc.LogCollectorServiceStub asyncStub;

    public LogCollectorClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        asyncStub = LogCollectorServiceGrpc.newStub(channel);
    }

    public CompletableFuture<LogCreatedResponse> log(LogForCreate logForCreate) {
        CompletableFuture<LogCreatedResponse> future = new CompletableFuture<>();
        StreamObserver<LogCreatedResponse> responseObserver = new StreamObserver<LogCreatedResponse>() {
            @Override
            public void onNext(LogCreatedResponse value) {
                future.complete(value);
            }

            @Override
            public void onError(Throwable t) {
                future.completeExceptionally(t);
            }

            @Override
            public void onCompleted() {
                // Nothing to do here
            }
        };

        asyncStub.create(logForCreate, responseObserver);
        return future;
    }
}
