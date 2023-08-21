FROM openjdk:11-oracle
WORKDIR /app/backend

COPY ./backend/target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]