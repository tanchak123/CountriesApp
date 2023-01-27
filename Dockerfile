# the first stage of our build will use a maven 3.6.3 parent image
FROM maven:3.8-openjdk-17 AS MAVEN_BUILD
#
## copy the pom and src code to the container
#COPY ./ ./
#
## package our application code
#RUN mvn clean package

FROM docker.io/library/openjdk:17

COPY --from=MAVEN_BUILD /target/countries-app:1.0-SNAPSHOT.jar /project.jar

CMD ["java", "-jar", "/project.jar"]