package de.adrianwilke.mlscit;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

/**
 * Categories
 * 
 * TODO: Own class for categories
 *
 * @author Adrian Wilke
 */
public class Categories {

	public static Map<String, Set<Incident>> categories = new LinkedHashMap<>();
	private static Map<String, Map<String, Set<Incident>>> correlations = new LinkedHashMap<>();

	public static enum Category {

		// phases

		phase1analysis,

		phase2preparation,

		phase3documentation,

		phase4setup,

		phase5programming,

		phase6manufacturing,

		phase7qualityControl,

		phaseUndefined,

		// classification

		processWork,

		processLearn,

		artifactDrawing,

		artifactContents,

		artifactProtocols,

		artifactUserExperience,

		artifactHardware,

		artifactMachines,

		artifactWorkpiece,

		artifactEnvironment,

		socialTrainer,

		socialOthers,

		socialObservators,

		// observations

		cnc,

		conventional,

		mls,

		analog
	};

	public Categories() {
		for (Category category : Category.values()) {
			categories.put(category.name(), new TreeSet<Incident>());
		}
	}

	void createCategories(Incident incident, Map<Integer, Observation> observations) throws IOException {

		if (incident.phase == Phases.phase1analysis) {
			categories.get(Category.phase1analysis.name()).add(incident);
		} else if (incident.phase == Phases.phase2preparation) {
			categories.get(Category.phase2preparation.name()).add(incident);
		} else if (incident.phase == Phases.phase3documentation) {
			categories.get(Category.phase3documentation.name()).add(incident);
		} else if (incident.phase == Phases.phase4setup) {
			categories.get(Category.phase4setup.name()).add(incident);
		} else if (incident.phase == Phases.phase5programming) {
			categories.get(Category.phase5programming.name()).add(incident);
		} else if (incident.phase == Phases.phase6manufacturing) {
			categories.get(Category.phase6manufacturing.name()).add(incident);
		} else if (incident.phase == Phases.phase7qualityControl) {
			categories.get(Category.phase7qualityControl.name()).add(incident);
		} else if (incident.phase == Phases.phaseUndefined) {
			categories.get(Category.phaseUndefined.name()).add(incident);
		} else {
			throw new IOException(incident.number + " " + incident.phase);
		}

		if (incident.processWork) {
			categories.get(Category.processWork.name()).add(incident);
		}
		if (incident.processLearn) {
			categories.get(Category.processLearn.name()).add(incident);
		}
		if (incident.artifactDrawing) {
			categories.get(Category.artifactDrawing.name()).add(incident);
		}
		if (incident.artifactContents) {
			categories.get(Category.artifactContents.name()).add(incident);
		}
		if (incident.artifactProtocols) {
			categories.get(Category.artifactProtocols.name()).add(incident);
		}
		if (incident.artifactUserExperience) {
			categories.get(Category.artifactUserExperience.name()).add(incident);
		}
		if (incident.artifactHardware) {
			categories.get(Category.artifactHardware.name()).add(incident);
		}
		if (incident.artifactMachines) {
			categories.get(Category.artifactMachines.name()).add(incident);
		}
		if (incident.artifactWorkpiece) {
			categories.get(Category.artifactWorkpiece.name()).add(incident);
		}
		if (incident.artifactEnvironment) {
			categories.get(Category.artifactEnvironment.name()).add(incident);
		}
		if (incident.socialTrainer) {
			categories.get(Category.socialTrainer.name()).add(incident);
		}
		if (incident.socialOthers) {
			categories.get(Category.socialOthers.name()).add(incident);
		}
		if (incident.socialObervators) {
			categories.get(Category.socialObservators.name()).add(incident);
		}

		if (observations.get(incident.observation).cnc) {
			categories.get(Category.cnc.name()).add(incident);
		} else {
			categories.get(Category.conventional.name()).add(incident);
		}

		if (observations.get(incident.observation).mls) {
			categories.get(Category.mls.name()).add(incident);
		} else {
			categories.get(Category.analog.name()).add(incident);
		}
	}

	public static String getShorthand(String category) throws IOException {
		if (category.equals(Category.phase1analysis.name())) {
			return "1-ana";
		} else if (category.equals(Category.phase2preparation.name())) {
			return "2-pre";
		} else if (category.equals(Category.phase3documentation.name())) {
			return "3-doc";
		} else if (category.equals(Category.phase4setup.name())) {
			return "4-set";
		} else if (category.equals(Category.phase5programming.name())) {
			return "5-pro";
		} else if (category.equals(Category.phase6manufacturing.name())) {
			return "6-man";
		} else if (category.equals(Category.phase7qualityControl.name())) {
			return "7-qual";
		} else if (category.equals(Category.phaseUndefined.name())) {
			return "0-none";
		} else if (category.equals(Category.processWork.name())) {
			return "pWork";
		} else if (category.equals(Category.processLearn.name())) {
			return "pLearn";
		} else if (category.equals(Category.artifactDrawing.name())) {
			return "aDraw";
		} else if (category.equals(Category.artifactContents.name())) {
			return "aCon";
		} else if (category.equals(Category.artifactProtocols.name())) {
			return "aProt";
		} else if (category.equals(Category.artifactUserExperience.name())) {
			return "aUX";
		} else if (category.equals(Category.artifactHardware.name())) {
			return "aHW";
		} else if (category.equals(Category.artifactMachines.name())) {
			return "aMach";
		} else if (category.equals(Category.artifactWorkpiece.name())) {
			return "aPiece";
		} else if (category.equals(Category.artifactEnvironment.name())) {
			return "aEnv";
		} else if (category.equals(Category.socialTrainer.name())) {
			return "sTrainer";
		} else if (category.equals(Category.socialOthers.name())) {
			return "sOther";
		} else if (category.equals(Category.socialObservators.name())) {
			return "sObser";
		} else if (category.equals(Category.cnc.name())) {
			return "CNC";
		} else if (category.equals(Category.conventional.name())) {
			return "CONV";
		} else if (category.equals(Category.mls.name())) {
			return "MLS";
		} else if (category.equals(Category.analog.name())) {
			return "analog";
		} else {
			throw new IOException("Unknown category: " + category);
		}

	}

	public static Map<String, Set<Incident>> getCorrelations(String category) {
		if (!correlations.containsKey(category)) {
			Map<String, Set<Incident>> catCorrelations = new LinkedHashMap<>();
			Collection<Incident> incidentsA = Categories.categories.get(category);
			for (Entry<String, Set<Incident>> categoryEntry : Categories.categories.entrySet()) {
				Set<Incident> intersection = new LinkedHashSet<Incident>(incidentsA);
				intersection.retainAll(categoryEntry.getValue());
				catCorrelations.put(categoryEntry.getKey(), intersection);
			}
			correlations.put(category, catCorrelations);
		}
		return correlations.get(category);
	}
}
