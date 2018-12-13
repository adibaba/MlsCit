package de.adrianwilke.mlscit.web;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sun.net.httpserver.HttpExchange;

import de.adrianwilke.mlscit.Categories;
import de.adrianwilke.mlscit.Incident;

/**
 * Correlation overview
 *
 * @author Adrian Wilke
 */
public class RootHandler extends AbstractHttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			super.getInfo(exchange);
			htmlBuilder = new StringBuilder();

			htmlBuilder.append("<table>");
			boolean first = true;
			for (Entry<String, Set<Incident>> category : Categories.categories.entrySet()) {
				Map<String, Set<Incident>> correlations = Categories.getCorrelations(category.getKey());
				if (first) {
					correlationsToTableHeader(htmlBuilder, correlations);
					first = false;
				}
				correlationsToTableRow(htmlBuilder, correlations, category.getKey());
			}
			htmlBuilder.append("</table>");

			super.handle(exchange);
		} catch (Exception e) {
			System.err.println(exceptionToString(e));
		}
	}

	void correlationsToTableHeader(StringBuilder htmlBuilder, Map<String, Set<Incident>> correlations)
			throws IOException {
		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td>");
		htmlBuilder.append("</td>");
		for (String category : correlations.keySet()) {
			htmlBuilder.append("<td>");
			htmlBuilder.append(Categories.getShorthand(category));
			htmlBuilder.append("</td>");
		}
		htmlBuilder.append("</tr>");
	}

	void correlationsToTableRow(StringBuilder htmlBuilder, Map<String, Set<Incident>> correlations, String first) {
		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td>");
		htmlBuilder.append("<a href=\"/");
		htmlBuilder.append(CategoryHandler.CAT);
		htmlBuilder.append("?");
		htmlBuilder.append(CategoryHandler.CAT);
		htmlBuilder.append("=");
		htmlBuilder.append(first);
		htmlBuilder.append("\">");
		htmlBuilder.append(first);
		htmlBuilder.append("</a>");
		htmlBuilder.append("</td>");
		for (Set<Incident> incidents : correlations.values()) {
			htmlBuilder.append("<td>");
			htmlBuilder.append(incidents.size());
			htmlBuilder.append("</td>");
		}
		htmlBuilder.append("</tr>");
	}
}