package logic.env;

import java.util.Random;

import utils.Constants;

/** Class ForestMaker responsible to make the forest */
public  class ForestMaker {
	
	/** Random used for positioning */
	public static Random random = new Random(System.currentTimeMillis());

	
	/**	Create a bunch of vegetation cells.
	 *	
	 * @param map Bidimensional array representing the environment
	 * @param x The position in x where it starts applying
	 * @param y The position in y where it starts applying
	 * @param counter Actual counter of iteration
	 * @param max_counter Counter limit
	 * 
	 * @return The new environment with some vegetation
	 */
	public static int[][] apply(int[][] map, int x, int y, int counter, int max_counter){
		
		if (x < 0 || y < 0 || x >= map.length || y >= map[x].length || counter >= max_counter)
			return map;

		if(x == Constants.TOWER_POS.getX() && y == Constants.TOWER_POS.getY()) //can't create forest in the place of the tower
			return map;

		map[x][y] = 1;
		counter++;
		int next_counter = random.nextInt(max_counter - 1) + counter;
		apply(map, x + 1, y, next_counter, max_counter);
		apply(map, x - 1, y, next_counter, max_counter);
		apply(map, x, y + 1, next_counter, max_counter);
		apply(map, x, y - 1, next_counter, max_counter);

		return map;

	}

	/**	Make the environment for the simulation.
	 *	
	 * @param map Bidimensional array representing the environment
	 * @param num_it Number of iterations
	 * @param max_counter Radius limit
	 * 
	 * @return The environment for the simulation
	 */
	public static int[][] make(int[][] map, int num_it, int max_counter)
	{
		for(int i = 0; i < num_it; i++){

			int x = random.nextInt(map.length);
			int y = random.nextInt(map.length);

			map = apply(map, x, y, 0, max_counter);
		}

		return map;
	}
}
