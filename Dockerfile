FROM openjdk:21-jdk

# Set working directory
WORKDIR /opt/app

# Copy Maven wrapper and project files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Make Maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies first (for layer caching)
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY ./src ./src

# Build the application
RUN ./mvnw clean install -DskipTests

# Copy the built JAR to a fixed location
RUN cp $(find target -type f -name "*.jar" | head -n 1) app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
