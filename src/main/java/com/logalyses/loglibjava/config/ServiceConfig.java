package com.logalyses.loglibjava.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "logalyses")
public class ServiceConfig {
    private ServiceDetails health = new ServiceDetails();
    private ServiceDetails logcollector = new ServiceDetails();

    public ServiceDetails getHealth() {
        return health;
    }

    public void setHealth(ServiceDetails health) {
        this.health = health;
    }

    public ServiceDetails getLogcollector() {
        return logcollector;
    }

    public void setLogcollector(ServiceDetails logcollector) {
        this.logcollector = logcollector;
    }

    public static class ServiceDetails {
        private String host;
        private int port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
