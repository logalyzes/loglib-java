package com.logalyses.loglibjava.service;

import com.logalyses.loglibjava.HealthCheckRequest;
import com.logalyses.loglibjava.HealthCheckResponse;
import com.logalyses.loglibjava.HealthGrpc;
import com.logalyses.loglibjava.config.ServiceConfig;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class HealthServiceImpl implements HealthService {
    private ManagedChannel channel;
    private HealthGrpc.HealthStub stub;
    private final ServiceConfig config;

    @Autowired
    public HealthServiceImpl(ServiceConfig config) {
        this.config = config;
    }

    @PostConstruct
    public void init() {
        this.channel = ManagedChannelBuilder
                .forAddress(config.getHealth().getHost(), config.getHealth().getPort())
                .usePlaintext()
                .build();
        this.stub = HealthGrpc.newStub(channel);
    }

    @Override
    public CompletableFuture<Boolean> isServiceAvailableAsync(String service) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        HealthCheckRequest request = HealthCheckRequest.newBuilder().setService(service).build();
        stub.check(request, new StreamObserver<HealthCheckResponse>() {
            @Override
            public void onNext(HealthCheckResponse response) {
                future.complete(response.getStatus() == HealthCheckResponse.ServingStatus.SERVING);
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
