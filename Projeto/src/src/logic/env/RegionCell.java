package logic.env;

/** Class responsible to store a cell of the environment */
public abstract class RegionCell {

	/** Direction of the wind in the environment */
	protected final int wind_direction; // 1 - N , 2 - NE, 3 - E, 4 -  SE, 5 - S, 6 - SO, 7 - O, 8 - NO 
	
	/** Speed of the wind in the environment */
	protected final int wind_speed; //0 - 5
	

	/**	Constructor of RegionCell
	 *	
	 * @param direction direction of the wind in the world environment
	 * @param speed speed of the wind in the world environment
	 * 
	 */
	public RegionCell(int direction, int speed) {
		wind_direction = direction;
		wind_speed = speed;
	}

	
	/**	Gets the direction of the wind in the environment in this cell.
	 * 
	 * @return A integer with the wind direction.
	 */
	protected int getWindDirection(){
		return wind_direction;
	}

	
	/**	Gets the speed of the wind in the environment in this cell.
	 * 
	 * @return A integer with the wind speed.
	 */
	protected int getWindSpeed() {
		return wind_speed;
	}

	/**	Abstract method that gets the type of the RegionCell son instance.
	 * 
	 * @return A string with the type of the RegionCell son instance.
	 */
	public abstract String getType();
}
