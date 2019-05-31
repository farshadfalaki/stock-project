FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/stock-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} stock.jar
ENTRYPOINT ["java","-jar","/stock.jar"]
