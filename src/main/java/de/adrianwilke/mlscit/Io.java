package de.adrianwilke.mlscit;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Data access
 *
 * @author Adrian Wilke
 */
public class Io {

	public Map<Integer, Incident> readIncidents(String file) throws FileNotFoundException, IOException {
		Map<Integer, Incident> incidents = new HashMap<>();
		@SuppressWarnings("resource")
		CSVParser csvParser = new CSVParser(new FileReader(file), CSVFormat.DEFAULT.withFirstRecordAsHeader());

		// Print header overview

		System.out.println("Incidents headers:");
		Map<String, Integer> headers = csvParser.getHeaderMap();
		StringBuilder headerInfoBuilder = new StringBuilder();
		for (Entry<String, Integer> header : headers.entrySet()) {
			headerInfoBuilder.append(header.getValue());
			headerInfoBuilder.append(" ");
			headerInfoBuilder.append(header.getKey().trim());
			headerInfoBuilder.append(System.lineSeparator());
		}
		System.out.println(headerInfoBuilder.toString());
		System.out.println();

		// Parse

		for (CSVRecord csvRecord : csvParser.getRecords()) {
			Incident incident = new Incident();

			incident.number = Integer.parseInt(csvRecord.get(0));
			incident.observation = Integer.parseInt(csvRecord.get(1));
			String phase = csvRecord.get(2);

			if (phase.startsWith("1")) {
				incident.phase = Phases.phase1analysis;
			} else if (phase.startsWith("2")) {
				incident.phase = Phases.phase2preparation;
			} else if (phase.startsWith("3")) {
				incident.phase = Phases.phase3documentation;
			} else if (phase.startsWith("4")) {
				incident.phase = Phases.phase4setup;
			} else if (phase.startsWith("5")) {
				incident.phase = Phases.phase5programming;
			} else if (phase.startsWith("6")) {
				incident.phase = Phases.phase6manufacturing;
			} else if (phase.startsWith("7")) {
				incident.phase = Phases.phase7qualityControl;
			} else if (phase.startsWith("-")) {
				incident.phase = Phases.phaseUndefined;
			} else {
				throw new IOException(incident.number + "" + phase);
			}
			incident.setDescription(csvRecord.get(3));
			incident.assessment = csvRecord.get(4);
			incident.explanation = csvRecord.get(5);
			String mergedCell = csvRecord.get(6).trim();
			if (!mergedCell.isBlank()) {
				if (mergedCell.contains(" ")) {
					mergedCell = mergedCell.substring(0, mergedCell.indexOf(" "));
				}
				incident.merged = Integer.parseInt(mergedCell);
			}
			incident.correction = csvRecord.get(7);
			incident.isDuplicate = !csvRecord.get(8).isBlank();

			incident.socialTrainer = !csvRecord.get(9).isBlank();
			incident.socialOthers = !csvRecord.get(10).isBlank();
			incident.socialObervators = !csvRecord.get(11).isBlank();
			incident.processWork = !csvRecord.get(12).isBlank();
			incident.processLearn = !csvRecord.get(13).isBlank();
			incident.artifactDrawing = !csvRecord.get(14).isBlank();
			incident.artifactContents = !csvRecord.get(15).isBlank();
			incident.artifactProtocols = !csvRecord.get(16).isBlank();
			incident.artifactUserExperience = !csvRecord.get(17).isBlank();
			incident.artifactHardware = !csvRecord.get(18).isBlank();
			incident.artifactMachines = !csvRecord.get(19).isBlank();
			incident.artifactWorkpiece = !csvRecord.get(20).isBlank();
			incident.artifactEnvironment = !csvRecord.get(21).isBlank();

			incidents.put(incident.number, incident);
		}
		csvParser.close();
		return incidents;
	}

	public Map<Integer, Observation> readObservations(String file) throws FileNotFoundException, IOException {
		Map<Integer, Observation> observations = new HashMap<>();
		@SuppressWarnings("resource")
		CSVParser csvParser = new CSVParser(new FileReader(file), CSVFormat.DEFAULT.withFirstRecordAsHeader());

		// Print header overview

		System.out.println("Observation headers:");
		Map<String, Integer> headers = csvParser.getHeaderMap();
		StringBuilder headerInfoBuilder = new StringBuilder();
		for (Entry<String, Integer> header : headers.entrySet()) {
			headerInfoBuilder.append(header.getValue());
			headerInfoBuilder.append(" ");
			headerInfoBuilder.append(header.getKey().trim());
			headerInfoBuilder.append(System.lineSeparator());
		}
		System.out.println(headerInfoBuilder.toString());
		System.out.println();

		// Parse

		for (CSVRecord csvRecord : csvParser.getRecords()) {
			Observation observation = new Observation();

			observation.number = Integer.parseInt(csvRecord.get(0));
			observation.formNumber = Integer.parseInt(csvRecord.get(1));
			observation.observerFocus = csvRecord.get(2);
			observation.year = Integer.parseInt(csvRecord.get(3));
			observation.job = csvRecord.get(4);
			observation.task = csvRecord.get(5);
			String manufacturing = csvRecord.get(6).trim();
			if (!manufacturing.isBlank()) {
				if (manufacturing.equals("CNC")) {
					observation.cnc = true;
				} else if (manufacturing.startsWith("Konven")) {
					observation.cnc = false;
				} else {
					throw new IOException(observation.number + "" + manufacturing);
				}
			} else {
				throw new IOException(observation.number + "" + manufacturing);
			}
			String software = csvRecord.get(7).trim();
			if (!software.isBlank()) {
				if (software.startsWith("Mit")) {
					observation.mls = true;
				} else if (software.startsWith("Ohne")) {
					observation.mls = false;
				} else {
					throw new IOException(observation.number + "" + software);
				}
			} else {
				throw new IOException(observation.number + "" + software);
			}
			observation.situation = csvRecord.get(8);
			observation.notes = csvRecord.get(9);

			observations.put(observation.number, observation);
		}
		csvParser.close();
		return observations;
	}

}