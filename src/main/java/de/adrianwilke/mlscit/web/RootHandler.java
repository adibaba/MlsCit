package de.adrianwilke.mlscit.web;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

	void correlationsToTableRow(StringBuilder htmlBuilder, Map<String, Set<Incident>> correlations, String categoryKey)
			throws IOException {
		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td>");
		htmlBuilder.append("<a href=\"/");
		htmlBuilder.append(CategoryHandler.CAT);
		htmlBuilder.append("?");
		htmlBuilder.append(CategoryHandler.CAT);
		htmlBuilder.append("=");
		htmlBuilder.append(categoryKey);
		htmlBuilder.append("\">");
		htmlBuilder.append(categoryKey);
		htmlBuilder.append("</a>");
		htmlBuilder.append("</td>");

		if (!correlations.containsKey(categoryKey)) {
			throw new IOException("Could not find category: " + categoryKey);
		}
		int numberOfIncidentsInCategory = correlations.get(categoryKey).size();

		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		decimalFormat.setDecimalFormatSymbols(otherSymbols);

		boolean evilLatexHack = false;
		for (Set<Incident> incidents : correlations.values()) {
			htmlBuilder.append("<td>");

			if (evilLatexHack) {
				double percent = 1.0 * incidents.size() / numberOfIncidentsInCategory;
				if (percent == 1) {
					htmlBuilder.append("\\textbf{");
					htmlBuilder.append(incidents.size());
					htmlBuilder.append("}");
				} else {
					htmlBuilder.append(decimalFormat.format(percent));
				}
				htmlBuilder.append(" &");
			} else {
				htmlBuilder.append(incidents.size());
			}

			htmlBuilder.append("</td>");
		}
		htmlBuilder.append("</tr>");
	}
}