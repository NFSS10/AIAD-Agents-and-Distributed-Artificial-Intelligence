package logic.env;

import utils.Constants;

/**  Class VegetationOnFire
 * It is a type of a region Cell
 * 	
 * @see RegionCell
 *
 */
public class VegetationOnFire extends Vegetation {
	
	/** The water need to extinguish in case of fire */
	private int necessaryWater;

	/**	Constructor of VegetationOnFire
	 *
	 * @param vegetation_density The density of the vegetation
	 * @param wind_direction direction of the wind in the world environment
	 * @param wind_speed speed of the wind in the world environment
	 * 
	 */
	public VegetationOnFire(int vegetation_density, int wind_direction, int wind_speed) {
		super(vegetation_density, wind_direction, wind_speed);
		
		this.necessaryWater = Constants.COMBURENT_FACTOR * super.getDensity();
	}

	/** Method that gets his role.
	 * 
	 * @return A string with the role assigned to VegetationOnFire.
	 */
	public String getType() {
		return "Fire";
	}
	
	/** Gets the density.
	 * 
	 * @return A integer with values between 1 and 3.
	 */
	public int getDensity()
	{
		return super.getDensity();
	}
	
	/** Update the necessary water to extinguish fire
	 * 
	 */
	public void updateNecessaryWater()
	{
		this.necessaryWater = Math.min(this.necessaryWater, super.getComburentLeft());
	}
	
	/** Gets the necessary water to extinguish fire
	 * 
	 * @return A integer corresponding to the water needed.
	 */
	public int getNecessaryWater()
	{
		return this.necessaryWater;
	}
	
	/** Extinguish step
	 * 
	 * @param step The value to be decrement to necessaty water
	 */
	public void extinguish(int step)
	{
		if((this.necessaryWater - step) > 0)
			this.necessaryWater -= step;
		else
			this.necessaryWater = 0;
	}
	
	/**	Gets the speed of the wind in the environment in this cell.
	 * 
	 * @return A integer with the wind speed.
	 */
	public int getWindSpeed()
	{
		return super.getWindSpeed();
	}
	
	/**	Gets the direction of the wind in the environment in this cell.
	 * 
	 * @return A integer with the wind direction.
	 */
	public int getWindDirection()
	{
		return super.getWindDirection();
	}
	
	
}