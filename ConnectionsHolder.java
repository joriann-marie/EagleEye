import java.util.ArrayList;

public class ConnectionsHolder {

	private ArrayList<Connection> allConnections;
	private ArrayList<Connection> overThreshConnections;
	private ArrayList<Connection> offHourConnections;
	private ArrayList<Connection> outsideRegionConnections;

	public ConnectionsHolder() {
		allConnections = new ArrayList<>();
		overThreshConnections = new ArrayList<>();
		offHourConnections = new ArrayList<>();
		outsideRegionConnections = new ArrayList<>();
	}

	/*
	*  Adds a connection to the array and updates the alert arraylists
	*/
	public void addConnection(Connection newCon) {
		allConnections.add(newCon);
		if (!newCon.underThreshold()) overThreshConnections.add(newCon);
		if (!newCon.withinTimeOfDay()) offHourConnections.add(newCon);
		if (!newCon.withinRegion()) outsideRegionConnections.add(newCon);
	}

	public boolean anyOverThreshold() {
		return overThreshConnections.size() != 0;
	}

	public boolean anyOffHours() {
		return offHourConnections.size() != 0;
	}

	public boolean anyOutsideRegions() {
		return outsideRegionConnections.size() != 0;
	}

	public ArrayList<Connection> allOverThreshold() {
		return overThreshConnections;
	}

	public ArrayList<Connection> allOffHours() {
		return offHourConnections;
	}

	public ArrayList<Connection> allOutsideRegions() {
		return outsideRegionConnections;
	}
}