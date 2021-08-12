FROM adoptopenjdk/openjdk11:armv7l-ubuntu-jdk11u-nightly-slim
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

COPY src ./src

EXPOSE 8999

CMD ["./mvnw", "spring-boot:run"]