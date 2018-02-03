package peertopeer.application;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CentralServer implements Runnable {

	private static final int DEFAULT_BYTES = 10000;
	private static final int DEFAULT_PORT = 4444;
	private static final int MAX_REQUESTS = 30;
	// server socket to create the connection between the server and the client;
	private ServerSocketChannel serverSocketChannel;
	// socket channel to process clients' requests;
	private SocketChannel acceptSocketChannel;
	// selector to dispatch the requests;
	private Selector selector;
	// contains all channels;
	private Map<SocketChannel, ByteBuffer> queuedWrites = new HashMap<>();
	// contains mapping: username - collection of files;
	private Map<String, List<Path>> allUsersFiles = new ConcurrentHashMap<>();
	// contains mapping: username - IP address + port;
	private Map<String, String> allUserIPAddresses = new ConcurrentHashMap<>();
	// contains the usernames of all active users;
	private Set<String> activeUsers = new HashSet<>();

	public CentralServer() {

		for (String username : allUsersFiles.keySet()) {
			allUsersFiles.put(username, new ArrayList<Path>());
		}
		
	}

	void init() throws IOException {

		System.out.println("Server started: ");

		serverSocketChannel = ServerSocketChannel.open();

		serverSocketChannel.configureBlocking(false);

		serverSocketChannel.socket().bind(new InetSocketAddress(DEFAULT_PORT));

		selector = Selector.open();

		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

	}

	private void processRequest() throws IOException, ClosedChannelException {
		while (true) {
			int readyChannels = selector.select();
			if (readyChannels == 0) {
				continue;
			}

			Set<SelectionKey> selectedKeys = selector.selectedKeys();
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

	private void write(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		ByteBuffer towrite = queuedWrites.get(socketChannel);

		while (true) {
			int bytesSize = socketChannel.write(towrite);

			if (bytesSize == 0 || towrite.remaining() == 0)
				break;
		}

		key.interestOps(SelectionKey.OP_READ);

	}

	private Set<String> getPaths(String[] components) {
		return null;
	}

	private String getRequestedFiles(List<String> componentsList) {
		return null;
	}

	private void read(SelectionKey key) throws IOException {

		// for each method read the specific information:
		SocketChannel sChannel = (SocketChannel) key.channel();
		ByteBuffer rBuffer = ByteBuffer.allocate(DEFAULT_BYTES);

		int numread;
		while (true) {

			numread = sChannel.read(rBuffer);

			if (numread <= 0) {
				break;
			}

			// if a remote entity shut the socket down -> close the channel;
			if (numread == -1) {
				key.channel().close();
				key.cancel();
				continue;
			}

			String read = new String(rBuffer.array());

			// contains user info, the first is always the request status;
			String[] components = read.split(Client.STRING_SEPARATOR);

			String result = executeCommand(components);

			rBuffer.flip();
			rBuffer.clear();
			rBuffer = ByteBuffer.allocate(DEFAULT_BYTES);
			rBuffer.put(result.getBytes());
			// returns the channel back to the queue and
			// changes the operation to write;
			queuedWrites.put(sChannel, rBuffer);
			key.interestOps(SelectionKey.OP_WRITE);

		}

	}

	private void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);
	}

	private String executeCommand(String[] components) {

		List<String> componentsList = new ArrayList<>(Arrays.asList(components));
		RequestStatus requestStatus = RequestStatus.valueOf(components[0]);
		componentsList.remove(requestStatus.toString());
		String result = requestStatus.toString();
		StringBuilder builder = new StringBuilder(result);

		switch (requestStatus) {
		case SEED:
			receiveSeed(componentsList);
			builder.append("Seed was successfully completed!");
			break;
		case UNREGISTER:
			receiveUnregister(componentsList);
			builder.append("Unregister was successfully completed!");
			break;
		case LIST:
			builder.append(getAllFiles());
			break;
		case SEARCH:
			builder.append(getRequestedFiles(componentsList));
			break;
		case DOWNLOAD:
			builder.append(receiveDownload(componentsList));
			break;
		default:
			break;
		}

		return builder.toString();
	}

	private String receiveDownload(List<String> componentsList) {

		String username = componentsList.get(1);
		String resourceUsername = componentsList.get(2);

		if (allUserIPAddresses.containsKey(username) && allUserIPAddresses.containsKey(resourceUsername)) {

			Path resourcePath = Paths.get(componentsList.get(3));

			if (allUsersFiles.get(resourceUsername).contains(resourcePath)) {
				return getUserIPByName(resourceUsername);
			}

		}
		return null;

	}

	private String getUserIPByName(String userName) {

		for (String username : allUserIPAddresses.keySet()) {

			if (username.equals(userName)) {
				return allUserIPAddresses.get(username);
			}

		}

		return null;
	}

	public void receiveSeed(List<String> componentsList) {

		String username = componentsList.get(0);
		String directory = componentsList.get(1);

		addFiles(username, directory);

	}

	public void addFiles(String username, String directory) {

		try {
			if(!Files.exists(Paths.get(directory))){
				Files.createDirectory(Paths.get(directory));
			}
			Files.walk(Paths.get(directory)).filter(Files::isRegularFile)
					.forEach(file -> allUsersFiles.get(username).add(file));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void receiveUnregister(List<String> componentsList) {

		String userIP = componentsList.get(0);
		componentsList.remove(0);
		String username = getUsernameByIP(userIP);

		for (String name : allUsersFiles.keySet()) {
			if (name.equals(username)) {
				List<Path> paths = allUsersFiles.get(name);
				for (String file : componentsList) {
					Path filePath = Paths.get(file);
					if (paths.contains(filePath)) {
						paths.remove(filePath);
					}
				}
			}
		}
	}

	public String getUsernameByIP(String userIP) {

		for (String username : allUserIPAddresses.keySet()) {
			String currentUserIP = allUserIPAddresses.get(username);
			if (currentUserIP.equals(userIP)) {
				return username;
			}
		}
		return null;
	}

	private String getAllFiles() {
		
		StringBuilder builder = new StringBuilder();
		
		for (List<Path> paths : allUsersFiles.values()) {
			
			for (Path path : paths) {
				String name = path.toFile().getName();
				builder.append(name);
			}
			
			builder.append(File.separator);
			
		}
		
		return builder.toString();
	}

	Set<Path> receiveSearch(Set<String> paths) {
		return null;
	}

	Set<Path> receiveListFiles() {
		return null;
	}

	void receiveUpdateRequest() {

	}

	public Map<String, List<Path>> getAllUsersFiles() {
		return Collections.unmodifiableMap(allUsersFiles);
	}
	
	@Override
	public void run() {

		try {
			init();

			processRequest();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		CentralServer server = new CentralServer();
		
		new Thread(new CentralServer()).start();
		
	}
	
}
