FROM eclipse-temurin:17
WORKDIR /music-bot
ADD /build/libs/music-bot-0.0.1.jar music-bot-0.0.1.jar
CMD ["java", "-jar", "music-bot-0.0.1.jar"]