FROM maven:latest

#RUN apt-get update
#RUN apt-get install -y maven

WORKDIR /Users/paul.zhang/Documents/projects/java/nearestmeetup

ADD . /Users/paul.zhang/Documents/projects/java/nearestmeetup
RUN ["mvn", "dependency:resolve"]
#RUN ["mvn", "verify"]
RUN ["mvn", "clean", "install"]

EXPOSE 4567
CMD ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", "/Users/paul.zhang/Documents/projects/java/nearestmeetup/target/nearestmeetup-1.0-SNAPSHOT.jar", "/Users/paul.zhang/Documents/projects/java/nearestmeetup/target/nearestmeetup-1.0-SNAPSHOT-jar-with-dependencies.jar"]