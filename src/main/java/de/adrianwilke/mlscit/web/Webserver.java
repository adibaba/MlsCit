package de.adrianwilke.mlscit.web;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

/**
 * Web server
 * 
 * Dev note: On Eclipse com.sun.net.httpserver problems add access rule in
 * project libraries: com/sun/net/httpserver/**
 * https://stackoverflow.com/a/25945740
 *
 * @author Adrian Wilke
 */
public class Webserver {

	public void start(int port) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/", new RootHandler());
		server.createContext("/" + CategoryHandler.CAT, new CategoryHandler());
		server.createContext("/" + IncidentHandler.INC, new IncidentHandler());
		server.start();
	}
}