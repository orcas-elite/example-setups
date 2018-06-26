FROM openjdk:8-jdk-slim

EXPOSE 8080

COPY target/b-2.0.jar /

CMD java -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=* -jar b-2.0.jar
