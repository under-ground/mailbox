package mailbox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.util.logging.ExceptionLogger;
import org.javacord.core.util.logging.LoggerUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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



}
