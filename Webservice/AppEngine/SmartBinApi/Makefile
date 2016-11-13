all: clean compile deploy

clean:
	mvn clean
	
compile:
	mvn install
	
deploy:
	appcfg.sh update target/smartbinapi-1.0/

fix:
	@export PATH=$PATH:~/apache-maven-3.3.9/bin
	@export PATH=$PATH:~/appengine-java-sdk-1.9.46/bin
