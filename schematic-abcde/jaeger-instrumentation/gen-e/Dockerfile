FROM java:openjdk-8

EXPOSE 8080

COPY target/e-5.0.jar /

CMD java -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=* -jar e-5.0.jar
