package logic.env;

import utils.Constants;

/**  Class Vegetation
 * It is a type of a region Cell
 * 	
 * @see RegionCell
 *
 */
public class Vegetation extends RegionCell {

	/** Density of vegetation */
	private final int density; // min - 1 and max - 3
	/** Temperature of vegetation, with limit 100 */
	private int temperature;
	/** Material available to be burned */
	private int comburent_left;

	/**	Constructor of Vegetation
	 *
	 * @param vegetation_density The density of the vegetation
	 * @param wind_direction direction of the wind in the world environment
	 * @param wind_speed speed of the wind in the world environment
	 * 
	 */
	public Vegetation(int vegetation_density, int wind_direction, int wind_speed) {
		super(wind_direction, wind_speed);
		this.density = vegetation_density;
		this.temperature = 0;
		this.comburent_left = Constants.COMBURENT_FACTOR * density;
	}
	
	/**	Constructor of Vegetation
	 *
	 * @param vegetation_density The density of the vegetation
	 * @param wind_direction direction of the wind in the world environment
	 * @param wind_speed speed of the wind in the world environment
	 * @param comburent_left The material that is still available to be burned. 
	 */
	public Vegetation(int vegetation_density, int wind_direction, int wind_speed, int comburent_left)
	{
		super(wind_direction, wind_speed);
		this.density = vegetation_density;
		this.temperature = 0;
		this.comburent_left = comburent_left;
	}

	/** Gets the density.
	 * 
	 * @return A integer with values between 1 and 3.
	 */
	public int getDensity() {

		return density;
	}

	/** Method that gets his role.
	 * 
	 * @return A string with the role assigned to Vegetation.
	 */
	public String getType() {
		return "Vegetation";
	}
	
	/** Gets the temperature of vegetation
	 * 
	 * @return A integer with the actual value of vegetation temperature.
	 */
	public int getTemperature()
	{
		return this.temperature;
	}
	
	/** Sets its temperature
	 * 
	 * @param temp Temperature
	 */
	public void setTemperature(int temp)
	{
		this.temperature = temp;
	}
	
	/** Gets the material that is still available to be burned
	 * 
	 * @return A integer corresponding to that material left.
	 */
	public int getComburentLeft() 
	{
		return this.comburent_left;
	}
	
	/** Function used for a burning step.
	 * 
	 */
	public void burn() 
	{
		if(this.comburent_left - Constants.COMBUSTION_CONSUMPTION < 0)
			this.comburent_left = 0;
		else
			this.comburent_left -= Constants.COMBUSTION_CONSUMPTION;
	}

}