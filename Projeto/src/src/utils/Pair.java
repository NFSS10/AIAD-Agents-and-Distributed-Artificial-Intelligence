package utils;

/** Class Pair responsible to store the position of the agents in the environment */
public class Pair<X,Y> {
	
	/** Position x in the environment */
	private X x;
	
	/** Position y in the environment */
	private Y y;

	
	/**	Constructor of Pair
	 *	
	 * @param x Position x of the agent in the environment.
	 * @param y Position y of the agent in the environment.
	 * 
	 */
	public Pair(X x, Y y){
		this.x = x;
		this.y = y;
	}

	
	/**	Gets the x position of the agent
	 * 
	 * @return A X that indicates the position X of the agent in the environment.
	 */
	public X getX(){
		return x;
	}

	
	/**	Gets the y position of the agent
	 * 
	 * @return A Y that indicates the position Y of the agent in the environment.
	 */
	public Y getY(){
		return y;
	}

	
	/**	Sets a new x position of the agent.
	 * 
	 * @param x The new x position of the agent.
	 */
	public void setX(X x){
		this.x = x;
	}

	
	/**	Sets a new y position of the agent.
	 * 
	 * @param y The new y position of the agent.
	 */
	public void setY(Y y){
		this.y = y;
	}
	
	
	/**	Sets a new pair position of the agent.
	 * 
	 * @param x The new x position of the agent.
	 * @param y The new y position of the agent.
	 */
	public void setPair(X x, Y y)
	{
		setX(x);
		setY(y);
	}
	
	/**	Override method to check if a pair is equal to the current pair
	 * 
	 * @param obj The object pair to compare the the current pair.
	 * 
	 * @return True if the pair is equal, false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (!Pair.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    final Pair<X, Y> other = (Pair<X, Y>) obj;
	    
	    if(other.getX() != null && other.getY() != null)
	    {
	    	if(other.getX() == this.x && other.getY() == this.y)
	    		return true;
	    }
	    return false;
	}

	
	
}