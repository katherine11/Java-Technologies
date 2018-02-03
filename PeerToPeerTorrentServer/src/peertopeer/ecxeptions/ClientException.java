package peertopeer.ecxeptions;

public class ClientException extends Exception {

	private static final long serialVersionUID = 3918885533287335867L;

	public ClientException() {
		super();
	}
		
	public ClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientException(String message) {
		super(message);
	}

	public ClientException(Throwable cause) {
		super(cause);
	}

}
