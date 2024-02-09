FROM openjdk:17
VOLUME /gym_data
EXPOSE 8080
ARG JAR_FILE=target/gym-manager.jar
COPY ${JAR_FILE} /gym.jar
ENTRYPOINT ["java", "-jar", "/gym.jar"]
