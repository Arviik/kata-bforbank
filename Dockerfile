FROM eclipse-temurin:21-jdk AS build

WORKDIR /app
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean install

FROM eclipse-temurin:21-jdk

COPY --from=build /app/target/*.jar /opt/app/app.jar

CMD ["java", "-jar", "/opt/app/app.jar"]
