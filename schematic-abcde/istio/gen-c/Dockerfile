FROM openjdk:8-jdk-slim

EXPOSE 8080

COPY target/c-1.0.jar /

CMD java -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=* -jar c-1.0.jar
