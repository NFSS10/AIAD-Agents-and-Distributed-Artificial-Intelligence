package logic.agents;

import java.util.ArrayList;

import jason.asSyntax.Literal;
import logic.env.RegionCell;
import logic.env.Vegetation;
import logic.env.VegetationOnFire;
import utils.Constants;
import utils.Pair;

/**  Class FireFighter responsible to store all the information about a fire fighter such as environment perceptions, partners,etc.
 * 
 * @see Agent
 *
 */
public class FireFighter extends Agent {

	/** The fire perceptions that he and his team have in each step */
	private ArrayList<Pair<Integer, Integer>> firePerceptions;
	/** The burned area perceptions that he and his team have in each step */
	private ArrayList<Pair<Integer, Integer>> burnedPerceptions;

	/** The new fire perceptions comparing to the last step */
	private ArrayList<Pair<Integer, Integer>> newFirePerceptions;
	/** The new burned area perceptions comparing to the last step */
	private ArrayList<Pair<Integer, Integer>> newBurnedPerceptions;
	
	/** Object used for synchronizing attempt of fire perceptions */
	private final Object lock_fire_perceps = new Object();
	/** Object used for synchronizing attempt of burned area perceptions */
	private final Object lock_burned_perceps = new Object();
	
	/** The list of his partners */
	private ArrayList<String> partners;

	/** Health of the fire fighter */
	private Integer hp;

	/** Indicates if the individual is dead or not */
	private boolean isDead;

	/**	Constructor of FireFighter.
	 * 
	 * @param pos_x Position x of the fire fighter in the environment.
	 * @param pos_y Position y of the fire fighter in the environment.
	 * @param name Designation of the fire fighter in the environment. 
	 */
	public FireFighter(int pos_x, int pos_y, String name)
	{
		super(pos_x, pos_y, name);
		this.firePerceptions = new ArrayList<Pair<Integer, Integer>>();
		this.burnedPerceptions = new ArrayList<Pair<Integer, Integer>>();

		this.newFirePerceptions = new ArrayList<Pair<Integer, Integer>>();
		this.newBurnedPerceptions = new ArrayList<Pair<Integer, Integer>>();

		this.partners = new ArrayList<String>();
		initializePartners();

		this.isDead = false;

		this.hp = Constants.HP;
	}

	/** Function used for creating the initial team.
	 * 
	 */
	private void initializePartners()
	{
		for(int i = 0; i < Constants.FIREFIGHTERS_NUMBER; i++)
		{
			String ff_name = "firefighter" + (i + 1);
			if(!this.name.equals(ff_name))
				this.partners.add("firefighter" + (i + 1));
		}
	}

	/**	Get the team of a fire fighter.
	 * 
	 * @return A data structure with all of his partners.
	 */
	public ArrayList<String> getPartners()
	{
		return this.partners;
	}
	
	/**	Add the perception of a dead partner
	 * 
	 */
	public void partnerDied(String name)
	{
		for(int i = 0; i < this.partners.size(); i++)
		{
			if(this.partners.get(i).equals(name))
			{
				this.partners.remove(i);
				break;
			}
		}
	}
	
	/**	Checks if the fire fighter is dead.
	 * 
	 * @return True if he is, indeed, dead and false otherwise.
	 */
	public boolean isDead()
	{
		return this.isDead;
	}

	/**	Get the actual hp.
	 * 
	 * @return A integer corresponding to his hp.
	 */
	public Integer getHp() {
		return this.hp;
	}

	/**	Function used for updating his health.
	 * @param value The amount to be subtracted
	 */
	public void updateHp(int value)
	{
		if(!isDead)
		{
			if((this.hp - value) < 0)
			{
				isDead = true;
				this.hp = 0;
			}
			else
				this.hp -= value;
		}
	}

	/**	Moves towards a position.
	 * @param x Position in x
	 * @param y Position in y
	 * @return True if move was successful, false otherwise
	 */
	public boolean moveTowardsPosition(int x, int y)
	{
		int agent_px = position.getX();
		int agent_py = position.getY();

		int dx = Math.abs(x - agent_px);
		int dy = Math.abs(y - agent_py);

		int sx = (agent_px < x) ? 1 : -1;
		int sy = (agent_py < y) ? 1 : -1;

		int err = dx - dy;

		if (agent_px == x && agent_py == y) {
			return false;
		}

		int e2 = 2 * err;

		if (e2 > -dy) {
			err = err - dy;
			agent_px = agent_px + sx;
		}

		if (e2 < dx) {
			err = err + dx;
			agent_py = agent_py + sy;
		}

		
		if(!this.firePerceptions.contains(new Pair<Integer, Integer>(agent_px, agent_py)))
		{
			this.position.setX(agent_px);
			this.position.setY(agent_py);
			return true;
		}
		
		return false;
	}
	
