FROM openjdk:11-oracle
WORKDIR /app
ENV PROFILE prod
COPY target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]