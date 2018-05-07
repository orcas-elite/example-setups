FROM java:openjdk-8

EXPOSE 8080

COPY target/b-2.0.jar /

CMD java -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=* -jar b-2.0.jar
