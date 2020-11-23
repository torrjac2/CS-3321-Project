

/**
 * This event class states the event parameters
 * An event consists of a Name and a TimeInterval.
 */
public class Event 
{
	private String name;
	private TimeInterval timeInterval;
	private boolean isRegular;
	
	//Event constructor
	public Event(String n, TimeInterval tI, boolean isR)
	{ name = n; timeInterval = tI; isRegular = isR; }
	
	//Returns the name of the event
	public String getName() { return name; }
	
	//Returns the time interval class for the given event
	public TimeInterval getTimeInterval()
	{
		return timeInterval;
	}
	
	//Returns a boolean value if the event given is a regular even or not
	public boolean getIsRegular()
	{
		return isRegular;
	}
}

//END