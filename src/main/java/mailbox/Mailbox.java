package mailbox;

import mailbox.listeners.ServerJoinActions;
import mailbox.listeners.MessageActions;
import mailbox.listeners.ReactionActions;
import mailbox.listeners.ServerLeaveActions;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.util.logging.ExceptionLogger;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;
import org.javacord.core.util.logging.LoggerUtil;



/**
 * @author Cosmos
 */
public class Mailbox {


    public static final Logger logger = LoggerUtil.getLogger(Mailbox.class);


    /**
     * Begins the bot login process and logging initializations
     *
     * @param args
     */
    public static void main(String args[]) {

        login(args);
        BotUtil.loggingInit();

    }


    /**
     * Logs in using cli or env token
     *
     * @param args
     */
    private static void login(String[] args) {
        logger.info("Mailbox - Designed by Cosmos open source under GPL 3.0 license. "
                + "Special thanks to everyone on the Javacord Discord server for creating such a great library");

        // Used for when Log4j isn't in use.
        FallbackLoggerConfiguration.setDebug(true);


        // The code below will check for cli argument or environment variable and close if neither are found
        String token = null;
        if (args.length > 0) {
            token = args[0];
        }
        if (token == null) {
            token = System.getenv("MAILBOX_TOKEN");
        }
        if (token == null) {
            System.err.println("No Token supplied.");
            System.err.println("Supply Token als Command Line Argument or Environment Variable MAILBOX_TOKEN");
            System.exit(1);
        }


        // Use bot token in command arguments or environment variables to run bot.
        new DiscordApiBuilder().setToken(token).login().thenAccept(api -> {

            addListeners(api);

        }).exceptionally(ExceptionLogger.get());


    }

    /**
     * Registers the listeners after successful login called in login() method
     * @param api
     */
    private static void addListeners(DiscordApi api) {

            api.addServerJoinListener(new ServerJoinActions());
            api.addServerLeaveListener(new ServerLeaveActions());
            api.addMessageCreateListener(new MessageActions());
            api.addReactionAddListener(new ReactionActions());

    }



}