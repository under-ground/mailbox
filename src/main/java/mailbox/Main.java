package mailbox;


import org.javacord.api.DiscordApiBuilder;

/**
 *
 * @author Cosmos
 */
public class Main {

    // Used to allow calls from other classes.
    public static org.javacord.api.DiscordApi api;

    /*
    Main method that handles the bot initial connection from runtime arguments (Bot token)
     */
    public static void main(String args[]) {

        // Use bot token in command arguments to run bot.
        api = new DiscordApiBuilder().setToken(args[0]).login().join();

    }

}