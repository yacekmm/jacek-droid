package valueobjects;

import helpers.Converter;

import java.util.Map;

import org.json.simple.JSONArray;

public class PlayingInfo 
{
	private Sphere[] spheres; 
	
	/**
	 * The current radius of the arena, in pixels
	 */
	private int arenaRadius;

	@SuppressWarnings("unchecked")
	public PlayingInfo(Map<String, Object> playingInfoAsJson)
	{
		arenaRadius = Converter.toInt(playingInfoAsJson.get("arenaRadius"));
		JSONArray array = (JSONArray) playingInfoAsJson.get("players");
		spheres = new Sphere[array.size()];
		for (int i = 0; i < array.size(); i++) {
			Map<String, Object> array_element = (Map<String, Object>)array.get(i);
			spheres[i] = new Sphere(array_element);
		}
	}
	
	public Sphere[] getSpheres() {
		return spheres;
	}

	public int getArenaRadius() {
		return arenaRadius;
	}

	@Override
	public String toString() {
		return "PlayingInfo [arenaRadius=" + arenaRadius + "]";
	}
	
}
