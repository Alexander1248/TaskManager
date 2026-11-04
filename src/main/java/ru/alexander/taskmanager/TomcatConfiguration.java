package ru.alexander.taskmanager;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Accept-Language sent by some browsers can contain more locales than Tomcat's
 * default limit (10).  Increase the limit so those requests reach the H2
 * servlet and REST controllers instead of receiving HTTP 500.
 */
@Configuration
public class TomcatConfiguration {

    @Bean
    WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatLocaleCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> connector.setMaxLocaleCount(100));
    }
}
