import java.util.Scanner;
import java.util.Stack;

public class Connection { // 1651074984 43227 172.31.24.7 172.31.24.7 123 108.61.56.35 108.61.56.35
                          // us-east-1 76

    // Values that determine if a connection triggers an alert
    private long THRESHOLD; // Above this triggers an alert
    private String MODE_REGION; // Not from here triggers an alert
    private long BEGIN_DAY; // Before this triggers an alert
    private long END_DAY; // After this triggers an alert

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
    private String toString;

    /*
     * Pre: flowLogLine not null, modeRegion not null
     * 
     * FlowLogLine is a line from the flow log who has the elements listed in the order they appear
     * above this constructor
     */
    public Connection(String flowLogLine, long threshold, String modeRegion, long beginDay, long endDay) { 
        Scanner flowLogLineReader = new Scanner(flowLogLine);
        Stack<String> tokenHolder = new Stack<>();
        toString = flowLogLine;
        while (flowLogLineReader.hasNext()) {
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
        THRESHOLD = threshold;
        MODE_REGION = region;
        BEGIN_DAY = beginDay;
        END_DAY = endDay;
        flowLogLineReader.close();
    }

    public long getStart() {
        return start;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public String getSourcePacketIP() {
        return sourcePacketIP;
    }

    public int getDestPort() {
        return destPort;
    }

    public String getDestIP() {
        return destIP;
    }

    public String getDestPacketIP() {
        return destPacketIP;
    }

    public String getRegion() {
        return region;
    }

    public long getBytes() {
        return bytes;
    }

    public String toString() {
        return toString;
    }

    /*
     * Returns true if the start time contained within tokenHolder is within
     * BEGIN_DAY and END_DAY
     */
    public boolean withinTimeOfDay() {
        long time = getStart();
        return (time > BEGIN_DAY && time < END_DAY);
    }

    /*
     * Returns true if region matches the user-specified mode region
     */
    public boolean withinRegion() {
        return (region.equals(MODE_REGION));
    }

    /*
     * Returns true if the number of bytes is under the user-defined byte threshold
     */
    public boolean underThreshold() {
        return bytes < THRESHOLD;
    }
}
