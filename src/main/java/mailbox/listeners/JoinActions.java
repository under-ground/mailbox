package mailbox.listeners;


import mailbox.Main;
import mailbox.GuildUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.javacord.api.entity.channel.ServerTextChannelBuilder;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.listener.server.ServerJoinListener;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;


public class JoinActions implements ServerJoinListener {

    /**
     * Handles events on bot server join
     *
     * @param event
     */
    @Override
    public void onServerJoin(ServerJoinEvent event) {

        File directory = new File("./data/");


        // Verifies directory exists and creates it if not
        if (!directory.exists()) {
            directory.mkdir();
        }

        try {

            GuildUtil.configureGuild(event.getServer().getId());

        } catch (ConfigurationException e) {

            Main.logger.error(e.getMessage());

        } catch (IOException e) {

            Main.logger.error(e.getMessage());

        }


        try {
            // Specifies the specific Guild configuration file location and assigns name based on the events Guild ID
            PropertiesConfiguration config = new PropertiesConfiguration("./data/" + event.getServer().getId() + ".properties");
            File file = new File("./data/" + event.getServer().getId() + ".properties");
            if (file.exists()) {
                Permissions permissions = new PermissionsBuilder().setAllDenied().build();

                if (!config.containsKey("messageInbox")) {
                    CompletableFuture<Void> inboxChannel = new ServerTextChannelBuilder(event.getServer())
                            .setName("message-inbox")
                            .setTopic("Use " + GuildUtil.botPrefix(event.getServer().getId()) + "reply <User ID> <Message> to respond to messages")
                            .setAuditLogReason("Automated creation from bot to set up message system")
                            .addPermissionOverwrite(event.getServer().getEveryoneRole(), permissions)
                            .create().thenAccept(channel -> {
                                try {
                                    GuildUtil.addMessageInbox(event.getServer().getId(), channel.getIdAsString());
                                    channel.sendMessage(" The " + channel.getMentionTag() + " has been set up successfully");
                                } catch (Exception ex) {
                                    channel.sendMessage("The inbox was unable to be set up");
                                }
                            });

                } else if (!Main.api.getServerById(event.getServer().getId()).get().getChannelById(config.getProperty("messageInbox").toString()).isPresent()) {
                    CompletableFuture<Void> inboxChannel = new ServerTextChannelBuilder(event.getServer())
                            .setName("message-inbox")
                            .setTopic("Use " + GuildUtil.botPrefix(event.getServer().getId()) + "reply <User ID> <Message> to respond to messages")
                            .setAuditLogReason("Automated creation from bot to set up message system")
                            .addPermissionOverwrite(event.getServer().getEveryoneRole(), permissions)
                            .create().thenAccept(channel -> {
                                try {
                                    GuildUtil.addMessageInbox(event.getServer().getId(), channel.getIdAsString());
                                    channel.sendMessage(" The Message Inbox channel was previously created but was unable to be verified. A new replacement channel " + channel.getMentionTag() + " has been created as a result.");
                                } catch (Exception ex) {
                                    channel.sendMessage("Automated creation from bot to set up message system");
                                }
                            });

                }
                if (!config.containsKey("messageChannel")) {
                    CompletableFuture<Void> messageChannel = new ServerTextChannelBuilder(event.getServer())
                            .setName("message-channel")
                            .setTopic("Send a message in this channel and it will automatically be deleted and sent to server staff.")
                            .setAuditLogReason("Automated creation from bot to set up message system")
                            .addPermissionOverwrite(event.getServer().getEveryoneRole(), permissions)
                            .create().thenAccept(channel -> {
                                try {
                                    GuildUtil.addMessageChannel(event.getServer().getId(), channel.getIdAsString());
                                    channel.sendMessage(" The " + channel.getMentionTag() + " has been set up successfully");
                                } catch (Exception ex) {
                                    channel.sendMessage("Message channel was unable to be created");
                                }
                            });
                } else if (!Main.api.getServerById(event.getServer().getId()).get().getChannelById(config.getProperty("messageChannel").toString()).isPresent()) {

                    CompletableFuture<Void> messageChannel = new ServerTextChannelBuilder(event.getServer())
                            .setName("message-channel")
                            .setTopic("Send a message in this channel and it will automatically be deleted and sent to server staff.")
                            .setAuditLogReason("Automated creation from bot to set up message system")
                            .addPermissionOverwrite(event.getServer().getEveryoneRole(), permissions)
                            .create().thenAccept(channel -> {
                                try {
                                    GuildUtil.addMessageChannel(event.getServer().getId(), channel.getIdAsString());
                                    channel.sendMessage(" The Message channel was previously created but was unable to be verified. A new replacement channel " + channel.getMentionTag() + " has been created as a result.");
                                } catch (Exception ex) {
                                    channel.sendMessage("Message channel was unable to be created");
                                }
                            });
                }

            }

        } catch (ConfigurationException e) {
            Main.logger.error(e.getMessage());
        }

    }
}