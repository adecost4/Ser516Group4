FROM maven:3.9.6-eclipse-temurin-17
WORKDIR /app

COPY swent0linux_asu.pem /tmp/swent0linux_asu.pem
RUN keytool -importcert -noprompt -cacerts -storepass changeit -alias swent0linux_asu -file /tmp/swent0linux_asu.pem

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src

# Default command
CMD ["mvn", "-q", "clean", "compile", "exec:java", "-Dexec.mainClass=com.cohesion.Main", "-Dexec.args=src/main/java"]