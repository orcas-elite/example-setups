FROM java:openjdk-8

EXPOSE 8080

COPY target/c-3.0.jar /

CMD java -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=* -jar c-3.0.jar
