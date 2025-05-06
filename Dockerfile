# Usa una imagen de OpenJDK adecuada para Spring Boot y Oracle
FROM openjdk:21-ea-24-oracle

# Crea un directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo .jar al contenedor y lo renombra como app.jar
COPY target/microservicio-usuarios-0.0.1-SNAPSHOT.jar app.jar

# Copia la carpeta de wallet descomprimida al contenedor
COPY Wallet_JZ2D47CKFSRL0463 /app/oracle_wallet/

# Expone el puerto por donde se comunicará tu app (8080 por defecto en Spring Boot)
EXPOSE 8081

# Comando para ejecutar la app dentro del contenedor
CMD ["java", "-jar", "app.jar"]
