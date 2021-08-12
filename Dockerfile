FROM hypriot/rpi-java

RUN mkdir -p /app
#Set the current working directory inside the image
WORKDIR /app

COPY .mvn .mvn

COPY pom.xml .

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw package -DskipTests


#FROM hypriot/rpi-java
#WORKDIR /app
#
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#
#RUN chmod +x ./mvnw
#RUN ./mvnw dependency:go-offline
#
#COPY src ./src
#
#EXPOSE 8999
#
#CMD ["./mvnw", "spring-boot:run"]