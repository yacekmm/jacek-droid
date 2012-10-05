package sumoarena;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import valueobjects.AccelerationVector;
import valueobjects.PlayingInfo;
import valueobjects.RoundEndInfo;
import valueobjects.RoundStartInfo;

/**
 * handles the connection to the server
 */
public class GameClient {

	public static final String PREPARE_ROUND_ACTION = "prepare";
	public static final String PLAY_ACTION = "play";
	public static final String FINISH_ROUND_ACTION = "finishRound";

	private Socket socket;
	private BufferedReader bufferedReader;
	private JSONParser parser;
	private Player player;
	private Boolean verbose;

	/**
	 * Creates a new communicator layer
	 * 
	 * @param hostname
	 *            the URL of the game engine server
	 * @param port
	 *            the port number of the game engine server
	 * @param name
	 *            the name of the client
	 * @param team
	 *            the name of the team for this sumo
	 * @param url
	 *            the avatar URL
	 * @param verbose
	 *            true when verbose mode is active
	 * @throws Exception
	 *             thrown when communication with the server can't be
	 *             established
	 */
	public GameClient(String hostname, int port, String name, String team, String url, Boolean verbose) throws Exception {
		this.verbose = verbose;
		Boolean socketOpened = false;
		while (!socketOpened) {
			socketOpened = open(hostname, port);
		}
		player = new Player();
		parser = new JSONParser();
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		connect(name, team, url);
	}

	/**
	 * Registers this client on the game server
	 * 
	 * @param name
	 *            the name of the client
	 * @param team
	 *            the name of the team for this sumo
	 * @param url
	 *            optional avatar URL
	 * @return true when connection is established, false otherwise
	 */
	@SuppressWarnings("unchecked")
	private Boolean connect(String name, String team, String url) {
		Boolean connected = false;
		JSONObject connectionRequest = new JSONObject();
		connectionRequest.put("action", "connectPlayer");
		JSONObject parameters = new JSONObject();
		parameters.put("name", name);
		parameters.put("team", team);
		parameters.put("avatarUrl", url);
		connectionRequest.put("parameters", parameters);
		log("\n>sent: " + connectionRequest);

		write(connectionRequest.toJSONString());
		JSONObject responseAsJson = read();
		if(responseAsJson != null)
		{
			String action = (String) responseAsJson.get("action");
			String returnedName = (String) ((JSONObject) responseAsJson.get("parameters")).get("yourName");
			connected  = "acknowledgeConnection".equals(action) && name.equals(returnedName);
		}
		return connected;
	}

	/**
	 * Send a message to the server
	 * 
	 * @param writeTo
	 *            the string to be sent
	 * @throws Exception
	 *             when writing failed
	 */
	public void write(String writeTo) {
		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bufferedWriter.write(writeTo);
			bufferedWriter.flush();
		} catch (IOException e) {
			log("\n>ERROR: message was not sent. " + e.getMessage(), true);
		}
	}

	/**
	 * Wait for a message from the server
	 * 
	 * @return the server message, as String
	 */
	public JSONObject read() {
		JSONObject responseAsJson = null;
		try {
			String response = bufferedReader.readLine();
			log("\n>Received: " + response);
			if (response != null) {
				responseAsJson = (JSONObject) parser.parse(response);
			}

		} catch (ParseException e) {
			log("\n>ERROR: received message is not valid JSON. " + e.getMessage(), true);

		} catch (IOException e) {
			log("\n>ERROR: message reading failed. " + e.getMessage(), true);
		}
		return responseAsJson;
	}

	/**
	 * Parses the data received from the game server
	 * @param message the received data, as a Json String
	 */
	@SuppressWarnings("unchecked")
	public void processServerMessage(JSONObject message) {
		if (message == null) {
			return;
		}
		String action = (String) message.get("action");

		Map<String, Object> parameters = (Map<String, Object>) message.get("parameters");
		if (PREPARE_ROUND_ACTION.equals(action)) {
			startRound(parameters);
		} else if (PLAY_ACTION.equals(action)) {
			play(parameters);
		} else if (FINISH_ROUND_ACTION.equals(action)) {
			finishRound(parameters);
		}
	}

	/**
	 * notifies the player of the starting of a new round 
	 * @param parameters the round start information received from the game server
	 */
	private void startRound(Map<String, Object> parameters) {
		RoundStartInfo roundStartInfo = new RoundStartInfo(parameters);
		player.onRoundStart(roundStartInfo);
	}

	/**
	 * notifies the player of the end of the current round 
	 * @param parameters the round end information received from the game server
	 */
	private void finishRound(Map<String, Object> parameters) {
		RoundEndInfo roundEndInfo = new RoundEndInfo(parameters);
		player.onRoundEnd(roundEndInfo);
	}

	/**
	 * notifies the player when it has to play
	 * @param parameters the information received from the game server for the current turn
	 */
	@SuppressWarnings("unchecked")
	private void play(Map<String, Object> parameters) {
		PlayingInfo playingInfo = new PlayingInfo(parameters);
		AccelerationVector accelerationVector = player.onPlayRequest(playingInfo);

		if (accelerationVector != null) {
			JSONObject playRequest = new JSONObject();
			playRequest.put("action", "updateVector");
			JSONObject updateParameters = new JSONObject();
			updateParameters.put("dVx", accelerationVector.getdVx());
			updateParameters.put("dVy", accelerationVector.getdVy());
			playRequest.put("parameters", updateParameters);
			log("\n>sent: " + playRequest);
			write(playRequest.toJSONString());
		}
	}

	/**
	 * Open a socket connection to the given server on the given port, with an
	 * infinite timeout
	 * 
	 * @param hostname
	 *            the URL of the game engine server
	 * @param port
	 *            the port number of the game engine server
	 */
	private Boolean open(String hostname, int port) {
		InetAddress inetAddress;
		Boolean connected = false;
		try {
			inetAddress = InetAddress.getByName(hostname);
			SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
			socket = new Socket();
			socket.connect(socketAddress, 0);
			connected = true;
		} catch (IOException e) {
			log("\n>ERROR: Connection failed to server Url=" + hostname + " port=" + port, true);
		}
		return connected;
	}

	/**
	 * Closes the socket
	 */
	public void close() throws Exception {
		bufferedReader.close();
		socket.close();
		socket = null;
		bufferedReader = null;
	}

	public void log(String message) {
		log(message, false);
	}

	public void log(String message, Boolean isError) {
		if (verbose || isError) {
			System.out.println(message);
		}
	}
}
