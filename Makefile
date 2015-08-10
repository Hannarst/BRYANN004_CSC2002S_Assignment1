JAVAC = javac
JFLAGS = -g
SOURCES = SerialMedianFilterUI.java ParallelMedianFilterUI.java 

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JFLAGS) $<
    
SerialVParallelTest.class: SerialMedianFilterUI.java ParallelMedianFilterUI.java

clean:
	@rm –f $(SOURCES:.java=.class)
