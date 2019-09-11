package logic.env;

import utils.Constants;
import utils.Pair;
import java.util.ArrayList;

/** Class Spreadfire responsible to do a propagation step */
public class SpreadFire {
	
	/** The environment of the simulation */
	RegionCell[][] region;

	/**	Constructor of Spreadfire
	 *	
	 * @param region The environment of the simulation
	 * 
	 */
	public SpreadFire(RegionCell[][] region){
		this.region = region;
	}

	/**	Does a step of fire propagation in the environment.
	 *	
	 * @param region The environment of the simulation
	 * @param wind_direction direction of the wind in the world environment
	 * @param wind_speed speed of the wind in the world environment
	 * 
	 * @return The new region after the step
	 */
	public RegionCell[][] step(RegionCell[][] region, int wind_direction, int wind_speed) {

		ArrayList<Pair<Integer, Integer>> fire_locations = getFireLocations();

		for(int i = 0; i < fire_locations.size(); i++)
			applyPropagation(fire_locations.get(i), wind_direction, wind_speed, region);

		for(int k = 0; k < this.region.length; k++)
		{
			for(int r = 0; r < this.region[k].length; r++)
			{
				if (region[k][r].getType().equals("Vegetation"))
				{
					Vegetation v = (Vegetation) region[k][r];
					if(v.getTemperature() == 100)
						region[k][r] = new VegetationOnFire(v.getDensity(), wind_direction, wind_speed);
				}
			}
		}
		return region;
	}

	/**	Update the temperature at a given position according to the relative cell.
	 *	
	 * @param x The position in x
	 * @param y The position in y
	 * @param relative_position The direction to the relative cell
	 * @param wind_direction direction of the wind in the world environment
	 * @param wind_speed speed of the wind in the world environment
	 * @param region The environment of the simulation
	 * 
	 */
	private void updateFireDegreeAt(Integer x, Integer y, int relative_position, int wind_direction, int wind_speed, RegionCell[][] region)
	{
		double wind_consideration = 0;
		
		if(region[x][y].getType().equals("Vegetation"))
		{
			Vegetation v = (Vegetation) region[x][y];
			
			if(relative_position == wind_direction)
				wind_consideration = 1;
			
			int density = v.getDensity();

			int atual_temperature = v.getTemperature();

			int new_temperature = (int) (atual_temperature + (wind_consideration * wind_speed + Constants.PROPAGATION_FACTOR) * density);

			if(new_temperature > 100)
				v.setTemperature(100);
			else
				v.setTemperature(new_temperature);
		}

	}

	/**	Apply the propagation from a given position.
	 *	
	 * @param location Position of the cell from where it propagates
	 * @param wind_direction direction of the wind in the world environment
	 * @param wind_speed speed of the wind in the world environment
	 * @param region The environment of the simulation
	 * 
	 */
	private void applyPropagation(Pair<Integer, Integer> location, int wind_direction, int wind_speed, RegionCell[][] region){


		int same_x = location.getX();
		int incr_x = (location.getX() < region.length - 1) ? location.getX() + 1 : region.length - 2;
		int decr_x = (location.getX() > 0) ? location.getX() - 1 : 0;
		int same_y = location.getY();
		int incr_y = (location.getY() < region.length - 1) ? location.getY() + 1 : region.length - 2;
		int decr_y = (location.getY() > 0) ? location.getY() - 1 : 0;


		updateFireDegreeAt(same_x, decr_y, 1, wind_direction, wind_speed, region); //N

		updateFireDegreeAt(decr_x, decr_y, 8, wind_direction, wind_speed, region); //NO

		updateFireDegreeAt(incr_x, same_y, 3, wind_direction, wind_speed, region); //E

		updateFireDegreeAt(incr_x, incr_y, 4, wind_direction, wind_speed, region); //SE

		updateFireDegreeAt(same_x, incr_y, 5, wind_direction, wind_speed, region); //S

		updateFireDegreeAt(decr_x, incr_y, 6, wind_direction, wind_speed, region); //SO

		updateFireDegreeAt(decr_x, same_y, 7, wind_direction, wind_speed, region); //O

		updateFireDegreeAt(incr_x, decr_y, 2, wind_direction, wind_speed, region); //NE

	}

	/**	Gets all the fire locations in the environment
	 *	
	 * @return A array with all fire locations
	 */
	private ArrayList<Pair<Integer, Integer>> getFireLocations(){

		ArrayList<Pair<Integer, Integer>> fire_locations = new ArrayList<Pair<Integer, Integer>>();

		for(int i = 0; i < this.region.length; i++)
		{
			for(int j = 0; j < this.region[i].length; j++)
			{
				if(region[i][j].getType().equals("Fire"))
					fire_locations.add(new Pair<Integer, Integer>(i,j));
			}
		}

		return fire_locations;
	}

}