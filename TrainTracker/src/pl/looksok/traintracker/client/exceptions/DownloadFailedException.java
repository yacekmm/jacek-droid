package pl.looksok.traintracker.client.exceptions;

public class DownloadFailedException extends RuntimeException {
	public DownloadFailedException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = -1795695966487636541L;

}
