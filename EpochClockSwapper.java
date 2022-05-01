import java.util.Date;

/*
*  Class for easing the conversion of UNIX epoch time. Intended for use with begin/end of day and 
*  for specifying up to when connections in the flow log should be processed
*
*/
public class EpochClockSwapper {
        
    private Date date; // Main Date object
    
    /*
    *  Takes current local epoch time in seconds
    */
    public EpochClockSwapper (long UNIXTime) {
        date = new Date(UNIXTime);
    }
    
    /*
    * Returns the time as a long epoch in seconds
    */
    public long getUNIXTime() {
        return date.getTime();
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
        final int SECONDS_PER_HOUR = 3_600;
        final int SECONDS_PER_MINUTE = 60;
        String timeAsString = getTruncatedDateTime();
        long hour = Long.parseLong(timeAsString.substring(0, 2));
        long minute = Long.parseLong(timeAsString.substring(3, 5));
        long second = Long.parseLong(timeAsString.substring(6));
        return hour*SECONDS_PER_HOUR + minute*SECONDS_PER_MINUTE + second;
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
