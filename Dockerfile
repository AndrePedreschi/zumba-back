FROM openjdk

WORKDIR /app

COPY target/zumba-squad-0.0.1-SNAPSHOT.jar /app/zumba-squad.jar

ENTRYPOINT ["java", "-jar", "zumba-squad.jar"]