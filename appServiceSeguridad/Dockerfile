FROM eclipse-temurin:17

LABEL mentainer="bcrp.gob.pe"

WORKDIR /app

COPY target/appServiceSeguridad-0.0.1-SNAPSHOT.jar /app/appServiceSeguridad.jar

VOLUME /tmp

ENTRYPOINT ["java", "-jar","appServiceSeguridad.jar"]
