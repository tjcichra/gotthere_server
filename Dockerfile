FROM openjdk:11                                 
WORKDIR /app                                    
COPY gotthere_server-0.0.1-SNAPSHOT.jar .       
CMD java -jar gotthere_server-0.0.1-SNAPSHOT.jar