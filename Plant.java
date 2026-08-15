public abstract class Plant implements Alertable
{
	private String name;
	private String type;

	// Stores the exact date and time the plant was last watered.
	// Format: 2026-08-07T11:30:15
	
	// the variable that we are specifically supposed to hide 
	private String lastWateredDate;
//default constructor
	public Plant()
	{

	}
//parameterized constructor 
	public Plant(String name, String type, String lastWateredDate)
	{
		this.name = name;
		this.type = type;
		this.lastWateredDate = lastWateredDate;
	}
	
//getter and setter methods

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getLastWateredDate()
	{
		return lastWateredDate;
	}

	public void setLastWateredDate(String lastWateredDate)
	{
		this.lastWateredDate = lastWateredDate;
	}

	// to return 'true' if this plant needs watering.
	public abstract boolean checkNeedsWatering();

	// to return the watering interval in seconds.
	public abstract long getWaterIntervalSeconds();
}