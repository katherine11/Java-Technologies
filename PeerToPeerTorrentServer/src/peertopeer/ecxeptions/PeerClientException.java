package peertopeer.ecxeptions;

public class PeerClientException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6471513833972255385L;

	public PeerClientException() {
	}

	public PeerClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PeerClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public PeerClientException(String message) {
		super(message);
	}

	public PeerClientException(Throwable cause) {
		super(cause);
	}

	
}
