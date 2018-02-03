package peertopeer.application;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import peertopeer.ecxeptions.PeerClientException;

public class PeerClient extends Client implements Runnable {

	private static final int DEFAULT_PORT = 0;
	private ServerSocketChannel miniServerSocketChannel;
	private Selector miniServerSelector;
	private SocketChannel miniClientChannel;
	private Map<SocketChannel, ByteBuffer> queuedWrites = new HashMap<SocketChannel, ByteBuffer>();
	private Deque<String> queuedReads = new ArrayDeque<String>();

	private String resourceUserName;
	// private Path toDownload;
	private Path toSave;

	public PeerClient(User user, RequestStatus status) throws ClientException {
		super(user, status);
	}

	public PeerClient(User user, RequestStatus requestStatus, String resourceUser, String toSave)
			throws IOException, PeerClientException, ClientException {
		super(user, requestStatus);

		if (resourceUser == null || toSave == null) {
			throw new PeerClientException("Invalid peer data given!");
		}

		try {
			this.resourceUserName = resourceUser;
			this.toSave = Paths.get(toSave);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// this.toDownload = Paths.get(toDownloadFrom);

	}

	@Override
	public void run() {
		super.run();

		try {
			initServer();
			initClient();

			new Thread(new DownloadThread(user, status, resourceUserName, toSave)).start();
			
			//could block here:
			while (true) {
				processMiniServer();
				processMiniClient();
			}

		} catch (IOException | ClientException e1) {
			e1.printStackTrace();
		}

	}

	private void processMiniClient() throws IOException {
		while (true) {
			int readyChannels = miniServerSelector.select();
			if (readyChannels == 0) {
				continue;
			}

			Set<SelectionKey> selectedKeys = miniServerSelector.selectedKeys();
			Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
			while (keyIterator.hasNext()) {

				SelectionKey key = keyIterator.next();
				keyIterator.remove();

				if (key.isAcceptable()) {
					accept(key);
				}

				if (key.isReadable()) {

					read(key);
				}

				if (key.isWritable()) {

					write(key);
				}
			}
		}

	}

	private void accept(SelectionKey key) throws IOException {

		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		socketChannel.register(miniServerSelector, SelectionKey.OP_READ);
	}

	private void processMiniServer() {

	}

	private void initClient() throws IOException {
		miniServerSelector = Selector.open();

		clientChannel = SocketChannel.open();

		clientChannel.connect(new InetSocketAddress(user.getIPaddress(), user.getPort()));

		clientChannel.configureBlocking(false);

		clientChannel.register(miniServerSelector, SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT);

	}

	private void initServer() throws IOException {

		miniServerSocketChannel = ServerSocketChannel.open();

		miniServerSocketChannel.configureBlocking(false);

		miniServerSocketChannel.socket().bind(new InetSocketAddress(DEFAULT_PORT));

		miniServerSelector = Selector.open();

		miniServerSocketChannel.register(miniServerSelector, SelectionKey.OP_ACCEPT);

	}

}
