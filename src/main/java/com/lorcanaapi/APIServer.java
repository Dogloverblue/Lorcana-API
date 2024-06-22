package com.lorcanaapi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;

import com.lorcanaapi.handlers.BulkHandler;
import com.lorcanaapi.handlers.CardsHandler;
import com.lorcanaapi.handlers.SetsHandler;
import com.lorcanaapi.handlers.StatisticsHandler;
import com.lorcanaapi.legacy.LegacyAPIServer;
import com.lorcanaapi.parameters.MandatorySQLExecutor;
import com.lorcanaapi.precursors.bulkhandler.BulkCardsPrecursor;
import com.sun.net.httpserver.HttpServer;

public class APIServer {

	HttpServer server;

	public void startServer() {
		try {
			Integer port = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8080"));
			server = HttpServer.create(new InetSocketAddress(port), 0);
			server.setExecutor(null);
			server.start();
			try {
				LegacyAPIServer.startLegacyServer(server);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			server.createContext("/cards", new CardsHandler());
			server.createContext("/sets", new SetsHandler());
			server.createContext("/bulk", new BulkHandler());
			server.createContext("/stats", new StatisticsHandler());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public APIServer(String SQLDBURL, String SQLUser, String SQLPass) {
		MandatorySQLExecutor.setSQLCreddentials(SQLDBURL, SQLUser, SQLPass);
		startServer();
	}
	
}
