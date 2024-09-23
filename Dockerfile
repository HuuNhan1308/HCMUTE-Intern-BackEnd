
#------------------------BUILD------------------------
FROM maven:3.9.9-ibm-semeru-21-jammy as build

WORKDIR /app

COPY . .

RUN "mvn install -DskipTests=true"

#------------------------RUN------------------------
FROM openjdk:21

RUN adduser -D hcmute

WORKDIR /run
COPY --from=build /app/target/hcmute-intern-0.0.1-SNAPSHOT.jar /run/hcmute-intern-0.0.1-SNAPSHOT.jar

RUN chown -R hcmute:hcmute /run

USER hcmute

# Render automatically detect port
# EXPOSE 8080

ENTRYPOINT "java -jar /run/hcmute-intern-0.0.1-SNAPSHOT.jar"