package mailbox;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerTextChannelBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.concurrent.CompletableFuture;
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
                if (api.getServerById(guildId).isPresent())

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

    /**
     * Removes the channels if they are listed in the configuration file as an
     * ID and removes the entries.
     *
     * @param guildId
     * @param channel
     */
    public static void resetMessageSystem(long guildId, ServerTextChannel channel, DiscordApi api) {
        try {
            // Specifies the specific Guild configuration file location and assigns name based on the events Guild ID
            File file = new File("./data/" + guildId + ".properties");

            PropertiesConfiguration config = new PropertiesConfiguration("./data/" + guildId + ".properties");
            if (file.exists()) {
                api.getServerById(guildId).ifPresent(server -> {
                    server.getChannelById(config.getString("messageChannel")).ifPresent(serverChannel -> serverChannel.delete());
                    server.getChannelById(config.getString("messageInbox")).ifPresent(serverChannel -> serverChannel.delete());

                });
                config.clearProperty("messageInbox");
                config.clearProperty("messageChannel");
                config.save();
                channel.sendMessage(" The Message System has been reset. "
                        + "Delete any remaining channels on the server and use " + botPrefix(guildId) + "setup if you wish to set up the Message System again.");

            }

        } catch (ConfigurationException ex) {
            Logger.getLogger(GuildUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    public static void addMessageChannels(Server server, DiscordApi api) {
        try {
            // Specifies the specific Guild configuration file location and assigns name based on the events Guild ID
            PropertiesConfiguration config = new PropertiesConfiguration("./data/" + server.getId() + ".properties");
            File file = new File("./data/" + server.getId() + ".properties");
            File image = new File("./images/mailboxicon.png");

            EmbedBuilder embed = new EmbedBuilder()
                    .setThumbnail(new File(image.getPath()))
                    .setColor(Color.white)
                    .setTitle("How to send a message")
                    .setDescription("If you have an issue you want to contact the server staff about, simply send a message in this channel" +
                            " and it will be auto deleted. Don't worry, the server staff will still see it! If we have successfully received" +
                            " your message, you will receive a private confirmation message depending on your privacy settings. Make sure to have" +
                            " **Direct Messages** enabled in order to get a response!");

            if (file.exists()) {
                Permissions permissions = new PermissionsBuilder().setAllDenied().build();

                if (!config.containsKey("messageInbox")) {
                    CompletableFuture<Void> inboxChannel = new ServerTextChannelBuilder(server)
                            .setName("message-inbox")
                            .setTopic("Use " + GuildUtil.botPrefix(server.getId()) + "reply <User ID> <Message> to respond to messages")
                            .setAuditLogReason("Automated creation from bot to set up message system")
                            .addPermissionOverwrite(server.getEveryoneRole(), permissions)
                            .create().thenAcceptAsync(channel -> {
                                try {
                                    GuildUtil.addMessageInbox(server.getId(), channel.getIdAsString(), api);
                                    channel.sendMessage(" The " + channel.getMentionTag() + " has been set up successfully");
                                } catch (Exception ex) {
                                    channel.sendMessage("The inbox was unable to be set up");
                                }
                            });

                } else if (!api.getServerById(server.getId()).get().getChannelById(config.getProperty("messageInbox").toString()).isPresent()) {
                    CompletableFuture<Void> inboxChannel = new ServerTextChannelBuilder(server)
                            .setName("message-inbox")
                            .setTopic("Use " + GuildUtil.botPrefix(server.getId()) + "reply <User ID> <Message> to respond to messages")
                            .setAuditLogReason("Automated creation from bot to set up message system")
                            .addPermissionOverwrite(server.getEveryoneRole(), permissions)
                            .create().thenAcceptAsync(channel -> {
                                try {
                                    GuildUtil.addMessageInbox(server.getId(), channel.getIdAsString(), api);
                                    channel.sendMessage(" The Message Inbox channel was previously created but was unable to be verified. A new replacement channel " + channel.getMentionTag() + " has been created as a result.");
                                } catch (Exception ex) {
                                    channel.sendMessage("Automated creation from bot to set up message system");
                                }
                            });

                }
                if (!config.containsKey("messageChannel")) {
                    addMessageChannel(server, api, embed, permissions);
                } else if (!api.getServerById(server.getId()).get().getChannelById(config.getProperty("messageChannel").toString()).isPresent()) {

                    addMessageChannel(server, api, embed, permissions);
                }

            }

        } catch (ConfigurationException e) {
            Main.logger.error(e.getMessage());
        }
    }

    private static void addMessageChannel(Server server, DiscordApi api, EmbedBuilder embed, Permissions permissions) {
        CompletableFuture<Void> messageChannel = new ServerTextChannelBuilder(server)
                .setSlowmodeDelayInSeconds(120)
                .setName("message-channel")
                .setTopic("Send a message in this channel and it will automatically be deleted and sent to server staff.")
                .setAuditLogReason("Automated creation from bot to set up message system")
                .addPermissionOverwrite(server.getEveryoneRole(), permissions)
                .create().thenAccept(channel -> {
                    try {
                        GuildUtil.addMessageChannel(server.getId(), channel.getIdAsString(), api);
                        channel.sendMessage(embed);
                    } catch (Exception ex) {
                        channel.sendMessage("Message channel was unable to be created");
                    }
                });
    }


}
