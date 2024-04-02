FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar smartlearn.jar
ENTRYPOINT ["java","-jar","/smartReport.jar"]
EXPOSE 8080