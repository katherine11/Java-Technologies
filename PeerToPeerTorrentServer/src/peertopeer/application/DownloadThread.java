package peertopeer.application;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.file.Path;

public class DownloadThread extends Client implements Runnable {

	private String resourceIP;
	private String resourceUserName;
	private Path toDownload;
	private Path toSave;
	
	public DownloadThread(User user, RequestStatus status) throws ClientException {
		super(user, status);
	}
	
	public DownloadThread(User user, RequestStatus requestStatus, String resourceUser,  Path toSave) throws IOException, ClientException {
		this(user, requestStatus);
		
		if(resourceUserName == null || toDownload == null | toSave == null){
			throw new ClientException("Invalid user data");
		}
		
		this.resourceUserName = resourceUser;
		this.toSave = toSave;
	}
	
	
	@Override
	protected void processRequest() throws IOException {
		String download = status + STRING_SEPARATOR + resourceUserName + STRING_SEPARATOR;
		writeQueue.add(download);
		super.processRequest();
	}
	
	@Override
	protected void read(SelectionKey key) throws IOException {
		
		readBuffer.clear();

		while (true) {

			int numRead = clientChannel.read(readBuffer);

			if (numRead <= 0) {
				break;
			}
		}

		this.resourceIP = new String(readBuffer.array());

		key.interestOps(SelectionKey.OP_WRITE);	
		
	}
	
	void download() {
		
		
		
	}

	@Override
	public void run() {
		super.run();

		download();

	}

}
