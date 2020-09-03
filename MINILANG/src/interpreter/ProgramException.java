package interpreter;

import interpreter.node.*;

@SuppressWarnings("serial")
public class ProgramException extends RuntimeException {

	private Token location;

	public ProgramException(Token location, String message) {

		super("at line " + location.getLine() + " pos " + location.getPos() + " " + message);

		this.location = location;
	}

	public Token getLocation() {

		return location;
	}

}
