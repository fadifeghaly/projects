FLAGS = -std=c++11
OPTIONS = -O2
TARGET = tp1

.PHONY: default
default: tp1

tp1: $(TARGET).o
	@g++ $(OPTIONS) $(FLAGS) -o $(TARGET) $(TARGET).o 
	
$(TARGET).o: $(TARGET).cpp	
	@g++ $(FLAGS) -c $(TARGET).cpp
	
clean:
	@rm -f $(TARGET)
	@rm -f $(TARGET).o