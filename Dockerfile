FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY build/libs/architer-0.0.1.jar architer-0.0.1.jar
EXPOSE 8080
CMD ["java", "-jar", "architer-0.0.1.jar"]