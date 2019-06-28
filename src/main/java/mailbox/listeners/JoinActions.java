package mailbox.listeners;


import mailbox.Main;
import mailbox.GuildUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.javacord.api.event.server.ServerEvent;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.listener.server.ServerJoinListener;

import java.io.File;
import java.io.IOException;

import static mailbox.GuildUtil.addMessageChannels;


public class JoinActions implements ServerJoinListener {

    /**
     * Handles events on bot server join
     *
     * @param event
     */
    @Override
    public void onServerJoin(ServerJoinEvent event) {
        event.getApi().getThreadPool().getExecutorService().submit(() -> {
            messageSetup(event);
        });
    }

     void messageSetup(ServerEvent event) {
        File directory = new File("./data/");

        // Verifies directory exists and creates it if not
        if (!directory.exists()) {
            directory.mkdir();
        }

        try {
            GuildUtil.configureGuild(event.getServer().getId());
        } catch (IOException | ConfigurationException e) {
            Main.logger.error(e.getMessage());
        }

         addMessageChannels(event.getServer(), event.getApi());
    }
}