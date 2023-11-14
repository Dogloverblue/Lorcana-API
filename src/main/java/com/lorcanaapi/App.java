package com.lorcanaapi;

import java.io.File;

import com.sun.net.httpserver.HttpServer;

public class App {

	static File dir = new File("src//data");

	public static void main(String[] args) {
		APIServer server = new APIServer();
	}

}
