FROM java:openjdk-8

EXPOSE 8080

COPY target/f-6.0.jar /

CMD java -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=* -jar f-6.0.jar
