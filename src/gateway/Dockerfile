FROM adoptopenjdk/openjdk11
EXPOSE 8080
COPY build/libs/gateway.jar gateway.jar
ENTRYPOINT ["java","-jar","./gateway.jar"]