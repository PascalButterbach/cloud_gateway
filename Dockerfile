FROM hypriot/rpi-java:latest as packager

ENV JAVA_MINIMAL=/opt/jre
ENV PATH="$PATH:$JAVA_MINIMAL/bin"

COPY --from=packager "$JAVA_MINIMAL" "$JAVA_MINIMAL"
COPY "build/libs/opt/cloud-gateway.jar" "/opt/cloud-gateway.jar"

EXPOSE 8999
CMD [ "-jar", "/opt/cloud-gateway.jar" ]
ENTRYPOINT [ "java" ]

#FROM arm32v7/adoptopenjdk:11-jdk-hotspot
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