FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
# Can derivate an error here
WORKDIR /app
COPY --from=build /app/target/*.jar CarBox.jar
EXPOSE 8080
CMD ["java", "-jar", "CarBox.jar"]
