package com.logalyses.loglibjava.service;

import com.logalyses.loglibjava.LogCollectorServiceGrpc;
import com.logalyses.loglibjava.LogsMessages;
import com.logalyses.loglibjava.LogCreatedResponse;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class LogCollectorServiceImpl extends LogCollectorServiceGrpc.LogCollectorServiceImplBase{

}
