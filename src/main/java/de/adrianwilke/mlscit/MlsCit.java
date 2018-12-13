package de.adrianwilke.mlscit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.adrianwilke.mlscit.web.Webserver;

/**
 * Data analysis of MLS CIT observations
 *
 * @author Adrian Wilke
 */
public class MlsCit {

	// TODO: Singleton or Factory
	public static MlsCit mlsCit;

	public Map<Integer, Incident> incidents;
	Map<Integer, Observation> observations;
	Categories categories;

	public static void main(String[] args) throws FileNotFoundException, IOException {

		if (args.length == 0) {
			System.out.println("Two arguments needed:");
			System.out.println("- Incidents CSV file");
			System.out.println("- Observations CSV file");
			System.out.println("- Webserver port (e.g. 8080)");
			System.exit(1);
		}
		mlsCit = new MlsCit();
		mlsCit.readData(args[0], args[1]);

		// Incidents

		int incidentCounter = 0;
		int incidentDuplicateCounter = 0;
		for (Entry<Integer, Incident> incident : mlsCit.incidents.entrySet()) {
			incidentCounter++;
			if (incident.getValue().isDuplicate) {
				incidentDuplicateCounter++;
			}
		}
		System.out.println("Number of incidents: " + incidentCounter);
		System.out.println("Number of non-duplicates: " + incidentDuplicateCounter);

		// Observations

		int observationsCounter = 0;
		for (@SuppressWarnings("unused")
		Entry<Integer, Observation> observation : mlsCit.observations.entrySet()) {
			observationsCounter++;
		}
		System.out.println("Number of observations: " + observationsCounter);
		System.out.println();

		// Categories

		mlsCit.categories = new Categories();
		for (Incident incident : mlsCit.incidents.values()) {
			// Do not include duplicates
			if (!incident.isDuplicate) {
				mlsCit.categories.createCategories(incident, mlsCit.observations);
			}
		}
		System.out.println("Categories:");
		for (Entry<String, Set<Incident>> categories : Categories.categories.entrySet()) {
			System.out.print(categories.getKey());
			System.out.print("  ");
			System.out.println(categories.getValue().size());
		}
		System.out.println();

		// Start webserver

		new Webserver().start(Integer.parseInt(args[2]));
	}

	void readData(String incicentsFile, String observationsFile) throws FileNotFoundException, IOException {
		Io io = new Io();
		incidents = io.readIncidents(incicentsFile);
		observations = io.readObservations(observationsFile);
	}

}