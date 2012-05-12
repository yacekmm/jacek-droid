package pl.looksok.exception;

public class BadInputDataException extends RuntimeException {
	public BadInputDataException(String msg) {
		super(msg);
	}

	public BadInputDataException() {
		super();
	}

	private static final long serialVersionUID = -7260389541072829419L;

}
