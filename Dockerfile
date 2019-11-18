FROM maven:3.6.2-jdk-8

#RUN apt-get update
#RUN apt-get install -y maven

WORKDIR /app

ADD . /app/
#RUN ["mvn", "dependency:resolve"]
#RUN ["mvn", "verify"]
RUN ["mvn", "clean", "install"]

#EXPOSE 4567
CMD ["java", "-jar", "/app/target/nearestmeetup-1.0-SNAPSHOT.jar"]