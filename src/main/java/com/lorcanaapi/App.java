package com.lorcanaapi;

import java.io.File;
import java.sql.DriverManager;

import com.sun.net.httpserver.HttpServer;

public class App {

	static File dir = new File("src//data");

	public static void main(String[] args) {
//		try {
//			Class.forName("java.sql.Connection");
//			Class.forName("java.sql.Driver");
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (args.length < 3) {
			System.err.println("You must provide SQL details for this service to run properly. Put in '<JDBC URL> <Database Username> <Database Password>' into JVM arguments.");
			System.exit(0);
		}

		System.out.println("Server successfully booted!");
//		System.err.println("Hey You! You need to edit the source code and change the ");
		APIServer server = new APIServer(args[0], args[1], args[2]);
		
		if (args.length > 3) {
			if (args[3].equals("admin")) {
				server.setAdministratorMode(true);
			}
		}
		
		server.startServer();
		
		
	}

}
