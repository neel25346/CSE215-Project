import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main
{

	public static void main(String[] args)
	{
		String fileName = "plants.txt";

		Plant[] plant = loadPlants(fileName);
		new PlantFrame(plant, fileName);
	}

	// Reads plants.txt and builds a Plant array
	public static Plant[] loadPlants(String fileName)
	{
		Plant[] plantList = new Plant[0];

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));

			String name = reader.readLine();

			while(name != null)
			{
				String type = reader.readLine();
				String date = reader.readLine();

				Plant newPlant;

				if(type.equals("Succulent"))
				{
					newPlant = new Succulent(name, date);
				}
				else
				{
					newPlant = new Fern(name, date);
				}

				Plant[] newList = new Plant[plantList.length + 1];

				for(int i = 0; i < plantList.length; i++)
				{
					newList[i] = plantList[i];
				}

				newList[plantList.length] = newPlant;
				plantList = newList;

				// Skip blank line (if there is one)
				reader.readLine();

				name = reader.readLine();
			}

			reader.close();
		}
		catch(IOException e)
		{
			System.out.println("Could not read plants.txt, starting with no plants.");
		}

		return plantList;
	}

	// Writes the plant array back out to a text file, in the same
	// name / type / lastWateredDate / blank-line format loadPlants reads.
	public static void savePlants(Plant[] plants, String fileName)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

			for(int i = 0; i < plants.length; i++)
			{
				writer.write(plants[i].getName());
				writer.newLine();

				writer.write(plants[i].getType());
				writer.newLine();

				writer.write(plants[i].getLastWateredDate());
				writer.newLine();

				writer.newLine();
			}

			writer.close();
		}
		catch(IOException e)
		{
			System.out.println("Could not save plants.txt.");
		}
	}
}