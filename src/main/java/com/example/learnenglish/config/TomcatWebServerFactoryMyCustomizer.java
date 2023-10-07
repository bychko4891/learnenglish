package com.example.learnenglish.config;
/*
*  Цей класс потрібен щоб отримати IP юзера, якщо додаток на сервері опрацьовує HTTP/HTTPS запроси через зворотний проксі
*  Також належно потрібно налаштувати веб сервер який і буде працювати в режимі зворотного проксі
*  Корректні настройки NGINX в режимі зворотного проксі:
*  location / {
*	     proxy_pass https://localhost:8443;
*        proxy_set_header Host $host:$server_port;
*	     proxy_set_header X-Forwarded-Host $server_name;
*        proxy_set_header X-Real-IP $remote_addr;
*	     proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
*                }
* Також обов'язково в application.properties --> server.forward-headers-strategy=native
*/

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
        valve.setProtocolHeaderHttpsValue("https");
        return valve;
    }
}
