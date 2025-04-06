# Build the spring boot project
FROM eclipse-temurin:21-alpine AS builder

WORKDIR /app
COPY . .

RUN ./mvnw -Dmaven.test.skip=true clean package

# Copy the fat-jar and build a smaller docker image
FROM eclipse-temurin:21-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Add these environment variables for better logging
ENV JAVA_OPTS="-Dspring.output.ansi.enabled=always -Dlogging.level.root=INFO"
ENV TZ=UTC

# Use exec form of ENTRYPOINT
ENTRYPOINT ["java", "-jar", "app.jar"]
