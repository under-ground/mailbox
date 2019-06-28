package mailbox;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

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


    /**
     * Used to set the prefix of the guild using the Guild ID fetched from the
     * event that initiates the call adding the Prefix into the servers
     * configuration file.
     *
     * @param guildId
     * @param prefix
     * @throws FileAlreadyExistsException
     * @throws ConfigurationException
     */
    public static void setPrefix(long guildId, String prefix) throws FileAlreadyExistsException, ConfigurationException {

        // Specifies the specific Guild configuration file location and assigns name based on the events Guild ID
        File file = new File("./data/" + guildId + ".properties");
        PropertiesConfiguration config = new PropertiesConfiguration("./data/" + guildId + ".properties");

        // Checks to see if the file exists, if it does the method is stopped and an exception is thrown
        if (file.exists()) {
            config.setProperty("prefix", prefix);
            config.save();
        }

    }

    /**
     * Global bot prefix when called produces the saved prefix configuration in
     * the servers configuration file If no file exists, the prefix is defaulted
     * to "?"
     *
     * @param guildId
     * @return
     */
    public static String botPrefix(long guildId) {

        try {
            // Specifies the specific Guild configuration file location and assigns name based on the events Guild ID
            File file = new File("./data/" + guildId + ".properties");
            PropertiesConfiguration config = new PropertiesConfiguration("./data/" + guildId + ".properties");
            return (file.exists()) ? config.getProperty("prefix").toString() : "?";

        } catch (ConfigurationException | NullPointerException ex) {
            return "?";
        }

    }

    /**
     * Used to configure MessageChannel
     *
     * @param guildId
     * @param messageChannel
     * @throws ConfigurationException
     * @throws IOException
     * @throws Exception
     */
    public static void addMessageChannel(long guildId, String messageChannel, DiscordApi api) throws ConfigurationException, IOException, Exception {
        // Specifies the specific Guild configuration file location and assigns name based on the events Guild ID
        PropertiesConfiguration config = new PropertiesConfiguration("./data/" + guildId + ".properties");
        File file = new File("./data/" + guildId + ".properties");

        // Checks to see if the file exists, if it does the method is stopped and an exception is thrown
        if (file.exists()) {
            if (config.containsKey("messageChannel") && api.getServerById(guildId).get().getChannelById(config.getProperty("messageChannel").toString()).isPresent()) {
                throw new Exception("The Message System Module is already is already set up");
            } else {
                config.setProperty("messageChannel", messageChannel);
            }
            config.save();
        }
    }

    /**
     * Used to configure MessageInbox
     *
     * @param guildId
     * @param messageInbox
     * @throws ConfigurationException
     * @throws IOException
     * @throws Exception
     */
    public static void addMessageInbox(long guildId, String messageInbox, DiscordApi api) throws ConfigurationException, IOException, Exception {
        // Specifies the specific Guild configuration file location and assigns name based on the events Guild ID
        PropertiesConfiguration config = new PropertiesConfiguration("./data/" + guildId + ".properties");
        File file = new File("./data/" + guildId + ".properties");

        // Checks to see if the file exists, if it does the method is stopped and an exception is thrown
        if (file.exists()) {
            if (config.containsKey("messageInbox") && api.getServerById(guildId).get().getChannelById(config.getProperty("messageInbox").toString()).isPresent()) {
                throw new Exception("The Message System Module is already is already set up");
            } else {
                config.setProperty("messageInbox", messageInbox);
            }
            config.save();
        }
    }

    /**
     * Used to return the messageChannel Channel ID d to allow for message listener to
     * verify which channel to delete messages from
     *
     * @param guildId
     * @return
     */
    public static String getMessageChannelId(long guildId, DiscordApi api) {

        try {

            File file = new File("./data/" + guildId + ".properties");

            PropertiesConfiguration config = new PropertiesConfiguration("./data/" + guildId + ".properties");
            if (file.exists()) {
                if (api.getServerById(guildId).get().getChannelById(config.getProperty("messageChannel").toString()).isPresent()) {
                    return config.getProperty("messageChannel").toString();
                }

            }

        } catch (ConfigurationException ex) {
            Logger.getLogger(GuildUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Used to return the messageInbox Channel ID to allow for message listener to
     * verify which channel reply commands will be used in
     *
     * @param guildId
     * @return
     */
    public static String getInboxChannelId(long guildId, DiscordApi api) {

        try {

            File file = new File("./data/" + guildId + ".properties");

            PropertiesConfiguration config = new PropertiesConfiguration("./data/" + guildId + ".properties");
            if (file.exists()) {
                if (api.getServerById(guildId).get().getChannelById(config.getProperty("messageInbox").toString()).isPresent()) {
                    return config.getProperty("messageInbox").toString();
                }

            }

        } catch (ConfigurationException ex) {
            Logger.getLogger(GuildUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static User getTargetUserByArgs(String args, int index, DiscordApi api) throws InvalidUsageException {
        try {
            String[] splitArgs = args.split(" ");
            User targetUser = api.getUserById(Long.parseLong(splitArgs[index].replaceAll("[<@!>]", ""))).get();
            return targetUser;

        } catch (InterruptedException | ExecutionException | NumberFormatException ex) {
            throw new InvalidUsageException("❌ The target user could not be found.");
        }
    }


    public static class InvalidUsageException extends Exception {

        public InvalidUsageException(String message) {
            super(message);
        }
    }

}
