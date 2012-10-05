package valueobjects;

import java.util.Map;

/**
 * A Value Object describing a player status
 * @author peal6230
 */
public class Sphere 
{
	public Sphere(Map<String, Object> sphereAsJson)
	{
		index = ((Number) sphereAsJson.get("index")).intValue();
		team = ((Number) sphereAsJson.get("team")).intValue();
		x = ((Number) sphereAsJson.get("x")).intValue();
		y = ((Number) sphereAsJson.get("y")).intValue();
		vx = ((Number) sphereAsJson.get("vx")).intValue();
		vy = ((Number) sphereAsJson.get("vy")).intValue();
		inArena = (Boolean) sphereAsJson.get("inArena");
	}
	
	/**
	 * The index of this player, during the current game
	 */
	public int index;
	
	/**
	 * The index of the team of this player, during the current game
	 */
	public int team;
	
	/**
	 * The horizontal coordinate of the sphere center
	 */
	public int x;
	
	/**
	 * The vertical coordinate of the sphere center
	 */
	public int y;
	
	/**
	 * The horizontal component of the velocity vector of this sphere
	 */
	public int vx;
	
	/**
	 * The vertical component of the velocity vector of this sphere
	 */
	public int vy;
	
	/**
	 * True when the sphere is inside the arena, false otherwise
	 */
	public Boolean inArena;

	@Override
	public String toString() {
		return "Sphere [index=" + index + ", team=" + team + ", x=" + x + ", y=" + y + ", vx=" + vx + ", vy=" + vy + ", inArena="
				+ inArena + "]";
	}
}
