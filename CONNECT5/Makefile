FLAGS = -std=c++11
OPTIONS = -O2
TARGET = tp2

.PHONY: default
default: tp2

tp2: $(TARGET).o
	@g++ $(OPTIONS) $(FLAGS) -o $(TARGET) joueur.o 
		
$(TARGET).o: joueur.cpp	
	@g++ $(FLAGS) -c joueur.cpp
		
clean:
	@rm -f $(TARGET)
	@rm -f joueur.o
	
interface:
	java -jar inf4230-connect5.jar
