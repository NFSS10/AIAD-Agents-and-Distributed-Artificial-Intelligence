package logic.agents;

import java.util.ArrayList;

import utils.Constants;

/**  Class Tower responsible to send the firefighters and to receive the individuals
 * 
 * @see Agent
 *
 */
public class Tower extends Agent {

	/** List of all lost men who want to be saved */
	private ArrayList<String> menLostInField;

	
	/**	Constructor of Tower
	 *	The constructor of tower don't have parameters and the position used is on the constants class
	 *
	 * @see Constants
	 */
	public Tower()
	{
		super(Constants.TOWER_POS.getX(), Constants.TOWER_POS.getY(), "tower");

		this.menLostInField = new ArrayList<String>();
	}
	
	
	/**	Gets the position of the agent in the environment
	 * 
	 * @return True if the men was added successfully, false if the men was already on the list.
	 */
	public boolean addToMenLost(String name)
	{
		if(!this.menLostInField.contains(name))
		{
			this.menLostInField.add(name);
			return true;
		}
		return false;
	}

}
