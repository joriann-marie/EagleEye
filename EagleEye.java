import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;

public class EagleEye {

    // Dates between which connections will be considered. In epoch time seconds
    private static final long START_DATE = Long.MIN_VALUE; //Make Long.MIN_VALUE to start from the beginning of the flow log
    private static final long END_DATE = Long.MAX_VALUE; // Make Long.MAX_VALUE to continue until end of the flow log

    // Values that determine if a connection triggers an alert
    private static final long THRESHOLD = 100; // In bytes, Above this triggers an alert
    private static final String MODE_REGION = "us-east-1"; // Not from here triggers an alert
    private static final long BEGIN_DAY = 32_400; //9am, before this triggers an alert
    private static final long END_DAY = 61_200; //5pm, after this triggers an alert
    
    // Indices for the flow log. Flow log should have exactly these fields in exactly this order
    private static final int START_INDEX = 0;
    /*private static final int SOURCEPORT_INDEX = 1;
    private static final int SOURCEIP_INDEX = 2;
    private static final int SOURCEPACKETIP_INDEX = 3;
    private static final int DESTPORT_INDEX = 4;
    private static final int DESTIP_INDEX = 5;
    private static final int DESTPACKETIP_INDEX = 6;
    private static final int REGION_INDEX = 7;
    private static final int BYTE_INDEX = 8;
    */
    private static final int NUM_FLAGS = 9;

    // Main hash map of connections, key is IP and value is a ConnectionsHolder
    private static HashMap<String, ConnectionsHolder> IPHolder = new HashMap<>();;

    // Holds all of the information that is given to the user
    private static Report userReport = new Report();


    public static void main (String[] args) throws FileNotFoundException {
        File flowLog = getFile();
        createConnections(flowLog);
        assembleReport();
        outputReport();
    }
    /*
    *  Accomplishes phase I of the design brief: 
    *  Pre: flowLog is named "log-events-viewer-result.csv" and is in the working directory
    */
    private static File getFile() {
        File result = new File("log-events-viewer-result.csv");
        if (!result.exists()) {
            throw new IllegalStateException("\"log-events-viewer-result.csv\" not found");
        } else {
            return result;
        }
    }

    /*
    *  Accomplishes phase II of the design brief: Populates the IP map
    *  Pre: flowLog is a parseable flow log from the AWS cloud with the necessary flags in the necessary order
    */
    private static void createConnections(File flowLog) throws FileNotFoundException {
        Scanner flowLogScanner = new Scanner(flowLog);
        flowLogScanner.nextLine(); // Very first line is a message
        while (flowLogScanner.hasNextLine()) { //Iterates over rest of the lines
            String flowLogLine = sanitizeConnectionLine(flowLogScanner.nextLine());
            Scanner flowLogLineReader = new Scanner(flowLogLine);
            ArrayList<String> tokenHolder = new ArrayList<>();
            while (flowLogLineReader.hasNext()) { // Puts all tokens of a line into an ArrayList
                tokenHolder.add(flowLogLineReader.next());
            }
            flowLogLineReader.close();
            // Phase IIi
            if (flowLogLineWellFormed(tokenHolder) && withinDateRange(tokenHolder)) { 
                // Only enters if the line has the appropriate number of tokens and is within the specified dates
                // Phase IIii
                Connection toAdd = new Connection(flowLogLine, THRESHOLD, MODE_REGION, BEGIN_DAY, END_DAY);
                addToMap(toAdd);
            }
        }
        flowLogScanner.close();
    }

    /*
    *  Removes up until the , for the line in the flow log
    */ 
    private static String sanitizeConnectionLine (String flowLogLine) {
        int keepPast = flowLogLine.indexOf(',') + 1;
        return flowLogLine.substring(keepPast);
    }
    
    private static boolean flowLogLineWellFormed (ArrayList<String> flowLogLines) {
        return flowLogLines.size() == NUM_FLAGS;
    }

    /*
     * Returns true if the start time contained within tokenHolder is within
     * START_DATE and END_DATE
     */
    private static boolean withinDateRange(ArrayList<String> tokenHolder) {
        long time = Long.parseLong(tokenHolder.get(START_INDEX));
        return (time >= START_DATE && time <= END_DATE);
    }

    /*
    *  Phase IIiii
    *  Inserts toAdd into the IP hash map. Key is the SourcePacketIP, value is ConnectionsHolder. 
    *  Will add a ConnectionsHolder pair if one is missing. Otherwise, updates the existing ConnectionsHolder. 
    */
    private static void addToMap(Connection toAdd) {
        String ip = toAdd.getSourcePacketIP();
        if (IPHolder.containsKey(ip)) {
            ConnectionsHolder addConnectionTo = IPHolder.get(ip);
            addConnectionTo.addConnection(toAdd);
        } else {
            ConnectionsHolder addIntoMap = new ConnectionsHolder();
            addIntoMap.addConnection(toAdd);
            IPHolder.put(ip, addIntoMap);
        }
    }

    /*
    *  Phase III
    *  Iterates over the IP hash map, adds trigger-alerting connection to the report, and updates the Report object
    *  Each key in the map is a Source Packet IP, each key a ConnectionsHolder
    *  Each ConnectionsHolder has an ArrayList<Connection> for the alert-triggering connections of each flavor for that IP
    */
    private static void assembleReport() {
        Set<String> hashMapKeys = IPHolder.keySet();
        for (String IP : hashMapKeys) {
            ConnectionsHolder currentHolder = IPHolder.get(IP);
            if (currentHolder.anyOffHours()) {
                ArrayList<Connection> offHourConnections = currentHolder.allOffHours();
                for (Connection con : offHourConnections) {
                    userReport.addToOffHour(con);
                }
            }
            if (currentHolder.anyOverThreshold()) {
                ArrayList<Connection> overThreshConnections = currentHolder.allOverThreshold();
                for (Connection con : overThreshConnections) {
                    userReport.addToOverThresh(con);
                }
            }
            if (currentHolder.anyOutsideRegions()) {
                ArrayList<Connection> outerRegionConnections = currentHolder.allOutsideRegions();
                for (Connection con : outerRegionConnections) {
                    userReport.addToOutOfRegion(con);
                }
            }
        }   
    }
/*
 *Phase IV
 * The updated report object is output to a file for user access
 * Essentially just a matter of taking the Report's toString() and writing it to the correct file/directory
 * Not sure how files would work for AWS, may need some work to make that work.
 *
 */
    private static void outputReport() throws FileNotFoundException {
        File userFile = new File("Eagle Eye Report.txt");
        PrintStream output = new PrintStream(userFile);
        output.print(userReport.toString());
        output.close();
    }
}