package logic.env;

/**  Class Terrain
 * It is a type of a Region Cell
 * 	
 * @see RegionCell
 *
 */
public class Terrain extends RegionCell {

	/**	Constructor of Terrain
	 *	
	 * @param wind_direction direction of the wind in the world environment
	 * @param wind_speed speed of the wind in the world environment
	 * 
	 */
	public Terrain(int wind_direction, int wind_speed){	
		super(wind_direction, wind_speed);
	}

	/**Method that gets his role.
	 * 
	 * @return A string with the role assigned to Terrain.
	 */
	public String getType() {
		return "Terrain";
	}
}