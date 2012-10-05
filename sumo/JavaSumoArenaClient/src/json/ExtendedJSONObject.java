/**
 * 
 */
package json;

import org.json.simple.JSONObject;

/**
 * @author peal6230
 * Adds typed accessors to JSonObject
 */
public class ExtendedJSONObject extends JSONObject {

	private static final long serialVersionUID = 1L;
	
	public int getIntValue(String key)
	{
		return ((Number) get(key)).intValue();
	}

}
