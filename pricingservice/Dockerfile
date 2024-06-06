FROM openjdk:21
ADD target/pricingservice-0.0.1-SNAPSHOT.jar pricing-service.jar
EXPOSE 10001
ENTRYPOINT ["java","-jar","/pricing-service.jar"]
