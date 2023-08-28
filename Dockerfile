# Fetching version of Java
FROM maven:3.8.3-openjdk-17-slim AS build

# Setting up work directory
WORKDIR /app

# Copy the jar file into our app
COPY ./target/hospital-0.0.1-SNAPSHOT.jar /app

# Exposing
EXPOSE 8061

# Starting the application
CMD ["java", "-jar", "hospital-0.0.1-SNAPSHOT.jar"]