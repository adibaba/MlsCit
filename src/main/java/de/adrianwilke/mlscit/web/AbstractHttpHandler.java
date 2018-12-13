package de.adrianwilke.mlscit.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Helpers for handlers
 *
 * @author Adrian Wilke
 */
public abstract class AbstractHttpHandler implements HttpHandler {

	StringBuilder htmlBuilder;
	boolean isGetRequest;
	protected Map<String, String> parameters = new HashMap<String, String>();

	public void getInfo(HttpExchange exchange) throws IOException {
		isGetRequest = exchange.getRequestMethod().toUpperCase().equals("GET");

		extractParameters(exchange);
		// Closing an exchange without consuming all of the request body is not an error
		// but may make the underlying TCP connection unusable for following exchanges.
		exchange.getRequestBody().readAllBytes();
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<!DOCTYPE html>");
		stringBuilder.append("<html lang=\"en\">");
		stringBuilder.append("<head>");
		stringBuilder.append("<meta charset=\"utf-8\">");
		stringBuilder.append("<title>MLS CIT</title>");
		stringBuilder.append("<style>");
		stringBuilder.append("tr:nth-child(even)  {background:#EEE} ");
		stringBuilder.append("</style>");
		stringBuilder.append("</head>");
		stringBuilder.append("<body>");
		stringBuilder.append(htmlBuilder);
		stringBuilder.append("</body>");
		stringBuilder.append("</html>");

		byte[] bytes = stringBuilder.toString().getBytes();
		exchange.sendResponseHeaders(200, bytes.length);
		OutputStream outputStream = exchange.getResponseBody();
		outputStream.write(bytes);
		exchange.close();
	}

	public static String exceptionToString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		return stringWriter.toString();
	}

	/**
	 * Parses query string.
	 * 
	 * @param parameters map has to be constructed before, as it the needed HTTP
	 *                   request body can only be read once.
	 * 
	 * @see https://www.codeproject.com/Tips/1040097/Create-simple-http-server-in-Java
	 * 
	 * @throws WebserverIoException on errors reading request body or decoding
	 *                              parameters.
	 */
	protected void extractParameters(HttpExchange exchange) throws IOException {
		String query = null;
		if (isGetRequest) {
			try {
				query = exchange.getRequestURI().getRawQuery();
				if (query == null) {
					// Undefined query
					return;
				}
			} catch (NullPointerException e) {
				throw new IOException("NPE while filling GET parameters.", e);
			}
		} else {
			try {
				query = inputstramToString(exchange.getRequestBody());
			} catch (IOException e) {
				throw new IOException("Can not read request body.", e);
			} catch (NullPointerException e) {
				throw new IOException("NPE while filling POST parameters.", e);
			}
		}

		for (String pair : query.split("[&]")) {
			String param[] = pair.split("[=]");
			String key = null;
			String value = null;
			try {
				if (param.length > 0) {
					key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
				}
				if (param.length > 1) {
					value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
				}
			} catch (UnsupportedEncodingException e) {
				throw new IOException("Can not decode parameters.", e);
			}
			parameters.put(key, value);
		}
	}

	/**
	 * @see https://stackoverflow.com/a/35446009
	 */
	String inputstramToString(InputStream inputStream) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		// StandardCharsets.UTF_8.name() > JDK 7
		return result.toString("UTF-8");
	}
}