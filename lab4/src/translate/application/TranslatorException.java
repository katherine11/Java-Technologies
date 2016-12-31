package translate.application;

public class TranslatorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2896603291574921134L;

	public TranslatorException() {
	}

	public TranslatorException(String arg0) {
		super(arg0);
	}

	public TranslatorException(Throwable arg0) {
		super(arg0);
	}
	
	public TranslatorException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TranslatorException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