	/**	Gets the literal of the fire perception. 
	 * @param pos Position of the fire
	 * 
	 * @return A literal with the fire perception
	 */
	public Literal getFirePercepPosLiteral(Pair<Integer, Integer> pos) {
		return Literal.parseLiteral("fire(" + pos.getX()+", " + pos.getY() + ")");
	}

	/**	Gets the literal of the burned perception.
	 * @param pos Position of the burned area
	 * 
	 * @return A literal with the burned area perception
	 */
	public Literal getBurnedPercepPosLiteral(Pair<Integer, Integer> pos)
	{
		return Literal.parseLiteral("burned(" + pos.getX()+", " + pos.getY() + ")");
	}

	/**	Gets the literal of state-dead.
	 * 
	 * @return The literal corresponding to the dead state.
	 */
	public Literal deadStateLiteral()
	{
		return Literal.parseLiteral("dead");
	}

	/**	Gets all the new burning area perceptions as a literal.
	 * 
	 * @return A literal with his new burning area perceptions.
	 */
	public Literal getNewBurnedPercepPosLiteral()
	{
		String newburned = "new_burned([";
		
		synchronized (this.lock_burned_perceps) {
			for(int i = 0; i < this.newBurnedPerceptions.size(); i++)
			{	
				Pair<Integer, Integer> pos = this.newBurnedPerceptions.get(i);
				newburned += "[" + pos.getX() + ", " + pos.getY();
				
				if(i == (this.newBurnedPerceptions.size() - 1))
					newburned += "]";
				else
					newburned += "],";
			}
		}
		
		newburned += "])";

		return Literal.parseLiteral(newburned);
	}

	/**	Gets all the new fire perceptions as a literal.
	 * 
	 * @return A literal with his new fire perceptions.
	 */
	public Literal getNewFiresPercepPosLiteral()
	{
		String newfires = "new_fires([";
		
		synchronized (this.lock_fire_perceps)
		{
			for(int i = 0; i < this.newFirePerceptions.size(); i++)
			{	
				Pair<Integer, Integer> pos = this.newFirePerceptions.get(i);
				newfires += "[" + pos.getX() + ", " + pos.getY();
				if(i == (this.newFirePerceptions.size() - 1))
					newfires += "]";
				else
					newfires += "],";
			}	
		}

		newfires += "])";

		return Literal.parseLiteral(newfires);
	}

	/**	Resets the newest perceptions to start another step.
	 * 
	 */
	public void resetNewestPerceptions()
	{
		synchronized (this.lock_fire_perceps) {
			this.newFirePerceptions = new ArrayList<Pair<Integer, Integer>>();
		}
		
		synchronized (this.lock_burned_perceps) {
			this.newBurnedPerceptions = new ArrayList<Pair<Integer, Integer>>();
		}

	}

	/**	Gets all the fire perceptions of the fire fighter.
	 * 
	 * @return A array with all the fire perceptions
	 */
	public ArrayList<Pair<Integer, Integer>> getFirePerceptions() {
		return firePerceptions;
	}

	/**	Gets all the burned area perceptions of the fire fighter.
	 * 
	 * @return A array with all the burned area perceptions
	 */
	public ArrayList<Pair<Integer, Integer>> getBurnedPerceptions(){
		return burnedPerceptions;
	}

	/**	Gets all the new fire perceptions of the fire fighter.
	 * 
	 * @return A array with all the new fire perceptions
	 */
	public ArrayList<Pair<Integer, Integer>> getNewFirePerceptions() {
		return newFirePerceptions;
	}

	/**	Gets all the new burned area perceptions of the fire fighter.
	 * 
	 * @return A array with all the new burned area perceptions
	 */
	public ArrayList<Pair<Integer, Integer>> getNewBurnedPerceptions(){
		return newBurnedPerceptions;
	}

