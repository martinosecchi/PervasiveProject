MVN=~/apache-maven-3.3.9/bin/
GAE=~/appengine-java-sdk-1.9.46/bin/

all: clean compile deploy

clean:
	$(MVN)mvn clean
	
compile:
	$(MVN)mvn install
	
deploy:
	$(GAE)appcfg.sh update target/smartbinapi-1.0/
