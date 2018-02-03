package tests.peertopeer.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import peertopeer.application.CentralServer;

public class TestServer {

	CentralServer server = new CentralServer();

	@Test
	public void testSeed() throws IOException {
		String expectedUsername = "katherine";
		String expectedDirectory = "directory";

		if (!Files.exists(Paths.get("directory"))) {
			Files.createDirectory(Paths.get("directory"));
		}

		if (existFiles()) {
			Path path1 = Files.createFile(Paths.get("directory/file1"));
			Path path2 = Files.createFile(Paths.get("directory/file2"));
			Path path3 = Files.createFile(Paths.get("directory/file3"));
			List<Path> paths = new ArrayList<>();
			paths.add(path1);
			paths.add(path2);
			paths.add(path3);
			Map<String, List<Path>> expectedFiles = new HashMap<>();
			expectedFiles.put(expectedUsername, paths);
			List<String> components = new ArrayList<>();
			components.add(expectedUsername);
			components.add(expectedDirectory);

			server.receiveSeed(components);

			Assert.assertEquals(expectedFiles, server.getAllUsersFiles());
		}

	}

	@Test
	public void testUnregister() throws IOException {

		String expectedUsername = "katherine";
		String expectedDirectory = "directory";

		if (!Files.exists(Paths.get("directory"))) {
			Files.createDirectory(Paths.get("directory"));
		}

		if (existFiles()) {
			Path path1 = Files.createFile(Paths.get("directory/file1"));
			Path path2 = Files.createFile(Paths.get("directory/file2"));
			Path path3 = Files.createFile(Paths.get("directory/file3"));
			Path path4 = Files.createFile(Paths.get("directory/file4"));
			Path path5 = Files.createFile(Paths.get("directory/file5"));
			
			List<Path> paths = new ArrayList<>();
			paths.add(path1);
			paths.add(path2);
			paths.add(path3);
			paths.add(path4);
			paths.add(path5);
			
			Map<String, List<Path>> expectedFiles = new HashMap<>();
			expectedFiles.put(expectedUsername, paths);
			
			List<String> componentsList = new ArrayList<>();
			String userIP = "127.0.0.1";
			componentsList.add(userIP);
			componentsList.add(path1.toFile().getName());
			componentsList.add(path2.toFile().getName());
			server.receiveUnregister(componentsList);
		
			Assert.assertEquals(expectedFiles, server.getAllUsersFiles());
		}
		
	}

	private boolean existFiles() {
		return !Files.exists(Paths.get("directory/file1")) && !Files.exists(Paths.get("directory/file2"))
				&& !Files.exists(Paths.get("directory/file3")) && !Files.exists(Paths.get("directory/file4")) 
						&& !Files.exists(Paths.get("directory/file5"));
	}

}
