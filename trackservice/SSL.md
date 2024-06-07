# SSL Configuration

- **SSL**: Secure Sockets Layer
- **TLS**: Transport Layer Security

## Useful Links
- [Securing Spring Boot App with SSL Blog](https://spring.io/blog/2023/06/07/securing-spring-boot-applications-with-ssl)
- [Securing Spring Boot 3 Applications With SSL Bundles](https://www.baeldung.com/spring-boot-security-ssl-bundles)
- [Spring Boot Docs](https://docs.spring.io/spring-boot/reference/io/rest-client.html)

Activate SSL Debug Mode On: -Djavax.net.debug=ssl:handshake

## What is an SSL certificate?
- An SSL certificate is like an ID card or a badge that proves someone is who they say they are. [learn more .. ](https://www.cloudflare.com/learning/ssl/what-is-ssl/)

### Types of SSL Certificates
- Single-domain: A single-domain SSL certificate applies to only one domain (a "domain" is the name of a website, like www.cloudflare.com).
- Wildcard: Like a single-domain certificate, a wildcard SSL certificate applies to only one domain. However, it also includes that domain's subdomains. For example, a wildcard certificate could cover www.cloudflare.com, blog.cloudflare.com, and developers.cloudflare.com, while a single-domain certificate could only cover the first.
- Multi-domain: As the name indicates, multi-domain SSL certificates can apply to multiple unrelated domains.


Properties used to configure SSL trust material are under the `spring.ssbundle` prefix in an `application.yaml`
- `spring.ssl.bundle.jks` can be used to configure bundles using Java keystore files.
- `spring.ssl.bundle.pem` can be used to configure bundles using PEM-encoded text files.

### Key Tool Certificate Generation Command

#### Format
```
keytool -genkeypair -alias `alias-name` -keyalg `key-alrgorithm-to-use: default: RSA` -keysize `Default 2048` -storetype `PKCS12` -keystore `name-of-the-store`
```
#### Example
```bash
 keytool -genkeypair -alias pricing-ssl -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore pricing-ssl.p12
```
### Properties Configuration

To Configure Bundle
```properties
spring.ssl.bundle.jks.web-server.key.alias = larkuspring
spring.ssl.bundle.jks.web-server.key.password = ${CLIENT_PASSWORD}
spring.ssl.bundle.jks.web-server.keystore.location = classpath:larkUKeyfile.p12
spring.ssl.bundle.jks.web-server.keystore.password = ${CLIENT_PASSWORD}
spring.ssl.bundle.jks.web-server.keystore.type = PKCS12
```

To Specify Bundle's Name
```properties
server.ssl.bundle = web-server
```

### Anil Notes
```
To configure RestClient to use SSL

  Two ways to make it happen:
  - Set up the Client to use your self signed certificate created above.
    More info below.
  - Or, add your certificate the the official jdk certificates on your machine
    (the cacerts file).  Also more info below.
    
Configure the client to use a certificate:  Look in SSLConfig.java
  I still haven't found a clean way to set up just the RestClient to use SSL directly.
  
  Doing it right now through the RestTemplate.
    - Even with the RestTemplate, allegedly it should work through SSL bundles, but not
      happening for me yet.
    - So we are using pretty boiler plate code to set up the RestTemplate, and then
      creating the RestClient from that.
```

In order to set up ssl we need to:
- let Tomcat know that we are going to use SSL through configuration
   ```java
      public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                super.postProcessContext(context);

                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(createHttpConnector());
        return tomcat;
    }

  ``` 
- Set up redirection for http coming up to our server by setting a connector
  ```java
    private Connector createHttpConnector() {
    Connector connector = new Connector(CONNECTOR_PROTOCOL);
    connector.setScheme("http");
    connector.setSecure(false);
    connector.setPort(8080);
    connector.setRedirectPort(8443);
    return connector;
    }
    ``` 
  
### Another Approach

If we want to avoid writing a lot of code, we can create a store and import the self signed
certificate we created on the store. This will allow spring to recognize that
as a trusted certificate

```bash
keytool -import -noprompt -trustcacerts -alias ALIASNAME -file FILENAME_OF_THE_INSTALLED_CERTIFICATE -keystore PATH_TO_CACERTS_FILE -storepass PASSWORD
```

```bash
keytool -exportcert -keystore larkUKeyfile.p12 -storepass password -storetype PKCS12 -alias larkuspring -file larkUPublicKey.cer
```