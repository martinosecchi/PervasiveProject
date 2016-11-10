all: clean compile deploy

clean:
	mvn clean
	
compile:
	mvn install
	
deploy:
	appcfg.sh update target/smartbinapi-1.0/