import java.util.Date;

/*
*  Class for easing the conversion of UNIX epoch time. Intended for use with begin/end of day and 
*  for specifying up to when connections in the flow log should be processed
*
*/
public class EpochClockSwapper {
    
    private static final long TRUNCATE_DATE = 86_400; // Shave off MM/dd/yyyy and only return clock time
    private static final int OFFSET = 3_600; // Don't know why but having this perfectly aligns truncated time to give just the hour and minute
    
    private Date date; // Main Date object
    
    /*
    *  Takes current local epoch time in milliseconds
    */
    public EpochClockSwapper (long UNIXTime) {
        date = new Date(UNIXTime);
    }
    
    /*
    * Returns the time as a long epoch in seconds
    */
    public long getUNIXTime() {
        return date.getTime()/1000;
    }
    
    /*
    * Returns the time in MM/dd/yyyy HH:mm:ss format
    */
    public String getDateTime() {
            // Taken from https://www.epochconverter.com/
            return new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (date.getTime()));
    }
    
    /*
    * Returns the time as a long epoch in seconds, but as if it were on day 0 so as to only have the clock time
    */
    public long getUNIXTruncatedTime() {
        String timeAsString = getTruncatedDateTime();
        String hour = timeAsString.substring(0, 2);
        String minute = timeAsString.substring(3, 5);
        String second = timeAsString.substring(6);
        // TODO
        
    }
    
    /*
    * Returns the time in HH:mm:ss format
    */
    public String getTruncatedDateTime() {
            // Taken from https://www.epochconverter.com/
            final int TRUNCATE_CALENDAR_DATE = 11;
            String result = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (
                    date.getTime()));
            return result.substring(TRUNCATE_CALENDAR_DATE);
    }
}
