package logic.env;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import logic.agents.FireFighter;
import logic.agents.Individual;
import logic.agents.Tower;
import utils.Constants;
import utils.Pair;
import gui.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**  Class RegionEnv
 * 
 * It is an extended instance of Environment class of Jason
 * And it is responsible for everything that happens in the environment
 *
 */
public class RegionEnv extends Environment {

	/** Wind of the environment */
	public Wind wind;

	/** List of lists representing the environment of the world */
	public RegionCell[][] region;

	/** Random object to generate positions of random environment situations */
	public Random random = new Random(System.currentTimeMillis());

	/** Gui responsible to simulate and show to the user all the world environment */
	public GUI gui;

	/** Input Gui responsible to receive inputs from the user to the environment constants */
	public InputGui input;

	/** Object responsible to spread the fire in the environment */
	public SpreadFire spread_fire;

	/** List of all the firefighters in the simulation */
	public ArrayList<FireFighter> firefighters;

	/** List of all the individuals in the simulation */
	public ArrayList<Individual> people;


	/** List of all the fire cells in the simulation being extinguished */
	public ArrayList<Pair<Integer, Integer>> fire_being_extinguished;

	/** Tower object in the environment */
	public Tower tower;

	/** Boolean that stores if the simulation already started. */
	public boolean started = false;

	/** Position of the start of the fire in the environment. */
	private Pair<Integer, Integer> fire_starter_pos;


	/**	
	 * Constructor of RegionEnv
	 * Responsible for the start of the input gui.
	 */
	public RegionEnv() {
		input = new InputGui(this);
	}


	/**	
	 * Responsible for the start of the environment, including the start of the fire, wind, firefighters, people and the forest.
	 */
	public void startEnv() {
		this.wind = new Wind();
		this.firefighters = new ArrayList<>(Constants.FIREFIGHTERS_NUMBER);
		this.people = new ArrayList<>(Constants.PEOPLE_NUMBER);
		this.fire_being_extinguished = new ArrayList<Pair<Integer, Integer>>();

		region = new RegionCell[Constants.GRID_SIZE][Constants.GRID_SIZE];

		int[][] forest = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
		forest = ForestMaker.make(forest, 90, 20);
		int x = 0, y = 0;

		for(int i = 0; i < forest.length; i++) {

			for(int j = 0; j < forest[i].length; j++)
			{
				int range = Constants.MAX_DENSITY - Constants.MIN_DENSITY + 1;
				int density = random.nextInt(range) + Constants.MIN_DENSITY;

				if(forest[i][j] == 1)
					region[i][j] = new Vegetation(density, wind.getWindDirection(), wind.getWindSpeed());
				else
					region[i][j] = new Terrain(wind.getWindDirection(), wind.getWindSpeed());
			}
		}

		boolean fire_started = false;
		while(!fire_started){
			x = random.nextInt(Constants.GRID_SIZE);
			y = random.nextInt(Constants.GRID_SIZE);

			if(region[x][y].getType().equals("Vegetation"))
			{
				Vegetation v = (Vegetation) region[x][y];
				region[x][y] = new VegetationOnFire(v.getDensity(), wind.getWindDirection(), wind.getWindSpeed());
				spread_fire = new SpreadFire(region);
				fire_starter_pos = new Pair<Integer, Integer>(x, y);
				fire_started = true;
				break;
			}
		}

		for(int i = 0; i < Constants.FIREFIGHTERS_NUMBER; i++)
		{
			FireFighter a = new FireFighter(0, 0, "firefighter" + (i + 1));
			this.firefighters.add(a);
		}

		this.tower = new Tower();

		int px = 0;
		int py = 0;
		for(int j = 0; j < Constants.PEOPLE_NUMBER; j++)
		{
			
			do {
				px = random.nextInt(Constants.GRID_SIZE);
				py = random.nextInt(Constants.GRID_SIZE);
			}
			while(px == x && py == y);
		
			
			Individual i = new Individual(px, py, "individual" + (j + 1), region);
			this.people.add(i);
		}

		gui = new GUI(this);

		updateFireSimulation();

		for(int i = 0; i < this.firefighters.size(); i++)
		{
			updatePercepts(this.firefighters.get(i).getName(), false, false);
		}
		updatePercepts("tower", false, false);
	}


