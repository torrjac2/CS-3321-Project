

import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Calendar class that defines the calendar dates and format
 * Defines a HashMap to hold an ArrayList of events, using the LocalDate date as the key
 */
public class MyCalendar 
{
	private HashMap<LocalDate, ArrayList<Event>> myCal;
	private LocalDate currentDate;

	//Constructs a Calendar (myCal) using a HashMap with the LocalDate as the key
	public MyCalendar(LocalDate cd)
	{ myCal = new HashMap<LocalDate, ArrayList<Event>>(); currentDate = cd; }
	
	//Adds the event to the given LocalDate key's ArrayList
	public void addEvent(LocalDate d, Event e)
	{
		if(myCal.containsKey(d))
		{
			ArrayList<Event> events = myCal.get(d);
			events.add(e);
			myCal.put(d, events);
		}
		else
		{
			ArrayList<Event> events = new ArrayList<Event>();
			events.add(e);
			myCal.put(d, events);
		}
	}

	//Removes a single event from the ArrayList, only if it's not regular
	public boolean removeOneTimeEventSpecific(LocalDate d, Event e)
	{
		boolean removed = false;
		if(myCal.containsKey(d))
		{
			ArrayList<Event> events = myCal.get(d);
			Event ev = findEvent(d, e.getName());
			if(!ev.getIsRegular())
				events.remove(e);
			myCal.put(d, events);
			
			if(events.isEmpty())
				myCal.remove(d);
			
			removed = true;
		}
		return removed;
	}	
	
	//Removes all non-regular events on the given day (Only occurs once)
	public boolean removeOneTimeEventAll(LocalDate d)
	{
		boolean removed = false;
		if(myCal.containsKey(d))
		{
			ArrayList<Event> events = myCal.get(d);
			
			java.util.Iterator<Event> itr = events.iterator();
			while(itr.hasNext())
			{
				Event e = itr.next();
				if(!e.getIsRegular())
					itr.remove();
			}
			
			myCal.put(d, events);
			
			if(events.isEmpty())
				myCal.remove(d);
			
			removed = true;
		}
		
		return removed;
	}	
	
	//Removes a single regular event on the given day (Just that day)
	public boolean removeRegularEvent(LocalDate d, Event e)
	{
		boolean removed = false;
		if(myCal.containsKey(d))
		{
			ArrayList<Event> events = myCal.get(d);

			if(e.getIsRegular())
				events.remove(e);
			myCal.put(d, events);
			
			if(events.isEmpty())
				myCal.remove(d);
			
			removed = true;
		}
		
		return removed;
	}	
	
	//Returns to the current day
	public LocalDate currentDay()
	{
		return currentDate;
	}
	
	//Moves one day back from the current day
	public LocalDate previousDay()
	{
		return currentDate = currentDate.minusDays(1);
	}
	
	//Moves one day forward from the current day
	public LocalDate nextDay()
	{
		return currentDate = currentDate.plusDays(1);
	}
	
	//Moves one month back from the current month
	public LocalDate previousMonth()
	{
		return currentDate = currentDate.minusMonths(1);
	}
	
	//Moves one month forward from the current month
	public LocalDate nextMonth()
	{
		return currentDate = currentDate.plusMonths(1);
	}
	
	//Finds an event from the given name and date
	public Event findEvent(LocalDate d, String name)
	{
		Event event = null;
		if(!myCal.containsKey(d))
			return event;
		
		ArrayList<Event> events = myCal.get(d);
		for(Event ev : events)
		{
			if(name.equals(ev.getName()))
				event = ev;
		}
		return event;
	}
	
	//Gets the ArrayList of events, loops through, and checks if any overlap with the given event
	public boolean timeOverlapCheck(LocalDate d, Event e)
	{
		boolean overlap = false;
		if(myCal.containsKey(d))
		{
			ArrayList<Event> events = myCal.get(d);
			for(Event ev : events)
			{
				if(ev.getTimeInterval().overlap(e))
					overlap = true;
			}
		}
		return overlap;
	}
	
	//Returns a String of all the events in the given LocalDate in a formatted order
	public String getEvents(LocalDate c)
	{
		String eventList = "";
		
		if(!myCal.containsKey(c))
			eventList = "No events listed!";
		else
		{
			sortEventsInOrderByTime(myCal.get(c));
			ArrayList<Event> events = myCal.get(c);
			
			for(Event e : events)
			{
				eventList = eventList + "  " + e.getName() + " | " + 
							e.getTimeInterval().startPrint() + " - " +
							e.getTimeInterval().endPrint() + "\n";
			}
		}
		
		return eventList;
	}
	
	//Returns a String of all the regular events in the given LocalDate in a formatted and sorted order
	public String getRegEvents(LocalDate c)
	{
		String eventList = "";
		
		if(!myCal.containsKey(c))
			eventList = "No events listed!";
		else
		{
			sortEventsInOrderByTime(myCal.get(c));
			ArrayList<Event> events = myCal.get(c);
		
			for(Event e : events)
			{
				if(e.getIsRegular())
				{
					eventList = eventList + "  " + e.getName() + " | " + 
							e.getTimeInterval().startPrint() + " - " +
							e.getTimeInterval().endPrint() + "\n";
				}
			}
		}
		
		return eventList;
	}
	
