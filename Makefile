JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        Board.java \
        Card.java \
        Deadwood.java \
        gameSystem.java \
	ParseXML.java \
	Part.java \
	Player.java \
	Room.java \
	Upgrade.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
