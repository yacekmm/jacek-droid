package pl.looksok.logic.exceptions;

public class DuplicatePersonNameException extends RuntimeException {
	private static final long serialVersionUID = 1361496439390021369L;
	
	public DuplicatePersonNameException(String message){
		super(message);
	}

}
