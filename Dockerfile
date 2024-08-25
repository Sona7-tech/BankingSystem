FROM alpine
COPY build/libs/BankingSystem-0.0.1-SNAPSHOT.jar /app/BankingSystem-0.0.1-SNAPSHOT.jar
RUN apk add --no-cache openjdk17
WORKDIR "/app"
ENTRYPOINT ["java"]
CMD ["-jar", "/app/BankingSystem-0.0.1-SNAPSHOT.jar"]