package mailbox;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public class GuildUtil {

    /**
     * Creates the server configuration file if none exists. If the file exists,
     * the method throws FileAlreadyExistsException.
     *
     * @throws FileAlreadyExistsException
     * @throws IOException
     * @throws ConfigurationException
     */
    public static void configureGuild(long guildId) throws FileAlreadyExistsException, IOException, ConfigurationException {
        // Assigns the guilds long id to the configuration file
        File file = new File("./data/" + guildId + ".properties");

        // Checks to see if the file exists, if it does the method is stopped and an exception is thrown
        if (file.exists()) {
            throw new FileAlreadyExistsException("The specified file '" + file.getPath() + "' already exists");
        }

        // Creates new configuration file if one does not already exists
        file.createNewFile();

        PropertiesConfiguration config = new PropertiesConfiguration("./data/" + guildId + ".properties");

        // Adds default prefix after file is created.
        config.addProperty("prefix", "?");
        config.save();

    }
}
