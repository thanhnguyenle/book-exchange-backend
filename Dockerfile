FROM openjdk:21-jdk-slim

WORKDIR /app

RUN addgroup --system spring && adduser --system spring --ingroup spring

COPY target/your-app.jar app.jar

RUN chown -R spring:spring /app

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]