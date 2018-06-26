FROM openjdk:8-jdk-slim

EXPOSE 8080

COPY target/d-4.0.jar /

CMD java -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=* -jar d-4.0.jar
