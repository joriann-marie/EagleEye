import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

//TODO Determine if within timeframe 
// see https://www.epochconverter.com/
// Phase IIi

public class EagleEye {

    private static final int NUM_FLAGS = 9;
    private static final String MODE_REGION = "us-east-1";
    private static final long BEGIN_DAY; //9am
    
    /*
     * Pre: flowLog is imported from the flow log with the following flags: 
     * 
     * private long start; 
     * private int sourcePort; 
     * private String sourceIP; 
     * private String sourcePacketIP;
     * private int destPort; 
     * private String destIP; 
     * private String destPacketIP; 
     * private String region; 
     * private long bytes;
     */
    public static void createConnections(File flowLog) throws FileNotFoundException {
        Scanner flowLogScanner = new Scanner(flowLog);
        flowLogScanner.nextLine(); // Very first line is a message
        while (flowLogScanner.hasNextLine()) { //Iterates over rest of the lines
            String flowLogLine = flowLogScanner.nextLine();
            Scanner flowLogLineReader = new Scanner(flowLogLine);
            Stack<String> tokenHolder = new Stack<>();
            while (flowLogLineReader.hasNext()) { // Puts all tokens of a line into stack
                tokenHolder.push(flowLogLineReader.next());
            }
            flowLogLineReader.close();
            if (flowLogLineWellFormed(tokenHolder)) { // Must have appropriate # of tokens
                
            }
            
            
        }
        flowLogScanner.close();
    }
    
    private static boolean flowLogLineWellFormed (Stack<String> flowLogLines) {
        return flowLogLines.size() == NUM_FLAGS;
    }
}
