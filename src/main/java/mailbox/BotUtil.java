package mailbox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.util.logging.ExceptionLogger;
import org.javacord.core.util.logging.LoggerUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BotUtil {

    public static final Logger logger = LoggerUtil.getLogger(Mailbox.class);

    /**
     * Handles the logger initialization so it can be called from other classes to output to console
     *
     * @throws IOException
     */
    static void loggingInit() {
        System.setProperty("java.util.logging.manager", LogManager.class.getName());
        String log4jConfigurationFileProperty = System.getProperty("log4j.configurationFile");
        if (log4jConfigurationFileProperty != null) {
            Path log4jConfigurationFile = Paths.get(log4jConfigurationFileProperty);
            if (!Files.exists(log4jConfigurationFile)) {
                try {
                    try (InputStream fallbackLog4j2ConfigStream = Mailbox.class.getResourceAsStream("/log4j2.xml")) {
                        Files.copy(fallbackLog4j2ConfigStream, log4jConfigurationFile);
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }

        Thread.setDefaultUncaughtExceptionHandler(ExceptionLogger.getUncaughtExceptionHandler());
    }


    public static void messageSelfDestruct(MessageCreateEvent event, String messageToSend, int delay, boolean error) {

        String unicode;

        if (error) unicode = "❌";
        else unicode = "✅";

        event.getChannel().sendMessage(unicode + " "
                + event.getMessageAuthor().asUser().get().getNicknameMentionTag()
                + " " + messageToSend).thenAccept(message -> {
            ScheduledExecutorService scheduler
                    = Executors.newSingleThreadScheduledExecutor();

            // Deletes the message notification to the user after 10 seconds.
            Runnable task = message::delete;

            scheduler.schedule(task, delay, TimeUnit.SECONDS);
            scheduler.shutdown();
        });
    }



}
