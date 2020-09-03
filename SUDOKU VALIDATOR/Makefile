CFLAGS = -Wall -pedantic -std=c99
TARGET = sudoku

.PHONY: default
default: exec

exec: $(TARGET).o
	@gcc $(CFLAGS) -pthread -o $(TARGET) -o $(TARGET) -lm $(TARGET).o
	
$(TARGET).o: $(TARGET).c	
	@gcc $(CFLAGS) -c $(TARGET).c

clean:
	@rm -f $(TARGET)
	@rm -f $(TARGET).o
	@echo "--> Supression complétée"
