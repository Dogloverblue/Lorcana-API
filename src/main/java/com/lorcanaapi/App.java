package com.lorcanaapi;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import com.lorcanaapi.handlers.FuzzyHandler;
import com.lorcanaapi.handlers.JSONHandler;
import com.lorcanaapi.handlers.SearchHandler;
import com.lorcanaapi.handlers.StrictHandler;
import com.lorcanaapi.handlers.TextHandler;
import com.lorcanaapi.handlers.TrackingHandler;
import com.sun.net.httpserver.HttpServer;



public class App {
	static HttpServer server;
	static File dir = new File("src//data");
	static HashMap<String, String> data;
    public static void main(String[] args) throws Exception {
        Integer port = Integer.parseInt(
          Optional.ofNullable(System.getenv("PORT")).orElse("8080")
        );
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new TextHandler("That's not a card! To learn the proper syntax, go to https://lorcana-api.com"));
        server.createContext("/tracking", new TrackingHandler());
        doMainSetupStuff();
	   
        server.setExecutor(null);
        server.start();

    }
    
    public static String getAllNames() {
    	File cardDirectory = new File("src//data//cards");
    	StringBuilder sb = new StringBuilder("[\n");
    	
    	for (File file : cardDirectory.listFiles()) {
    		try {
				JSONObject jo = new JSONObject(Files.readString(Paths.get(file.getAbsolutePath())));
				jo.getString("name");
			} catch (JSONException | IOException e) {
				continue;
			}
    		sb.append("    \"" + LAPIUtils.removeFileExtension(file.getName(), false) + "\",\n");
    	}
    	sb.deleteCharAt(sb.length() - 2);
    	sb.append("]");
    	return sb.toString();
    }
public static void doMainSetupStuff() {
	setupHashMap();
	String name;
	try {

		server.createContext("/fuzzy/", new FuzzyHandler(data));
		server.createContext("/strict/", new StrictHandler());
		server.createContext("/search", new SearchHandler(data));
		 System.out.println(dir.exists());
		 System.out.println(dir.getPath());
		File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    for (File childdir : directoryListing) {
		    	
		    	name = childdir.getName();
		    	File[] fileListing = childdir.listFiles();
				if (fileListing != null) {
				    for (File child : fileListing) {
		    	String content = new String(Files.readAllBytes(Paths.get("src//data/" + name + "/" + child.getName())), StandardCharsets.UTF_8);
		    	System.out.println("/" + name + "/" + child.getName().replaceFirst("[.][^.]+$", ""));
		    	JSONHandler jh = new JSONHandler(content);
		      server.createContext("/" + name + "/" + child.getName().replaceFirst("[.][^.]+$", ""), jh);
//		      server.createContext("/strict/" + child.getName().replaceFirst("[.][^.]+$", ""), jh);
				    }
				} 
			}
		    server.createContext("/lists/names", new JSONHandler(getAllNames()));
		    server.createContext("/lists/cards", new JSONHandler(getAllNames()));
		   
		    
		  }
		
	
	} catch (Exception e) {
		e.printStackTrace();
	}
}


static void setupHashMap() {
  	data = new HashMap<String, String>();
  	File dir = new File("src//data//cards");
  	File[] directoryListing = dir.listFiles();
  	 if (directoryListing != null) {
		    for (File child : directoryListing) {
		    	try {
					data.put(child.getName().replaceFirst("[.][^.]+$", ""), Files.readString(Paths.get(child.getPath())));
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }}
		    
  }
    
    
   
}