	//Returns a String of all the events in the myCal HashMap in a formatted and sorted order
	public String getEventListAll()
	{
		String eventList = new String();
		TreeMap<LocalDate, ArrayList<Event>> sortedMyCal = new TreeMap<>();
		sortedMyCal.putAll(myCal);
		
		for(LocalDate day : sortedMyCal.keySet())
		{
			ArrayList<Event> events = myCal.get(day);
			
			if (!eventList.contains(Integer.toString(day.getYear())))
				eventList = eventList + day.getYear() + "\n";

			eventList = eventList + "  " + day.getMonth() + "\n";
			
			eventList = eventList + "    " + day.getDayOfMonth() + " " + day.getDayOfWeek() + "\n";
			
			sortEventsInOrderByTime(events);
			for(Event e : events)
			{
				eventList = eventList + "      " + e.getName() + " | " + 
							e.getTimeInterval().startPrint() + " - " +
							e.getTimeInterval().endPrint() + "\n";
			}
		}
		
		return eventList;
	}
	
	//Sorts the ArrayList of events in descending order by time using the Comparator interface, and comparing the start times
	public void sortEventsInOrderByTime(ArrayList<Event> events)
	{
		class sortEventsByTime implements Comparator<Event>
		{
			public int compare(Event e1, Event e2) 
			{
				int event1 = e1.getTimeInterval().getStart();
				int event2 = e2.getTimeInterval().getStart();
				return Integer.compare(event1, event2);
			}
		}
		
		Collections.sort(events, new sortEventsByTime());
	}
	
	//Prints the output in a formatted order to the PrintWriter object. User must quit the program to obtain output
	public void printOutput(PrintWriter out)
	{
		String eventList = new String();
		TreeMap<LocalDate, ArrayList<Event>> sortedMyCal = new TreeMap<>();
		sortedMyCal.putAll(myCal);
		
		for(LocalDate day : sortedMyCal.keySet())
		{
			ArrayList<Event> events = myCal.get(day);
			
			if (!eventList.contains(Integer.toString(day.getYear())))
			{
				eventList = eventList + day.getYear() + "\n";
				out.println(day.getYear());
			}
			
			eventList = eventList + "  " + day.getMonth() + "\n";
			out.println("  " + day.getMonth());
			
			eventList = eventList + "    " + day.getDayOfMonth() + " " + day.getDayOfWeek() + "\n";
			out.println("    " + day.getDayOfMonth() + " " + day.getDayOfWeek());
			
			sortEventsInOrderByTime(events);
			for(Event e : events)
			{
				eventList = eventList + "      " + e.getName() + " | " + 
							e.getTimeInterval().startPrint() + " - " +
							e.getTimeInterval().endPrint() + "\n";
				out.println("      " + e.getName() + " | " + 
						e.getTimeInterval().startPrint() + " - " +
						e.getTimeInterval().endPrint());
			}
		}
	}
	
	//Print method that prints a calendar in month view with the given LocalDate and highlights
	//Prints with brackets on all days that have an event as well as the current day
    public void printCalendar(LocalDate c)
    {  
    	LocalDate beginning = LocalDate.of(c.getYear(), c.getMonth(), 1);
    	String firstDayString = beginning.getDayOfWeek().toString();
    	
    	int firstDayInt = 0;
		switch(firstDayString){
		case "SUNDAY":
			firstDayInt = 1;
			break;
		case "MONDAY":
			firstDayInt = 2;
			break;
		case "TUESDAY":
			firstDayInt = 3;
			break;
		case "WEDNESDAY":
			firstDayInt = 4;
			break;
		case "THURSDAY":
			firstDayInt = 5;
			break;
		case "FRIDAY":
			firstDayInt = 6;
			break;
		case "SATURDAY":
			firstDayInt = 7;
			break;
		}
    	
    	System.out.println("             " + c.getMonth() + " " + c.getYear());
    	System.out.println(" Su    Mo    Tu    We    Th    Fr    Sa");
    	
    	int weekday = 0;
    	//Print proper spaces for first day
    	for (int firstDaySpaces=1; firstDaySpaces<firstDayInt; firstDaySpaces++) 
    	{
            System.out.print("      ");
            weekday++;
        }
    	
    	LocalDate today = LocalDate.now();
    	
    	for (int day=1; day<=c.lengthOfMonth(); day++)
    	{
    		if ((day == today.getDayOfMonth() && c.getMonthValue() == today.getMonthValue() && c.getYear() == today.getYear())
    				|| myCal.containsKey(beginning))
    		{
        		if (day<10)
        			System.out.print(" [" + day + "]");
        		else
        			System.out.print("[" + day + "]"); 
        		weekday++;
        		if(weekday%7 == 0)
        			System.out.println();
        		else
        			System.out.print("  ");
    		}
    		else
    		{
        		if (day<10)
        			System.out.print("  " + day);
        		else
        			System.out.print(" " + day); 
        		weekday++;
        		if(weekday%7 == 0)
        			System.out.println();
        		else
        			System.out.print("   ");
    		}
    		
    		beginning = beginning.plusDays(1);
    	}
    	
    	System.out.println("\n");
    }
}

//END