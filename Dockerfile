
#------------------------BUILD------------------------
FROM maven:3.9.9-ibm-semeru-21-jammy as build

WORKDIR /app

COPY . .

RUN mvn install -DskipTests=true

#------------------------RUN------------------------
FROM alpine:3.20.3

RUN adduser -D hcmute

RUN apk add openjdk21

WORKDIR /run
COPY --from=build /app/target/hcmute-intern-0.0.1-SNAPSHOT.jar /run/hcmute-intern-0.0.1-SNAPSHOT.jar

RUN chown -R hcmute:hcmute /run

USER hcmute

EXPOSE 8080

ENTRYPOINT java -jar /run/hcmute-intern-0.0.1-SNAPSHOT.jar