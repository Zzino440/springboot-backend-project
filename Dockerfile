FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/springboot-backend-0.0.1-SNAPSHOT.jar springboot-backend.jar
CMD ["java", "-jar", "springboot-backend.jar"]