	/**	Extinguish fire at location designed.
	 * 
	 * @param region The environment of the simulation
	 * @param fireposx The position of fire in x
	 * @param fireposy The position of fire in y
	 * 
	 * @return True if fire is extinguished, false otherwise
	 */
	public boolean extinguishFire(RegionCell[][] region, int fireposx, int fireposy)
	{
		if(region[fireposx][fireposy].getType().equals("Fire"))
		{
			VegetationOnFire fire = (VegetationOnFire) region[fireposx][fireposy];
			fire.extinguish(Constants.WATER_FLUX);

			if(fire.getNecessaryWater() == 0)
			{
				region[fireposx][fireposy] = new Vegetation(fire.getDensity(), fire.getWindDirection(), fire.getWindSpeed(), fire.getComburentLeft());
				removeFirePerception(fireposx, fireposy);
				return true;
			}
		}
		else
            removeFirePerception(fireposx, fireposy);

		return false;
	}

	/**	Forgets about a fire perception.
	 * 
	 * @param fireposx The position of fire in x
	 * @param fireposy The position of fire in y
	 * 
	 */
	public void removeFirePerception(int fireposx, int fireposy)
	{
		Pair<Integer, Integer> location = new Pair<Integer, Integer>(fireposx, fireposy);
		if(firePerceptions.contains(location)) {
			firePerceptions.remove(location);
		}
	}

	/**	Add a fire perception.
	 * 
	 * @param x The position of fire in x
	 * @param y The position of fire in y
	 * 
	 */
	public void addFirePerception(Integer x, Integer y)
	{
		Pair<Integer, Integer> location = new Pair<Integer, Integer>(x,y);

		if(!firePerceptions.contains(location)) {
			firePerceptions.add(location);
		}
	}

	/**	Add a burned area perception.
	 * 
	 * @param x The position of fire in x
	 * @param y The position of fire in y
	 * 
	 */
	public void addBurnedPerception(Integer x, Integer y)
	{
		Pair<Integer, Integer> location = new Pair<Integer, Integer>(x,y);
		if(!burnedPerceptions.contains(location))
		{
			burnedPerceptions.add(location);
			removeFirePerception(x, y);
		}
	}


	/**	Update his fire perceptions according to his location.
	 * 
	 * @param region The environment of the simulation.
	 * 
	 */
	public synchronized void updateFirePerceptions(RegionCell[][] region)
	{
		int x = position.getX();
		int y = position.getY();

		resetNewestPerceptions();

		updateFirePerceptionsRadius(region, x, y, 0, Constants.INDIVIDUAL_PERCEPTION_RADIUS);
	}

	/**	Update his fire perceptions according to a determined location with a radius of one cell.
	 * 
	 * @param region The environment of the simulation.
	 * @param x Position in x
	 * @param y Position in y
	 * @param counter Actual counter of radius
	 * @param max_counter Limit radius of perception
	 * 
	 */
	public void updateFirePerceptionsRadius(RegionCell[][] region, int x, int y, int counter, int max_counter) {

		if(counter > max_counter || x < 0 || y < 0 || x >= region.length || y >= region[x].length)
			return;

		Pair<Integer, Integer> location = new Pair<Integer, Integer>(x, y);

		if(region[x][y].getType().equals("Fire"))
		{
			if(!this.firePerceptions.contains(location))
			{
				this.firePerceptions.add(location);
				this.newFirePerceptions.add(new Pair<Integer, Integer>(x, y));
			}
		}
		else 
		{
			removeFirePerception(x, y);
			if(region[x][y].getType().equals("BurnedArea"))
			{
				if(!this.burnedPerceptions.contains(location))
				{
					this.burnedPerceptions.add(location);
					this.newBurnedPerceptions.add(new Pair<Integer, Integer>(x, y));
				}
			}
		}
		
		counter++;

		updateFirePerceptionsRadius(region, x + 1, y, counter, max_counter);
		updateFirePerceptionsRadius(region, x - 1, y, counter, max_counter);
		updateFirePerceptionsRadius(region, x, y + 1, counter, max_counter);
		updateFirePerceptionsRadius(region, x, y - 1, counter, max_counter);

		updateFirePerceptionsRadius(region, x + 1, y + 1, counter, max_counter);
		updateFirePerceptionsRadius(region, x - 1, y - 1, counter, max_counter);
		updateFirePerceptionsRadius(region, x + 1, y - 1, counter, max_counter);
		updateFirePerceptionsRadius(region, x - 1, y + 1, counter, max_counter);
	}
}
