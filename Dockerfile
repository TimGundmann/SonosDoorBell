FROM openjdk:10.0.2-13-jre-sid

COPY target/sonos.jar /app/sonos.jar

CMD ["java", "-jar", "/app/sonos.jar", "--spring.config.location=classpath:application.properties,file:/security/security.properties"]
