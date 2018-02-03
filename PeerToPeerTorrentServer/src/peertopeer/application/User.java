package peertopeer.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import peertopeer.ecxeptions.PeerClientException;

public class User {
	
	public static final String MAPPING_SEPARATOR = " - ";
	public static final String SEPARATOR = ":";
	private String username;
	private String IPaddress;
	private int port;
	//each user contains a set of his own files
	private Set<Path> files = new HashSet<>();
	
	public User(String username, String IPaddress, int port) throws UserException {
		if(username != null && IPaddress != null && port < 0){
			throw new UserException("Invalid data given!");
		}
		this.username = username;
		this.IPaddress = IPaddress;
		this.port = port;
	}
	
	public void addFile(String file){
		Path destination = Paths.get(file);
		if(Files.exists(destination)){
			files.add(destination);			
		}
	}
	
	//sends the server the default directory from which to download files;
	public void seed(String directory) throws ClientException{
		register(directory);
	}
	
	//adds all the files from the given directory to the collection with mapping: username - set of files;
	private void register(String directory) throws ClientException{
		try {
			try {
				new Thread(new Client(this,directory,RequestStatus.SEED)).start();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	//removes the selected files from the central server;
	//it is identified by the user IP:
	public void unregister(Set<String> paths) throws ClientException{
			new Thread(new Client(this,paths, RequestStatus.UNREGISTER)).start();
	}
	
	//prints all the registered user files in the server;
	public void listFiles() throws ClientException{
		try {
			new Thread(new Client(this, RequestStatus.LIST)).start();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	//returns a collection of files with the given names/paths ? 
	public Set<Path> search(Set<String> fileNames){
		return null;
	}
	
	//downloads a file from a specific user:
	public void download(String user, String toDownloadfrom, String toSave) throws IOException, PeerClientException, ClientException{
//		new Thread(new PeerClient(this,RequestStatus.DOWNLOAD,user, toDownloadfrom,toSave)).start();
	}

	@Override
	public String toString() {
		return username + MAPPING_SEPARATOR + IPaddress + SEPARATOR + port; 
	}

	public int getPort() {
		return port;
	}
	
	public String getIPaddress() {
		return IPaddress;
	}
	
	public String getUsername() {
		return username;
	}
}