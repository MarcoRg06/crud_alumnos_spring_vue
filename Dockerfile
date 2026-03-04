FROM openjdk:21-ea

VOLUME /tmp

EXPOSE 8080

ADD springboot-vue/alumnos/target/alumnos-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]