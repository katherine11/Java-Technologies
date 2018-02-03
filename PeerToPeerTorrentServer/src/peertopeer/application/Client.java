package peertopeer.application;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.print.attribute.standard.PrinterIsAcceptingJobs;

public class Client implements Runnable {

	private static final int DEFAULT_BYTES = 10000;
	private static final int MAX_DOWNLOADS = 30;
	// separates the data when written in the buffer;
	public static final String STRING_SEPARATOR = ";";
	// current user;
	protected User user;
	// default directory to download files;
	private String directory;
	// shows the type of the command: seed, unregister...;
	protected RequestStatus status;
	// contains file names;
	private Set<String> paths;
	// file that keeps peer mappings;
	private Path peerMappings;
	// client selector;
	private Selector selector;
	// client chanel;
	protected SocketChannel clientChannel;
	// byte buffer to read and write in;
	protected ByteBuffer readBuffer = ByteBuffer.allocate(DEFAULT_BYTES);
	// deque to contain all the data when the channel is ready;
	public Deque<String> writeQueue = new ArrayDeque<String>();

	public Client(User user2, RequestStatus status) throws ClientException {
		// start a daemon thread to update files;

		if (user2 == null || status == null) {
			throw new ClientException("Invalid user data given!");
		}
		this.user = user2;
		this.status = status;
	}

	// for the seed command:
	public Client(User user2, String directory, RequestStatus status) throws ClientException {
		this(user2, status);
		if (directory == null) {
			throw new ClientException("Invalid directory given! ");
		}
		this.directory = directory;
	}

	// for the unregister command:
	public Client(User user, Set<String> paths, RequestStatus status) throws ClientException {
		this(user, status);
		if (paths == null) {
			throw new ClientException("Invalid file names given!");
		}
		this.paths = paths;
	}

	private void init() throws IOException, ClosedChannelException {

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		selector = Selector.open();

		clientChannel = SocketChannel.open();

		clientChannel.connect(new InetSocketAddress(user.getIPaddress(), user.getPort()));

		clientChannel.configureBlocking(false);

		clientChannel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT);
	}

	protected void processRequest() throws IOException {

		int keys = selector.select();
		Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();

		while (selectionKeys.hasNext()) {

			SelectionKey key = selectionKeys.next();

			selectionKeys.remove();

			if (!key.isValid()) {
				continue;
			}

			if (key.isReadable()) {
				read(key);
			}

			if (key.isWritable()) {
				write(key);
			}

		}
	}

	protected void write(SelectionKey key) throws IOException {

		String toWrite = writeQueue.pollFirst();

		if (toWrite != null) {
			String[] components = toWrite.split(STRING_SEPARATOR);

			ByteBuffer buffer = ByteBuffer.wrap(toWrite.getBytes());

			while (true) {
				int bytesSize = clientChannel.write(buffer);

				if (bytesSize == 0 || buffer.remaining() == 0) {
					break;
				}

			}

		}

		key.interestOps(SelectionKey.OP_READ);
	}

	protected void read(SelectionKey key) throws IOException {
		readBuffer.clear();

		while (true) {

			int numRead = clientChannel.read(readBuffer);

			if (numRead <= 0) {
				break;
			}
		}

		System.out.println("Response: " + new String(readBuffer.array()));

		key.interestOps(SelectionKey.OP_WRITE);
	}

	private void addDataInWriteQueue() {

		String dataToAdd;

		switch (status) {
		case SEED:
			dataToAdd = status.toString() + STRING_SEPARATOR + user.getUsername() + STRING_SEPARATOR + directory
					+ STRING_SEPARATOR + user.getIPaddress();
			break;
		case UNREGISTER:
			dataToAdd = status.toString() + STRING_SEPARATOR + user.getIPaddress() + STRING_SEPARATOR + getPaths();
			break;
		case LIST:
			dataToAdd = status.toString() + STRING_SEPARATOR + user.getUsername();
			break;
		case SEARCH:
			dataToAdd = status.toString() + STRING_SEPARATOR + getPaths();
			break;
		default:
			dataToAdd = "";
			break;
		}

		if (dataToAdd != "") {
			writeQueue.add(dataToAdd);
		}

	}

	private String getPaths() {
		StringBuilder builder = new StringBuilder();
		for (String path : paths) {
			builder.append(path);
			builder.append(STRING_SEPARATOR);
		}
		return builder.toString();
	}

	@Override
	public void run() {

		try {

			init();
			addDataInWriteQueue();
			processRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
