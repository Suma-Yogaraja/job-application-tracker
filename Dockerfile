FROM amazoncorretto:17-alpine
WORKDIR /app
COPY target/job_application_tracker-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]