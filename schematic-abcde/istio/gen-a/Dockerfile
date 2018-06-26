FROM openjdk:8-jdk-slim

EXPOSE 8080

COPY target/a-1.0.jar /

CMD java -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=* -jar a-1.0.jar
