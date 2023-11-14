package com.lorcanaapi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;

import com.lorcanaapi.handlers.CardsHandler;
import com.sun.net.httpserver.HttpServer;

public class APIServer {

	HttpServer server;

	public void startServer() {
		try {
			Integer port = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8080"));
			server = HttpServer.create(new InetSocketAddress(port), 0);
			server.setExecutor(null);
			server.start();
			
			server.createContext("/cards", new CardsHandler(null));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public APIServer() {
		startServer();
	}
	
}
