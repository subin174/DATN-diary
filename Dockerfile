FROM maven:3.6.3-jdk-8 AS build
RUN apt-get update

COPY ./ ./
 # package our application code
RUN mvn clean package -DskipTests

FROM openjdk:8-jre-slim
CMD ["java", "-jar", "target/health-care-0.0.1.war"]