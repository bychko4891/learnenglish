package com.example.learnenglish.config;

import org.apache.catalina.Valve;
import org.apache.catalina.valves.RemoteIpValve;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatWebServerFactoryMyCustomizer {
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {
        return (TomcatServletWebServerFactory factory) -> {
            factory.addContextValves(createRemoteIpValve());
        };
    }

    private Valve createRemoteIpValve() {
        RemoteIpValve valve = new RemoteIpValve();
        valve.setRemoteIpHeader("X-Forwarded-For");
        valve.setProtocolHeader("X-Forwarded-Proto");
        valve.setProtocolHeaderHttpsValue("https"); // Optional: Specify the HTTPS protocol value
        return valve;
    }
}
