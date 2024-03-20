# Makefile for Java Programs

# Define the Java compiler
JC = javac

# Define the Java source files
CLASSES = MainApplication.java

# Define the Java class files
CLASSFILES = $(CLASSES:.java=.class)

# Define the target
all: $(CLASSFILES)

# Define the rule for compiling Java source files
%.class: %.java
	$(JC) $<

# Define the rule for cleaning up
clean:
	rm -f $(CLASSFILES)