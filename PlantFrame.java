import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;

public class PlantFrame extends JFrame
{
	private Plant[] plants;
	private PlantCard[] cards;
	private JPanel cardsPanel;
	private String fileName;
//parameterized constructor
	public PlantFrame(Plant[] plants, String fileName)
	{
		this.plants = plants;
		this.fileName = fileName;

		setTitle("Plant Watering Tracker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Save to the file whenever the window is closed.
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				Main.savePlants(PlantFrame.this.plants, PlantFrame.this.fileName);
			}
		});

		//button to add a new plant
		JButton addButton = new JButton("Add Plant");
		addButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				addPlant(); 
				//calls our method to add a plant
			}
		});

		// the panel for the add plant button at the top
		JPanel controlPanel = new JPanel();
		controlPanel.add(addButton);
		add(controlPanel, BorderLayout.NORTH);

		
		cardsPanel = new JPanel(new GridLayout(0, 2));
		//creates the container panel that will hold all our plant cards.
		//arranges elements into 2 columns side-by-side. 
		//Setting rows to 0 tells Java to dynamically create as many new rows as needed as plants get added.
		
	//instantiating an empty array to store references to all PlantCard UI objects.
		cards = new PlantCard[plants.length];

	// using the array to display all the plants as cards using our createCard method
		for(int i = 0; i < plants.length; i++)
		{
			cards[i] = createCard(plants[i]);
			cardsPanel.add(cards[i]);
		}

		//puts a scroll bar and allows us to scroll from anywhere in the window
		JScrollPane scrollPane = new JScrollPane(cardsPanel);
		add(scrollPane, BorderLayout.CENTER);
//setting the window dimensions and stuff
		setSize(700, 500);
		setLocationRelativeTo(null);
		setVisible(true);
		
//master timer trigger
		startWateringChecks();
	}
	
	
	
	
	// Builds a card for a plant, wiring up the save-on-change and delete actions.
		private PlantCard createCard(final Plant plant)
		{
			final PlantCard card = new PlantCard(plant, new Runnable()
			{
				@Override
				public void run()
				{
					Main.savePlants(PlantFrame.this.plants, PlantFrame.this.fileName);
				}
			});
//code to trigger card deletion
			card.setOnDelete(new Runnable()
			{
				@Override
				public void run()
				{
					removePlant(plant, card);
				}
			});

			return card;
		}

		
		
		
		
	// method to add a new plant, this opens a small form asking for a name and type, then adds the new plant.
	private void addPlant()
	{
		//creating a custom 2x2 container as a pop-up
		JTextField nameField = new JTextField();
		String[] types = { "Succulent", "Fern" };
		JComboBox<String> typeBox = new JComboBox<String>(types);

		JPanel inputPanel = new JPanel(new GridLayout(2, 2));
		inputPanel.add(new JLabel("Name:")); //Row 1, Column 1 (top-left).
		inputPanel.add(nameField); //Row 1, Column 2 (top-right).
		inputPanel.add(new JLabel("Type:")); //Row 2, Column 1 (bottom-left)
		inputPanel.add(typeBox); //Row 2, Column 2 (bottom-right).

		int result = JOptionPane.showConfirmDialog(
				this, //Centers the pop-up over the main window
				inputPanel, //Plugs our custom 2x2 grid panel
				"Add New Plant", //title bar  of pop-up
				JOptionPane.OK_CANCEL_OPTION); //attaches standard ok/cancel buttons

		//if we dont click OK, then it will just close it off and not save anything by returning nothing.
		if(result != JOptionPane.OK_OPTION)
		{
			return;
		}
		
//putting the name from input into a string
		String name = nameField.getText();
//this cancels operation if user leaves the name blank or puts blank spaces, and shows a message to the user
		if(name == null || name.trim().equals(""))
		{
			JOptionPane.showMessageDialog(this, "Please enter a name for the plant.");
			return;
		}
//putting the type name from input into a string
		String type = (String) typeBox.getSelectedItem();
	//putting the current time of data insertion into a string
		String now = LocalDateTime.now().toString();

		
//creating plant objects using the input, then passing into the constructors of succulent or fern
	// 'now' also serves as the last watered date, as we assume the plant is watered upon being added.
		Plant newPlant;

		if(type.equals("Succulent"))
		{
			newPlant = new Succulent(name, now);
		}
		else
		{
			newPlant = new Fern(name, now);
		}

		// Growing/updating the plants array.
		Plant[] newPlants = new Plant[plants.length + 1];

		for(int i = 0; i < plants.length; i++)
		{
			newPlants[i] = plants[i];
		}
//putting the new plant into the last slot of the array
		newPlants[plants.length] = newPlant;
		//updating reference
		plants = newPlants;

// Grow the cards array and add the new card to the screen.
		PlantCard[] newCards = new PlantCard[cards.length + 1];

		for(int i = 0; i < cards.length; i++)
		{
			newCards[i] = cards[i];
		}
//creating the new card
		PlantCard newCard = createCard(newPlant);
//putting the new card into the array
		newCards[cards.length] = newCard;
		//updating reference
		cards = newCards;
//adding the new card to the window
		cardsPanel.add(newCard);
		cardsPanel.revalidate(); //recalculates positioning
		cardsPanel.repaint(); //putting the pixels in place

		// Save right away so the new plant isn't lost if the app closes.
		Main.savePlants(plants, fileName);
	}

	

	// Removes a plant and its card, then saves the updated list to the file.
	private void removePlant(Plant plantToRemove, PlantCard cardToRemove)
	{
		int confirm = JOptionPane.showConfirmDialog(
				this,
				"Delete " + plantToRemove.getName() + "?",
				"Confirm Delete",
				JOptionPane.YES_NO_OPTION);

		if(confirm != JOptionPane.YES_OPTION)
		{
			return;
		}

		int removeIndex = -1;

		for(int i = 0; i < plants.length; i++)
		{
			if(plants[i] == plantToRemove)
			{
				removeIndex = i;
				break;
			}
		}

		if(removeIndex == -1)
		{
			return;
		}

		// Shrink the plants array.
		Plant[] newPlants = new Plant[plants.length - 1];
		int j = 0;

		for(int i = 0; i < plants.length; i++)
		{
			if(i != removeIndex)
			{
				newPlants[j] = plants[i];
				j++;
			}
		}

		plants = newPlants;

		// Shrink the cards array.
		PlantCard[] newCards = new PlantCard[cards.length - 1];
		j = 0;

		for(int i = 0; i < cards.length; i++)
		{
			if(cards[i] != cardToRemove)
			{
				newCards[j] = cards[i];
				j++;
			}
		}

		cards = newCards;

		cardsPanel.remove(cardToRemove);
		cardsPanel.revalidate();
		cardsPanel.repaint();

		// Save right away so the deleted plant doesn't come back next run.
		Main.savePlants(plants, fileName);
	}

	// Starts a timer that periodically checks every plant and pops up 
	// a warning for any plant that needs watering, as a safety net
	private void startWateringChecks()
	{
		Timer timer = new Timer(5000, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				checkAllPlants();
			}
		});

		timer.start();
	}

	// Refreshes every card so the color/status stays up to date.
	private void checkAllPlants()
	{
		for(int i = 0; i < plants.length; i++)
		{
			cards[i].refresh();
		}
	}
}