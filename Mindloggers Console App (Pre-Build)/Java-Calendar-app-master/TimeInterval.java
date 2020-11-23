

/**
 * The TimeInterval class represents an interval of time.
 * Includes a method to check whether two intervals overlap.
 */
public class TimeInterval 
{
	private int startTime;
	private int endTime;
	private String startTimeString;
	private String endTimeString;
	
	//Constructs a new TimeInterval with the a given start and end time
	public TimeInterval(int s, int e, String ss, String es)
	{
		startTime = s;
		endTime = e;
		startTimeString = ss;
		endTimeString = es;
	}
	
	//Returns the Start Time as an integer
	public int getStart()
	{
		return startTime;
	}
	
	//Returns the Start Time as a String
	public String startPrint()
	{
		return startTimeString;
	}
	
	//Returns the End Time as an integer
	public int getEnd()
	{
		return endTime;
	}
	
	//Returns the End Time as a String
	public String endPrint()
	{
		return endTimeString;
	}
	
	//Checks to see if two event's TimeInterval's overlap
	//If the Start Time or the End Time is in between another's, return true
	public boolean overlap(Event e)
	{
		boolean overlap = false;
		
		if(e.getTimeInterval().getStart() >= getStart() && e.getTimeInterval().getStart() <= getEnd())
			overlap = true;
		else if(e.getTimeInterval().getEnd() >= getStart() && e.getTimeInterval().getEnd() <= getEnd())
			overlap = true;
		else if (e.getTimeInterval().getStart() == getStart() || e.getTimeInterval().getStart() == getEnd()
			     || e.getTimeInterval().getEnd() == getStart() || e.getTimeInterval().getEnd() == getEnd())
			overlap = true;
		
		return overlap;
	}
}

//END