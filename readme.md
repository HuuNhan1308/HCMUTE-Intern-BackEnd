# Spring Boot Application

This document provides instructions for setting up and running the Spring Boot application.

## Prerequisites

- JDK 21 ([Download from Oracle](https://www.oracle.com/java/technologies/downloads/#java21) or use OpenJDK)
- Apache Maven 3.9+ ([Download](https://maven.apache.org/download.cgi))
- Your favorite IDE (IntelliJ IDEA, Eclipse, or VS Code recommended)

## Environment Setup

1. **Verify Java Installation**
   ```bash
   java -version
   ```
   Expected output should show Java 21.

2. **Verify Maven Installation**
   ```bash
   mvn -version
   ```
   Make sure it's using Java 21.

## Building the Application

1. **Clone the repository**
   ```bash
   git clone https://github.com/HuuNhan1308/HCMUTE-Intern-BackEnd.git
   cd [project-directory]
   ```

2. **Build the project**
   ```bash
   mvn clean install -DskipTests=true
   ```

## Running the Application

### Method 1: Using Maven
```bash
mvn spring-boot:run
```

### Method 2: Using JAR file
```bash
java -jar target/hcmute-intern-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080` by default.

## Configuration

- Application properties can be found in `src/main/resources/application.properties`

## Troubleshooting

1. **Java Version Issues**
   - Ensure JAVA_HOME points to JDK 21
   - Verify Java version in Maven settings

2. **Build Failures**
   - Run `mvn clean` and try building again
   - Check for dependency conflicts in pom.xml

3. **Port Already in Use**
   - Change server port in application.properties:
     ```properties
     server.port=8081
     ```


## Support

For issues or questions:
1. Check the troubleshooting section above
2. Review project documentation
3. Create an issue in the project repository

