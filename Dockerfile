# Usa una imagen base con JDK 17 de Eclipse Temurin para compilar la app (etapa builder)
FROM eclipse-temurin:17-jdk AS builder

# Define el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia todos los archivos del proyecto al contenedor
COPY . .

# Ejecuta Maven Wrapper para compilar el proyecto y empaquetar el .jar, omitiendo los tests
RUN ./mvnw clean package -DskipTests

# Usa una imagen base ligera solo con JRE 17 para ejecutar la app (etapa final)
FROM eclipse-temurin:17-jre

# Define el directorio de trabajo donde se ejecutará la app
WORKDIR /app

# Copia el archivo .jar generado en la etapa builder al contenedor actual, renombrándolo como app.jar
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto 8080 (puerto por defecto de Spring Boot)
EXPOSE 8080

# Comando por defecto para arrancar la aplicación Java
CMD ["java", "-jar", "app.jar"]
