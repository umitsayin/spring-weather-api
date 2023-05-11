FROM openjdk:18

WORKDIR /app

COPY target/ROOT.jar .

CMD ["java", "-jar", "ROOT.jar"]