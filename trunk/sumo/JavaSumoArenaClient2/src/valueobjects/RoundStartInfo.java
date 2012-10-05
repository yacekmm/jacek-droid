package valueobjects;

import helpers.Converter;

import java.util.Map;

/**
 * The data transmitted at the beginning of the current round
 * @author peal6230
 */
public class RoundStartInfo 
{
	public  RoundStartInfo(Map<String, Object> roundStartInfoAsJson)
	{
		myIndex = Converter.toInt(roundStartInfoAsJson.get("yourIndex"));
		myTeamIndex = Converter.toInt(roundStartInfoAsJson.get("yourTeamIndex"));
		playerCount = Converter.toInt(roundStartInfoAsJson.get("playerCount"));
		arenaInitialRadius = Converter.toInt(roundStartInfoAsJson.get("arenaInitialRadius"));
		sphereRadius = Converter.toInt(roundStartInfoAsJson.get("sphereRadius"));
		maxSpeedVariation = Converter.toInt(roundStartInfoAsJson.get("maxSpeedVariation"));
		currentRound = Converter.toInt(roundStartInfoAsJson.get("currentRound"));
		roundForVictory = Converter.toInt(roundStartInfoAsJson.get("roundForVictory"));
	}
	
	
	/**
	 * The index of your sphere in the update message sent by the server
	 */
    public int myIndex;

	/**
	 * The index of your team in the update message sent by the server
	 */
    public int myTeamIndex;
    
    /**
	 * The total number of players in the current game
	 */
    public int playerCount;

    /**
	 * The radius of the arena, in pixels, when beginning the round.
	 */
    public int arenaInitialRadius;

    /**
	 * The radius of the spheres, in pixels
	 */
    public int sphereRadius;

    /**
	 * The maximum variation allowed for the velocity vector of the sphere, 
	 * at each update
	 */
    public int maxSpeedVariation;

    /**
	 * The number of the current round, starting at 1
	 */
    public int currentRound;

    /**
	 * The number of rounds required for winning the current game
	 */
    public int roundForVictory;

	@Override
	public String toString() {
		return "RoundStartInfo [myIndex=" + myIndex + ", playerCount=" + playerCount + ", arenaInitialRadius="
				+ arenaInitialRadius + ", sphereRadius=" + sphereRadius + ", maxSpeedVariation=" + maxSpeedVariation
				+ ", currentRound=" + currentRound + ", roundForVictory=" + roundForVictory + "]";
	}
}
