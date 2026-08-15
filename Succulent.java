import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.swing.JOptionPane;

public class Succulent extends Plant{

	//default constructor
	public Succulent(){}
//parameterized constructor 
	public Succulent(String name, String lastWateredDate)
	{
		super(name, "Succulent", lastWateredDate);
	}

	
	@Override
	public boolean checkNeedsWatering()
	{
		try
		{
//gets the last watered date and time (if any) and stores it as an object of the LocalDateTime class
			LocalDateTime lastDateTime = LocalDateTime.parse(getLastWateredDate());
//gets the current date and time
			LocalDateTime now = LocalDateTime.now();

//This Calculates seconds passed between last watered time and RIGHT NOW
			long secondsPassed = ChronoUnit.SECONDS.between(lastDateTime, now);
			
//by using a comparison operator, this expression automatically returns boolean value 
//'true' if passed time exceeds or equals 7 days in seconds, else false 
			return secondsPassed >= getWaterIntervalSeconds();
		}
		catch(Exception e)
		{
			// If date string parsing fails for any reason, default to needing water
			return true;
		}
	}

	
// calculating 7 days in seconds here, and applying the abstract method from the plant class.
	@Override
	public long getWaterIntervalSeconds()
	{
// 7 days = 7 days * 24 hrs * 60 mins * 60 secs
		return 7 * 24 * 60 * 60;
	}
	
	
	
	
	// Applying the method from the interface Alertable to show a warning message as a small pop-up window
	@Override
	public void triggerWarning()
	{ 
		JOptionPane.showMessageDialog(
//parent component decides where to show the pop-up, here 'null' centers it on the entire monitor screen
				null,
// The body text or display component
				getName() + " needs watering!",
// The text displayed on the title bar
				"Watering Reminder",
//The icon style
				JOptionPane.WARNING_MESSAGE);
	}
	
}