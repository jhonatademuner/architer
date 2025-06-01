FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY build/libs/archter-0.0.1.jar archter-0.0.1.jar
EXPOSE 8080
CMD ["java", "-jar", "archter-0.0.1.jar"]