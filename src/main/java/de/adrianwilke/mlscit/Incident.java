package de.adrianwilke.mlscit;

import java.io.IOException;

import de.adrianwilke.mlscit.web.AbstractHttpHandler;

/**
 * Incidents
 * 
 * TODO: duplicates
 *
 * @author Adrian Wilke
 */
public class Incident implements Comparable<Incident> {

	public int number;
	public int observation;
	public int phase;
	private String description;
	public String assessment;
	public String explanation;
	public Integer merged;
	public String correction;
	public boolean isDuplicate;

	public boolean processWork;
	public boolean processLearn;
	public boolean artifactDrawing;
	public boolean artifactContents;
	public boolean artifactProtocols;
	public boolean artifactUserExperience;
	public boolean artifactHardware;
	public boolean artifactMachines;
	public boolean artifactWorkpiece;
	public boolean artifactEnvironment;
	public boolean socialTrainer;
	public boolean socialOthers;
	public boolean socialObervators;

	@Override
	public int compareTo(Incident o) {
		try {
			int assessment = Integer.compare(getAssessmentAsInt(), o.getAssessmentAsInt());
			if (assessment == 0) {
				return Integer.compare(number, o.number);
			} else {
				return assessment;
			}
		} catch (IOException e) {
			System.err.println(Incident.class.getName() + " " + AbstractHttpHandler.exceptionToString(e));
			return Integer.compare(number, o.number);
		}
	}

	public int getAssessmentAsInt() throws IOException {
		if (assessment.equals("-")) {
			return 7;
		} else if (assessment.equals("stark negativ")) {
			return 1;
		} else if (assessment.equals("negativ")) {
			return 2;
		} else if (assessment.equals("leicht negativ")) {
			return 3;
		} else if (assessment.equals("leicht positiv")) {
			return 4;
		} else if (assessment.equals("positiv")) {
			return 5;
		} else if (assessment.equals("stark positiv")) {
			return 6;
		} else {
			throw new IOException(number + " " + assessment);
		}
	}

	public Observation getObservation() {
		return MlsCit.mlsCit.observations.get(observation);
	}

	public String getObsObserfocus() throws IOException {
		return getObservation().observerFocus;
	}

	public String getObsJob() throws IOException {
		return getObservation().job;
	}

	public String getObsTask() throws IOException {
		return getObservation().task;
	}

	public String getObsSituation() throws IOException {
		return getObservation().situation;
	}

	public String getObsNotes() throws IOException {
		return getObservation().notes;
	}

	public int getObsNumber() throws IOException {
		return getObservation().number;
	}

	public int getObsFormNumber() throws IOException {
		return getObservation().formNumber;
	}

	public int getObsYear() throws IOException {
		return getObservation().year;
	}

	public boolean isObsCnc() throws IOException {
		return getObservation().cnc;
	}

	public boolean isObsMls() throws IOException {
		return getObservation().mls;
	}

	// Text

	public void setDescription(String text) {
		this.description = text;
	}

	public String getDescription() {
		if (merged == null) {
			return description;
		} else {
			return description + "<br />{" + MlsCit.mlsCit.incidents.get(merged).getRawDescription() + "}";
		}
	}

	public String getRawDescription() {
		return description;
	}

	// Assessment

	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}

	public String getAssessment() throws IOException {
		if (merged == null) {
			return getRawAssessment();
		} else {
			return getRawAssessment() + "<br />{" + MlsCit.mlsCit.incidents.get(merged).getRawAssessment() + "}";
		}
	}

	public String getRawAssessment() throws IOException {
		switch (getAssessmentAsInt()) {
		case 7:
			return "?";
		case 1:
			return "---";
		case 2:
			return "--";
		case 3:
			return "-";
		case 4:
			return "+";
		case 5:
			return "++";
		case 6:
			return "+++";
		default:
			return "?";
		}
	}
}