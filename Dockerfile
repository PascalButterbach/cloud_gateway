FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

COPY src ./src

EXPOSE 9000 9001 9002 8761

CMD ["./mvnw", "spring-boot:run"]