	/**	
	 * Responsible for the fire simulation at each instant.
	 * It spreads the fire at each tick.
	 */
	private void updateFireSimulation() {
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				try {
					wind.updateWind();
					region = spread_fire.step(region, wind.getWindDirection(), wind.getWindSpeed());

					for(int i = 0; i < region.length; i++) {

						for(int j = 0; j < region[i].length; j++) {

							if(region[i][j].getType().equals("Fire"))
							{
								VegetationOnFire fire = ((VegetationOnFire) region[i][j]);
								fire.burn();
								fire.updateNecessaryWater();

								if(((VegetationOnFire) region[i][j]).getComburentLeft() == 0)
									region[i][j] = new BurnedArea(wind.getWindDirection(), wind.getWindSpeed());
							}
						}
					}


					fire_being_extinguished.clear();
					updatePercepts("individuals", false, false);

					gui.update();
				} catch (Exception e) {
					System.out.println("Exception caught : " + e);
				}
			}
		}, 0, Constants.UPDATE_TIME_ENV);
	}


	/**	
	 * Responsible for executing an action for a given agent.
	 * 
	 * @param agent Agent to execute the action.
	 * @param action Action to be executed by the agent.
	 * 
	 * @return True
	 */
	public boolean executeAction(String agent, Structure action) {

		FireFighter a = this.getFirefighterByName(agent);

		System.out.println(agent + "  " + action);

		boolean tick_action = false;

		boolean update_newest = true;

		switch (action.getFunctor())
		{
		case "move_towards_fire": 	
			int x = (new Integer(action.getTerm(0).toString())).intValue();
			int y = (new Integer(action.getTerm(1).toString())).intValue();

			if(a.moveTowardsPosition(x, y))
			{
				threadSleep();
				a.updateFirePerceptions(region);
				tick_action = true;
				updatePercepts("tower", tick_action, false);
			}
			
			break;
		case "put_water":
			threadSleep();
			int fireposx = (new Integer(action.getTerm(0).toString())).intValue();
			int fireposy = (new Integer(action.getTerm(1).toString())).intValue();

			if(region[fireposx][fireposy].getType().equals("Fire"))
			{
				if(a.extinguishFire(region, fireposx, fireposy))
					gui.out("Extinguished fires at (" + fireposx + ", " + fireposy + ")");
				else
					gui.out("Extinguish fires at (" + fireposx + ", " + fireposy + ")");
				fire_being_extinguished.add(new Pair(fireposx,fireposy));
			}
			
			a.updateFirePerceptions(region);
			tick_action = true;
			break;
		case "move_towards":
			int movex = (new Integer(action.getTerm(0).toString())).intValue();
			int movey = (new Integer(action.getTerm(1).toString())).intValue();

			
			if(a.moveTowardsPosition(movex, movey))
			{
				threadSleep();
				a.updateFirePerceptions(region);
				tick_action = true;
				updatePercepts("tower", tick_action, false);
			}
			
			break;
		case "move_away":

			Pair<Integer, Integer> safest_place = safestPlaceToGo(a.getPosition());
			
			if(safest_place != null)
			{
				threadSleep();
				a.setPosition(safest_place.getX(), safest_place.getY());
				a.updateFirePerceptions(region);
				tick_action = true;
			}
			else
				update_newest = false;

			
			break;
		case "carry":
			String individual_name = action.getTerm(0).toString();

			for(int i = 0; i < this.people.size(); i++)
			{
				Individual ind = this.people.get(i);
				if(ind.getName().equals(individual_name))
				{
					if(!ind.beingHelpedOut())
						ind.changeHelpState();

					break;
				}
			}
			update_newest = false;
			break;
		case "drop_individual":
			String individual_name_2 = action.getTerm(0).toString();

			int ff_posx = (new Integer(action.getTerm(1).toString())).intValue();
			int ff_posy = (new Integer(action.getTerm(2).toString())).intValue();
			for(int i = 0; i < this.people.size(); i++)
			{
				Individual ind = this.people.get(i);
				if(ind.getName().equals(individual_name_2))
				{
					if(ind.beingHelpedOut())
					{
						ind.changeHelpState();
						ind.setPosition(ff_posx, ff_posy);
					}

					break;
				}
			}
			update_newest = false;
			break;
		case "remove_fire_perception":
			int fire_percep_posx = (new Integer(action.getTerm(0).toString())).intValue();
			int fire_percep_posy = (new Integer(action.getTerm(1).toString())).intValue();

			a.removeFirePerception(fire_percep_posx, fire_percep_posy);
			update_newest = false;
			break;

		case "add_new_burned_areas":
			String output = action.getTerm(0).toString();
			output = output.replaceAll("[\\[\\](){}]","");
			String[] values = output.split(",");

			for(int i = 0; i < (values.length - 1); i+=2)
				a.addBurnedPerception(Integer.parseInt(values[i]), Integer.parseInt(values[i + 1]));

			update_newest = false;
			break;
		case "add_new_fire_areas":
			String output2 = action.getTerm(0).toString();
			output2 = output2.replaceAll("[\\[\\](){}]","");
			String[] values2 = output2.split(",");

			for(int i = 0; i < (values2.length - 1); i+=2)
				a.addFirePerception(Integer.parseInt(values2[i]), Integer.parseInt(values2[i + 1]));

			update_newest = false;
			break;	
		case "partner_died":
			String dead_agent_name = action.getTerm(0).toString();
			a.partnerDied(dead_agent_name);
			update_newest = false;
			break;
		case "rescued":
			String individual_rescued = action.getTerm(0).toString();

			for(int i = 0; i < this.people.size(); i++)
			{
				Individual ind = this.people.get(i);
				if(ind.getName().equals(individual_rescued))
				{
					if(!ind.getRescued())
						ind.changeRescuedState(true);

					break;
				}
			}
			update_newest = false;
			break;
		}

		updatePercepts(agent, tick_action, update_newest);

		informAgsEnvironmentChanged();

		gui.update();

		return true;
	}


	/**	
	 * Responsible for executing the simulation at a given rate.
	 */
	private void threadSleep() {
		try {
			Thread.sleep(Constants.UPDATE_TIME_FIREFIGHTERS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	/**	Update perceptions of the environment in the point of view of the agent.
	 * 
	 * @param agent Agent to get the update of the perceptions
	 * @param tick_action Boolean to check if it is necessary to update the health of the firefighter
	 * @param update_newest Boolean to check if it is necessary to update the newest perceptions
	 */
	void updatePercepts(String agent, boolean tick_action, boolean update_newest) {

		clearPercepts(agent);

		if(agent.equals(tower.getName()))
		{
			addPercept(agent, tower.getPosLiteral());

			for(int i = 0; i < firefighters.size(); i++)
			{
				FireFighter ff = firefighters.get(i);
				if(!ff.isDead())
				{
					Literal ffpos = Literal.parseLiteral("ffPos(" + ff.getName() + ", " + ff.getPosition().getX() + ", " + ff.getPosition().getY() +")" );
					addPercept(agent, ffpos);					
				}
				else
				{
					if(tower.addToMenLost(ff.getName()))
					{
						Literal dead_man = Literal.parseLiteral("ffdied(" + ff.getName() + ", " + ff.getPosition().getX() + ", " + ff.getPosition().getY() + ")");
						addPercept(agent, dead_man);	
					}
				}
			}

			if(!started)
			{
				Literal starter_pos = Literal.parseLiteral("startAt(" +fire_starter_pos.getX() + ", " + fire_starter_pos.getY() + ")");
				addPercept(agent, starter_pos);
				started = true;
			}
		}
		else if("firefighter".equals(agent.substring(0, 11))) //FireFighters
		{
			FireFighter a = this.getFirefighterByName(agent);

			if(!a.isDead())
			{
				addPercept(agent, a.getPosLiteral());
				for(int i = 0; i < a.getFirePerceptions().size(); i++)
				{
					Pair<Integer, Integer> pos = a.getFirePerceptions().get(i);
					Literal firepercep = a.getFirePercepPosLiteral(pos);
					addPercept(agent, firepercep);
				}
				
				System.out.println(agent + " Percepts:" + consultPercepts(agent));

				for(int k = 0; k < a.getBurnedPerceptions().size(); k++)
				{
					Pair<Integer, Integer> pos = a.getBurnedPerceptions().get(k);
					Literal burnedpercep = a.getBurnedPercepPosLiteral(pos);
					addPercept(agent, burnedpercep);
				}

				ArrayList<String> partners = a.getPartners();

				for(int i = 0; i < partners.size(); i++)
				{
					Literal partner = Literal.parseLiteral("partner(" + partners.get(i) +")" );
					addPercept(agent, partner);
				}


				Pair<Integer, Integer> agent_pos = a.getPosition();

				if(region[agent_pos.getX()][agent_pos.getY()].getType().equals("Fire") && tick_action)
				{
					int density = ((VegetationOnFire) region[agent_pos.getX()][agent_pos.getY()]).getDensity();
					a.updateHp(density * Constants.HP_DECREASE_FACTOR);
				}	


				if(a.isDead())
					addPercept(agent, a.deadStateLiteral());

				if(update_newest)
				{
					if(a.getNewBurnedPerceptions().size() > 0)
						addPercept(agent, a.getNewBurnedPercepPosLiteral());

					if(a.getNewFirePerceptions().size() > 0)
						addPercept(agent, a.getNewFiresPercepPosLiteral());
				}
				//perceptions of the new burned locations


				//System.out.println(agent + " Percepts:" + consultPercepts(agent));
			}
		}
		else { 

			for(int i = 0; i < this.people.size(); i++)
			{
				Individual ind = this.people.get(i);
				addPercept(ind.getName(), ind.getPosLiteral()); //position percept

				if(!ind.alreadyAskedForHelp())
				{
					if(ind.isFireNear(region))
					{
						ind.helpMessageSent();
						addPercept(ind.getName(), ind.getFireNearLiteral());
					}
				}
			}
		}
	}

	/**	Returns the regioncell object that represents the environment
	 * 
	 * @return A RegionCell list of lists representing each cell of the environment
	 */
	public RegionCell[][] getRegion() {
		return region;
	}


	/**	
	 * Responsible to stop the simulation
	 */
	public void stop() {
		super.stop();
		gui.dispose();
	}


	
	/**	Gets all the fire cells positions, currently being extinguished in the simulation environment
	 * 
	 * @return A List of all the fire cells positions being extinguished
	 */
	public ArrayList<Pair<Integer, Integer>> getAllFireBeingExtinguished()
	{
		return this.fire_being_extinguished;
	}


	/**	Gets all the firefighters in the simulation environment
	 *
	 * @return A List of all the firefighters in the world
	 */
	public ArrayList<FireFighter> getFirefighters()
	{
		return this.firefighters;
	}


	/**	Gets all the firefighters positions in the simulation environment
	 * 
	 * @return A List of all the firefighters positions in the world
	 */
	public ArrayList<Pair<Integer, Integer>> getFirefightersPos()
	{
		ArrayList<Pair<Integer, Integer>> firefightersPos = new ArrayList<Pair<Integer, Integer>>(Constants.FIREFIGHTERS_NUMBER);

		for(int i = 0; i < this.firefighters.size(); i++)
		{
			firefightersPos.add(this.firefighters.get(i).getPosition());
		}

		return firefightersPos;
	}


	/**	Gets all individuals in the simulation environment
	 * 
	 * @return A List of all the individual in the world
	 */
	public ArrayList<Individual> getPeople()
	{
		return this.people;
	}

	/**	Gets the firefighter from the list that has the parameter name
	 * 
	 * @param name Name of the firefighter to search
	 * 
	 * @return The Firefighter that has the input name
	 */
	public FireFighter getFirefighterByName(String name)
	{
		for(int i = 0; i < this.firefighters.size(); i++)
		{
			if(this.firefighters.get(i).getName().equals(name))
				return this.firefighters.get(i);
		}

		return null;
	}

	/**	Gets the safest adjacent cell
	 * 
	 * @param ff_pos Position of the fire fighter
	 * 
	 * @return A pair containing the position of the safest cell
	 */
	public synchronized Pair<Integer, Integer> safestPlaceToGo(Pair<Integer, Integer> ff_pos)
	{
		int x = ff_pos.getX();
		int y = ff_pos.getY();

		List<Integer> values = new ArrayList<>();
		List<Pair<Integer, Integer>> positions = new ArrayList<>();

		positions.add(new Pair<Integer, Integer>(x + 1, y));
		values.add(evaluatePosToGo(x + 1, y));
		
		positions.add(new Pair<Integer, Integer>(x - 1, y));
		values.add(evaluatePosToGo(x - 1, y));
		
		positions.add(new Pair<Integer, Integer>(x, y + 1));
		values.add(evaluatePosToGo(x, y + 1));
		
		positions.add(new Pair<Integer, Integer>(x, y - 1));
		values.add(evaluatePosToGo(x, y - 1));

		positions.add(new Pair<Integer, Integer>(x + 1, y + 1));
		values.add(evaluatePosToGo(x + 1, y + 1));
		
		positions.add(new Pair<Integer, Integer>(x - 1, y - 1));
		values.add(evaluatePosToGo(x - 1, y - 1));
		
		positions.add(new Pair<Integer, Integer>(x + 1, y - 1));
		values.add(evaluatePosToGo(x + 1, y - 1));
		
		positions.add(new Pair<Integer, Integer>(x - 1, y + 1));
		values.add(evaluatePosToGo( x - 1, y + 1));

		int max = 0;
		int max_index = 0;
		for(int i = 0; i < values.size(); i++)
		{
			if(values.get(i) > max)
			{
				max = values.get(i);
				max_index = i;
			}
		}
		
		
		if(max != 0)
			return new Pair<Integer, Integer>(positions.get(max_index).getX(),positions.get(max_index).getY());
		

		return null;
	}
	
	/**	Evaluate a cell
	 * 
	 * @param x The position in x of the cell to be evaluated
	 * @param y The position in y of the cell to be evaluated
	 * 
	 * @return The value of going to that cell, the higher the better
	 */
	private int evaluatePosToGo(int x, int y)
	{
		if(x < 0 || y < 0 || x >= Constants.GRID_SIZE || y >= Constants.GRID_SIZE)
			return 0;
		
		String type = region[x][y].getType();
		if(type.equals("Terrain") || type.equals("BurnedArea")) 
			return 100;
		else if(type.equals("Vegetation"))
		{
			Vegetation veg = (Vegetation) region[x][y];
			return 100 - veg.getTemperature();
		}
		else
			return 0;
	}


	/**	Gets the number of firefighter from the list that are dead
	 *
	 * @return The number of dead Firefighters
	 */
	public int getDeadFirefightersNumber()
	{
		int numberOfDeadFirefighters = 0;

		for(int i = 0; i < this.firefighters.size(); i++)
			if(this.firefighters.get(i).isDead())
				numberOfDeadFirefighters++;

		return numberOfDeadFirefighters;
	}

	/**	Gets the number of firefighter from the list that are alive
	 *
	 * @return The number of Firefighters alive
	 */
	public int getAliveFirefightersNumber()
	{
		return this.firefighters.size() - getDeadFirefightersNumber();
	}


	/**	Gets the number of people from the list that were rescued
	 *
	 * @return The number of People rescued
	 */
	public int getPeopleRescuedNumber()
	{
		int nPeopleRescued = 0;

		for(int i = 0; i < this.people.size(); i++)
			if(this.people.get(i).getRescued())
				nPeopleRescued++;

		return nPeopleRescued;
	}

	/**	Gets the number of people from the list that asked and are waiting for rescue
	 *
	 * @return The number of People that asked and are waiting for rescue
	 */
	public int getPeopleAskedHelpNumber()
	{
		int nPeopleAskedHelp = 0;

		for(int i = 0; i < this.people.size(); i++)
			if(this.people.get(i).alreadyAskedForHelp())
				nPeopleAskedHelp++;

		return nPeopleAskedHelp;
	}

}
