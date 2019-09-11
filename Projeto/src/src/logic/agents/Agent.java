package logic.agents;

import jason.asSyntax.Literal;
import utils.Pair;

/** Class responsible to store some data from the running agent */
public class Agent {

	/** Position of the agent in the environment */
	protected Pair<Integer, Integer> position;
	
	/** Name of the agent */
	protected String name;
	
	/** Literal position of the agent to be added to the precepts */
	protected Literal pos;
	
	
	/**	Constructor of Agent.
	 * 
	 * @param pos_x Position x of the object in environment.
	 * @param pos_y Position y of the object in environment.
	 * @param name Designation of the object in environment. 
	 */
	public Agent(int pos_x, int pos_y, String name) {
		this.position = new Pair<Integer, Integer>(pos_x, pos_y);
		this.name = name;
	}
	
	
	/**	Gets the position of the agent in the environment
	 * 
	 * @return A pair with the position (x,y) of the agent.
	 */
	public Pair<Integer, Integer> getPosition()
	{
		return this.position;
	}
	
	
	/**	Sets the new position of the agent in the environment
	 * 
	 * @param pos_x	The new 'x' position of the agent
	 * @param pos_y The new 'y' position of the agent
	 */
	public void setPosition(int pos_x, int pos_y)
	{
		this.position.setPair(pos_x, pos_y);
	}
	
	
	/**	Gets the name of the agent
	 * 
	 * @return A string with the name of the agent
	 */
	public String getName()
	{
		return this.name;
	}
	
	
	/**	Gets the literal of the position to be passed to the agent precepts
	 * 
	 * @return A literal with the position precept
	 */
	public Literal getPosLiteral()
	{
		pos = Literal.parseLiteral("my_pos(" + this.position.getX() + "," + this.position.getY() + ")");
		return pos;
	}
}
