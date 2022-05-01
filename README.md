# EagleEye
An AWS network monitor


Development Plan
PHASE I - Import flow log in its entirety
PHASE II - Iterate over all flow log entries
IIi - If the entry has the correct number of flags and is within the user-specified timeframe:
IIii - Make a Connection object out of the flow log entry
IIiii - Place it in the IP map
PHASE III - Iterate over all keys in the IP Map
IIIi - Make a StringBuilder for off-hour, over-threshold, and out-of-region reports
IIIii - If the ConnectionHolder breaks a criteria, retrieve the corresponding ArrayList of Connections and add the Connections'toString() to the corresponding report
PHASE IV - Use each report StringBuilder to output the alert report

CLASSES AND DATA STRUCTURES

IP HashMap :
  key is the source packet ip as a String
  value is a ConnectionsHolder object
  
Report:
  holds the StringBuilders that output the entirety of the report
  toString() will give the entirety of the report 

ConnectionsHolder:
  holds all the connections that share a source packet ip
  the addConnection() method will determine whether the Connection violates any alert triggers and add them to the appropriate internal ArrayList
  used to return all the Connections that trigger each alert for that source packet ip
  
Connections:
  contains all the info in one flow log entry
  Phase II code should determine if well-formed and is within the user-specified-timeframe. That is, don't create a Connection from an entry that should've been filtered
  will determine if the connection violates any alert triggers upon construction
  held in ConnectionsHolder, its toString() will print the log entry in a user-readable format and will constitute the bulk of the alert report

EpochClockSwapper:
  makes converting from UNIX system time to calendar time simpler
  takes LOCAL UNIX time IN MILLISECONDS in constructor, all returns are in SECONDS
  can be used primarily within Connection to determine if the connection falls within the user-defined time of day and in toString()
