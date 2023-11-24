package com.lorcanaapi;

import java.io.File;

import com.sun.net.httpserver.HttpServer;

public class App {

	static File dir = new File("src//data");

	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("You must provide SQL details for this service to run properly. Put in '<JDBC URL> <Database Username> <Database Password>' into JVM arguments.");
			System.exit(0);
		}
		System.out.println("Server successfully booted!");
//		System.err.println("Hey You! You need to edit the source code and change the ");
		APIServer server = new APIServer(args[0], args[1], args[2]);
	}

}
