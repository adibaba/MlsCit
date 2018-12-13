package de.adrianwilke.mlscit.web;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.sun.net.httpserver.HttpExchange;

import de.adrianwilke.mlscit.Categories;
import de.adrianwilke.mlscit.Incident;

/**
 * Category correlations
 *
 * @author Adrian Wilke
 */
public class CategoryHandler extends AbstractHttpHandler {

	static String CAT = "cat";

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			super.getInfo(exchange);
			htmlBuilder = new StringBuilder();

			if (parameters.containsKey(CAT) && Categories.categories.keySet().contains(parameters.get(CAT))) {
				String category = parameters.get(CAT);
				htmlBuilder.append("<h1>");
				htmlBuilder.append(category);
				htmlBuilder.append("</h1>");
				Map<String, Set<Incident>> correlations = Categories.getCorrelations(category);
				for (Entry<String, Set<Incident>> correlation : correlations.entrySet()) {
					htmlBuilder.append("<h2>");
					htmlBuilder.append(correlation.getKey());
					htmlBuilder.append(" (");
					htmlBuilder.append(correlation.getValue().size());
					htmlBuilder.append(")");
					htmlBuilder.append("</h2>");

					htmlBuilder.append("<table>");
					boolean first = true;
					for (Incident incident : correlation.getValue()) {
						if (first) {
							incidentToTableHeader(htmlBuilder, incident);
							first = false;
						}
						incidentToTableRow(htmlBuilder, incident);
					}
					htmlBuilder.append("</table>");
				}

			} else {
				htmlBuilder.append("Unknown: " + parameters.get(CAT));
			}

			super.handle(exchange);
		} catch (Exception e) {
			System.err.println(exceptionToString(e));
		}
	}

	void incidentToTableHeader(StringBuilder htmlBuilder, Incident incident) throws IOException {
		htmlBuilder.append("<tr>");

		htmlBuilder.append("<th>");
		htmlBuilder.append("no");
		htmlBuilder.append("</th>");

		htmlBuilder.append("<th>");
		htmlBuilder.append("ass");
		htmlBuilder.append("</th>");

		htmlBuilder.append("<th>");
		htmlBuilder.append("mls");
		htmlBuilder.append("</th>");

		htmlBuilder.append("<th>");
		htmlBuilder.append("cnc");
		htmlBuilder.append("</th>");

		htmlBuilder.append("<th>");
		htmlBuilder.append("description");
		htmlBuilder.append("</th>");

		htmlBuilder.append("</tr>");
	}

	void incidentToTableRow(StringBuilder htmlBuilder, Incident incident) throws IOException {
		htmlBuilder.append("<tr>");

		htmlBuilder.append("<td>");
		htmlBuilder.append("<a href=\"/");
		htmlBuilder.append(IncidentHandler.INC);
		htmlBuilder.append("?");
		htmlBuilder.append(IncidentHandler.INC);
		htmlBuilder.append("=");
		htmlBuilder.append(incident.number);
		htmlBuilder.append("\">");
		htmlBuilder.append(incident.number);
		htmlBuilder.append("</a>");
		htmlBuilder.append("</td>");

		htmlBuilder.append("<td>");
		htmlBuilder.append(incident.getAssessment());
		htmlBuilder.append("</td>");

		htmlBuilder.append("<td>");
		htmlBuilder.append(incident.isObsMls() ? "mls" : "analog");
		htmlBuilder.append("</td>");

		htmlBuilder.append("<td>");
		htmlBuilder.append(incident.isObsCnc() ? "cnc" : "conv");
		htmlBuilder.append("</td>");

		htmlBuilder.append("<td>");
		htmlBuilder.append(incident.getDescription());
		htmlBuilder.append("</td>");

		htmlBuilder.append("</tr>");
	}
}