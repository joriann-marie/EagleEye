import java.util.Date;

public class EpochClockSwapper {
    
    private static final long TRUNCATE_DATE = 86_400; // UNIX time will only give clock time
    
    private Date date;
    
    private String dateTruncatedTime = null;

    public EpochClockSwapper (long UNIXTime) {
        date = new Date(UNIXTime);
    }
    
    public long getUNIXTime() {
        return date.getTime();
    }
    
    public String getDateTime() {
            // Taken from https://www.epochconverter.com/
            return new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (getUNIXTime()));
    }
    
    public long getUNIXTruncatedTime() {
        return getUNIXTime() % TRUNCATE_DATE;
    }
    
    public String getTruncatedDateTime() {
            // Taken from https://www.epochconverter.com/
            return new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (getUNIXTruncatedTime()));
    }
}
