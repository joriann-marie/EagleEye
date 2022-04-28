import java.util.Scanner;
import java.util.Stack;

//TODO Redo constructor. Will already be verified as well-formed and within the timeframe

public class Connection { // 1651074984 43227 172.31.24.7 172.31.24.7 123 108.61.56.35 108.61.56.35
                          // us-east-1 76

    // Flow log line elements
    private long start;
    private int sourcePort;
    private String sourceIP;
    private String sourcePacketIP;
    private int destPort;
    private String destIP;
    private String destPacketIP;
    private String region;
    private long bytes;

    // Meta Info
    private boolean wellFormed;
    private String toString;
    
    // Anomaly booleans
    private boolean overThreshold;
    private boolean modeRegion;
    private boolean duringBusinessDay;

    /*
     * Pre: flowLogLine not null, modeRegion not null, startOfDay < endOfDay, 
     * 
     * FlowLogLine is a line from the flow log who has the elements listed in the order they appear
     * above this constructor
     */
    public Connection(String flowLogLine, long threshold, String modeRegion, long startOfDay, long endOfDay) { 
        Scanner flowLogLineReader = new Scanner(flowLogLine);
        Stack<String> tokenHolder = new Stack<>();
        while (flowLogLineReader.hasNext()) {
            tokenHolder.push(flowLogLineReader.next());
        }
        if (tokenHolder.size() != 9) { // Not well-formed
            wellFormed = false;
        } else { // Well-formed, populate instance variables
            wellFormed = true;
            toString = flowLogLine;

            bytes = Long.parseLong(tokenHolder.pop());
            region = tokenHolder.pop();
            destPacketIP = tokenHolder.pop();
            destIP = tokenHolder.pop();
            destPort = Integer.parseInt(tokenHolder.pop());
            sourcePacketIP = tokenHolder.pop();
            sourceIP = tokenHolder.pop();
            sourcePort = Integer.parseInt(tokenHolder.pop());
            start = Long.parseLong(tokenHolder.pop());
        }
        flowLogLineReader.close();
    }
}
