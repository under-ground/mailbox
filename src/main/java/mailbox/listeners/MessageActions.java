package mailbox.listeners;

import mailbox.GuildUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;


import java.awt.*;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.List;

import static mailbox.GuildUtil.addMessageChannels;

/**
 * @author Cosmos
 */
public class MessageActions implements MessageCreateListener {

    // Used for separation of commandArgs
    private static final Pattern separator = Pattern.compile(" ");

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        if (!event.getServer().isPresent()) return;
        Server server = event.getServer().get();

        try {

            if (event.getChannel().getIdAsString() == null ? GuildUtil.getInboxChannelId(server.getId(), event.getApi()) == null : event.getChannel().getIdAsString().equals(GuildUtil.getMessageChannelId(server.getId(), event.getApi()))) {

                event.getApi().getThreadPool().getExecutorService().submit(() -> {
                    try {

                        if (!event.getMessageAuthor().isBotUser()) {

                            // Grabs the text of the newMessage
                            String newUserMessage = event.getMessageContent();
                            // Grabs the attachments of the newMessage
                            List<MessageAttachment> MessageAttachments = event.getMessage().getAttachments();

                            EmbedBuilder privateMessage = new EmbedBuilder()
                                    .setThumbnail(event.getMessageAuthor().getAvatar())
                                    .setTitle("**Your message to the " + server.getName() + " staff was successful**").setColor(Color.green);
                            if (!newUserMessage.isEmpty()) {
                                // If there is text, it will be added to the embed
                                privateMessage.addField("Message", newUserMessage, true);
                            }
                            if (!MessageAttachments.isEmpty()) {
                                // Adds an image to the embed, if there is one
                                if (MessageAttachments.get(0).isImage()) {
                                    privateMessage.setImage(MessageAttachments.get(0).downloadAsByteArray().join());
                                }
                            }
                            event.getApi().getUserById(event.getMessageAuthor().getId()).thenAcceptAsync(user -> {
                                try {

                                    user.sendMessage(privateMessage);
                                } catch (NoSuchElementException | CompletionException ex) {
                                    event.getApi().getChannelById(GuildUtil.getInboxChannelId(server.getId(), event.getApi())).get().asTextChannel().get().sendMessage("❌ " + event.getMessageAuthor().asUser().get().getNicknameMentionTag()
                                            + " sent us a message, but they are unable to receive direct messages.");

                                    event.getChannel().sendMessage("❌ "
                                            + event.getMessageAuthor().asUser().get().getNicknameMentionTag()
                                            + " your message was received successfully, but your direct messages are not open, please update your privacy settings.").thenAccept(message -> {
                                        ScheduledExecutorService scheduler
                                                = Executors.newSingleThreadScheduledExecutor();

                                        // Deletes the message notification to the user after 10 seconds.
                                        Runnable task = new Runnable() {
                                            public void run() {
                                                message.delete();
                                            }
                                        };

                                        int delay = 10;
                                        scheduler.schedule(task, delay, TimeUnit.SECONDS);
                                        scheduler.shutdown();
                                    });

                                }
                            });
                            // Constructing newMessage for the recieving channel
                            EmbedBuilder userMessage = new EmbedBuilder()
                                    .setThumbnail(event.getMessageAuthor().asUser().get().getAvatar())
                                    .setTitle(event.getMessageAuthor().asUser().get().getDiscriminatedName() + " has sent a message").setColor(Color.red)
                                    .setDescription(event.getMessageAuthor().asUser().get().getNicknameMentionTag());
                            if (!newUserMessage.isEmpty()) {
                                // Adds a newMessage to the embed, if there is one
                                userMessage.addField("Message", newUserMessage, true);
                            }
                            if (!MessageAttachments.isEmpty()) {
                                // Adds an image to the embed, if there is one
                                if (MessageAttachments.get(0).isImage()) {
                                    userMessage.setImage(MessageAttachments.get(0).downloadAsByteArray().join());
                                }
                            }
                            // Puts the sending user's ID at the footer of the embed
                            userMessage.setFooter("ID: " + event.getMessageAuthor().getIdAsString());

                            // Sends the newMessage in the Receiving Channel
                            event.getApi().getChannelById(GuildUtil.getInboxChannelId(server.getId(), event.getApi())).get().asTextChannel().get().sendMessage(userMessage).thenAcceptAsync(message -> {
                                // Deletes the newMessage
                                event.getMessage().delete();
                                // Adds ❌ reaction to allow staff to see if a message has not been responded to yet
                                message.addReaction("❌");
                                // Sends the sending user's ID in the Receiving Channel for ease-of-access copy-paste
                                event.getApi().getChannelById(GuildUtil.getInboxChannelId(server.getId(), event.getApi())).get().asTextChannel().get().sendMessage(event.getMessageAuthor().getIdAsString());
                            });

                        }
                    } catch (CompletionException | NoSuchElementException ex) {

                    }
                });
            }

            if (event.getChannel().getIdAsString() == null ? GuildUtil.getInboxChannelId(server.getId(), event.getApi()) == null : event.getChannel().getIdAsString().equals(GuildUtil.getInboxChannelId(server.getId(), event.getApi()))) {
                event.getApi().getThreadPool().getExecutorService().submit(() -> {
                    if (event.getMessageContent().startsWith(GuildUtil.botPrefix(server.getId()) + "anonreply ") || event.getMessageContent().startsWith(GuildUtil.botPrefix(server.getId()) + "reply ")) {

                        try {
                            // Gets rid of /anonreply or /reply
                            String message = (event.getMessageContent().startsWith(GuildUtil.botPrefix(server.getId()) + "anonreply ")) ? event.getMessageContent().replace(GuildUtil.botPrefix(server.getId()) + "anonreply ", "") : event.getMessageContent().replace(GuildUtil.botPrefix(server.getId()) + "reply ", "");
                            while (message.indexOf(" ") == 0) {
                                // Gets rid of any extra whitespace
                                message = message.substring(1);
                            }
                            // Calls a custom function to grab a user via command arguments
                            User targetUser = GuildUtil.getTargetUserByArgs(message, 0, event.getApi());
                            // Removes the @ or ID from the new newMessage
                            message = message.substring(message.indexOf(" ") + 1);

                            EmbedBuilder userMessage = new EmbedBuilder()
                                    .setThumbnail(server.getIcon().get())
                                    .setTitle("**The " + server.getName() + " staff have sent you a message**").setColor(Color.green)
                                    .addField("Message", message, true)
                                    .addField("Note", "To reply, revisit the <#" + GuildUtil.getMessageChannelId(server.getId(), event.getApi()) + "> channel or reach out to a moderator directly.", true);
                            if (event.getMessageContent().startsWith(GuildUtil.botPrefix(server.getId()) + "reply ")) {
                                // Adds the newMessage composer if the command is /reply, and sets the thumbnail to their profile picture
                                userMessage.setDescription("*Composed by " + event.getMessageAuthor().asUser().get().getNicknameMentionTag() + "*")
                                        .setThumbnail(event.getMessageAuthor().getAvatar());
                            }

                            // Deletes the command
                            event.getMessage().delete().thenAcceptAsync(aVoid -> {

                                try {
                                    // Sends the target the embed
                                    targetUser.sendMessage(userMessage).join();
                                } catch (NoSuchElementException | CompletionException ex) {
                                    event.getChannel().sendMessage("❌ Your message could not be sent; This could be due to user privacy settings.");

                                }

                            });

                            // Sends the embed in the Receiving Channel of the newMessage system
                            event.getChannel().sendMessage(userMessage);
                            // Adds Sent by and Sent to info for the Staff to see
                            event.getChannel().sendMessage("Sent by <@!" + event.getMessageAuthor().getIdAsString() + "> • Sent to <@!" + targetUser.getIdAsString() + ">");

                        } catch (CompletionException | NoSuchElementException ex) {
                            event.getChannel().sendMessage(" Your message is too large! Please shorten the message before sending and try again");
                        } catch (GuildUtil.InvalidUsageException e) {
                            event.getChannel().sendMessage(e.getMessage());
                        } catch (NullPointerException ex) {

                        }

                    }
                });
            }

        } catch (NullPointerException ex) {
            // Ignores if message system isn't currently set up
        }
        // verifies that the message starts with the guild prefix
        if (event.getMessageContent().startsWith(GuildUtil.botPrefix(server.getId()))) {

            String command = event.getMessageContent().substring(1);
            String[] commandArgs = separator.split(command, 2);

            switch (commandArgs[0]) {

                case "help": {

                    // Check if the author is an administrator
                    if (!event.getMessageAuthor().isServerAdmin()) {
                        event.getChannel().sendMessage("This command can only be used by a server administrator");
                        return;
                    }

                    if (event.getMessageAuthor().isServerAdmin()) {
                        String prefix = GuildUtil.botPrefix(server.getId());
                        EmbedBuilder modCommands = new EmbedBuilder()
                                .setThumbnail(event.getApi().getYourself().getAvatar())
                                .setTitle("**Using Mailbox Commands:**").setColor(Color.white)
                                .addField(prefix + "reply <UserID> <Message>", " Used to reply to users using the Message System module. Can either mention the user or just use their ID" + "\n", true)
                                .addField(prefix + "anonreply <UserID> <Message>", " Same as the **reply** command, but does not inform the user who sent the message" + "\n", true)
                                .addField(prefix + "serverinfo", "Provides general information about the server such as owner and user count" + "\n", true)
                                .addField(prefix + "reset", "Resets the entire message system module and deletes channels." + "\n", true)
                                .addField(prefix + "setup", "Sets up the message system module again, best if used after a reset. (will be ignored if already set up)" + "\n", true)
                                .addField(prefix + "prefix", "Changes the prefix used by the bot for commands", true);
                        event.getChannel().sendMessage(modCommands);
                    }
                }

                break;

                case "prefix":
                        event.getApi().getThreadPool().getExecutorService().submit(() -> {
                            // Check if the author is an administrator
                            if (!event.getMessageAuthor().isServerAdmin()) {
                                event.getChannel().sendMessage("This command can only be used by a server administrator");
                                return;
                            }
                            try {
                                GuildUtil.setPrefix(server.getId(), commandArgs[1]);
                                event.getChannel().sendMessage("The server prefix has been changed to `" + GuildUtil.botPrefix(server.getId()) + "`");

                            } catch (ConfigurationException | FileAlreadyExistsException ex) {
                                Logger.getLogger(MessageActions.class.getName()).log(Level.SEVERE, null, ex);
                                event.getChannel().sendMessage("The configuration file for this server has not been set up or is corrupted");
                            }
                        });

                    break;

                case "serverinfo":


                    if (event.getMessageAuthor().isServerAdmin()) {
                        Date serverCreationDate = Date.from(server.getCreationTimestamp());

                        EmbedBuilder serverinfo = new EmbedBuilder()
                                .setThumbnail(server.getIcon().get())
                                .setTitle("**Server Information:**").setColor(Color.white)
                                .addField("**Server Name:**", server.getName(), true)
                                .addField("**Server ID:**", server.getIdAsString(), true)
                                .addField("**Member Count:**", Integer.toString(server.getMemberCount()), true)
                                .addField("**ServerCreation Date:**", serverCreationDate.toString(), true)
                                .addField("**Server Owner:**", server.getOwner().toString(), true)
                                .addField("**Server Region:**", server.getRegion().toString(), true)
                                .addField("**Verification Level:**", server.getVerificationLevel().toString(), true)
                                .addField("**Text Channels:**", Integer.toString(server.getTextChannels().size()), true)
                                .addField("**Voice Channels:**", Integer.toString(server.getVoiceChannels().size()), true);
                        event.getChannel().sendMessage(serverinfo);
                    }
                    break;

                case "reset":

                    event.getServerTextChannel().ifPresent(serverTextChannel -> {
                        GuildUtil.resetMessageSystem(server.getId(), event.getServerTextChannel().get(), event.getApi());
                    });

                    break;

                // Used if the system has been reset and requires setup again
                case "setup":

                    // Check if the author is an administrator
                    if (!event.getMessageAuthor().isServerAdmin()) {
                        event.getChannel().sendMessage("This command can only be used by a server administrator");
                        return;
                    }

                    event.getApi().getThreadPool().getExecutorService().submit(() -> {
                        addMessageChannels(server, event.getApi());
                    });

                    break;

            }

        }

    }

}