/**
 * 
 */
package sumoarena;

import org.json.simple.JSONObject;

import ml.options.OptionSet;
import ml.options.Options;
import ml.options.Options.Multiplicity;
import ml.options.Options.Prefix;
import ml.options.Options.Separator;

/**
 * @author peal6230
 * 
 */
public class SumoClient {

	public static final String NAME_OPTION = "name";
	public static final String TEAM_OPTION = "team";
	public static final String HOSTNAME_OPTION = "hostname";
	public static final String PORT_OPTION = "port";
	public static final String AVATAR_OPTION = "avatar";
	public static final String VERBOSE_OPTION = "verbose";

	// private static GameClient gameClient;

	/**
	 * Default parameters
	 */
	private static String name = null;
	private static String team = null;
	private static String hostname = "localhost";
	private static int port = 9090;
	private static String avatar = null;
	private static Boolean verbose;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		readOptions(args);
		boolean connected = false;
		GameClient gameClient;
		while (true) {
			try {
				avatar = "https://dl.dropbox.com/u/6503570/av.png";
				gameClient = new GameClient(hostname, port, name, team, avatar, verbose);
				JSONObject serverMessage;
				connected = true;
				while (connected) {
					serverMessage = gameClient.read();
					connected = serverMessage != null;
					if (connected) {
						gameClient.processServerMessage(serverMessage);
					}
					else {
						gameClient.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Parses the command line arguments, then set the client parameters
	 * 
	 * @param args
	 *            the command line arguments
	 */
	private static void readOptions(String[] args) {
		Options options = new Options(args, Prefix.DASH, Multiplicity.ZERO_OR_ONE);
		OptionSet optionSet = options.getSet();

		optionSet.addOption(NAME_OPTION, Separator.EQUALS, Multiplicity.ONCE);
		optionSet.addOption(TEAM_OPTION, Separator.EQUALS, Multiplicity.ONCE);
		optionSet.addOption(HOSTNAME_OPTION, Separator.EQUALS);
		optionSet.addOption(PORT_OPTION, Separator.EQUALS);
		optionSet.addOption(AVATAR_OPTION, Separator.EQUALS);
		optionSet.addOption(VERBOSE_OPTION);

		if (!options.check(true, false)) {
			System.out
					.println("usage: java SumoClient -name=<your client name> -team=<your team name> [-hostname=<server>] [-port=<server port number>] [-avatar=<url of the image>] [-verbose]");
			System.exit(1);
		}
		name = optionSet.getOption(NAME_OPTION).getResultValue(0);
		
		team = optionSet.getOption(TEAM_OPTION).getResultValue(0);

		if (optionSet.isSet(HOSTNAME_OPTION)) {
			hostname = optionSet.getOption(HOSTNAME_OPTION).getResultValue(0);
		}
		if (optionSet.isSet(PORT_OPTION)) {
			port = Integer.parseInt(optionSet.getOption(PORT_OPTION).getResultValue(0));
		}
		if (optionSet.isSet(AVATAR_OPTION)) {
			avatar = optionSet.getOption(AVATAR_OPTION).getResultValue(0);
		}
		verbose = optionSet.isSet(VERBOSE_OPTION);
	}
}
