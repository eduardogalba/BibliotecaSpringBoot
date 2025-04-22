# Capa 1: Obtenemos la imagen Java que necesitamos
FROM openjdk:21-ea-slim-buster

# Capa 2: Nos situamos en el directorio de la aplicación
WORKDIR /app

# Capa 3: Copiamos el ejecutable de la aplicación
COPY target/biblioteca-0.0.1-SNAPSHOT.jar .

# No genera capa: Exponemos puerto 8080 para conectarse a localhost:8080
EXPOSE 8080

# No genera capa: Ejecuta la aplicación Spring Boot
CMD ["java", "-jar", "biblioteca-0.0.1-SNAPSHOT.jar"]