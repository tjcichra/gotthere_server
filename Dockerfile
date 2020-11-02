FROM openjdk:11                                 
WORKDIR /app                                    
COPY target/gotthere_server-0.0.1-SNAPSHOT.jar .       
CMD java -jar gotthere_server-0.0.1-SNAPSHOT.jar