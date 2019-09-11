package logic.agents;

import java.util.ArrayList;

import jason.asSyntax.Literal;
import logic.env.RegionCell;
import utils.Constants;
import utils.Pair;


/**  Class Individual responsible to store information about the lost mens in the field.
 * 
 * @see Agent
 *
 */
public class Individual extends Agent{

	/** Indicates if the help message was already sent */
	private boolean help_message_sent;
	
	/** Adjacent forest cells that are reachable from the individual position */
	private ArrayList<Pair<Integer, Integer>> reachable;

	/** Indicates if the individual is already being carried */
	private boolean being_carried;
	
	/** Indicates if the individual has already been rescued */
	private boolean rescued;
	
	
	/**	Constructor of Individual.
	 * 
	 * @param pos_x Position x of the individual in environment.
	 * @param pos_y Position y of the individual in environment.
	 * @param name Designation of the individual in environment. 
	 * @param region Environment of the world in witch the individual is situated.
	 */
	public Individual(int pos_x, int pos_y, String name, RegionCell[][] region) {
		super(pos_x, pos_y, name);
		this.help_message_sent = false;
		reachable = new ArrayList<Pair<Integer, Integer>>();
		being_carried = false;
		rescued = false;
		
		constructReachable(region, reachable);
	}
	
	/**	Function that returns if the fire fighter was rescued or not
	 * 
	 * @return True if it was rescued, false otherwise
	 */
	public boolean getRescued()
	{
		return rescued;
	}
	
	/**	Change the Rescued state of the fire fighter
	 * 
	 */
	public void changeRescuedState(boolean rescued)
	{
		this.rescued = rescued;
	}
	
	
	/**	Show's if the individual is being carried.
	 * 
	 * @return True if the individual is already being carried, false otherwise
	 */
	public boolean beingHelpedOut()
	{
		return this.being_carried;
	}
	
	
	/**	
	 * Changes the help state of the individual
	 * If he is being carried, he stops of being carried
	 * If he is not being carried, he starts being carried
	 * */
	public void changeHelpState()
	{
		this.being_carried = !this.being_carried;
	}
	
	
	/**	Gets the literal of the help message to be send to the agent precepts
	 * 
	 * @return A literal with the fireNear help message
	 */
	public Literal getFireNearLiteral() {
		return Literal.parseLiteral("fireNear");
	}

	
	/**	Checks if the individual already asked for help.
	 * 
	 * @return True if he already asked for help, false otherwise.
	 */
	public boolean alreadyAskedForHelp()
	{
		return this.help_message_sent;
	}

	
	/**	
	 * Sets to true the help message variable.
	 */
	public void helpMessageSent()
	{
		this.help_message_sent = true;
	}

	
	/**	Constructs the possible reachable cells from his position.
	 * 
	 * @param region Environment in the world
	 * @param reachable Data structure with the cells that are reachable
	 */
	private void constructReachable(RegionCell[][] region, ArrayList<Pair<Integer, Integer>> reachable)
	{
		if(region[this.position.getX()][this.position.getY()].getType().equals("Vegetation"))
			checkAdjCells(region, this.position.getX(), this.position.getY(), 0, Constants.INDIVIDUAL_PERCEPTION_RADIUS, reachable, "Vegetation");
	}
	
	/** Recursive function to check adjacent cells with a limit radius.	
	 * 
	 * @param region Environment in the world
	 * @param Position in x where it starts checking 
	 * @param y Position in y where it starts checking 
	 * @param counter Actual counter of radius
	 * @param max_counter Limit radius of perception
	 * @param positions Array with the positions
	 * @param type Type of cell e.g Vegetation
	 */
	private void checkAdjCells(RegionCell[][] region, int x, int y, int counter, int max_counter, ArrayList<Pair<Integer, Integer>> positions, String type)
	{
		if(counter > max_counter || x < 0 || y < 0 || x >= region.length || y >= region[x].length)
			return;

		if(!(x == this.position.getX() && y == this.position.getY()) && region[x][y].getType().equals(type))
		{
			Pair<Integer, Integer> location = new Pair<Integer, Integer>(x, y);
			if(!positions.contains(location))
				positions.add(location);
		}

		
		counter++;

		checkAdjCells(region, x + 1, y, counter, max_counter, positions, type);
		checkAdjCells(region, x - 1, y, counter, max_counter, positions, type);
		checkAdjCells(region, x, y + 1, counter, max_counter, positions, type);
		checkAdjCells(region, x, y - 1, counter, max_counter, positions, type);

		checkAdjCells(region, x + 1, y + 1, counter, max_counter, positions, type);
		checkAdjCells(region, x - 1, y - 1, counter, max_counter, positions, type);
		checkAdjCells(region, x + 1, y - 1, counter, max_counter, positions, type);
		checkAdjCells(region, x - 1, y + 1, counter, max_counter, positions, type);
	}
	
	/** Verifies if there is fire near and reachable from his position, in other words, it means he is in danger.	
	 * 
	 * 
	 * @param region Environment in the world
	 * 
	 * @return True in a danger situation , false otherwise
	 */
	public synchronized boolean isFireNear(RegionCell[][] region) {

		if(region[this.position.getX()][this.position.getY()].getType().equals("Vegetation"))
		{
			ArrayList<Pair<Integer, Integer>> near_fire_locations = new ArrayList<Pair<Integer, Integer>>();
			checkAdjCells(region, this.position.getX(), this.position.getY(), 0, Constants.INDIVIDUAL_PERCEPTION_RADIUS, near_fire_locations, "Fire");

			for(int i = 0; i < reachable.size(); i++)
			{
				if(near_fire_locations.size() > 0)
				{
					if(near_fire_locations.contains(reachable.get(i)))
						return true;
				}	
			}
		}
		
		return false;
	}

}
