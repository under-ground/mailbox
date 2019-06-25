package mailbox;

import mailbox.listeners.JoinActions;
import mailbox.listeners.MessageActions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.util.logging.ExceptionLogger;
import org.javacord.core.util.logging.LoggerUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Cosmos
 */
public class Main {

    // Used to allow calls from other classes.
    public static org.javacord.api.DiscordApi api;

    public static final Logger logger = LoggerUtil.getLogger(Main.class);

    /*
    Main method that handles the bot initial connection from runtime arguments (Bot token)
     */
    public static void main(String args[]) {


        logger.info("Mailbox - Designed by Cosmos open source under GPL 3.0 license. "
                + "Special thanks to everyone on the Javacord Discord server for creating such a great library");

        // Used for when Log4j isn't in use. Not recommended
        //FallbackLoggerConfiguration.setDebug(true);

        // Use bot token in command arguments to run bot.
        api = new DiscordApiBuilder().setToken(args[0]).login().join();

        // Registering listener classes
        api.addServerJoinListener(new JoinActions());
        api.addMessageCreateListener(new MessageActions());

    }

    /*
Handles the logger initialization so it can be called from other classes to output to console
 */
    private static void initializeLogging() throws IOException {
        System.setProperty("java.util.logging.manager", LogManager.class.getName());
        String log4jConfigurationFileProperty = System.getProperty("log4j.configurationFile");
        if (log4jConfigurationFileProperty != null) {
            Path log4jConfigurationFile = Paths.get(log4jConfigurationFileProperty);
            if (!Files.exists(log4jConfigurationFile)) {
                try (InputStream fallbackLog4j2ConfigStream = Main.class.getResourceAsStream("/log4j2.xml")) {
                    Files.copy(fallbackLog4j2ConfigStream, log4jConfigurationFile);
                }
            }
        }

        Thread.setDefaultUncaughtExceptionHandler(ExceptionLogger.getUncaughtExceptionHandler());
    }

}