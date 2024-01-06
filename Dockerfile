FROM azul/zulu-openjdk-alpine:17-latest

WORKDIR /app

ARG JAR_PATH=./build/libs

COPY ${JAR_PATH}/*.jar ${JAR_PATH}/*.jar

EXPOSE 80

CMD ["java","-jar","-Dspring.profiles.active=dev","./build/libs/*.jar"]
