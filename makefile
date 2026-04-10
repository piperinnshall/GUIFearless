MODULE = com.piperinnshall.fluentguijava
# MAIN   = com.piperinnshall.fluentguijava.demo.Demo
# MAIN   = com.piperinnshall.fluentguijava.demo.ShapeDemo
MAIN   = com.piperinnshall.fluentguijava.demo.EmptyDemo
SRC    = $(shell find fluentguijava/src -name '*.java')
TEST   = $(shell find fluentguijava/test -name '*.java')
OUT      = fluentguijava/out
OUT_TEST = fluentguijava/out-test
JARS   = lib/junit-jupiter-api-5.10.5.jar:lib/junit-jupiter-engine-5.10.5.jar:lib/junit-platform-engine-1.10.5.jar:lib/junit-platform-commons-1.10.5.jar:lib/opentest4j-1.3.0.jar:lib/apiguardian-api-1.1.2.jar

run: compile
	@java --module-path $(JARS):$(OUT) -m $(MODULE)/$(MAIN)

compile:
	@mkdir -p $(OUT)
	@javac --module-path $(JARS) -d $(OUT) $(SRC)

test: compile
	@mkdir -p $(OUT_TEST)
	@javac --module-path $(JARS):$(OUT) --add-modules org.junit.jupiter.api -d $(OUT_TEST) $(TEST)
	@java -Djava.awt.headless=true -jar lib/junit-platform-console-standalone-1.10.5.jar execute --classpath $(OUT_TEST):$(OUT) --scan-classpath --details=tree

clean:
	rm -rf $(OUT) $(OUT_TEST)
