package com.kinsta.helloworld;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;


import java.net.URISyntaxException;



import java.io.File;

import com.kinsta.helloworld.handlers.FuzzyHandler;
import com.kinsta.helloworld.handlers.JSONHandler;
import com.kinsta.helloworld.handlers.SearchHandler;
import com.kinsta.helloworld.handlers.TextHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;


public class App {
	static HttpServer server;
	static File dir = new File("src//data");
	static HashMap<String, String> data;
    public static void main(String[] args) throws Exception {
        Integer port = Integer.parseInt(
          Optional.ofNullable(System.getenv("PORT")).orElse("8080")//d
        );
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new TextHandler("That's not a card! To learn the proper syntax, go to https://lorcana-api.com"));
        doMainSetupStuff();//f
	   
        server.setExecutor(null);
        server.start();

    }
    
    public static String getAllNames() {
    	File cardDirectory = new File("src//data//cards");
    	StringBuilder sb = new StringBuilder("[\n");
    	for (File file : cardDirectory.listFiles()) {
    		sb.append("    \"" + removeFileExtension(file.getName(), true) + "\",\n");
    	}
    	sb.deleteCharAt(sb.length() - 2);
    	sb.append("]");
    	return sb.toString();
    }
public static void doMainSetupStuff() {
	setupHashMap();
	String line;
	String name;
	try {

		server.createContext("/fuzzy/", new FuzzyHandler(data));
		server.createContext("/search", new SearchHandler(data));
		server.createContext("/lists/names", new JSONHandler(getAllNames()));
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
		      server.createContext("/strict/" + child.getName().replaceFirst("[.][^.]+$", ""), jh);
				    }
				} 
			}
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
    
    public static String removeFileExtension(String filename, boolean removeAllExtensions) {
        if (filename == null || filename.isEmpty()) {
            return filename;
        }

        String extPattern = "(?<!^)[.]" + (removeAllExtensions ? ".*" : "[^.]*$");
        return filename.replaceAll(extPattern, "");
    }
   
}

