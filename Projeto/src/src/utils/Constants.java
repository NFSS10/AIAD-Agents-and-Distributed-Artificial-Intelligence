package utils;

/** Class Constants responsible for saving all the constants that are used in the world */
public class Constants {
	
	/** Indicates the grid size of the environment */
	public static int GRID_SIZE = 32;
	
	/** Indicates the number of the wind directions that exist in the world */
	public static int NUMBER_WIND_DIRECTIONS = 8;
	
	/** Indicates the max wind speed in the environment */
	public static int MAX_WIND_SPEED = 25;
	
	/** Indicates the simulation window size */
	public static int WINDOW_SIZE = 640;
	
	/** Indicates the width of each cell */
	public static int CELL_WIDTH = WINDOW_SIZE/GRID_SIZE;
	
	/** Indicates the height of each cell */
	public static int CELL_HEIGHT = WINDOW_SIZE/GRID_SIZE;
	
	/** Indicates the minimum density of the vegetation */
	public static int MIN_DENSITY = 1;
	
	/** Indicates the maximum density of the vegetation */
	public static int MAX_DENSITY = 3;
	
	/** Indicates the comburent factor of the vegetation (Ex: Density 1 vegetation has 50, density 2 has 100) */
	public static int COMBURENT_FACTOR = 50;
	
	/** Indicates the number of firefighters in the simulation */
	public static int FIREFIGHTERS_NUMBER = 6;
	
	/** Indicates the combustion consumption at each tick */
	public static int COMBUSTION_CONSUMPTION = 2;
	
	/** Tickrate to update the environment */
	public static int UPDATE_TIME_ENV = 200;
	
	/** Probability of wind keeping in the same direction and intensity */
	public static int PROB_WIND_STAYS = 80;
	
	/** Percentage value */
	public static int PERCENTAGE = 100;
	
	/** Indicates the propagation factor. With a lower value the fire will spread slower. */
	public static double PROPAGATION_FACTOR = 1;
	
	/** Tickrate to update the firefighters */
	public static int UPDATE_TIME_FIREFIGHTERS = 200;

	/** Water flux of the firefighters at each tick */
	public static int WATER_FLUX = 100;
	
	/** Tower position in the environment */
	public static Pair<Integer, Integer> TOWER_POS = new Pair<Integer, Integer>(0, 0);
	
	/** Number of lost people to be saved in the environment */
	public static int PEOPLE_NUMBER = 4;
	
	/** Indicates the radius of the individual perception */
	public static int INDIVIDUAL_PERCEPTION_RADIUS = 2;
	
	/** Indicates the maximum amount of life of the firefighter */
	public static int HP = 100;
	
	/** Indicates the decrease factor of the firefighter life if he is on the fire */
	public static int HP_DECREASE_FACTOR = 1;
	
	/** Indicates the right side size for the bar */
	public static int RIGHT_INFO_BAR_SIZE = 115;

	/** Indicates the right side wind position */
	public static Pair<Integer, Integer> WIND_DIR_POS = new Pair<Integer, Integer>(GRID_SIZE, 0);

	/** Indicates the right side firefighter info position */
	public static Pair<Integer, Integer> FIREFIGHTER_INFO_POS = new Pair<Integer, Integer>(GRID_SIZE, 6);

	/** Indicates the right side people info position */
	public static Pair<Integer, Integer> PEOPLE_INFO_POS = new Pair<Integer, Integer>(GRID_SIZE, 9);

}