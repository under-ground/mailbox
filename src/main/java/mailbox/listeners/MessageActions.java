package mailbox.listeners;

import mailbox.GuildUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.awt.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author Cosmos
 */
public class MessageActions implements MessageCreateListener {

    // Used for separation of commandArgs
    private static final Pattern separator = Pattern.compile(" ");

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        // verifies that the message starts with the guild prefix
        if (event.getMessageContent().startsWith(GuildUtil.botPrefix(event.getServer().get().getId()))) {

            String command = event.getMessageContent().substring(1);
            String[] commandArgs = separator.split(command, 2);

            switch (commandArgs[0]) {

                case "prefix":

                    // Check if the author is an administrator
                    if (!event.getMessageAuthor().isServerAdmin()) {
                        event.getChannel().sendMessage("This command can only be used by a server administrator");
                        return;
                    }
                    try {
                        GuildUtil.setPrefix(event.getServer().get().getId(), commandArgs[1]);
                        event.getChannel().sendMessage("The server prefix has been changed to `" + GuildUtil.botPrefix(event.getServer().get().getId()) + "`");

                    } catch (ConfigurationException | FileAlreadyExistsException ex) {
                        Logger.getLogger(MessageActions.class.getName()).log(Level.SEVERE, null, ex);
                        event.getChannel().sendMessage("The configuration file for this server has not been set up or is corrupted");
                    }

                    break;

                case "serverinfo":
                    // Builds embed
                    if (event.getMessageAuthor().isServerAdmin()) {
                        Date serverCreationDate = Date.from(event.getServer().get().getCreationTimestamp());
                        EmbedBuilder serverinfo = new EmbedBuilder()
                                .setThumbnail(event.getServer().get().getIcon().get())
                                .setTitle("**Server Information:**").setColor(Color.white)
                                .addField("**Server Name:**", event.getServer().get().getName(), true)
                                .addField("**Server ID:**", event.getServer().get().getIdAsString(), true)
                                .addField("**Member Count:**", Integer.toString(event.getServer().get().getMemberCount()), true)
                                .addField("**ServerCreation Date:**", serverCreationDate.toString(), true)
                                .addField("**Server Owner:**", event.getServer().get().getOwner().toString(), true)
                                .addField("**Server Region:**", event.getServer().get().getRegion().toString(), true)
                                .addField("**Verification Level:**", event.getServer().get().getVerificationLevel().toString(), true)
                                .addField("**Text Channels:**", Integer.toString(event.getServer().get().getTextChannels().size()), true)
                                .addField("**Voice Channels:**", Integer.toString(event.getServer().get().getVoiceChannels().size()), true);
                        // Send the embed. It logs every exception, besides missing permissions (you are not allowed to send newMessage in the channel)
                        event.getChannel().sendMessage(serverinfo);
                    }
                    break;

                case "reset":

                    // TODO: Reset entire message system

                    break;

            }

        }

    }

}