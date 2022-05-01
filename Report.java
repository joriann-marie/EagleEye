public class Report {
	/*
	 *
	 * holds the StringBuilders that output the entirety of the report
	 * toString() will give the entirety of the report
	 *
	 */

	private StringBuilder offHours;
	private StringBuilder overThresh;
	private StringBuilder outOfRegion;
	
	public Report () {
		offHours = new StringBuilder();
		overThresh = new StringBuilder();
		outOfRegion = new StringBuilder();
	}

	public void addToOffHour (Connection toAdd) {
		genericReportAdder(toAdd, offHours);
	}

	public void addToOverThresh(Connection toAdd) {
		genericReportAdder(toAdd, overThresh);
	}

	public void addToOutOfRegion(Connection toAdd) {
		genericReportAdder(toAdd, outOfRegion);
	}
	private void genericReportAdder (Connection toAdd, StringBuilder sb) {
		sb.append(toAdd.toString());
		sb.append("\n");
	}

	/*
	*  Outputs all of the alert-triggering connections, along with some guiding text for the user
	*/
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("ALL ACCEPTED CONNECTIONS OUTSIDE OF YOUR DEFINED BUSINESS HOURS");
		result.append("\n");
		result.append(offHours.toString());
		result.append("\n\n");
		result.append("ALL ACCEPTED CONNECTIONS OVER YOUR DEFINED BYTE THRESHOLD");
		result.append("\n");
		result.append(overThresh.toString());
		result.append("\n\n");
		result.append("ALL ACCEPTED CONNECTIONS FROM OUTSIDE OF YOUR DEFINED REGION");
		result.append("\n");
		result.append(outOfRegion.toString());
		return result.toString();
	}
}
