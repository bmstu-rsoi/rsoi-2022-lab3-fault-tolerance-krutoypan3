FROM adoptopenjdk/openjdk11
EXPOSE 8050
COPY build/libs/rating.jar rating.jar
ENTRYPOINT ["java","-jar","./rating.jar"]