package logic.env;

/**  Class BurnedArea
 * It is a type of a region Cell
 * 	
 * @see RegionCell
 *
 */
public class BurnedArea extends RegionCell{

	/**	Constructor of BurnedArea
	 *	
	 * @param wind_direction direction of the wind in the world environment
	 * @param wind_speed speed of the wind in the world environment
	 * 
	 */
	public BurnedArea(int wind_direction, int wind_speed) {
		super(wind_direction, wind_speed);
	}
	
	/**Method that gets his role.
	 * 
	 * @return A string with the role assigned to BurnedArea.
	 */
	public String getType() {
		return "BurnedArea";
	}

	
}
