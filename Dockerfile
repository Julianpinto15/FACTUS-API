# Etapa de construcción
FROM eclipse-temurin:21-jdk AS builder

# Directorio de trabajo
WORKDIR /app

# Copiar todo el proyecto (incluye pom.xml, mvnw, .mvn, src, etc.)
COPY . .

# Dar permisos al wrapper
RUN chmod +x ./mvnw

# Descargar dependencias y construir el .jar
RUN ./mvnw clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:21-jre

# Directorio donde se ejecutará la app
WORKDIR /app

# Copiar el .jar desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto por defecto de Spring Boot
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
