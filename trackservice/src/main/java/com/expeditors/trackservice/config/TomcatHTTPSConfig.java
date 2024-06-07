package com.expeditors.trackservice.config;


import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.expeditors.trackservice.config.profiles.Profiles.SSL;

//@Configuration
@Profile( value = {SSL})
public class TomcatHTTPSConfig {

    private final static String CONNECTOR_PROTOCOL = "org.apache.coyote.http11.Http11NioProtocol";

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {

        return factory -> {

            var httpConnector = createHttpConnector();

            factory.getAdditionalTomcatConnectors().add(httpConnector);
            factory.getTomcatContextCustomizers().add(context -> {

                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);

                context.addConstraint(securityConstraint);
            });
        };
    }

    private Connector createHttpConnector() {
        Connector connector = new Connector(CONNECTOR_PROTOCOL);
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setPort(10004);
        connector.setRedirectPort(10005);
        return connector;
    }
}
