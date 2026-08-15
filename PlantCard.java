import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;




public class PlantCard extends JPanel
{
	//using the awt color class we imported to set some colors in some variables.
	private static final Color HEALTHY_COLOR = new Color(150, 230, 150);
	private static final Color THIRSTY_COLOR = new Color(255, 150, 150);

	//more variables
	private boolean hasAlerted = false;
	
	private JLabel nameLabel;
	private JLabel typeLabel;
	private JLabel statusLabel;
	private JLabel countdownLabel;
	private JButton waterButton;
	private JButton deleteButton;
	
	private Plant plant;
	// onChange is to be called whenever this card's plant data changes
	// (e.g. so the frame can save it to a file). Can be null.
	private Runnable onChange;
	private Runnable onDelete;

	
	

	
// this method marks the plant as watered right now and updates the display.
		public void waterPlant()
		{
			//this line sets the last watered date to the time it was triggered.
			plant.setLastWateredDate(LocalDateTime.now().toString());
			
			
			hasAlerted = false;
			
			refresh();

			if(onChange != null)
			{
				onChange.run();
			}
		}
		

// this method updates the labels and background color to reflect the plant's current state.
		public void refresh()
		{
			nameLabel.setText("Name: " + plant.getName());
			typeLabel.setText("Type: " + plant.getType());

			 if(plant.checkNeedsWatering())
		        {
		            setBackground(THIRSTY_COLOR);
		            statusLabel.setText("Status: Thirsty!");
		            countdownLabel.setText("Needs water now");
		            // Triggers the warning once per thirsty cycle, so calls trigger only if hasAlerted is false, 
		            //and resets hasAlerted = false when the plant is healthy
		            if (hasAlerted == false)
		            {
		hasAlerted = true; //basically if we haven't watered yet then it will not push multiple pop-ups as this variable will change upon watering.
		                plant.triggerWarning();
		            }
		        }
		        else
		        {
		            setBackground(HEALTHY_COLOR);
		            statusLabel.setText("Status: Healthy");
		            countdownLabel.setText("Thirsty in: " + formatDuration(getSecondsRemaining()));

		            //( this  line Resets card when healthy)
		            hasAlerted = false; 
		        }
		    }
	
	
//creating a parameterized constructor for the plant card	
	public PlantCard(Plant plant, Runnable onChange)
	{
		this.plant = plant;
		this.onChange = onChange;

//organize everything added to this card into a grid with 6 rows and 1 column 
		setLayout(new GridLayout(6, 1));
	//Drawing a simple gray line around the edges of the card so it doesn't blend into the background.
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		//making the background solid rather than transparent
		setOpaque(true);

	//creating some labels
		nameLabel = new JLabel();
		typeLabel = new JLabel();
		statusLabel = new JLabel();
		countdownLabel = new JLabel();

	//creating the 'water now' button
		waterButton = new JButton("Water Now");
//using the actionListener class to make the button functional
		waterButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				waterPlant();
			}
		});

		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(onDelete != null)
				{
					onDelete.run();
				}
			}
		});

		add(nameLabel);
		add(typeLabel);
		add(statusLabel);
		add(countdownLabel);
		add(waterButton);
		add(deleteButton);

		refresh();

		// Ticks every second so the countdown and color stay live.
		//works by listening to the ticks every second and executing the refresh() method every second.
		Timer countdownTimer = new Timer(1000, new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				refresh(); 
			}
		});

		countdownTimer.start();
	}
	
	
	


	// Calculates how many seconds until this plant needs watering.
	private long getSecondsRemaining()
	{
		try
		{
//gets the last watered date and time (if any) 
			LocalDateTime lastDateTime = LocalDateTime.parse(plant.getLastWateredDate());
			//gets the current date and time
			LocalDateTime now = LocalDateTime.now();

//This Calculates seconds passed between last watered time and RIGHT NOW
			long secondsPassed = ChronoUnit.SECONDS.between(lastDateTime, now);

			return plant.getWaterIntervalSeconds() - secondsPassed;
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	
	
	
	

	// Turns a number of seconds into a readable "Xd Xh Xm Xs" style string.
	private String formatDuration(long totalSeconds)
	{
		if(totalSeconds < 0)
		{
			totalSeconds = 0;
		}

		long days = totalSeconds / 86400;
		long hours = (totalSeconds % 86400) / 3600;
		long minutes = (totalSeconds % 3600) / 60;
		long seconds = totalSeconds % 60;

		if(days > 0)
		{
			return days + "d " + hours + "h " + minutes + "m";
		}
		else if(hours > 0)
		{
			return hours + "h " + minutes + "m " + seconds + "s";
		}
		else
		{
			return minutes + "m " + seconds + "s";
		}
	}

	// Lets the frame supply the delete action after the card is created,
	// since the frame needs a reference to this card itself to remove it.
	//these getter and setter methods are to be used in other classes.
	public void setOnDelete(Runnable onDelete)
	{
		this.onDelete = onDelete;
	}

	public Plant getPlant()
	{
		return plant;
	}
}