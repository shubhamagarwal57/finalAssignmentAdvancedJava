FROM maven:3.8-jdk-11 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY . .



#RUN mvn clean test
RUN mvn package -DskipTests



FROM openjdk:8-jdk-alpine
EXPOSE 8080
COPY --from=builder /app/target/*.jar /app.jar
COPY --from=builder /app/target/classes /usr/local/target/classes/
ENTRYPOINT ["java","-jar","/app.jar"]
