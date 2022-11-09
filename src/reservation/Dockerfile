FROM adoptopenjdk/openjdk11
EXPOSE 8070
COPY build/libs/reservation.jar reservation.jar
ENTRYPOINT ["java","-jar","./reservation.jar"]