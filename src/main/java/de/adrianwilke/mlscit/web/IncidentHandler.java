package de.adrianwilke.mlscit.web;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import de.adrianwilke.mlscit.Incident;
import de.adrianwilke.mlscit.MlsCit;

/**
 * Incident information
 *
 * @author Adrian Wilke
 */
public class IncidentHandler extends AbstractHttpHandler {

	static String INC = "inc";

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			super.getInfo(exchange);
			htmlBuilder = new StringBuilder();

			if (parameters.containsKey(INC)
					&& MlsCit.mlsCit.incidents.containsKey(Integer.parseInt(parameters.get(INC)))) {
				int incidentNumber = Integer.parseInt(parameters.get(INC));
				Incident incident = MlsCit.mlsCit.incidents.get(incidentNumber);

				htmlBuilder.append("<h1>Incident ");
				htmlBuilder.append(incidentNumber);
				htmlBuilder.append("</h1>");

				htmlBuilder.append("<table>");

				toTableRow(htmlBuilder, "number", "" + incident.number);

				if (incident.merged != null) {
					StringBuilder mergedBuilder = new StringBuilder();
					mergedBuilder.append("<a href=\"/");
					mergedBuilder.append(IncidentHandler.INC);
					mergedBuilder.append("?");
					mergedBuilder.append(IncidentHandler.INC);
					mergedBuilder.append("=");
					mergedBuilder.append(incident.merged);
					mergedBuilder.append("\">");
					mergedBuilder.append(incident.merged);
					mergedBuilder.append("</a>");
					toTableRow(htmlBuilder, "merged", mergedBuilder.toString());
				} else {
					toTableRow(htmlBuilder, "merged", "" + incident.merged);
				}

				toTableRow(htmlBuilder, "description", incident.getDescription());
				toTableRow(htmlBuilder, "explanation", incident.explanation);
				toTableRow(htmlBuilder, "assessment", incident.assessment);
				toTableRow(htmlBuilder, "phase", "" + incident.phase);

				toTableRow(htmlBuilder, "observation " + "mls", incident.isObsMls() ? "MLS" : "analog");
				toTableRow(htmlBuilder, "observation " + "cnc", incident.isObsCnc() ? "CNC" : "conventional");
				toTableRow(htmlBuilder, "observation " + "observerFocus", incident.getObsObserfocus());

				toTableRow(htmlBuilder, "observation " + "year", "" + incident.getObsYear());
				toTableRow(htmlBuilder, "observation " + "job", incident.getObsJob());
				toTableRow(htmlBuilder, "observation " + "task", incident.getObsTask());
				toTableRow(htmlBuilder, "observation " + "situation", incident.getObsSituation());
				toTableRow(htmlBuilder, "observation " + "notes", incident.getObsNotes());
				toTableRow(htmlBuilder, "isDuplicate", incident.isDuplicate ? "true" : "false");
				toTableRow(htmlBuilder, "observation", "" + incident.observation);

				toTableRow(htmlBuilder, "observation " + "number", "" + incident.getObsNumber());
				toTableRow(htmlBuilder, "observation " + "formNumber", "" + incident.getObsFormNumber());

				toTableRow(htmlBuilder, "correction", incident.correction);

				htmlBuilder.append("</table>");

			} else {
				htmlBuilder.append("Unknown: " + parameters.get(INC));
			}

			super.handle(exchange);
		} catch (Exception e) {
			System.err.println(exceptionToString(e));
		}
	}

	void toTableRow(StringBuilder htmlBuilder, String key, String value) throws IOException {
		htmlBuilder.append("<tr>");

		htmlBuilder.append("<td>");
		htmlBuilder.append(key);
		htmlBuilder.append("</td>");

		htmlBuilder.append("<td>");
		htmlBuilder.append(value);
		htmlBuilder.append("</td>");

		htmlBuilder.append("</tr>");
	}
}