FROM openjdk:17-jdk-alpine
EXPOSE 8090
ARG JAR_FILE=target/advanced-spring-boot-demo.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]