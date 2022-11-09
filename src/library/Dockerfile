FROM adoptopenjdk/openjdk11
EXPOSE 8060
COPY build/libs/library.jar library.jar
ENTRYPOINT ["java","-jar","./library.jar"]