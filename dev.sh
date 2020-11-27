# build jar with maven
docker run --rm -it -v ${PWD}:/app -v ${PWD}/.m2:/root/.m2/ maven:3.6.3-jdk-11 bash -c " cd /app && mvn -B package --file pom.xml"
# test it out
docker-compose down || /bin/true
docker-compose up --build -d
