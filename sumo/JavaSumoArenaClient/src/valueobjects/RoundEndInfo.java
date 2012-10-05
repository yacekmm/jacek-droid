package valueobjects;

import helpers.Converter;

import java.util.List;
import java.util.Map;

/**
 * The data transmitted at the end of each round
 * @author peal6230
 */
public class RoundEndInfo 
{
	@SuppressWarnings("unchecked")
	public  RoundEndInfo(Map<String, Object> roundEndInfoAsJson)
	{
		gameWinners = (List<Number>) roundEndInfoAsJson.get("gameWinners");
		roundWinners = (List<Number>) roundEndInfoAsJson.get("roundWinners"); 
		currentRound = Converter.toInt(roundEndInfoAsJson.get("currentRound"));
	}
	
	/**
	 * The index(es) of the player(s) who won the game, or an empty list if the game is not finished yet 
	 * 
	 */
    public List<Number> gameWinners;

	/**
	 * The index(es) of the player(s) who won the round, or an empty list if there was no winner
	 */
    public List<Number> roundWinners;   
    
    /**
	 * The number of the current round, starting at 1
	 */
    public int currentRound;

	@Override
	public String toString() {
		return "RoundEndInfo [gameWinners=" + gameWinners + ", roundWinners=" + roundWinners
				+ ", currentRound=" + currentRound + "]";
	}
    
}
