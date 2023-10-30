FROM maven:3.6.3-jdk-8

COPY ./ ./
 # package our application code
RUN mvn clean package

#FROM openjdk:8-jre-slim
CMD ["java", "-jar", "target/health-care-0.0.1.war"]