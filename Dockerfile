# Use Maven with a JDK (choose 17 unless your project requires another)
FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

# Copy pom first to leverage Docker layer caching
COPY pom.xml .

# Download dependencies (speeds up rebuilds)
RUN mvn -q -DskipTests dependency:go-offline

# Copy source
COPY src ./src

# Default command
CMD ["mvn", "-q", "clean", "compile", "exec:java", "-Dexec.mainClass=com.cohesion.Main", "-Dexec.args=src/main/java"]