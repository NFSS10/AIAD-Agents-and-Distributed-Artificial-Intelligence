package logic.env;

import java.util.Random;

import utils.Constants;

/** Class Wind responsible to set the wind in the environment */
public class Wind {
	
	/** Direction of the wind in the environment */
	public int wind_direction;
	
	/** Speed of the wind in the environment */
	public int wind_speed;
	
	/** Random member so that the wind can change his state */
	private Random random;
	
	
	/**	Constructor of Wind
	 *	The constructor of tower don't have parameters the initial values of the wind_direction and the wind_speed are selected randomly
	 *
	 * @see Constants
	 */
	public Wind()
	{
		random = new Random();
		this.wind_direction = random.nextInt(Constants.NUMBER_WIND_DIRECTIONS) + 1;
		this.wind_speed = random.nextInt(Constants.MAX_WIND_SPEED);
	}
	
	/**	
	 * Updates the wind in the environment.
	 */
	public void updateWind()
	{
		updateWindDirection();
		updateWindSpeed();
	}
	
	
	/**	Updates the wind direction in the environment taking into account the probability of the wind remaining in the same state.
	 * 
	 * @return A integer with the new wind_direction
	 * 
	 * @see Constants
	 */
	public int updateWindDirection()
	{
		int number = this.random.nextInt(Constants.PERCENTAGE);
		
		if(number > Constants.PROB_WIND_STAYS)
			this.wind_direction = random.nextInt(Constants.NUMBER_WIND_DIRECTIONS) + 1;
		
		return this.wind_direction;
	}
	
	
	/**	Updates the wind speed in the environment taking into account the probability of the wind remaining in the same state.
	 * 
	 * @return A integer with the new wind_speed
	 * 
	 * @see Constants
	 */
	public int updateWindSpeed()
	{
		int number = this.random.nextInt(Constants.PERCENTAGE);
		
		if(number > Constants.PROB_WIND_STAYS)
			this.wind_speed = random.nextInt(Constants.MAX_WIND_SPEED);
		
		return this.wind_speed;
	}
	
	
	/**	Gets the direction of the wind in the environment.
	 * 
	 * @return A integer with the wind direction.
	 */
	public int getWindDirection()
	{
		return this.wind_direction;
	}
	
	
	/**	Gets the speed of the wind in the environment.
	 * 
	 * @return A integer with the wind speed.
	 */
	public int getWindSpeed()
	{
		return this.wind_speed;
	}
}
