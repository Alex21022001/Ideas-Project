FROM openjdk:17
ADD /build/libs/ideas-project-0.0.1-SNAPSHOT.jar ideas-project.jar
COPY src/main/resources/ /app/resources/
ENTRYPOINT ["java","-jar","ideas-project.jar"]