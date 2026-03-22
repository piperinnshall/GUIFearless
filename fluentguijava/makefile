MODULE = com.piperinnshall.fluentguijava
MAIN   = com.piperinnshall.fluentguijava.fluentgraphics.WindowDemo
SRC    = $(shell find src -name '*.java')
TEST   = $(shell find test -name '*.java')
JARS   = lib/junit-jupiter-api-5.10.5.jar:lib/junit-jupiter-engine-5.10.5.jar:lib/junit-platform-engine-1.10.5.jar:lib/junit-platform-commons-1.10.5.jar:lib/opentest4j-1.3.0.jar:lib/apiguardian-api-1.1.2.jar

run: compile
	@java --module-path $(JARS):out -m $(MODULE)/$(MAIN)

compile:
	@javac --module-path $(JARS) -d out $(SRC)

test: compile
	@mkdir -p out-test
	@javac --module-path $(JARS):out --add-modules org.junit.jupiter.api -d out-test $(TEST)
	@java -Djava.awt.headless=true -jar lib/junit-platform-console-standalone-1.10.5.jar execute --classpath out-test:out --scan-classpath --details=tree

clean:
	rm -rf out out-test
