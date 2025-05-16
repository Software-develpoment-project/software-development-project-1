FROM openjdk:21-jdk

WORKDIR /opt/app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

COPY ./src ./src
RUN ./mvnw clean install -DskipTests

# If you don't want to install find:
RUN cp target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
