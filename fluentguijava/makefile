MODULE = com.piperinnshall.fluentguijava
MAIN   = com.piperinnshall.fluentguijava.swing.Main
SRC    = $(shell find src -name '*.java')

run: compile
	java --module-path out -m $(MODULE)/$(MAIN)

compile:
	javac -d out $(SRC)

clean:
	rm -rf out
